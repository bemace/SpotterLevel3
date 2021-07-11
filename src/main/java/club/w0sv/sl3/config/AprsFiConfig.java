package club.w0sv.sl3.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("aprsfi")
public class AprsFiConfig {
    private String apikey;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }
}
