package com.Code.Compiler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // You can specify the origins allowed for CORS
                .allowedMethods("GET", "POST", "PUT", "DELETE") // You can specify the HTTP methods allowed for CORS
                .allowedHeaders("*"); // You can specify the headers allowed for CORS
    }
}
