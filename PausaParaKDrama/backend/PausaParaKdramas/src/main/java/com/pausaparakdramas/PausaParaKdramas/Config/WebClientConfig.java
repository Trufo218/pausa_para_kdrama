package com.pausaparakdramas.PausaParaKdramas.Config;

import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Configuration
public class WebClientConfig {

    @Value("b9125aaaa72cb16a6c1d28fa89e4f9ca")
    private String apiKey;


    @Bean
    public WebClient tmdbClient() {

        return WebClient.builder()
                .baseUrl("https://api.themoviedb.org/3")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .filter(addApiKeyFilter())
                .build();
    }

    // Añade automáticamente api_key a TODAS las peticiones
    private ExchangeFilterFunction addApiKeyFilter() {
        return (request, next) -> {
            URI newUri = UriComponentsBuilder.fromUri(request.url())
                    .queryParam("api_key", apiKey)
                    .queryParam("language", "es-ES")   // TMDB en español
                    .build(true)
                    .toUri();

            ClientRequest newRequest = ClientRequest.from(request)
                    .url(newUri)
                    .build();

            return next.exchange(newRequest);
        };
    }
}
