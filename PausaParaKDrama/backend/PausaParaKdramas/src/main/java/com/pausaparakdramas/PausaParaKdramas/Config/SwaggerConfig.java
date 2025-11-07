package com.pausaparakdramas.PausaParaKdramas.Config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Kdramas")
                        .version("1.0")
                        .description("Documentación de la API para gestión de una web sobre Kdramas"));
    }
}


