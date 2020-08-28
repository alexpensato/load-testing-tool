package org.pensatocode.loadtest.services;

import lombok.extern.slf4j.Slf4j;
import org.pensatocode.loadtest.clients.BookApiClient;
import org.pensatocode.loadtest.core.handlers.PropertiesHandler;
import org.pensatocode.loadtest.core.pagination.Page;
import org.pensatocode.loadtest.core.service.AbstractService;
import org.pensatocode.loadtest.models.Book;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class BookService extends AbstractService {

    public BookService(Retrofit retrofit) {
        super(retrofit);
    }

    @Override
    public void internalTask() throws IOException {
        BookApiClient bookApiClient = retrofit.create(BookApiClient.class);
        Call<Page<Book>> callSync = bookApiClient.getBooks(0, PropertiesHandler.get().getHeaders());
        Response<Page<Book>> response = callSync.execute();
        Page<Book> pageOfBooks = response.body();
        assert pageOfBooks != null;
        List<Book> books = pageOfBooks.getContent();
        for (Book book : books) {
            log.debug(book.toString());
        }
    }
}
