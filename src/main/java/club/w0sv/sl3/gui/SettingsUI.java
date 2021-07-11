package club.w0sv.sl3.gui;

import java.util.Properties;

/**
 * A GUI panel used for changing program settings.
 * One {@code SettingsUI} may containing other {@code SettingsUI} objects within it.
 */
public interface SettingsUI {

    /**
     * Writes the changes made in the GUI back to the  underlying
     * {@link org.springframework.boot.context.properties.ConfigurationProperties} object
     */
    void applyChanges();

    /**
     * Replaces any changes made in the GUI with the current settings in the underlying
     * {@link org.springframework.boot.context.properties.ConfigurationProperties} object.
     */
    void displaySettings();

    /**
     * Stores the current values from the underlying 
     * {@link org.springframework.boot.context.properties.ConfigurationProperties} object
     * into the provided {@code Properties} object so they can be saved to disk.
     * @param props
     */
    void storeSettings(Properties props);
}