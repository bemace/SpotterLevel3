package club.w0sv.sl3.gui;

import club.w0sv.sl3.AprsIconSupplier;
import club.w0sv.sl3.AprsSymbol;
import club.w0sv.sl3.LocationService;
import club.w0sv.sl3.TrackingEntry;
import club.w0sv.sl3.event.TrackingUpdateEvent;
import club.w0sv.sl3.roster.RosterService;
import club.w0sv.util.QuantityUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import systems.uom.common.USCustomary;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Speed;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@org.springframework.stereotype.Component
public class TrackingPanel extends LateInitPanel {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private RosterService rosterService;
    private LocationService locationService;
    private IconManager iconManager;

    private TrackingTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;

    public TrackingPanel(RosterService rosterService, LocationService locationService) {
        super(new BorderLayout());
        this.rosterService = rosterService;
        this.locationService = locationService;

    }

    @Override
    protected void initializeContent() {
        tableModel = new TrackingTableModel(locationService.getEntries());
        table = new JTable(tableModel);
        table.setDefaultRenderer(AprsSymbol.class, new AprsSymbolRenderer());
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        add(createButtonBar(), BorderLayout.SOUTH);
    }

    private JToolBar createButtonBar() {
        JToolBar buttonBar = new JToolBar("tracking buttons", SwingConstants.HORIZONTAL);
        buttonBar.setFloatable(false);

        buttonBar.add(new DownloadLocationData());
        buttonBar.add(new RebuildLocationTable());

        for (int i = 0; i < buttonBar.getComponentCount(); i++) {
            if (buttonBar.getComponent(i) instanceof JButton) {
                JButton button = (JButton) buttonBar.getComponent(i);
                button.setHideActionText(false);
                button.setHorizontalTextPosition(SwingConstants.RIGHT);
                button.setVerticalTextPosition(SwingConstants.CENTER);
            }
        }
        return buttonBar;
    }

    @EventListener
    synchronized void handleTrackingUpdateEvent(TrackingUpdateEvent event) {
        logger.trace("receiving location data for {}", event.getTarget());
        int row = tableModel.entries.indexOf(event.getData());
        if (row >= 0) {
            tableModel.entries.set(row, event.getData());
            tableModel.fireTableRowsUpdated(row, row);
            logger.trace("updated location data for {}", event.getTarget());
        }
        else {
            tableModel.entries.add(event.getData());
            tableModel.fireTableRowsInserted(tableModel.getRowCount() - 1, tableModel.getRowCount() - 1);
            logger.trace("inserted location data for {}", event.getTarget());
        }

    }

    public IconManager getIconManager() {
        return iconManager;
    }

    @Autowired
    public void setIconManager(IconManager iconManager) {
        this.iconManager = iconManager;
    }

    private static class TrackingTableModel extends AbstractTableModel {
        static enum ColumnDef {
            SYMBOL("Symbol", AprsSymbol.class),
            APRS_ID("APRS ID", String.class),
            LATITUDE("Latitude", BigDecimal.class),
            LONGITUDE("Longitude", BigDecimal.class),
            COURSE("Course", Angle.class),
            SPEED("Speed", Speed.class),
            LATEST_TIME("Time", ZonedDateTime.class);

            private final String heading;
            private Class columnClass;

            ColumnDef(String heading, Class<?> columnClass) {
                this.heading = heading;
                this.columnClass = columnClass;
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
        public Class<?> getColumnClass(int columnIndex) {
            return ColumnDef.values()[columnIndex].columnClass;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            TrackingEntry entry = entries.get(rowIndex);
            switch (ColumnDef.values()[columnIndex]) {
                case SYMBOL:
                    return entry.getAprsSymbol();
                case APRS_ID:
                    return entry.getAprsId();
                case LATITUDE:
                    return entry.getLocation().getLatitude();
                case LONGITUDE:
                    return entry.getLocation().getLongitude();
                case COURSE:
                    return entry.getCourse();
                case SPEED:
                    Quantity<Speed> speed = entry.getSpeed();
                    if (speed == null)
                        return null;

                    Quantity<Speed> mph = speed.to(USCustomary.MILE_PER_HOUR);
                    if (QuantityUtil.toDecimal(mph, USCustomary.MILE_PER_HOUR).compareTo(BigDecimal.TEN) < 0)
                        return QuantityUtil.setScale(mph, 1, RoundingMode.HALF_UP);
                    else
                        return QuantityUtil.setScale(mph, 0, RoundingMode.HALF_UP);
                case LATEST_TIME:
                    return entry.getLatestTime();
            }
            return null;
        }
    }

    private class DownloadLocationData extends AbstractAction {

        public DownloadLocationData() {
            super("Download");
            putValue(Action.SHORT_DESCRIPTION, "Allows you to manually trigger a download of new location data without waiting for the next automatic update");
            putValue(Action.SMALL_ICON, iconManager.getJlfgrIcon("/toolbarButtonGraphics/general/Import16.gif").orElse(null));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                locationService.locate();
            }
            catch (Exception ex) {
                logger.error("error downloading location data", ex);
                JOptionPane.showMessageDialog(TrackingPanel.this, ExceptionUtils.getRootCauseMessage(ex), "Refresh failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class RebuildLocationTable extends AbstractAction {

        public RebuildLocationTable() {
            super("Rebuild Table");
            putValue(Action.SHORT_DESCRIPTION, "Rebuilds the location table from the current location data."
                    + " (This shouldn't be necessary unless something goes wrong.)");
            putValue(Action.SMALL_ICON, iconManager.getJlfgrIcon("/toolbarButtonGraphics/general/Refresh16.gif").orElse(null));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                tableModel = new TrackingTableModel(locationService.getEntries());
                table.setModel(tableModel);
                logger.trace("reloaded");
            }
            catch (Exception ex) {
                logger.error("error rebuilding location table", ex);
                tableModel = new TrackingTableModel(locationService.getEntries());
                table.setModel(tableModel);
                logger.trace("reloaded");
                JOptionPane.showMessageDialog(TrackingPanel.this, ExceptionUtils.getRootCauseMessage(ex), "Refresh failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class AprsSymbolRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            AprsSymbol symbol = (AprsSymbol) value;
            setText(null);
            setToolTipText("<html>" + symbol.getDescription() + "<br>"
                    + "Table: <tt>" + symbol.getTableIdentifier() + "</tt>, Symbol: <tt>" + symbol.getSymbolIdentifier() + "</tt>");
            try {
                setIcon(iconManager.getIcon(symbol).orElse(null));
            }
            catch (Exception ex) {
                logger.error("error loading icon for " + value, ex);
                setIcon(null);
            }
            return this;
        }
    }
}
