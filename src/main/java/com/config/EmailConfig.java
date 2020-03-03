package com.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author super lollipop
 * @date 20-2-26
 */
@Configuration
@ConfigurationProperties(prefix = "email")
@Data
public class EmailConfig {
    private String hostname;
    private String username;
    private String password;
}
