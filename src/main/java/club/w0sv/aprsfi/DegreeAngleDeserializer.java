package club.w0sv.aprsfi;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import systems.uom.common.USCustomary;
import tech.units.indriya.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import java.io.IOException;
import java.math.BigDecimal;

public class DegreeAngleDeserializer extends JsonDeserializer<Quantity<Angle>> {
    @Override
    public Quantity<Angle> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String angle = p.getValueAsString();
        if (angle == null)
            return null;
        else
            return Quantities.getQuantity(new BigDecimal(angle), USCustomary.DEGREE_ANGLE);
    }
}
