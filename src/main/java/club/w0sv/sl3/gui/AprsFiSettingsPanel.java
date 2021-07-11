package club.w0sv.sl3.gui;

import club.w0sv.sl3.config.AprsFiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.net.URI;

public class AprsFiSettingsPanel extends JPanel implements SettingsUI {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static URI aprsFi = URI.create("https://aprs.fi/");
    
    private AprsFiConfig aprsfiConfig;
    private JTextField aprsfiApiKey;

    public AprsFiSettingsPanel(AprsFiConfig config) {
        super(new BorderLayout());
        aprsfiConfig = config;

        JTextPane notes = new JTextPane();
        notes.setEditable(false);
        notes.setEditorKit(new HTMLEditorKit());
        notes.setText("<html><a href=\""+aprsFi +"\">aprs.fi</a> is used to obtain stations' locations."
                +" You can create an account and generate an API key for free by following the link.</html>");
        notes.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                }
                catch (Exception ex) {
                    logger.error("couldn't open link in browser: " + e.getURL(), ex);
                    JOptionPane.showMessageDialog(AprsFiSettingsPanel.this,"Unable to open your web browser: "+ex.getLocalizedMessage(), "Couldn't open link", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(notes, BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel(new GridLayout(0,2));
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(new JLabel("aprs.fi API key"));
        aprsfiApiKey = new JTextField(24);
        mainPanel.add(aprsfiApiKey);
    }

    @Override
    public void displaySettings() {
        aprsfiApiKey.setText(aprsfiConfig.getApikey());
    }

    @Override
    public void applyChanges() {
        aprsfiConfig.setApikey(aprsfiApiKey.getText().trim());
    }
}
