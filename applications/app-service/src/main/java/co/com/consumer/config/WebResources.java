package co.com.consumer.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebResources {

    @Bean
    public WebProperties.Resources webProperties() {
        return new WebProperties.Resources();
    }
}
