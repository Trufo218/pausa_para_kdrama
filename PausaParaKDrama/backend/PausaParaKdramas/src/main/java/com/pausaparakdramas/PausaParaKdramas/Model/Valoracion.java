package com.pausaparakdramas.PausaParaKdramas.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Valoracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idValoracion;

    private Integer puntaje;

    private String comentario;

    private LocalDate fechaValoracion;


    @ManyToOne
    @JoinColumn(name = "kdrama_id")
    private Kdrama kdrama;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
