package com.pausaparakdramas.PausaParaKdramas.Service.External;

import com.pausaparakdramas.PausaParaKdramas.Model.ExternalDTO.TmdbDetailsResponse;
import com.pausaparakdramas.PausaParaKdramas.Model.ExternalDTO.TmdbSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class TmdbApiService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.themoviedb.org/3")
            .build();

    public TmdbSearchResponse buscar(String nombre) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/tv")
                        .queryParam("query", nombre)
                        .queryParam("language", "es-ES")
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(TmdbSearchResponse.class)
                .block();
    }

    public TmdbDetailsResponse obtenerDetalle(int id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tv/" + id)
                        .queryParam("language", "es-ES")
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(TmdbDetailsResponse.class)
                .block();
    }
}
