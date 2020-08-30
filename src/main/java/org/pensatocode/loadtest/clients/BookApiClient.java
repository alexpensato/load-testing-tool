package org.pensatocode.loadtest.clients;

import org.pensatocode.loadtest.models.Book;
import org.pensatocode.loadtest.core.pagination.Page;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.Map;

public interface BookApiClient {

    @GET("/api/books")
    Call<Page<Book>> getBooks(@Query("page") int page, @HeaderMap Map<String, String> headers);

    @GET("/api/books/{id}")
    Call<Book> getOneBook(@Path("id") Integer bookId, @HeaderMap Map<String, String> headers);
}
