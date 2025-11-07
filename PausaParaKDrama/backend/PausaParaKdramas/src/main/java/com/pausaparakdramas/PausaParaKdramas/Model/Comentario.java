package com.pausaparakdramas.PausaParaKdramas.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComentario;


    @Column(nullable = false, length = 500)
    private String texto;

    private LocalDate fechaComentario;


    //Antes de ingresar a la BD.
    @PrePersist
    public void asignarFechaActual() {
        this.fechaComentario = LocalDate.now();
    }

    //Relaciones entre entidades.
    @ManyToOne
    @JoinColumn(name = "kdrama_id")
    private Kdrama kdrama;
}
