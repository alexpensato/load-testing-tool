package org.pensatocode.loadtest;

import org.pensatocode.loadtest.core.handlers.PropertiesHandler;
import org.pensatocode.loadtest.services.BookService;
import org.pensatocode.loadtest.services.PageOfBooksService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

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
        PageOfBooksService.run(retrofit,handler);
        BookService.run(retrofit,handler);
    }
}
