package club.w0sv.sl3;

import club.w0sv.aprsfi.AprsEntry;
import club.w0sv.sl3.gui.RosterPanel;
import club.w0sv.sl3.gui.TrackingPanel;
import club.w0sv.sl3.roster.RosterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

@SpringBootApplication
public class SpotterLevel3Application extends JFrame {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private RosterService rosterService;
    private AprsService aprsService;

    private RosterPanel rosterPanel;
    private TrackingPanel trackingPanel;

    public SpotterLevel3Application(@Autowired RosterService rosterService, @Autowired AprsService aprsService) {
        this.rosterService = rosterService;
        this.aprsService = aprsService;
        setTitle("SpotterLevel3");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        getContentPane().setLayout(new BorderLayout());
        
        rosterPanel = new RosterPanel(rosterService);
        rosterPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Roster"));
        getContentPane().add(rosterPanel, BorderLayout.WEST);
        
        trackingPanel = new TrackingPanel(rosterService, aprsService);
        trackingPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Tracking"));
        getContentPane().add(trackingPanel, BorderLayout.CENTER);
        
        JToolBar toolbar = new JToolBar("main toolbar", SwingConstants.HORIZONTAL);
        toolbar.setFloatable(false);
        add(toolbar, BorderLayout.NORTH);
        
        pack();
        
    }
    
    public static void main(String[] args) {

        var ctx = new SpringApplicationBuilder(SpotterLevel3Application.class)
                .headless(false).run(args);

        EventQueue.invokeLater(() -> {

            var ex = ctx.getBean(SpotterLevel3Application.class);
            ex.setVisible(true);
        });
    }
}