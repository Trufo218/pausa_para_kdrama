package com.pausaparakdramas.PausaParaKdramas.Repository;

import com.pausaparakdramas.PausaParaKdramas.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByFirebaseUid(String firebaseUid);
    Optional<Usuario> findByEmail(String email);
}
