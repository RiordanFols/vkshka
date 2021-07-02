package ru.chernov.prosto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Pavel Chernov
 */
@Configuration
public class UploadConfig {

    @Value("${upload.path}")
    private String uploadPath;

    @Bean
    public String avatarPath(@Value("${upload.path.avatar}") String avatarPath) {
        return uploadPath + avatarPath;
    }

    @Bean
    public String imagePath(@Value("${upload.path.images}") String imagePath) {
        return uploadPath + imagePath;
    }
}
