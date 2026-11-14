package com.example.uberbookingservice.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfiguration {
    @Value("${location.service.base-url}")
    private String locationServiceUrl;

    @Bean
    public Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(locationServiceUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
