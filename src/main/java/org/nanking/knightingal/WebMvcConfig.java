package org.nanking.knightingal;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("file:/home/knightingal/download/linux1000/");
        registry.addResourceHandler("/dist/**").addResourceLocations("classpath:static/frontEnd/dist/");
        registry.addResourceHandler("/lib/**").addResourceLocations("classpath:static/frontEnd/node_modules/");
        registry.addResourceHandler("/frontEnd/**").addResourceLocations("classpath:static/frontEnd/");
    }
}
