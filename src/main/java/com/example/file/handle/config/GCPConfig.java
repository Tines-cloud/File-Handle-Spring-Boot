package com.example.file.handle.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GCPConfig {

    @Bean
    public Storage storage() {
        try (InputStream credentialsStream = new ClassPathResource("gcp-key.json").getInputStream()) {

            GoogleCredentials credentials = ServiceAccountCredentials.fromStream(credentialsStream);

            return StorageOptions.newBuilder()
                    .setCredentials(credentials)
                    .build()
                    .getService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
