package club.w0sv.sl3.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public abstract class LateInitFrame extends JFrame {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private boolean initialized = false;

    protected abstract void initializeContent();

    public boolean isContentInitialized() {
        return initialized;
    }

    @Override
    public void addNotify() {
        super.addNotify();

        if (!initialized) {
            initialized = true;
            logger.trace("initializing frame content");
            initializeContent();
            pack();
        }
    }

    @Override
    public void setVisible(boolean b) {
        if (b && !initialized) {
            initialized = true;
            logger.trace("initializing frame content");
            initializeContent();
            pack();
        }
        super.setVisible(b);
    }

}
