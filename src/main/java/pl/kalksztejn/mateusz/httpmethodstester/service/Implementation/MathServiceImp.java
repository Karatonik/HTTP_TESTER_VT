package pl.kalksztejn.mateusz.httpmethodstester.service.Implementation;

import org.springframework.stereotype.Service;
import pl.kalksztejn.mateusz.httpmethodstester.service.Interface.MathService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Service
public class MathServiceImp implements MathService {
    private final Semaphore semaphore = new Semaphore(1);

    @Override
    public String calculateSqrt() {
        LongStream.rangeClosed(1, 100)
                .mapToObj(i -> String.format("%.2f", Math.sqrt(i)))
                .collect(Collectors.toList());
        return "ok";
    }

    @Override
    public String calculateFactorial() {
        BigInteger factorial = LongStream.rangeClosed(1, 100)
                .mapToObj(BigInteger::valueOf)
                .reduce(BigInteger.ONE, BigInteger::multiply);

        return " ok";
    }

    public String blockingMethod(int threadPool) {
        String filename =generateRandomFileName();
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(threadPool);

            List<Future<String>> futures = IntStream.range(0, threadPool)
                    .mapToObj(threadIndex -> executorService.submit(() -> {
                        try {
                            semaphore.acquire(); // Czekaj na dostęp do pliku
                            writeToFile("Hello from Thread " + threadIndex, filename); // Zapisz do pliku
                            semaphore.release(); // Zwolnij dostęp do pliku
                            return "Done";
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return "Error";
                        }
                    })).toList();

            executorService.shutdown();
            // Oczekuj na zakończenie pracy wszystkich wątków
            while (!executorService.isTerminated()) {
                Thread.sleep(100);
            }

            for (Future<String> future : futures) {
                if (!future.isDone()) {
                    return "Error";
                }
            }

            return "Done";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "Error";
        } finally {
            Path filePath = Paths.get(filename);
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void writeToFile(String line, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generateRandomFileName() {
        UUID uuid = UUID.randomUUID();
        return uuid +"txt";
    }
}
