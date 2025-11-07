package com.pausaparakdramas.PausaParaKdramas.Model;

import com.pausaparakdramas.PausaParaKdramas.Model.Enum.KdramaGenero;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "K-Dramas")
@Schema(description = "Representa un K-drama del sistema")

public class Kdrama {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Kdrama")
    private Long idKdrama;

    @NotBlank
    @Column(name = "titulo_K-drama",nullable = false)
    private String tituloKdrama;

    @NotNull
    @Column(name = "año")
    private LocalDate anio;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero")
    private KdramaGenero genero;

    @Column(name = "actores_principales")
    private String actoresPrincipales;

    @Column(name = "sinopsis", columnDefinition = "TEXT")
    private String sinopsis;
    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDate fechaRegistro;

    //Relaciones entre entidades.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;


    @OneToMany(mappedBy="kdrama",cascade= CascadeType.ALL)
    private List<Comentario> comentarios = new ArrayList<>();


    @OneToMany(mappedBy = "kdrama",cascade = CascadeType.ALL)
    private List<Valoracion> valoraciones = new ArrayList<>();

}
