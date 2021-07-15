package club.w0sv.sl3.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class IconManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private Map<String,Optional<Icon>> cache = new HashMap<>();
    
    public Optional<Icon> getJlfgrIcon(String path) {
        if (!cache.containsKey(path))
            cache.put(path, loadJlfgrIcon(path));
        
        return cache.get(path);
    }
    
    private Optional<Icon> loadJlfgrIcon(String path) {
        try {
            return Optional.of(new ImageIcon(ImageIO.read(getClass().getResource(path))));
        }
        catch (Exception ex) {
            logger.error("error loading {} from JLFGR", path, ex);
            return Optional.empty();
        }
    }
}
