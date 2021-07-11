package club.w0sv.sl3.gui;

import club.w0sv.sl3.config.AprsFiConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

public class SettingsWindow extends JFrame implements SettingsUI {
    private AprsFiConfig aprsfiConfig;
    
    private AprsFiSettingsPanel aprsFiSettingsPanel;
    
    public SettingsWindow(AprsFiConfig aprsfiConfig) {
        setTitle("Settings");
        this.aprsfiConfig = aprsfiConfig;
        
        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);
        contentPane.setBorder(BorderFactory.createEmptyBorder(3,5,3,5));
        
        JPanel mainPanel = new JPanel(new FlowLayout());
        contentPane.add(mainPanel, BorderLayout.CENTER);
        aprsFiSettingsPanel = new AprsFiSettingsPanel(aprsfiConfig);
        aprsFiSettingsPanel.setBorder(BorderFactory.createTitledBorder(aprsFiSettingsPanel.getBorder(), "aprs.fi"));
        mainPanel.add(aprsFiSettingsPanel);
        
        
        JPanel buttonBar = new JPanel();
        JButton okButton = new JButton(new OkAction());
        buttonBar.add(okButton);
        buttonBar.add(new JButton(new CancelAction()));
        buttonBar.add(new JButton(new ApplyAction()));
        getRootPane().setDefaultButton(okButton);
        
        getContentPane().add(buttonBar, BorderLayout.SOUTH);
        
        displaySettings();
    }

    @Override
    public void displaySettings() {
        aprsFiSettingsPanel.displaySettings();
    }

    @Override
    public void applyChanges() {
        aprsFiSettingsPanel.applyChanges();
    }
    
    public void close() {
        SettingsWindow.this.dispatchEvent(new WindowEvent(SettingsWindow.this, WindowEvent.WINDOW_CLOSING));
    }
    
    private class OkAction extends AbstractAction {
        public OkAction() {
            super("OK");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                applyChanges();
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(SettingsWindow.this,"Error apply changes", "Settings NOT updated",JOptionPane.ERROR_MESSAGE);
            }
            
            close();
        }
    }
    
    private class CancelAction extends AbstractAction {
        public CancelAction() {
            super("Cancel");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            close();
        }
    }
    
    private class ApplyAction extends AbstractAction {
        public ApplyAction() {
            super("Apply");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                applyChanges();
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(SettingsWindow.this,"Error apply changes", "Settings NOT updated",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
