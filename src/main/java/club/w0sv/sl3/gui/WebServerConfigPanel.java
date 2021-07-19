package club.w0sv.sl3.gui;

import club.w0sv.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

@Component
public class WebServerConfigPanel extends JPanel implements SettingsUI {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ServletWebServerApplicationContext webServerAppCtxt;
    private TomcatServletWebServerFactory tomcatServletWebServerFactory;
    private ServerProperties webServerConfig;
    private JTextField port;


    public WebServerConfigPanel() {
        super(new BorderLayout());
        this.webServerAppCtxt = webServerAppCtxt;
        this.webServerConfig = webServerConfig;

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
        // TODO: only do this if port was actually changed
//        webServerAppCtxt.getWebServer().shutDownGracefully(new GracefulShutdownCallback() {
//            @Override
//            public void shutdownComplete(GracefulShutdownResult result) {
//                logger.debug("web server shut down, applying changes");
//                webServerConfig.setPort(Integer.parseInt(port.getText().trim()));
//                logger.debug("restarting web server");
//                webServerAppCtxt.getWebServer().start();
//            }
//        });
    }

    @Override
    public void storeSettings(Properties props) {
        Integer port = getPort();
        if (port == null)
            props.remove("server.port");
        else
            props.put("server.port", port + "");
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
