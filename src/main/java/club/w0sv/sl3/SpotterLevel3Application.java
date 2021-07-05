package club.w0sv.sl3;

import club.w0sv.aprsfi.AprsEntry;
import club.w0sv.sl3.gui.MainWindow;
import club.w0sv.sl3.gui.RosterPanel;
import club.w0sv.sl3.gui.TrackingPanel;
import club.w0sv.sl3.roster.RosterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

@SpringBootApplication
public class SpotterLevel3Application  {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private MainWindow mainWindow;
    
    public SpotterLevel3Application(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
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