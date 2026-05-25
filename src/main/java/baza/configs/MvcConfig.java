package baza.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 Простая MVC-настройка для HTML-страниц.
 Здесь мы связываем URL со страницами из src/main/resources/templates.
 Для этих страниц не нужен отдельный Controller, потому что они просто
 возвращают HTML, а данные потом догружаются через JavaScript.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        // /user откроет templates/user.html
        registry.addViewController("/user").setViewName("user");
        // /admin откроет templates/admin.html
        registry.addViewController("/admin").setViewName("admin");
    }
}

