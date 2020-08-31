package org.pensatocode.loadtest.core.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.pensatocode.loadtest.core.handlers.PropertiesHandler;
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

    private final ExecutorService executorService;

    protected final Retrofit retrofit;

    protected AbstractService(Retrofit retrofit) {
        this.retrofit = retrofit;
        this.executorService = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());
    }

    Supplier<Duration> taskSupplier = () -> {
        try {
            Instant start = Instant.now();
            serviceTask();
            Instant end = Instant.now();
            return Duration.between(start, end);
        } catch (IOException | InterruptedException e) {
            log.warn("Task error: " + e.getMessage());
            return null;
        }
    };

    public abstract void serviceTask() throws IOException, InterruptedException;

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
            if (PropertiesHandler.getInstance().getInitialParamAsBoolean("print_info_log")) {
                log.info(String.format("Task duration is %d milliseconds.", duration.toMillis()));
            }
            stats.addValue(duration.toMillis());
        }
        log.info(String.format("Error count is %d.",errorCount));
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
