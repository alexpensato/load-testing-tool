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

    public void execute(Call<List<T>> callSync) throws IOException {
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
}
