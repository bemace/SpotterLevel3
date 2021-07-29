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

public class AprsSymbol {
    private static final Logger LOGGER = LoggerFactory.getLogger(AprsSymbol.class);
    private static final Table<Character, Character, AprsSymbol> symbolTable = HashBasedTable.create();

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

    @Override
    public String toString() {
        return description + " (" + tableIdentifier + symbolIdentifier + ")";
    }

    public static AprsSymbol from(char tableIndicator, char symbolIndicator) {
        AprsSymbol symbol = symbolTable.get(tableIndicator,symbolIndicator);
        if (symbol == null) {
            LOGGER.trace("unknown APRS symbol: {}{}", tableIndicator, symbolIndicator);
            return new AprsSymbol(tableIndicator,symbolIndicator);
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
}
