package com.pausaparakdramas.PausaParaKdramas.Repository;

import com.pausaparakdramas.PausaParaKdramas.Model.Kdrama;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KdramaRepository extends JpaRepository<Kdrama, Long> {

    //Métodos personalizados.
    Optional<Kdrama> findByTituloKdrama(String tituloKdrama);
}
