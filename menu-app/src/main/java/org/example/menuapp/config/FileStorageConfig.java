package org.example.menuapp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@ConfigurationProperties(prefix = "file")
@Setter @Getter
public class FileStorageConfig {
    private String location;

    public Path getFilepath() {
        return Paths.get(location).toAbsolutePath().normalize();
    }
}
