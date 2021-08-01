package club.w0sv.sl3.gui;

import club.w0sv.sl3.AprsSymbol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class AprsIconBrowser extends LateInitDialog {

    private IconManager iconManager;

    public AprsIconBrowser(Window owner) {
        super(owner, ModalityType.MODELESS);
        setTitle("APRS Symbol Reference");
    }

    @Override
    protected void initializeContent() {
        setLayout(new BorderLayout());
        rootPane.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        JPanel mainPanel = new JPanel(new GridLayout(0, 1));

        JPanel primaryPanel = createTablePanel(AprsSymbol.PRIMARY_TABLE);
        primaryPanel.setBorder(BorderFactory.createTitledBorder("Primary"));
        mainPanel.add(primaryPanel);

        JPanel alternatePanel = createTablePanel(AprsSymbol.ALTERNATE_TABLE);
        alternatePanel.setBorder(BorderFactory.createTitledBorder("Alternate"));
        mainPanel.add(alternatePanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createTablePanel(char tableIndicator) {
        JPanel panel = new JPanel(new GridLayout(0, 16));
        for (AprsSymbol symbol : AprsSymbol.byTable(tableIndicator).values()) {
            panel.add(createSymbolIconLabel(symbol));
        }

        return panel;
    }

    private JComponent createSymbolIconLabel(AprsSymbol symbol) {
        JComponent label;
        if (symbol.fromAlternateTable()) {
            label = new JButton(new ShowOverlaysAction(symbol));
        }
        else {
            label = new JLabel();         
            ((JLabel) label).setIcon(iconManager.getIcon(symbol).orElse(null));
        }
        
        label.setToolTipText("<html>" + symbol.getDescription() + "<br>Table: <tt>" + symbol.getTableIdentifier()
                + "</tt>, Symbol: <tt>" + symbol.getSymbolIdentifier() + "</tt>");
        return label;
    }

    public IconManager getIconManager() {
        return iconManager;
    }

    public void setIconManager(IconManager iconManager) {
        this.iconManager = iconManager;
    }

    public void showOverlaysDialog(AprsSymbol alternateSymbol) {
        OverlayDialog dialog = new OverlayDialog(alternateSymbol);
        dialog.pack();
        dialog.setVisible(true);
    }

    private class ShowOverlaysAction extends AbstractAction {
        private final AprsSymbol symbol;

        public ShowOverlaysAction(AprsSymbol symbol) {
            this.symbol = symbol;
            putValue(Action.SMALL_ICON, iconManager.getIcon(symbol).orElse(null));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                showOverlaysDialog(symbol);
            }
            catch (Exception ex) {
                logger.error("error displaying APRS overlays for {}", symbol, ex);
                JOptionPane.showMessageDialog(AprsIconBrowser.this, "error displaying APRS overlays", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class OverlayDialog extends JDialog {
        private final AprsSymbol alternateSymbol;

        public OverlayDialog(AprsSymbol alternateSymbol) {
            this.alternateSymbol = alternateSymbol;
            setTitle("Overlays of " + alternateSymbol);

            JPanel panel = new JPanel(new GridLayout(0, 13,2,2));
            for (char c = '0'; c <= '9'; c++) {
                AprsSymbol symbol = alternateSymbol.withOverlay(c);
                panel.add(createSymbolIconLabel(symbol));
            }
            for (int i = 0; i < 3; i++)
                panel.add(new JLabel());

            for (char c = 'A'; c <= 'Z'; c++) {
                AprsSymbol symbol = alternateSymbol.withOverlay(c);
                panel.add(createSymbolIconLabel(symbol));
            }

            setContentPane(panel);
        }
    }
}
