package com.pausaparakdramas.PausaParaKdramas.Config_firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initializeFirebase() throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) {
            System.out.println("⚙️ Firebase ya estaba inicializado.");
            return;
        }

        try (InputStream serviceAccount = getClass()
                .getClassLoader()
                .getResourceAsStream("firebase/pausaparakdrama.json")) {

            if (serviceAccount == null) {
                throw new IOException("❌ No se encontró el archivo firebase/pausaparakdrama.json");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase inicializado correctamente.");
        }
    }
}
