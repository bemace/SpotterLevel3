package club.w0sv.sl3.gui;

import club.w0sv.sl3.config.AprsFiConfig;
import club.w0sv.sl3.config.FilePaths;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DefaultPropertiesPersister;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@Component
public class SettingsWindow extends LateInitFrame implements SettingsUI {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private AprsFiConfig aprsfiConfig;

    private AprsFiSettingsPanel aprsFiSettingsPanel;
    private WebServerConfigPanel webServerConfigPanel;
    private Set<SettingsUI> configPanels = new HashSet<>();

    public SettingsWindow(AprsFiConfig aprsfiConfig) {
        setTitle("Settings");
        this.aprsfiConfig = aprsfiConfig;

    }

    @Override
    protected void initializeContent() {
        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);
        contentPane.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        contentPane.add(mainPanel, BorderLayout.CENTER);

        webServerConfigPanel.setBorder(BorderFactory.createTitledBorder(webServerConfigPanel.getBorder(), "Embedded Web Server"));
        mainPanel.add(webServerConfigPanel);

        aprsFiSettingsPanel.setBorder(BorderFactory.createTitledBorder(aprsFiSettingsPanel.getBorder(), "aprs.fi"));
        mainPanel.add(aprsFiSettingsPanel);
        
        JPanel buttonBar = new JPanel();
        JButton okButton = new JButton(new OkAction());
        buttonBar.add(okButton);
        buttonBar.add(new JButton(new CancelAction()));
        buttonBar.add(new JButton(new ApplyAction()));
        getRootPane().setDefaultButton(okButton);

        getContentPane().add(buttonBar, BorderLayout.SOUTH);
    }

    @Override
    public void displaySettings() {
        logger.trace("displaying current configuration values");
        for (SettingsUI panel : configPanels)
            panel.displaySettings();
    }

    @Override
    public void applyChanges() {
        logger.trace("applying user's changes");
        for (SettingsUI panel : configPanels)
            panel.applyChanges();

        logger.info("applied user's configuration changes");
    }

    public void writeSettingsToDisk() throws IOException {
        Properties props = new Properties();
        storeSettings(props);
        File file = FilePaths.getInstance().getUserSettingsPath().toFile();
        try (OutputStream os = new FileOutputStream(file)) {
            DefaultPropertiesPersister dpp = new DefaultPropertiesPersister();
            dpp.store(props, os, "SpotterLevel3 settings");
            logger.info("wrote settings to {}", file);
        }
        catch (Exception ex) {
            throw new IOException("error saving settings to " + file, ex);
        }
    }

    @Override
    public void storeSettings(Properties props) {
        for (SettingsUI panel : configPanels)
            panel.storeSettings(props);
    }

    public void close() {
        SettingsWindow.this.dispatchEvent(new WindowEvent(SettingsWindow.this, WindowEvent.WINDOW_CLOSING));
    }

    public WebServerConfigPanel getWebServerConfigPanel() {
        return webServerConfigPanel;
    }

    @Autowired
    public void setWebServerConfigPanel(WebServerConfigPanel webServerConfigPanel) {
        configPanels.remove(this.webServerConfigPanel);
        this.webServerConfigPanel = webServerConfigPanel;
        configPanels.add(webServerConfigPanel);
    }

    public AprsFiSettingsPanel getAprsFiSettingsPanel() {
        return aprsFiSettingsPanel;
    }

    @Autowired
    public void setAprsFiSettingsPanel(AprsFiSettingsPanel aprsFiSettingsPanel) {
        configPanels.remove(this.aprsFiSettingsPanel);
        this.aprsFiSettingsPanel = aprsFiSettingsPanel;
        configPanels.add(aprsFiSettingsPanel);
    }

    private class OkAction extends AbstractAction {
        public OkAction() {
            super("OK");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                applyChanges();
                writeSettingsToDisk();
            }
            catch (Exception ex) {
                logger.error("error applying changes", ex);
                JOptionPane.showMessageDialog(SettingsWindow.this, "Error applying changes: " + ExceptionUtils.getRootCauseMessage(ex), "Settings NOT updated", JOptionPane.ERROR_MESSAGE);
            }

            close();
        }
    }

    private class CancelAction extends AbstractAction {
        public CancelAction() {
            super("Cancel");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            close();
        }
    }

    private class ApplyAction extends AbstractAction {
        public ApplyAction() {
            super("Apply");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                applyChanges();
                writeSettingsToDisk();
            }
            catch (Exception ex) {
                logger.error("error applying changes", ex);
                JOptionPane.showMessageDialog(SettingsWindow.this, "Error applying changes: " + ExceptionUtils.getRootCauseMessage(ex), "Settings NOT updated", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
