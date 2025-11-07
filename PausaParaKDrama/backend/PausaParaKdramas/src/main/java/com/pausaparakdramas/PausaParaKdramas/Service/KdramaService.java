package com.pausaparakdramas.PausaParaKdramas.Service;

import com.pausaparakdramas.PausaParaKdramas.Repository.KdramaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KdramaService {

    private final KdramaRepository kdramaRepository;

    @Autowired
    public KdramaService(KdramaRepository kdramaRepository) {
        this.kdramaRepository = kdramaRepository;
    }

    //Crear un nuevo Kdrama
}
