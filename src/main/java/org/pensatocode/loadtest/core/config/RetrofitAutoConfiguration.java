package org.pensatocode.loadtest.core.config;

import org.pensatocode.loadtest.core.handlers.PropertiesHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RetrofitAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Retrofit retrofit() {
        PropertiesHandler handler = PropertiesHandler.getInstance();
        return new Retrofit.Builder()
                .baseUrl(handler.getInitialParam("base_api_url"))
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
