package pl.kalksztejn.mateusz.httpmethodstester.logger;

import com.sun.management.OperatingSystemMXBean;
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
    private final OperatingSystemMXBean osBean;
    private long totalDuration;

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
        // Zamknięcie writer i executorService
        writer.close();
        executorService.shutdown();
    }

    public PerformanceMeasurementAspect() {
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        this.osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        this.totalDuration = 0;
        this.totalMemoryUsed = 0;
        this.numCalls = 0;
        this.numError = 0;
    }

    @Around("execution(* pl.kalksztejn.mateusz.httpmethodstester.controller.*.*(..))")
    public Object measurePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            long startTime = System.nanoTime();
            double cpuUsageBefore = osBean.getProcessCpuLoad() * 100;

            Object result = joinPoint.proceed();

            long endTime = System.nanoTime();
            double cpuUsageAfter = osBean.getProcessCpuLoad() * 100;
            long duration = endTime - startTime;
            long memoryAfter = memoryMXBean.getHeapMemoryUsage().getUsed() / 1000000;
            double cpuUsageAv = cpuUsageAfter-cpuUsageBefore;

            totalDuration += duration;
            totalMemoryUsed += memoryAfter;
            numCalls++;

            String logMessage = String.format("%s/%d/%d/%.2f",
                    joinPoint.getSignature().getName(), duration, memoryAfter, cpuUsageAv);
            writer.println(logMessage);
            writer.flush();

            return result;
        } catch (OutOfMemoryError e) {
            numError++;
        }
        return null;
    }
}
