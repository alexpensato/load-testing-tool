package org.pensatocode.loadtest.core.interceptor;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Slf4j
public class LoggingInterceptor implements Interceptor {
    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        Instant start = Instant.now();
        log.info(String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        Instant end = Instant.now();
        log.info(String.format("Received response for %s in %dms%n%s",
                response.request().url(), Duration.between(start, end).toMillis(), response.headers()));

        return response;
    }
}
