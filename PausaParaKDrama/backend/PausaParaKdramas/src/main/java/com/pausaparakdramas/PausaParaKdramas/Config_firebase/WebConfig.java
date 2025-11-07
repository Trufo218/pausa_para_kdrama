package com.pausaparakdramas.PausaParaKdramas.Config_firebase;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://127.0.0.1:5500",
                        "http://localhost:5500",
                        "http://localhost:5173",
                        "http://localhost:8080"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapear la carpeta "uploads" para que sea accesible desde el front
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
