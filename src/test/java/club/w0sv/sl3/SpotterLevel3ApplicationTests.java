package club.w0sv.sl3;

import club.w0sv.aprsfi.AprsFiClient;
import club.w0sv.sl3.config.AprsFiConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(loader=GuiSpringBootContextLoader.class)
class SpotterLevel3ApplicationTests {

    @MockBean
    private AprsFiClient client;
    
    @Test
    void contextLoads() {
    }

}
