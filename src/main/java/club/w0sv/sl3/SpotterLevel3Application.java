package club.w0sv.sl3;

import club.w0sv.sl3.gui.MainWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import systems.uom.common.USCustomary;
import tech.units.indriya.format.SimpleUnitFormat;

import java.awt.*;

@SpringBootApplication
public class SpotterLevel3Application  {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private MainWindow mainWindow;
    
    public SpotterLevel3Application(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        applyCustomUnitConfiguration();
    }
    
    private void applyCustomUnitConfiguration() {
        SimpleUnitFormat.getInstance().label(USCustomary.DEGREE_ANGLE,"°");
    }
    
    public static void main(String[] args) {

        var ctx = new SpringApplicationBuilder(SpotterLevel3Application.class)
                .web(WebApplicationType.SERVLET)
                .headless(false).run(args);

        EventQueue.invokeLater(() -> {

            var ex = ctx.getBean(SpotterLevel3Application.class);
            ex.mainWindow.setVisible(true);
        });
    }
}