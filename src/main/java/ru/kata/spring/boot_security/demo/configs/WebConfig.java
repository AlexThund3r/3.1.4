package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");  // Обрабатываем статические ресурсы
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Настройка CORS
        registry.addMapping("/**")  // Разрешаем доступ ко всем путям
                .allowedOrigins("http://localhost:8080")  // Разрешенные источники (если фронтенд на порту 8080)
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Разрешенные HTTP методы
                .allowedHeaders("*")  // Разрешенные заголовки
                .allowCredentials(true);  // Разрешить отправку cookies
    }
}
