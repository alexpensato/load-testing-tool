package org.pensatocode.loadtest.core.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.pensatocode.loadtest.core.errors.ErrorResponse;
import org.pensatocode.loadtest.core.handlers.PropertiesHandler;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class SingleTask<T> {

    public void execute(Call<T> callSync) throws IOException {
        Instant start = Instant.now();
        Response<T> response = callSync.execute();
        Instant end = Instant.now();
        if (response.isSuccessful()) {
            T item = response.body();
            assert item != null;
            if (PropertiesHandler.getInstance().getInitialParamAsBoolean("print_debug_log")) {
                log.debug("*****************************");
                log.debug(String.format("Task duration is %d milliseconds.", Duration.between(start, end).toMillis()));
            }
        } else {
            ObjectMapper mapper = new ObjectMapper();
            ResponseBody errorBody = response.errorBody();
            assert errorBody != null;
            ErrorResponse errorResponse = mapper.readValue(errorBody.string(), ErrorResponse.class);
            if (PropertiesHandler.getInstance().getInitialParamAsBoolean("print_debug_log")) {
                log.debug("*****************************");
                log.debug(String.format("Task finished with Error. Duration was %d milliseconds.", Duration.between(start, end).toMillis()));
                log.debug(errorResponse.toString());
                Map<String,Object> map = errorResponse.getAdditionalProperties();
                String mapAsString = map.keySet().stream()
                        .map(key -> key + "=" + map.get(key))
                        .collect(Collectors.joining(", ", "{", "}"));
                log.debug(mapAsString);
            }
        }

    }
}
