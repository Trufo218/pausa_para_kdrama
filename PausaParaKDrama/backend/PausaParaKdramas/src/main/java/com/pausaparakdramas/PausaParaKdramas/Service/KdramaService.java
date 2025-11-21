package com.pausaparakdramas.PausaParaKdramas.Service;

import com.pausaparakdramas.PausaParaKdramas.Model.Enum.KdramaGenero;
import com.pausaparakdramas.PausaParaKdramas.Model.ExternalDTO.TmdbDetailsResponse;
import com.pausaparakdramas.PausaParaKdramas.Model.ExternalDTO.TmdbSearchResponse;
import com.pausaparakdramas.PausaParaKdramas.Model.Kdrama;
import com.pausaparakdramas.PausaParaKdramas.Repository.KdramaRepository;
import com.pausaparakdramas.PausaParaKdramas.Service.External.TmdbApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KdramaService {

    private final KdramaRepository kdramaRepository;
    private final TmdbApiService tmdbApiService;

    public Kdrama buscarYGuardar(String nombre) {

        // 1. Verificar si ya existe
        var existente = kdramaRepository.findByTituloKdrama(nombre);
        if (existente.isPresent()) return existente.get();

        // 2. Buscar en TMDB
        TmdbSearchResponse search = tmdbApiService.buscar(nombre);

        if (search == null || search.getResults() == null || search.getResults().isEmpty()) {
            throw new RuntimeException("No se encontró la serie en TMDB");
        }

        var resultados = search.getResults();

        // 3. Selección inteligente del Kdrama
        var first = resultados.stream()
                // Preferir series coreanas (las únicas que son Kdramas)
                .filter(r ->
                        r.getOriginCountry() != null &&
                                r.getOriginCountry().contains("KR"))
                .findFirst()

                //  Coincidencia exacta
                .or(() -> resultados.stream()
                        .filter(r -> r.getName() != null &&
                                r.getName().equalsIgnoreCase(nombre))
                        .findFirst())

                //  Coincidencia parcial
                .or(() -> resultados.stream()
                        .filter(r -> r.getName() != null &&
                                r.getName().toLowerCase().contains(nombre.toLowerCase()))
                        .findFirst())

                //  Último recurso: primera opción
                .orElse(resultados.get(0));

        System.out.println("SELECCIONADO DE TMDB → " + first);

        // 4. Detalles completos
        TmdbDetailsResponse details = tmdbApiService.obtenerDetalle(first.getId());

        // 5. Crear la entidad
        Kdrama nuevo = new Kdrama();
        nuevo.setTituloKdrama(details.getName());
        nuevo.setSinopsis(details.getOverview());

        // Poster
        String poster = details.getPosterPath();
        if (poster != null && !poster.equals("null") && !poster.isBlank()) {
            nuevo.setImagenUrl("https://image.tmdb.org/t/p/w500" + poster);
        } else {
            nuevo.setImagenUrl(null);
        }

        // Año
        Integer year = 0;
        if (details.getFirstAirDate() != null && !details.getFirstAirDate().isBlank()) {
            try {
                year = Integer.parseInt(details.getFirstAirDate().substring(0, 4));
            } catch (Exception ignored) {}
        }
        nuevo.setAnio(year);

        // Género
        if (details.getGenres() != null && !details.getGenres().isEmpty()) {
            String tmdbGenreName = details.getGenres().get(0).getName();
            nuevo.setGenero(KdramaGenero.fromTmdb(tmdbGenreName));
        } else {
            nuevo.setGenero(KdramaGenero.DESCONOCIDO);
        }

        nuevo.setFechaRegistro(LocalDate.now());

        return kdramaRepository.save(nuevo);
    }

    //Listar todos lo KDramas
    public List<Kdrama> listarTodos() {
        return kdramaRepository.findAll();
    }

    //Borrar KDramas
    public void eliminar(Long id) {
        kdramaRepository.deleteById(id);
    }
}
