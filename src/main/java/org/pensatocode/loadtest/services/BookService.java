package org.pensatocode.loadtest.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.pensatocode.loadtest.clients.BookApiClient;
import org.pensatocode.loadtest.core.handlers.PropertiesHandler;
import org.pensatocode.loadtest.core.service.AbstractService;
import org.pensatocode.loadtest.core.tasks.PageTask;
import org.pensatocode.loadtest.core.tasks.SingleTask;
import org.pensatocode.loadtest.core.util.StatsUtil;
import org.pensatocode.loadtest.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class BookService extends AbstractService {

    private BlockingQueue<Integer> queue;

    public BookService(@Autowired Retrofit retrofit) {
        super(retrofit);
    }

    @Override
    public void serviceTask() throws IOException, InterruptedException {
        BookApiClient bookApiClient = retrofit.create(BookApiClient.class);
        Integer pathId = queue.poll(2, TimeUnit.SECONDS);
        queue.offer(pathId, 2, TimeUnit.SECONDS);
        new SingleTask<Book>().execute(bookApiClient.getOneBook(pathId, PropertiesHandler.getInstance().getHeaders()));
    }

    public static void run(Retrofit retrofit, PropertiesHandler handler) {
        try (BookService service = new BookService(retrofit)) {
            service.loadParamsQueue(handler.getInitialParamAsInt("initial_capacity"));
            DescriptiveStatistics stats = service.runLoadTest(handler.getInitialParamAsInt("number_of_executions"));
            StatsUtil.printStats(stats, handler.getInitialParam("book_label"));
        } catch (InterruptedException| ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void loadParamsQueue(int initialCapacity) {
        queue = new ArrayBlockingQueue<>(initialCapacity);
        PropertiesHandler handler = PropertiesHandler.getInstance();
        String label = handler.getInitialParam("book_label");
        List<String> values = handler.getPathParamsFor(label);
        for (String value: values) {
            queue.offer(Integer.parseInt(value));
        }
    }
}
