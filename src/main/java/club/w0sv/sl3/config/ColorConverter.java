package club.w0sv.sl3.config;

import java.awt.*;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.stereotype.Component;

import org.springframework.core.convert.converter.Converter ;

@Component
@ConfigurationPropertiesBinding
public class ColorConverter implements Converter<String, Color> {

    @Override
    public Color convert(String source) {
        String[] parts = source.split(",");
        return new Color(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }
}
