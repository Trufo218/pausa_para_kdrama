package com.pausaparakdramas.PausaParaKdramas.Controller;

import com.pausaparakdramas.PausaParaKdramas.Model.Kdrama;
import com.pausaparakdramas.PausaParaKdramas.Service.KdramaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kdramas")
@CrossOrigin(origins = "http://localhost:5500") // o el puerto donde sirves el front
@RequiredArgsConstructor
public class KdramaController {

    private final KdramaService kdramaService;

    @GetMapping("/listar")
    public List<Kdrama> listar() {
        return kdramaService.listarTodos();
    }

    @GetMapping("/buscar")
    public Kdrama buscar(@RequestParam("q") String titulo) {
        return kdramaService.buscarYGuardar(titulo);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        kdramaService.eliminar(id);
    }
}
