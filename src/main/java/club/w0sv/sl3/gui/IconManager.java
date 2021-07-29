package club.w0sv.sl3.gui;

import club.w0sv.sl3.AprsSymbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class IconManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private Map<String,Optional<Icon>> cache = new HashMap<>();
    
    public Optional<Icon> getJlfgrIcon(String resourceName) {
        return getIcon(resourceName);
    }
    
    
    private Optional<Icon> loadIcon(String resourceName) {
        try {
            return Optional.of(new ImageIcon(ImageIO.read(getClass().getResource(resourceName))));
        }
        catch (Exception ex) {
            logger.error("error icon from {}", resourceName, ex);
            return Optional.empty();
        }
    }
    
    private String toResourceName(Path path) {
        StringBuilder sb = new StringBuilder();
        if (path.isAbsolute())
            sb.append('/');
        
        for (int i = 0; i < path.getNameCount(); i++) {
            if (sb.length() > 0)
                sb.append('/');
            sb.append(path.getName(i));
        }
        
        return sb.toString();
    }
    
    private Optional<Icon> getIcon(String resourceName) {
        if (!cache.containsKey(resourceName))
            cache.put(resourceName, loadIcon(resourceName));

        return cache.get(resourceName);
    }
    
    public Optional<Icon> getIcon(AprsSymbol symbol) {
        String resourceName = "/icons/APRS/symbol-" + ((int) symbol.getSymbolIdentifier()) + "-" + ((int) symbol.getTableIdentifier()) + ".png";
        return getIcon(resourceName);
    }
}
