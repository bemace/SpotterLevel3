package club.w0sv.sl3.gui;

import club.w0sv.sl3.config.PlaceFileConfig;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.EnumMap;
import java.util.Properties;

import javax.swing.*;

import org.springframework.stereotype.Component;

@Component
public class PlaceFileConfigPanel extends JPanel implements SettingsUI {
    private PlaceFileConfig config;

    private final ButtonGroup objectDisplayTypeButtonGroup = new ButtonGroup();
    private final EnumMap<PlaceFileConfig.ObjectDisplayType, JRadioButton> objectDisplayTypeButtons = new EnumMap<PlaceFileConfig.ObjectDisplayType, JRadioButton>(
            PlaceFileConfig.ObjectDisplayType.class);
    private final JButton textColor;
    private final ButtonGroup iconTypeButtonGroup = new ButtonGroup();
    private final EnumMap<PlaceFileConfig.IconType, JRadioButton> iconTypeButtons = new EnumMap<PlaceFileConfig.IconType, JRadioButton>(
            PlaceFileConfig.IconType.class);

    public PlaceFileConfigPanel(PlaceFileConfig config) {
        super(new BorderLayout());
        this.config = config;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        add(mainPanel, BorderLayout.CENTER);

        JPanel displayTypePanel = new JPanel();
        displayTypePanel.add(new JLabel("Display Objects as:"));
        for (PlaceFileConfig.ObjectDisplayType type : PlaceFileConfig.ObjectDisplayType.values()) {
            JRadioButton button = new JRadioButton(type.name());
            objectDisplayTypeButtonGroup.add(button);
            objectDisplayTypeButtons.put(type, button);
            displayTypePanel.add(button);
        }
        mainPanel.add(displayTypePanel);

        JPanel textColorPanel = new JPanel();
        textColorPanel.add(new JLabel("Text Color:"));
        textColor = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JColorChooser chooser = new JColorChooser(getTextColor());
                BooleanDialog dialog = new BooleanDialog(SwingUtilities.getWindowAncestor(textColor), "OK", "Cancel", true) {
                    @Override
                    protected Container createPrimaryContent() {
                        JPanel panel = new JPanel(new BorderLayout());
                        panel.add(chooser, BorderLayout.CENTER);
                        return panel;
                    }
                };
                dialog.setTitle("Select Text Color");
                dialog.pack();
                dialog.setVisible(true);
                setTextColor(chooser.getColor());
            }
        });
        textColorPanel.add(textColor);
        mainPanel.add(textColorPanel);
        
        
        JPanel iconTypePanel = new JPanel();
        iconTypePanel.add(new JLabel("Icon Type:"));
        for (PlaceFileConfig.IconType type : PlaceFileConfig.IconType.values()) {
            JRadioButton button = new JRadioButton(type.name());
            iconTypeButtonGroup.add(button);
            iconTypeButtons.put(type, button);
            iconTypePanel.add(button);
        }
        mainPanel.add(iconTypePanel);

    }

    public PlaceFileConfig.ObjectDisplayType getObjectDisplayType() {
        for (PlaceFileConfig.ObjectDisplayType type : PlaceFileConfig.ObjectDisplayType.values()) {
            if (objectDisplayTypeButtons.get(type).isSelected())
                return type;
        }

        return null;
    }

    public void setObjectDisplayType(PlaceFileConfig.ObjectDisplayType type) {
        objectDisplayTypeButtons.get(type).setSelected(true);
    }

    public Color getTextColor() {
        return textColor.getBackground();
    }
    
    public void setTextColor(Color color) {
        textColor.setBackground(color);
    }
    
    public PlaceFileConfig.IconType getIconType() {
        for (PlaceFileConfig.IconType type : PlaceFileConfig.IconType.values()) {
            if (iconTypeButtons.get(type).isSelected())
                return type;
        }

        return null;
    }

    public void setIconType(PlaceFileConfig.IconType type) {
        iconTypeButtons.get(type).setSelected(true);
    }

    @Override
    public void applyChanges() {
        config.setObjectDisplayType(getObjectDisplayType());
        config.setTextColor(getTextColor());
        config.setIconType(getIconType());
    }

    @Override
    public void displaySettings() {
        setObjectDisplayType(config.getObjectDisplayType());
        setTextColor(config.getTextColor());
        setIconType(config.getIconType());
    }

    @Override
    public void storeSettings(Properties props) {
        props.put("placefile.objectDisplayType", getObjectDisplayType().name());
        props.put("placefile.textColor", getTextColor().getRed() + "," + getTextColor().getGreen() + "," + getTextColor().getBlue());
        props.put("placefile.iconType", getIconType().name());
    }
}
