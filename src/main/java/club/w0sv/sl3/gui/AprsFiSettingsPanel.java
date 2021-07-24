package club.w0sv.sl3.gui;

import club.w0sv.sl3.config.AprsFiConfig;
import club.w0sv.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.time.Duration;
import java.util.Properties;

@Component
public class AprsFiSettingsPanel extends JPanel implements SettingsUI {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static URI aprsFi = URI.create("https://aprs.fi/");
    
    private AprsFiConfig aprsfiConfig;
    private JTextField aprsfiApiKey;
    private JTextField autoUpdateInterval;

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
        mainPanel.add(new JLabel("Auto-Update Interval"));
        autoUpdateInterval = new JTextField(10);
        autoUpdateInterval.setToolTipText("time between location update attempts (seconds)");
        autoUpdateInterval.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JTextField) input).getText();
                try {
                    if (text != null && !text.isEmpty()) {
//                        Duration.parse(text.trim());
                        Integer.parseInt(text.trim());
                        return true;
                    }
                }
                catch (Exception ex) {
                }
                return false;
            }
        });
        mainPanel.add(autoUpdateInterval);
    }
    
    public String getApiKey() {
        return StringUtils.trim(aprsfiApiKey.getText());
    }
    
    public void setApiKey(String key) {
        aprsfiApiKey.setText(key);
    }

    public Duration getAutoUpdateInterval() {
        try {
            String str = autoUpdateInterval.getText();
            if (str == null || str.isBlank())
                return null;
            
            return Duration.ofSeconds(Integer.parseInt(str));
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public void setAutoUpdateInterval(Duration duration) {
        autoUpdateInterval.setText(duration == null ? null : duration.toSeconds()+"");
    }
    
    @Override
    public void displaySettings() {
        setApiKey(aprsfiConfig.getApikey());
        setAutoUpdateInterval(aprsfiConfig.getAutoUpdateInterval());
    }

    @Override
    public void applyChanges() {
        aprsfiConfig.setApikey(getApiKey());
        aprsfiConfig.setAutoUpdateInterval(getAutoUpdateInterval());
    }
    
    @Override
    public void storeSettings(Properties props) {
        String key = getApiKey();
        if (key == null)
            props.remove("aprsfi.apikey");
        else
            props.put("aprsfi.apikey", key);
        
        Duration d = getAutoUpdateInterval();
        if (d == null)
            props.remove("aprsfi.auto-update-interval");
        else
            props.put("aprsfi.auto-update-interval", d.toString());
    }
}
