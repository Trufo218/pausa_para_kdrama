package com.pausaparakdramas.PausaParaKdramas.Model.ExternalDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TmdbSearchResponse {

    @JsonProperty("results")
    private List<Result> results;

    @Data
    public static class Result {

        private int id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("original_name")
        private String originalName;

        @JsonProperty("overview")
        private String overview;

        @JsonProperty("origin_country")
        private List<String> originCountry;

        @JsonProperty("original_language")
        private String originalLanguage;

        @JsonProperty("poster_path")
        private String posterPath;

        @JsonProperty("first_air_date")
        private String firstAirDate;
    }
}
