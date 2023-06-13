package pl.kalksztejn.mateusz.httpmethodstester.logger;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Aspect
@Component
public class PerformanceMeasurementAspect {
    private ExecutorService executorService;

    private static final String FILE_NAME = "performance_results.txt";

    private SimpleDateFormat dateFormat;
    private PrintWriter writer;


    private static final Logger logger = LoggerFactory.getLogger(PerformanceMeasurementAspect.class);
    private final MemoryMXBean memoryMXBean;
    private final ThreadMXBean threadMXBean;
    private final int availableProcessors;
    private long totalDuration;
    private long totalCpuUsed;
    private long totalAppCpuUsed;
    private long totalMemoryUsed;
    private int numCalls;

    private long numError;

    @PostConstruct
    public void init() throws IOException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        writer = new PrintWriter(new FileWriter(FILE_NAME, true));

        // Inicjalizacja executorService z odpowiednią ilością wątków
        executorService = Executors.newFixedThreadPool(1);
    }

    @PreDestroy
    public void cleanup() {
        // Zwalnianie pamięci i czyszczenie wątku
        memoryMXBean.gc();
        threadMXBean.setThreadCpuTimeEnabled(false);
        // Zamknięcie writer i executorService
        writer.close();
        executorService.shutdown();
    }

    public PerformanceMeasurementAspect() {
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        this.threadMXBean = ManagementFactory.getThreadMXBean();
        this.availableProcessors = Runtime.getRuntime().availableProcessors();
        this.totalDuration = 0;
        this.totalCpuUsed = 0;
        this.totalAppCpuUsed = 0;
        this.totalMemoryUsed = 0;
        this.numCalls = 0;
        this.numError = 0;
    }

    @Around("execution(* pl.kalksztejn.mateusz.httpmethodstester.controller.*.*(..))")
    public Object measurePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        try {

            long cpuBefore = threadMXBean.getCurrentThreadCpuTime();
            long startTime = System.nanoTime();

            Object result = joinPoint.proceed();

            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            long cpuAfter = threadMXBean.getCurrentThreadCpuTime();
            long cpuUsed = cpuAfter - cpuBefore;
            long memoryAfter = memoryMXBean.getHeapMemoryUsage().getUsed() / 1000000;
            double cpuUsage = ((double) cpuUsed / (duration * availableProcessors)) * 100;
            double appCpuUsed = ((double) cpuAfter / (duration * availableProcessors)) * 100;

            totalDuration += duration;
            totalCpuUsed += cpuUsed;
            totalAppCpuUsed += cpuAfter;
            totalMemoryUsed += memoryAfter;
            numCalls++;

            logger.info("[PERFORMANCE] Method '{}' duration {} ns, memory use: {} MB, CPU: {}%,",
                    joinPoint.getSignature().getName(), duration, memoryAfter, String.format("%.4f", cpuUsage));

            String logMessage = String.format("[PERFORMANCE] Method '%s' duration %d ns, memory use: %d MB, CPU: %.4f%%",
                    joinPoint.getSignature().getName(), duration, memoryAfter, cpuUsage);
            writer.println(logMessage);
            writer.flush();

            if (numCalls > 0) {
                logger.info("[PERFORMANCE] Average duration: {} ns, Average memory use: {} MB, Average  CPU: {}%, Errors: {}",
                        totalDuration / numCalls, totalMemoryUsed / numCalls, String.format("%.4f", (double) totalCpuUsed / (totalDuration * availableProcessors) * 100), numError);
            }
            return result;
        } catch (OutOfMemoryError e) {
            numError++;
        } finally {
            // Zwalnianie pamięci i czyszczenie wątku
            memoryMXBean.gc();
            threadMXBean.setThreadCpuTimeEnabled(false);
        }
        return null;
    }
}
