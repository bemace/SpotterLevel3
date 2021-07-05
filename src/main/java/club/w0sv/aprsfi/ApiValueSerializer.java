package club.w0sv.aprsfi;

import club.w0sv.util.ApiValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ApiValueSerializer extends JsonSerializer<ApiValue> {
    @Override
    public void serialize(ApiValue value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null)
            gen.writeNull();
        else
            gen.writeString(value.toApiString());
    }
}
