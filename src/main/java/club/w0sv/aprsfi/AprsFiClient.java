package club.w0sv.aprsfi;


import club.w0sv.util.AprsId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class AprsFiClient {
    private static final int MAX_TARGETS_PER_REQUEST = 20;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private RestTemplate restTemplate;
    
    private String apikey;
    
    public AprsFiClient(String apikey, RestTemplateBuilder builder) {
        if (apikey == null)
            throw new IllegalArgumentException("API key not provided");
        this.apikey = apikey;
        
        restTemplate = builder
                .defaultHeader("User-Agent", "SpotterLevel3 (https://w0sv.club)")
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
    }
    
    public Collection<AprsEntry> findLocations(Set<AprsId> callsigns) throws IOException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.aprs.fi/api/get")
                .queryParam("name",String.join(",",callsigns.stream().map(i -> i.toString()).collect(Collectors.toList())))
                .queryParam("what",What.LOCATION.toApiString())
                .queryParam("apikey", apikey)
                .queryParam("format", "json")
                ;
        Response response = restTemplate.getForObject(builder.toUriString(), Response.class);
        if (response.getResult() == Result.OK)
            logger.debug("got {} {}s from aprs.fi", response.getFound(), response.getWhat());
        else
            throw new IOException("aprs.fi query failed");
        
        return response.getEntries();
    }
}
