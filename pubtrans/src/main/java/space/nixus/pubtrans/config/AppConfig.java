package space.nixus.pubtrans.config;

import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
public class AppConfig {

    @Bean
    Logger getLogger() {
        return log;
    }
}
