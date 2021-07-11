package club.w0sv.sl3.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class UserPropertiesLoader implements EnvironmentPostProcessor {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final PropertiesPropertySourceLoader loader = new PropertiesPropertySourceLoader();
    
    public UserPropertiesLoader() {
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
         Resource userConfigPath = new FileSystemResource(FilePaths.getInstance().getUserSettingsPath());
         
         if (userConfigPath.exists()) {
             logger.debug("reading user settings from {}", userConfigPath);
             try {
                 PropertySource<?> userPropertySource = loader.load("custom-resource", userConfigPath).get(0);
                 environment.getPropertySources().addFirst(userPropertySource);
             }
             catch (Exception ex) {
                 logger.error("error loading user settings from {}", userConfigPath, ex);
             }
         }
         else {
             logger.info("User configuration file {} not found, using default settings", userConfigPath);
         }

    }
}
