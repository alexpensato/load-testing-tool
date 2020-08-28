package org.pensatocode.loadtest;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.pensatocode.loadtest.core.util.StatsUtil;
import org.pensatocode.loadtest.services.BookService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.ExecutionException;

/**
 * See https://github.com/square/retrofit/issues/3341
 * to understand Retrofit Warnings
 */
public class LoadTestMain {

    private final static String BASE_API_URL = "http://localhost:8080";

    public static void main(String... args) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        try (BookService bookService = new BookService(retrofit)) {
            DescriptiveStatistics stats = bookService.runLoadTest(10);
            StatsUtil.printStats(stats, "BookService");
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }
    }
}
