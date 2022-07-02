package com.abidevel.oauth.healthmetricservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource("classpath:key.properties")
@ConfigurationProperties(prefix = "spring.auth")
@Data
public class KeyConfiguration {
    private String publicKey;
    
}
