package club.w0sv.sl3.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public abstract class LateInitDialog extends JDialog {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private boolean initialized = false;

    public LateInitDialog(Window owner) {
        super(owner);
    }
    
    protected abstract void initializeContent();

    @Override
    public void setVisible(boolean b) {
        if (b && !initialized) {
            initializeContent();
            pack();
        }
        super.setVisible(b);
    }

}
