package club.w0sv.sl3.gui;

import club.w0sv.sl3.config.AprsFiConfig;
import club.w0sv.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.net.URI;
import java.util.Properties;

@Component
public class AprsFiSettingsPanel extends JPanel implements SettingsUI {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static URI aprsFi = URI.create("https://aprs.fi/");
    
    private AprsFiConfig aprsfiConfig;
    private JTextField aprsfiApiKey;

    public AprsFiSettingsPanel(AprsFiConfig config) {
        super(new BorderLayout());
        aprsfiConfig = config;

        add(new CallOut(CallOut.Type.INFO, "<html><a href=\""+aprsFi +"\">aprs.fi</a> is used to obtain stations' locations."
                +" You can get your API key from your <a href=\"https://aprs.fi/account/\">My Account</a> page.</html>"), BorderLayout.SOUTH);
        
        JPanel mainPanel = new JPanel(new GridLayout(0,2));
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(new JLabel("API key"));
        aprsfiApiKey = new JTextField(24);
        mainPanel.add(aprsfiApiKey);
    }
    
    public String getApiKey() {
        return StringUtils.trim(aprsfiApiKey.getText());
    }
    
    public void setApiKey(String key) {
        aprsfiApiKey.setText(key);
    }

    @Override
    public void displaySettings() {
        setApiKey(aprsfiConfig.getApikey());
    }

    @Override
    public void applyChanges() {
        aprsfiConfig.setApikey(getApiKey());
    }
    
    @Override
    public void storeSettings(Properties props) {
        String key = getApiKey();
        if (key == null)
            props.remove("aprsfi.apikey");
        else
            props.put("aprsfi.apikey", key);
    }
}
