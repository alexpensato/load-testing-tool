package org.pensatocode.loadtest.core.tasks;

import lombok.extern.slf4j.Slf4j;
import org.pensatocode.loadtest.core.handlers.PropertiesHandler;
import org.pensatocode.loadtest.core.pagination.Page;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
public class PageTask<T> {

    private final Call<Page<T>> callSync;

    private PageTask(Call<Page<T>> callSync) {
        this.callSync = callSync;
    }

    public void execute() throws IOException {
        Instant start = Instant.now();
        Response<Page<T>> response = callSync.execute();
        Page<T> page = response.body();
        assert page != null;
        Instant end = Instant.now();
        List<T> list = page.getContent();
        if (PropertiesHandler.getInstance().getInitialParamAsBoolean("print_debug_log")) {
            log.debug("*****************************");
            log.debug(String.format("Task duration is %d milliseconds.", Duration.between(start, end).toMillis()));
            for (T item : list) {
                log.debug(item.toString());
            }
        }
    }

    public static <V> PageTask<V> createTask(Call<Page<V>> callSync) {
        return new PageTask<>(callSync);
    }
}
