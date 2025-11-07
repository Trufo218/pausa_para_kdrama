package com.pausaparakdramas.PausaParaKdramas.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FileController {

    // Ruta donde se guardarán los archivos
    private static final String UPLOAD_DIR = "uploads";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Crear la carpeta si no existe
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Generar nombre único para evitar colisiones
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File dest = new File(uploadDir, filename);

            // Guardar el archivo
            file.transferTo(dest);

            // Retornar la ruta que podrá usar el front
            String fileUrl = "/uploads/" + filename;
            return ResponseEntity.ok(fileUrl);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al subir la imagen");
        }
    }
}
