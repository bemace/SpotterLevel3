package club.w0sv.sl3.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public abstract class AbstractAppAction extends AbstractAction {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    public AbstractAppAction(String name) {
        super(name);
    }

    @Override
    public Object getValue(String key) {
        if (Action.SHORT_DESCRIPTION.equals(key)) {
            String tooltip = (String) super.getValue(key);
            KeyStroke shortcut = (KeyStroke) getValue(Action.ACCELERATOR_KEY);
            if (shortcut != null)
                tooltip += "  [" + keystrokeToString(shortcut) + "]";
            
            return tooltip.trim();
        }
        
        return super.getValue(key);
    }
    
    private static String keystrokeToString(KeyStroke keyStroke) {
        String str = keyStroke.toString().replace("pressed", " ");
        str = str.replace("released", " ");
        str = str.replace("typed", " ");
        str = str.replaceAll("\\s+", " ");
        return str.trim();
    }
}
