package club.w0sv.aprsfi;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import systems.uom.common.USCustomary;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

import javax.measure.Quantity;
import javax.measure.quantity.Speed;
import java.io.IOException;
import java.math.BigDecimal;

public class KphSpeedDeserializer extends JsonDeserializer<Quantity<Speed>> {
    @Override
    public Quantity<Speed> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String speed = p.getValueAsString();
        if (speed == null)
            return null;
        else
            return Quantities.getQuantity(new BigDecimal(speed), Units.KILOMETRE_PER_HOUR);
    }
}
