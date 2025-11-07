package com.pausaparakdramas.PausaParaKdramas.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Episodio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEpisodio;

    private String tituloEpisodio;


    private Integer numeroEpisodio;

    private Integer duracionEpisodio;

    private String sinopsisEpisodio;

    private LocalDate fechaEmisionEpisodio;
}
