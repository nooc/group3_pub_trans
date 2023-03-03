package space.nixus.pubtrans.config;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.maps.GeoApiContext;

import lombok.extern.log4j.Log4j2;

/**
 * General app configurations.
 */
@Configuration
@Log4j2
public class AppConfig {

    @Value("${MAPS_KEY}")
    private String mapsKey;

    /**
     * Logger instance.
     * @return
     */
    @Bean
    Logger getLogger() {
        return log;
    }

    /**
     * Google GeoApiContext instance.
     * @return
     */
    @Bean
    GeoApiContext getGeoApiContext() {
        return new GeoApiContext.Builder()
            .apiKey(mapsKey)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .disableRetries()
            .build();
    }
}
