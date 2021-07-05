package club.w0sv.sl3.gui;

import club.w0sv.sl3.AprsService;
import club.w0sv.sl3.roster.RosterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class MainWindow extends JFrame {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private RosterService rosterService;
    private AprsService aprsService;

    private RosterPanel rosterPanel;
    private TrackingPanel trackingPanel;

    public MainWindow(@Autowired RosterService rosterService, @Autowired AprsService aprsService) {
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

}
