package com.tripnest.auth.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Path;
@Configuration public class UploadConfig implements WebMvcConfigurer {
 @Override public void addResourceHandlers(ResourceHandlerRegistry registry){registry.addResourceHandler("/uploads/**").addResourceLocations(Path.of("uploads").toAbsolutePath().toUri().toString());}
}
