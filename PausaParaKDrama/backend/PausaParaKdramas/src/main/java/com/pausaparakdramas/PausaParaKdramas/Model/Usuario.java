package com.pausaparakdramas.PausaParaKdramas.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuarios")
@Schema(description = "Representa un usuario del sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "firebase_uid", nullable = false, unique = true)
    private String firebaseUid;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "foto_perfil")
    private String fotoPerfil;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "rol")
    private List<String> roles;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDate.now();
    }

    // Relaciones
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonIgnore // Evita errores al serializar en JSON
    private List<Kdrama> kdramas = new ArrayList<>();



    @OneToMany(mappedBy = "usuario",cascade = CascadeType.ALL)
    private List<Valoracion> valoraciones = new ArrayList<>();

    @OneToMany(mappedBy = "usuario",cascade = CascadeType.ALL)
    private List<Seguimiento> seguimientos = new ArrayList<>();



}
