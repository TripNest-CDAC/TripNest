package com.tripnest.crud.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Path;
@Configuration
public class UploadConfig implements WebMvcConfigurer {
    private final String directory;
    public UploadConfig(@Value("${app.upload.directory:uploads}") String directory) { this.directory = directory; }
    @Override public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**").addResourceLocations(Path.of(directory).toAbsolutePath().normalize().toUri().toString());
    }
}
