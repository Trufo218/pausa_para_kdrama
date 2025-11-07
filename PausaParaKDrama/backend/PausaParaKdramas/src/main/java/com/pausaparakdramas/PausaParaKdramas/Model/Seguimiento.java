package com.pausaparakdramas.PausaParaKdramas.Model;

import com.pausaparakdramas.PausaParaKdramas.Model.Enum.SeguimientoEstado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Seguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSeguimiento;

    private SeguimientoEstado estado;

    private Integer progreso;

    //Si cambia el estado, cambia la fecha.
    private LocalDate fechaActualizacion;


    //Relaciones entre entidades.

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


}
