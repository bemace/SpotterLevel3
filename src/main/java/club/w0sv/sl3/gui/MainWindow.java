package club.w0sv.sl3.gui;

import club.w0sv.sl3.AprsService;
import club.w0sv.sl3.config.AprsFiConfig;
import club.w0sv.sl3.roster.RosterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@Component
public class MainWindow extends JFrame {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private AprsService aprsService;
    private AprsFiConfig aprsfiConfig;

    private JMenuBar menuBar;
    private RosterPanel rosterPanel;
    private TrackingPanel trackingPanel;

    public MainWindow(@Autowired RosterPanel rosterPanel, @Autowired AprsService aprsService, @Autowired AprsFiConfig aprsfiConfig) {
        this.rosterPanel = rosterPanel;
        this.aprsService = aprsService;
        this.aprsfiConfig = aprsfiConfig;
        setTitle("SpotterLevel3");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenu fileMenu = new JMenu("File",false);
        menuBar.add(fileMenu);
        fileMenu.add(new SettingsAction(this, aprsfiConfig));
        fileMenu.add(new ExitAction());
        
        getContentPane().setLayout(new BorderLayout());

        rosterPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Roster"));
        getContentPane().add(rosterPanel, BorderLayout.WEST);

        trackingPanel = new TrackingPanel(rosterPanel.getRosterService(), aprsService);
        trackingPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Tracking"));
        getContentPane().add(trackingPanel, BorderLayout.CENTER);

        JToolBar toolbar = new JToolBar("main toolbar", SwingConstants.HORIZONTAL);
        toolbar.setFloatable(false);
        add(toolbar, BorderLayout.NORTH);

        pack();

    }

    private static class SettingsAction extends AbstractAction {
        private Window parentComponent;
        private AprsFiConfig aprsfiConfig;
        
        public SettingsAction(Window parent, AprsFiConfig aprsfiConfig) {
            super("Settings");
            this.parentComponent = parent;
            this.aprsfiConfig = aprsfiConfig;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                SettingsWindow window = new SettingsWindow(aprsfiConfig);
                window.pack();
                window.setVisible(true);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(parentComponent,"error: ", ex.getLocalizedMessage(),JOptionPane.ERROR_MESSAGE);
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
