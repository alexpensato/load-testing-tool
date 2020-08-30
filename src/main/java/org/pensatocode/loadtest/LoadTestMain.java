package org.pensatocode.loadtest;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.pensatocode.loadtest.core.handlers.PropertiesHandler;
import org.pensatocode.loadtest.core.util.StatsUtil;
import org.pensatocode.loadtest.services.BookService;
import org.pensatocode.loadtest.services.PageOfBooksService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.ExecutionException;

/**
 * See https://github.com/square/retrofit/issues/3341
 * to understand Retrofit Warnings
 */
public class LoadTestMain {

    public static void main(String... args) {
        PropertiesHandler handler = PropertiesHandler.getInstance();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(handler.getInitialParam("base_api_url"))
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        try (PageOfBooksService service = new PageOfBooksService(retrofit)) {
            DescriptiveStatistics stats = service.runLoadTest(handler.getInitialParamAsInt("number_of_executions"));
            StatsUtil.printStats(stats, handler.getInitialParam("page_of_books_label"));
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }
        try (BookService service = new BookService(retrofit)) {
            service.loadParamsQueue(handler.getInitialParamAsInt("initial_capacity"));
            DescriptiveStatistics stats = service.runLoadTest(handler.getInitialParamAsInt("number_of_executions"));
            StatsUtil.printStats(stats, handler.getInitialParam("book_label"));
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }
    }
}
