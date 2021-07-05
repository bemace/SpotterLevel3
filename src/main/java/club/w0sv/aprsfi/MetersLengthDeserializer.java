package club.w0sv.aprsfi;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import systems.uom.common.USCustomary;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.io.IOException;
import java.math.BigDecimal;

public class MetersLengthDeserializer extends JsonDeserializer<Quantity<Length>> {
    @Override
    public Quantity<Length> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String length = p.getValueAsString();
        if (length == null)
            return null;
        else
            return Quantities.getQuantity(new BigDecimal(length), Units.METRE);
    }
}
