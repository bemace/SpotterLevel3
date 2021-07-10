package club.w0sv.util;

import tech.units.indriya.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.Unit;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class QuantityUtil {
    
    public static <Q extends Quantity<Q>> Quantity<Q> setScale(Quantity<Q> quantity, int scale, RoundingMode roundingMode) {
        if (quantity == null)
            return null;
        
        BigDecimal number = new BigDecimal(quantity.getValue().toString());
        return Quantities.getQuantity(number.setScale(scale,roundingMode), quantity.getUnit());
    }
    
    public static <Q extends Quantity<Q>> BigDecimal toDecimal(Quantity<Q> quantity, Unit<Q> unit) {
        return new BigDecimal(quantity.to(unit).getValue().toString());
    }
    
}
