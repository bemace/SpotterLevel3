package club.w0sv.sl3.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePaths {
    
    private static final FilePaths INSTANCE = new FilePaths();
    
    public static FilePaths getInstance() {
        return INSTANCE;
    }
    
    private Path userSettingsPath = Paths.get(System.getProperty("user.home"),"SpotterLevel3.properties");
    
    private FilePaths() {
        
    }
    
    public Path getUserSettingsPath() {
        return userSettingsPath;
    }
}
