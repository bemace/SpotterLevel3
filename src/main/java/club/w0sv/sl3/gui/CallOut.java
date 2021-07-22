package club.w0sv.sl3.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.net.URL;
import java.util.EnumMap;
import java.util.Optional;

public class CallOut extends JPanel {
    private static final Logger logger= LoggerFactory.getLogger(CallOut.class);
    
    private static final EnumMap<Type, Optional<Icon>> calloutIconCache = new EnumMap<>(CallOut.Type.class);


    private final JTextPane messagePane = new JTextPane();
    private final Type type;

    /**
     * 
     * @param type
     * @param message HTML is allowed, including links
     */
    CallOut(Type type, String message) {
        super(new BorderLayout());
        this.type = type;

        setBorder(BorderFactory.createMatteBorder(3,15,3,1,getBackground()));
        if (type.getBorderColor() != null) {
            setBorder(BorderFactory.createCompoundBorder(getBorder(), BorderFactory.createLineBorder(type.getBorderColor(),1)));
        }
        setBackground(new Color(255, 255, 255, 133));
        
        Optional<Icon> icon = getCallOutIcon(type);
        if (icon.isPresent()) {
            JLabel iconLabel = new JLabel(icon.get());
            iconLabel.setOpaque(false);
            iconLabel.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
            add(iconLabel, BorderLayout.WEST);
        }
        
        add(messagePane, BorderLayout.CENTER);
        messagePane.setEditable(false);
        messagePane.setOpaque(false);
        messagePane.setFont(new Font("Arial", Font.PLAIN, 8));
        messagePane.setEditorKit(new HTMLEditorKit());
        messagePane.setText(message);
        messagePane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                }
                catch (Exception ex) {
                    logger.error("couldn't open link in browser: " + e.getURL(), ex);
                    JOptionPane.showMessageDialog(CallOut.this,"Unable to open your web browser: "+ex.getLocalizedMessage(), "Couldn't open link", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    public enum Type {
        INFO("info.png", null),
        TIP("tip.png", new Color(26, 196, 25)),
        ATTENTION("attention.png", new Color(85, 212, 243)),
        WARNING("warning.png", new Color(232, 135, 14));
        
        private final String iconName;
        private final Color borderColor;
        
        Type(String iconName, Color borderColor) {
            this.iconName = iconName;
            this.borderColor = borderColor;
        }
        
        public String getIconName() {
            return iconName;
        }

        public Color getBorderColor() {
            return borderColor;
        }
    }

    private static Optional<Icon> getCallOutIcon(CallOut.Type type) {
        if (!calloutIconCache.containsKey(type)) {
            try {
                URL url = type.getClass().getResource("/icons/callouts/" + type.getIconName());
                calloutIconCache.put(type, Optional.of(loadAndScaleImage(url, 16)));
            }
            catch (Exception ex) {
                logger.error("couldn't load icon for {} from {}" ,type, type.getIconName());
                calloutIconCache.put(type, Optional.empty());
            }
        }

        return calloutIconCache.get(type);
    }

    private static ImageIcon loadAndScaleImage(URL resource, int sizeInPixels) {
        Image original = new ImageIcon(resource).getImage();
        Image scaled = original.getScaledInstance(sizeInPixels, sizeInPixels,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

}
