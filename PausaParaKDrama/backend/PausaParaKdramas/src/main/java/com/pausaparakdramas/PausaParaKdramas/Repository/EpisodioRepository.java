package com.pausaparakdramas.PausaParaKdramas.Repository;

import com.pausaparakdramas.PausaParaKdramas.Model.Episodio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodioRepository extends JpaRepository<Episodio, Integer> {
}
