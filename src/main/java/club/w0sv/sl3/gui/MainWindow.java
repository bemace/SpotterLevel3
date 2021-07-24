package club.w0sv.sl3.gui;

import club.w0sv.sl3.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@Component
public class MainWindow extends LateInitFrame {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private SettingsWindow settingsWindow;
    private LocationService locationService;

    private JMenuBar menuBar;
    private RosterPanel rosterPanel;
    private TrackingPanel trackingPanel;

    public MainWindow(@Autowired RosterPanel rosterPanel, @Autowired LocationService locationService, @Autowired SettingsWindow settingsWindow) {
        this.rosterPanel = rosterPanel;
        this.locationService = locationService;
        this.settingsWindow = settingsWindow;
        setTitle("SpotterLevel3");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    protected void initializeContent() {
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File",false);
        menuBar.add(fileMenu);
        fileMenu.add(new SettingsAction());
        fileMenu.add(new ExitAction());

        getContentPane().setLayout(new BorderLayout());

        rosterPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Roster"));
        getContentPane().add(rosterPanel, BorderLayout.WEST);

        trackingPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Tracking"));
        getContentPane().add(trackingPanel, BorderLayout.CENTER);

        JToolBar toolbar = new JToolBar("main toolbar", SwingConstants.HORIZONTAL);
        toolbar.setFloatable(false);
        add(toolbar, BorderLayout.NORTH);

        pack();
    }

    public TrackingPanel getTrackingPanel() {
        return trackingPanel;
    }

    @Autowired
    public void setTrackingPanel(TrackingPanel trackingPanel) {
        this.trackingPanel = trackingPanel;
    }

    private class SettingsAction extends AbstractAction {
        
        public SettingsAction() {
            super("Settings");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                settingsWindow.displaySettings();
                settingsWindow.pack();
                settingsWindow.setVisible(true);
            }
            catch (Exception ex) {
                logger.error("error loading settings window", ex);
                JOptionPane.showMessageDialog(MainWindow.this,"error: ", ex.getLocalizedMessage(),JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private static class ExitAction extends AbstractAction {

        public ExitAction() {
            super("Exit");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
