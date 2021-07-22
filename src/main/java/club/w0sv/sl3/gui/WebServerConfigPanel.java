package club.w0sv.sl3.gui;

import club.w0sv.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

@Component
public class WebServerConfigPanel extends JPanel implements SettingsUI {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;
    private ServletWebServerApplicationContext webServerAppCtxt;
    private TomcatServletWebServerFactory tomcatServletWebServerFactory;
    private ServerProperties webServerConfig;
    private JTextField port;


    public WebServerConfigPanel() {
        super(new BorderLayout());

        add(new CallOut(CallOut.Type.ATTENTION, "Changes to web server settings will not take affect until application is restarted."), BorderLayout.SOUTH);
        
        JPanel mainPanel = new JPanel(new GridLayout(0,2));
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(new JLabel("Port"));
        port = new JTextField(6);
        mainPanel.add(port);
    }

    public Integer getPort() {
        return StringUtils.parseInt(this.port.getText());
    }
    
    public void setPort(Integer port) {
        this.port.setText(port == null ? null : port+"");
    }
    
    @Override
    public void displaySettings() {
        int p = webServerAppCtxt.getWebServer().getPort();
        setPort(p < 0 ? null : p);
    }

    @Override
    public void applyChanges() {
        webServerConfig.setPort(getPort());
    }

    @Override
    public void storeSettings(Properties props) {
        Integer port = getPort();
        if (port == null)
            props.remove("server.port");
        else
            props.put("server.port", port + "");
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ServerProperties getWebServerConfig() {
        return webServerConfig;
    }

    @Autowired
    public void setWebServerConfig(ServerProperties webServerConfig) {
        this.webServerConfig = webServerConfig;
    }

    public TomcatServletWebServerFactory getTomcatServletWebServerFactory() {
        return tomcatServletWebServerFactory;
    }

    @Autowired
    public void setTomcatServletWebServerFactory(TomcatServletWebServerFactory tomcatServletWebServerFactory) {
        this.tomcatServletWebServerFactory = tomcatServletWebServerFactory;
    }

    public ServletWebServerApplicationContext getWebServerAppCtxt() {
        return webServerAppCtxt;
    }

    @Autowired
    public void setWebServerAppCtxt(ServletWebServerApplicationContext webServerAppCtxt) {
        this.webServerAppCtxt = webServerAppCtxt;
    }
}
