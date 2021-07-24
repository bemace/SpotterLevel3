package club.w0sv.sl3.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public abstract class LateInitPanel extends JPanel {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private boolean initialized = false;

    public LateInitPanel() {
        super();
    }
    
    public LateInitPanel(LayoutManager layoutManager) {
        super(layoutManager);
    }

    protected abstract void initializeContent();

    @Override
    public void addNotify() {
        if (!initialized) {
            initialized = true;
            logger.trace("initializing dialog content");
            initializeContent();
        }
        super.addNotify();
    }

}
