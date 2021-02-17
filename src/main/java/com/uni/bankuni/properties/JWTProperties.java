package com.uni.bankuni.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JWTProperties {

    private String secretKey;
    private long expirationTimeInMs;
}
