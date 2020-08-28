package org.pensatocode.loadtest.core.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import retrofit2.Retrofit;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * This AbstractService uses CompletableFuture instead of Future Interface to allow developers
 * implementing `internalTask` method to achieve any of these goals:
 *  - Non-blocking
 *  - Ability to Programmatically completing a future
 *  - Perform Error handling
 *  - Ability to Chain several futures
 *  - Ability to combine results of multiple futures (that run in parallel)
 */
@Slf4j
public abstract class AbstractService implements AutoCloseable {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());

    protected final Retrofit retrofit;

    protected AbstractService(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    Supplier<Duration> taskSupplier = () -> {
        try {
            Instant start = Instant.now();
            internalTask();
            Instant end = Instant.now();
            return Duration.between(start, end);
        } catch (IOException e) {
            log.warn("Task error: " + e.getMessage());
            return null;
        }
    };

    public abstract void internalTask() throws IOException;

    public DescriptiveStatistics runLoadTest(int numberOfExecutions) throws ExecutionException, InterruptedException {
        // Initialize tasks
        List<CompletableFuture<Duration>> tasks = new ArrayList<>();
        IntStream.rangeClosed(1, numberOfExecutions)
                .forEach(i -> tasks.add(CompletableFuture.supplyAsync(taskSupplier, executorService)));
        // Wait for all tasks to finish
        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).get();
        // Analyze results and compute statistics
        return computeStatistics(tasks);
    }

    private DescriptiveStatistics computeStatistics(List<CompletableFuture<Duration>> tasks) throws ExecutionException, InterruptedException {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        int errorCount = 0;
        for (CompletableFuture<Duration> task : tasks) {
            Duration duration = task.get();
            if(task.isCompletedExceptionally() || duration == null) {
                errorCount++;
                continue;
            }
            System.out.println(String.format("Task duration is %d milliseconds.",duration.toMillis()));
            stats.addValue(duration.toMillis());
        }
        System.out.println(String.format("Error count is %d.",errorCount));
        return stats;
    }

    @Override
    public void close() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
