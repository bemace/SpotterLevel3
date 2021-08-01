package club.w0sv.sl3;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AprsSymbol {
    private static final Logger LOGGER = LoggerFactory.getLogger(AprsSymbol.class);
    private static final Table<Character, Character, AprsSymbol> symbolTable = HashBasedTable.create();
    public static final char PRIMARY_TABLE = '/';
    public static final char ALTERNATE_TABLE = '\\';

    public static final AprsSymbol UNKNOWN = new AprsSymbol('\0', '\0', null,"UNKNOWN");
    
    @CsvBindByName(column = "Table", required = false)
    private char tableIdentifier;
    @CsvBindByName(column = "Symbol", required = false)
    private char symbolIdentifier;
    @CsvBindByName(column = "GPSxyz", required = false)
    private String gpsxyz;
    @CsvBindByName(column = "Description", required = false)
    private String description;

    public AprsSymbol() {}
    
    public AprsSymbol(char table, char symbol, String gpsxyz, String description) {
        this.tableIdentifier = table;
        this.symbolIdentifier = symbol;
        this.gpsxyz = gpsxyz;
        this.description = description;
    }

    /**
     * Creates an "unknown" symbol.
     * @param table
     * @param symbol
     */
    private AprsSymbol(char table, char symbol) {
        this(table, symbol, null, "UNKNOWN (" + table + symbol + ")");
    }

    public char getTableIdentifier() {
        return tableIdentifier;
    }

    public char getSymbolIdentifier() {
        return symbolIdentifier;
    }

    /**
     * Gets the alphanumeric identifier for this APRS symbol.
     *
     * @return
     */
    public String getXyz() {
        return gpsxyz;
    }

    public String getDescription() {
        return description;
    }
    
    public boolean fromPrimaryTable() {
        return tableIdentifier == PRIMARY_TABLE;
    }
    
    public boolean fromAlternateTable() {
        return tableIdentifier == ALTERNATE_TABLE;
    }
    
    public boolean isOverlay() {
        return tableIdentifier >= '0' && tableIdentifier <= '9' || tableIdentifier >= 'A' && tableIdentifier <= 'Z';
    }

    /**
     * Gets this symbol's non-overlay counterpart.
     * Returns {@code this} if this symbol is not an overlay.
     * @return
     */
    public AprsSymbol baseSymbol() {
        if (isOverlay())
            return AprsSymbol.from(ALTERNATE_TABLE, symbolIdentifier);
        else
            return this;
    }
    
    @Override
    public String toString() {
        return description;
    }
    
    public AprsSymbol primary() {
        if (tableIdentifier == PRIMARY_TABLE)
            return this;
        else
            return AprsSymbol.from(PRIMARY_TABLE,symbolIdentifier);
    }
    
    public AprsSymbol alternate() {
        if (tableIdentifier == ALTERNATE_TABLE)
            return this;
        else
            return AprsSymbol.from(ALTERNATE_TABLE,symbolIdentifier);
    }

    public AprsSymbol withOverlay(char overlayTable) {
        if (tableIdentifier == overlayTable)
            return this;
        else
            return AprsSymbol.from(overlayTable, symbolIdentifier);
    }
    
    public static AprsSymbol from(char tableIndicator, char symbolIndicator) {
        AprsSymbol symbol = symbolTable.get(tableIndicator,symbolIndicator);
        if (symbol == null) {
            if (isOverlayTable(tableIndicator)) {
                AprsSymbol alternate = AprsSymbol.from(ALTERNATE_TABLE, symbolIndicator);
                symbol = new AprsSymbol(tableIndicator, symbolIndicator, null, alternate.getDescription() + " with '" + tableIndicator + "' overlay");
            }
            else {
                symbol = new AprsSymbol(tableIndicator,symbolIndicator);
                LOGGER.trace("unknown APRS symbol table:  {}  symbol:  {}", tableIndicator, symbolIndicator);
            }
        }
        return symbol;
    }

    static {
        try {
            URL symbolFile = AprsSymbol.class.getResource("/APRS symbols.csv");
            if (symbolFile == null) {
                LOGGER.warn("APRS symbol file not found, APRS icons will not work.");
            }
            else {
                try (Reader reader = new BufferedReader(new InputStreamReader(symbolFile.openStream()))) {
                    CsvToBean<AprsSymbol> csvToBean = new CsvToBeanBuilder<AprsSymbol>(reader)
                            .withType(AprsSymbol.class)
                            .withIgnoreLeadingWhiteSpace(true)
                            .withIgnoreEmptyLine(true)
                            .withQuoteChar('"')
                            .withIgnoreQuotations(false)
                            .withEscapeChar('\\')
                            .build();
                    for (AprsSymbol symbol : csvToBean) {
                        symbolTable.put(symbol.getTableIdentifier(), symbol.getSymbolIdentifier(), symbol);
                    }
                }
                LOGGER.info("loaded {} APRS symbols from {}", symbolTable.size(), symbolFile);
            }
        }
        catch (Exception ex) {
            LOGGER.error("error loading APRS symbol data", ex);
        }
    }
    
    public static Map<Character,AprsSymbol> byTable(char tableIdentifier) {
        return Collections.unmodifiableMap(symbolTable.row(tableIdentifier));
    }
    
    public static Map<Character,AprsSymbol> overlaysFor(char symbolIdentifier) {
        Map<Character,AprsSymbol> overlays = new HashMap<>();
        for (char c = '0'; c <= '9'; c++)
            overlays.put(c, AprsSymbol.from(c,symbolIdentifier));

        for (char c = 'A'; c <= 'Z'; c++)
            overlays.put(c, AprsSymbol.from(c,symbolIdentifier));

        return overlays;
    }
    
    public static boolean isOverlayTable(char tableIdentifier) {
        return tableIdentifier >= '0' && tableIdentifier <= '9' || tableIdentifier >= 'A' && tableIdentifier <= 'Z';
    }
}
