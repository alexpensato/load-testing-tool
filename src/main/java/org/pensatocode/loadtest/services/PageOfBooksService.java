package org.pensatocode.loadtest.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.pensatocode.loadtest.clients.BookApiClient;
import org.pensatocode.loadtest.core.handlers.PropertiesHandler;
import org.pensatocode.loadtest.core.service.AbstractService;
import org.pensatocode.loadtest.core.tasks.PageTask;
import org.pensatocode.loadtest.core.util.StatsUtil;
import org.pensatocode.loadtest.models.Book;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class PageOfBooksService extends AbstractService {

    public PageOfBooksService(Retrofit retrofit) {
        super(retrofit);
    }

    @Override
    public void serviceTask() throws IOException {
        BookApiClient bookApiClient = retrofit.create(BookApiClient.class);
        new PageTask<Book>().execute(bookApiClient.getBooks(0, PropertiesHandler.getInstance().getHeaders()));
    }

    public static void run(Retrofit retrofit, PropertiesHandler handler) {
        try (PageOfBooksService service = new PageOfBooksService(retrofit)) {
            DescriptiveStatistics stats = service.runLoadTest(handler.getInitialParamAsInt("number_of_executions"));
            StatsUtil.printStats(stats, handler.getInitialParam("page_of_books_label"));
        } catch (InterruptedException| ExecutionException e) {
            e.printStackTrace();
        }
    }
}
