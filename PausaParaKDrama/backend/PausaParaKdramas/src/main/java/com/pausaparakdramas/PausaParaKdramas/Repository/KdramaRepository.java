package com.pausaparakdramas.PausaParaKdramas.Repository;

import com.pausaparakdramas.PausaParaKdramas.Model.Kdrama;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KdramaRepository extends JpaRepository<Kdrama, Long> {
}
