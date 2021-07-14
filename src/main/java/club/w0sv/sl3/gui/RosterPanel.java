package club.w0sv.sl3.gui;

import club.w0sv.sl3.event.RosterChangeEvent;
import club.w0sv.sl3.roster.RosterEntry;
import club.w0sv.sl3.roster.RosterService;
import club.w0sv.sl3.roster.RosterStatus;
import club.w0sv.util.AprsId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component
public class RosterPanel extends JPanel {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private RosterService rosterService;

    private final DefaultListModel<RosterEntry> listModel;
    private final JList<RosterEntry> list;
    private final JScrollPane scrollPane;
    private final JToolBar buttonBar;

    private AddEntry addEntryAction;
    private RemoveEntry removeEntryAction;

    public static final String ADD_ENTRY_ACTION_ID = "add-roster-entry";
    public static final String REMOVE_ENTRY_ACTION_ID = "remove-selected-roster-entries";


    public RosterPanel(RosterService rosterService) {
        super(new BorderLayout());
        this.rosterService = rosterService;
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu menu = createListPopupMenu();
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        addEntryAction = new AddEntry(this, rosterService);
        removeEntryAction = new RemoveEntry(this, rosterService);
        getActionMap().put(ADD_ENTRY_ACTION_ID, addEntryAction);
        getActionMap().put(REMOVE_ENTRY_ACTION_ID, removeEntryAction);
        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put((KeyStroke) addEntryAction.getValue(Action.ACCELERATOR_KEY), ADD_ENTRY_ACTION_ID);
        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put((KeyStroke) removeEntryAction.getValue(Action.ACCELERATOR_KEY), REMOVE_ENTRY_ACTION_ID);
        scrollPane = new JScrollPane(list);
        add(scrollPane, BorderLayout.CENTER);
        buttonBar = createButtonBar();
        add(buttonBar, BorderLayout.SOUTH);
    }

    private JToolBar createButtonBar() {
        JToolBar buttonBar = new JToolBar("Roster Tools", SwingConstants.HORIZONTAL);
        buttonBar.setFloatable(false);
        buttonBar.add(addEntryAction);
        buttonBar.add(removeEntryAction);
        buttonBar.add(new RefreshRosterList(this));
        return buttonBar;
    }

    private JPopupMenu createListPopupMenu() {
        JPopupMenu menu = new JPopupMenu("Roster Actions");

        for (RosterStatus status : RosterStatus.values()) {
            menu.add(new SetEntryStatus(this,rosterService,status));
        }
        
        menu.addSeparator();
        menu.add(removeEntryAction);

        return menu;
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
            listModel.addElement(event.getNewEntry());
            logger.trace("{} added to roster panel", event.getAprsId());
        }
        else if (event.getChangeType() == RosterChangeEvent.ChangeType.UPDATE) {
            int i = listModel.indexOf(event.getOldEntry());
            listModel.setElementAt(event.getNewEntry(),i);
        }
        else if (event.getChangeType() == RosterChangeEvent.ChangeType.REMOVE) {
            if (listModel.removeElement(event.getOldEntry()))
                logger.debug("removed {}", event.getOldEntry().getAprsId());
            else
                logger.trace("{} not found in roster, no removal necessary", event.getOldEntry().getAprsId());
        }
    }

    public synchronized void refresh() {
        listModel.clear();
        listModel.addAll(rosterService.getEntries());
        logger.trace("reloaded");
    }

    private static class AddEntry extends AbstractAction {
        private final RosterPanel rosterPanel;
        private final RosterService rosterService;

        public AddEntry(RosterPanel rosterPanel, RosterService rosterService) {
            super("Add");
            putValue(Action.SHORT_DESCRIPTION, "Add someone to the roster");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));
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
                    rosterService.addOrUpdate(new RosterEntry(dialog.getCallsignWithSSID(), dialog.getStatus()));
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
            putValue(Action.SHORT_DESCRIPTION, "Remove selected entries from the roster");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
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

                String confirmationMessage;
                if (entries.size() > 5)
                    confirmationMessage = "Remove " + entries.size() + " entries from roster?";
                else
                    confirmationMessage = "Remove " + entries.stream().map(e -> e.getAprsId().toString()).collect(Collectors.joining(", ")) + " from roster?";
                if (JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(rosterPanel),
                        confirmationMessage, "Confirm removal", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
                    entries.stream().forEach(e -> rosterService.remove(e.getAprsId()));
                }
            }
            catch (Exception ex) {
                rosterPanel.logger.error("couldn't remove {}", ex);
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(rosterPanel), "removal failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class SetEntryStatus extends AbstractAction {
        private final RosterPanel rosterPanel;
        private final RosterService rosterService;
        private final RosterStatus status;

        public SetEntryStatus(RosterPanel rosterPanel, RosterService rosterService, RosterStatus status) {
            super(status.toDisplayString());
            putValue(Action.SHORT_DESCRIPTION, "Change status of selected entries to " + status.toDisplayString());
//            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
            this.rosterPanel = rosterPanel;
            this.rosterService = rosterService;
            this.status = status;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            List<RosterEntry> entries = rosterPanel.list.getSelectedValuesList();
            entries.stream().forEach(e -> { e.setStatus(status); rosterService.addOrUpdate(e); });
        }
    }
    
    private static class RefreshRosterList extends AbstractAction {
        private final RosterPanel panel;

        public RefreshRosterList(RosterPanel panel) {
            super("Refresh");
            putValue(Action.SHORT_DESCRIPTION, "Reload the entire roster");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F5,0));
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                panel.refresh();
                panel.logger.debug("roster refreshed");
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
