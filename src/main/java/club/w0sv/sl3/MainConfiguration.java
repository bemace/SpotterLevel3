package club.w0sv.sl3;

import club.w0sv.aprsfi.AprsFiClient;
import club.w0sv.sl3.config.AprsFiConfig;
import club.w0sv.sl3.config.PlaceFileConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties({AprsFiConfig.class, PlaceFileConfig.class })
@EnableCaching
public class MainConfiguration {
    
    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }
    
    @Bean
    public RestTemplate restTemplate(@Autowired RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
    
    @Bean
    public AprsFiClient aprsFiClient(AprsFiConfig aprsfiConfig, RestTemplateBuilder restTemplateBuilder) {
        AprsFiClient client = new AprsFiClient(aprsfiConfig, restTemplateBuilder);
        return client;
    }
}
