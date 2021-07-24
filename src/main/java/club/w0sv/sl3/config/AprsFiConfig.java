package club.w0sv.sl3.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("aprsfi")
public class AprsFiConfig {
    private static final Duration DEFAULT_AUTO_UPDATE_INTERVAL = Duration.ofSeconds(60);
    private String apikey;
    private Duration autoUpdateInterval = DEFAULT_AUTO_UPDATE_INTERVAL;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public Duration getAutoUpdateInterval() {
        return autoUpdateInterval;
    }

    public void setAutoUpdateInterval(Duration autoUpdateInterval) {
        this.autoUpdateInterval = autoUpdateInterval == null ? DEFAULT_AUTO_UPDATE_INTERVAL : autoUpdateInterval;
    }
}
