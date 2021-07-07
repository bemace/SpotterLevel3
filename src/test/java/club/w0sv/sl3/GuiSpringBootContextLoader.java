package club.w0sv.sl3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootContextLoader;

public class GuiSpringBootContextLoader extends SpringBootContextLoader {
    @Override
    protected SpringApplication getSpringApplication() {
        SpringApplication  application = super.getSpringApplication();
        application.setHeadless(false);
        return application;
    }
}
