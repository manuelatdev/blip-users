package com.malbadalejo.users.infrastructure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Configurador para permitir solicitudes desde dispositivos en la red local (192.168.1.x).
// Define políticas CORS (Cross-Origin Resource Sharing) para restringir el acceso a orígenes específicos.
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://192.168.1.[0-255]:3000", "http://localhost:*")
                .allowedMethods("GET", "POST")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}