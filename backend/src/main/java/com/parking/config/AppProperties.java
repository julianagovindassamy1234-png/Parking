package com.parking.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.auto-approval")
@Getter
@Setter
public class AppProperties {

    private int windowHours = 24;
    private String schedulerCron = "0 */15 * * * *";
}
