package club.w0sv.sl3;

import club.w0sv.util.IconIdentifier;

public class AprsIconSupplier {
    public static final String primaryTablePath = "iconFiles/aprs/primary.png";
    public static final String alternateTablePath = "iconFiles/aprs/alternate.png";
    private String charSequence = "!\"#$%&'()*+,-./0" + "123456789:;<=>?@" + "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZ[\\]^_`"
            + "abcdefghijklmnop" + "qrstuvwxyz{|}~";
    
    public AprsIconSupplier() {
    }
    
    public IconIdentifier resolveSymbolIcon(AprsSymbol symbol) {
        String iconFilePath;
        if (symbol.getTableIdentifier() == '/')
            iconFilePath = primaryTablePath;
        else if (symbol.getTableIdentifier() == '\\')
            iconFilePath = alternateTablePath;
        else {
            return new IconIdentifier(primaryTablePath,95);
        }
        
        int index = charSequence.indexOf(symbol.getSymbolIdentifier());
        if (index < 0)
            return new IconIdentifier(primaryTablePath,95);

        return new IconIdentifier(iconFilePath,index + 1);
    }
    
}
