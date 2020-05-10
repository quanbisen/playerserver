package com.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author super lollipop
 * @date 5/10/20
 */
@Configuration
@ConfigurationProperties(prefix = "server")
@Data
public class ServerConfig {
    private String hostname;
}
