package club.w0sv.sl3.gui;

import club.w0sv.util.AprsId;
import club.w0sv.sl3.event.RosterChangeEvent;
import club.w0sv.sl3.roster.RosterEntry;
import club.w0sv.sl3.roster.RosterService;
import club.w0sv.sl3.roster.RosterStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.List;
import java.util.Map;

@Component
public class RosterPanel extends JPanel {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private RosterService rosterService;

    private final DefaultListModel<RosterEntry> listModel;
    private final JList<RosterEntry> list;
    private final JScrollPane scrollPane;
    private final JToolBar buttonBar;


    public RosterPanel(RosterService rosterService) {
        super(new BorderLayout());
        this.rosterService = rosterService;
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        scrollPane = new JScrollPane(list);
        add(scrollPane, BorderLayout.CENTER);
        buttonBar = createButtonBar();
        add(buttonBar, BorderLayout.SOUTH);
    }

    private JToolBar createButtonBar() {
        JToolBar buttonBar = new JToolBar("Roster Tools", SwingConstants.HORIZONTAL);
        buttonBar.setFloatable(false);
        buttonBar.add(new AddEntry(this, rosterService));
        buttonBar.add(new RemoveEntry(this, rosterService));
        buttonBar.add(new RefreshRosterList(this));
        return buttonBar;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        try {
            refresh();
        }
        catch (Exception ex) {
            logger.error("initial roster load failed", ex);
        }
    }

    public RosterService getRosterService() {
        return rosterService;
    }

    public void setRosterService(RosterService rosterService) {
        this.rosterService = rosterService;
    }

    @EventListener
    synchronized void handleRosterChangeEvent(RosterChangeEvent event) {
        if (event.getChangeType() == RosterChangeEvent.ChangeType.ADD) {
            listModel.addElement(new RosterEntry(event.getAprsId(), event.getNewStatus()));
            list.updateUI();
            list.repaint();
            logger.trace("{} added to roster panel", event.getAprsId());
        }
    }

    public synchronized void refresh() {
        listModel.clear();
        for (Map.Entry<AprsId, RosterStatus> entry : rosterService.getEntries()) {
            listModel.addElement(new RosterEntry(entry.getKey(), entry.getValue()));
        }
        logger.trace("reloaded");
    }

    private static class AddEntry extends AbstractAction {
        private final RosterPanel rosterPanel;
        private final RosterService rosterService;

        public AddEntry(RosterPanel rosterPanel, RosterService rosterService) {
            super("Add");
            putValue(Action.SHORT_DESCRIPTION, "Add someone to the roster");
            this.rosterPanel = rosterPanel;
            this.rosterService = rosterService;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            RosterEntryDialog dialog = new RosterEntryDialog(SwingUtilities.getWindowAncestor(rosterPanel));
            dialog.setTitle("New roster entry");
            dialog.pack();
            dialog.setVisible(true);
            try {
                if (dialog.getResult()) {
                    rosterService.addOrUpdate(dialog.getCallsignWithSSID(), dialog.getStatus());
                }
            }
            catch (Exception ex) {
                rosterPanel.logger.error("error adding roster entry", ex);
                JOptionPane.showMessageDialog(rosterPanel, ex.getLocalizedMessage(), "Roster addition failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private static class RemoveEntry extends AbstractAction {
        private final RosterPanel rosterPanel;
        private final RosterService rosterService;

        public RemoveEntry(RosterPanel rosterPanel, RosterService rosterService) {
            super("Remove");
            putValue(Action.SHORT_DESCRIPTION, "Remove someone from the roster");
            this.rosterPanel = rosterPanel;
            this.rosterService = rosterService;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                List<RosterEntry> entries = rosterPanel.list.getSelectedValuesList();
                if (entries.isEmpty()) {
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(rosterPanel), "No entries selected", "Can't remove", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(rosterPanel),
                        "Remove ...", "Confirm removal", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
                    entries.stream().forEach(e -> rosterService.remove(e.getAprsId()));
                }
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(rosterPanel),"removal failed","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class RefreshRosterList extends AbstractAction {
        private final RosterPanel panel;

        public RefreshRosterList(RosterPanel panel) {
            super("Refresh");
            putValue(Action.SHORT_DESCRIPTION, "Reload the entire roster");
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                panel.refresh();
            }
            catch (Exception ex) {
                panel.logger.error("error refreshing roster", ex);
                JOptionPane.showMessageDialog(panel, ex.getLocalizedMessage(), "Refresh failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class RosterEntryDialog extends BooleanDialog {
        private JTextField callsign;
        private JComboBox<RosterStatus> status;

        public RosterEntryDialog(Window owner) {
            super(owner, "Add", "Cancel", true);

        }

        @Override
        protected Container createPrimaryContent() {
            JPanel panel = new JPanel(new GridLayout(2, 2));
            panel.add(new JLabel("Callsign w/ SSID"));
            callsign = new JTextField();
            callsign.setName("callsign");
            panel.add(callsign);

            panel.add(new JLabel("Status"));
            status = new JComboBox<>(RosterStatus.values());
            panel.add(status);

            return panel;
        }

        public AprsId getCallsignWithSSID() {
            return AprsId.of(callsign.getText());
        }

        public RosterStatus getStatus() {
            return (RosterStatus) status.getSelectedItem();
        }
    }
}