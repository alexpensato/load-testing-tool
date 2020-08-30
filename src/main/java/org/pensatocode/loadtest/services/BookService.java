package org.pensatocode.loadtest.services;

import lombok.extern.slf4j.Slf4j;
import org.pensatocode.loadtest.clients.BookApiClient;
import org.pensatocode.loadtest.core.handlers.PropertiesHandler;
import org.pensatocode.loadtest.core.service.AbstractService;
import org.pensatocode.loadtest.models.Book;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class BookService extends AbstractService {

    private BlockingQueue<Integer> queue;

    public BookService(Retrofit retrofit) {
        super(retrofit);
    }

    @Override
    public void internalTask() throws IOException, InterruptedException {
        BookApiClient bookApiClient = retrofit.create(BookApiClient.class);
        Integer pathId = queue.poll(2, TimeUnit.SECONDS);
        Call<Book> callSync = bookApiClient.getOneBook(pathId, PropertiesHandler.getInstance().getHeaders());
        Response<Book> response = callSync.execute();
        Book book = response.body();
        queue.offer(pathId, 2, TimeUnit.SECONDS);
        assert book != null;
        log.debug(book.toString());
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
