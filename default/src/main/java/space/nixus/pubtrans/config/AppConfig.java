package space.nixus.pubtrans.config;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;

import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
public class AppConfig {

    @Value("${space.nixus.micro-services-378415.maps}")
    private String mapsKey;

    @Bean
    Logger getLogger() {
        return log;
    }

    @Bean
    GeoApiContext getGeoApiContext() {
        new GeoApiContext.Builder()
            .apiKey()
    }
}
