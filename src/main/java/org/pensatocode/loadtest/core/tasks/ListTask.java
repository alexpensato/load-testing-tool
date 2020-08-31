package org.pensatocode.loadtest.core.tasks;

import lombok.extern.slf4j.Slf4j;
import org.pensatocode.loadtest.core.handlers.PropertiesHandler;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
public class ListTask<T> {

    private final Call<List<T>> callSync;

    private ListTask(Call<List<T>> callSync) {
        this.callSync = callSync;
    }

    public void execute() throws IOException {
        Instant start = Instant.now();
        Response<List<T>> response = callSync.execute();
        List<T> list = response.body();
        assert list != null;
        Instant end = Instant.now();
        if (PropertiesHandler.getInstance().getInitialParamAsBoolean("print_debug_log")) {
            log.debug("*****************************");
            log.debug(String.format("Task duration is %d milliseconds.", Duration.between(start, end).toMillis()));
            for (T item : list) {
                log.debug(item.toString());
            }
        }
    }

    public static <V> ListTask<V> createTask(Call<List<V>> callSync) {
        return new ListTask<>(callSync);
    }
}
