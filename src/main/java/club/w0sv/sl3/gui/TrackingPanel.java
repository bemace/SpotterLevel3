package club.w0sv.sl3.gui;

import club.w0sv.sl3.AprsLookupException;
import club.w0sv.sl3.AprsService;
import club.w0sv.sl3.TrackingEntry;
import club.w0sv.sl3.roster.RosterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TrackingPanel extends JPanel {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private RosterService rosterService;
    private AprsService aprsService;
    
    private TrackingTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;
    
    public TrackingPanel(RosterService rosterService, AprsService aprsService) {
        super(new BorderLayout());
        this.rosterService = rosterService;
        this.aprsService = aprsService;
        
        tableModel = new TrackingTableModel(aprsService.getEntries());
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        add(createButtonBar(), BorderLayout.SOUTH);
    }
    
    private JToolBar createButtonBar() {
        JToolBar buttonBar = new JToolBar("tracking buttons",SwingConstants.HORIZONTAL);
        buttonBar.setFloatable(false);
        
        buttonBar.add(new RefreshTrackingData(this));
        
        return buttonBar;
    }

    public void refresh() throws AprsLookupException {
        aprsService.locate(rosterService.getAprsIds());
        tableModel = new TrackingTableModel(aprsService.getEntries());
        table.setModel(tableModel);
        logger.trace("reloaded");
    }

    private static class TrackingTableModel extends AbstractTableModel {
        static enum ColumnDef {
            APRS_ID("APRS ID"),
            LATITUDE("Latitude"), 
            LONGITUDE("Longitude"),
            COURSE("Course"),
            SPEED("Speed"),
            LATEST_TIME("Time");
            
            private final String heading;
            ColumnDef(String heading) {
                this.heading = heading;
            }
        }
        
        private List<TrackingEntry> entries = new ArrayList<>();
        
        public TrackingTableModel(Collection<TrackingEntry> c) {
            entries.addAll(c);
        }
        
        @Override
        public int getRowCount() {
            return entries.size();
        }

        @Override
        public int getColumnCount() {
            return ColumnDef.values().length;
        }

        @Override
        public String getColumnName(int column) {
            return ColumnDef.values()[column].heading;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            TrackingEntry entry = entries.get(rowIndex);
            switch (ColumnDef.values()[columnIndex]) {
                case APRS_ID:
                    return entry.getAprsId();
                case LATITUDE:
                    return entry.getLocation().getLatitude();
                case LONGITUDE:
                    return entry.getLocation().getLongitude();
                case COURSE:
                    return entry.getCourse();
                case SPEED:
                    return entry.getSpeed();
                case LATEST_TIME:
                    return entry.getLatestTime();
            }
            return null;
        }
    }
    
    private static class RefreshTrackingData extends AbstractAction {
        private final TrackingPanel panel;

        public RefreshTrackingData(TrackingPanel panel) {
            super("Refresh");
            putValue(Action.SHORT_DESCRIPTION, "Check for new tracking data");
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                panel.refresh();
            }
            catch (Exception ex) {
                panel.logger.error("error refreshing tracking data", ex);
                JOptionPane.showMessageDialog(panel, ex.getLocalizedMessage(), "Refresh failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
