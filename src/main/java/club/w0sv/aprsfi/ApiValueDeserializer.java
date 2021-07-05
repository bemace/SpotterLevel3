package club.w0sv.aprsfi;

import club.w0sv.util.ApiValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ApiValueDeserializer<T extends Enum<T> & ApiValue> extends JsonDeserializer<T> {
    private Class<T> clazz;
    
    public ApiValueDeserializer(Class<T> clazz) {
        this.clazz = clazz;
    }
    
    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String code = p.getValueAsString();
        if (code == null)
            return null;

        for (T value : clazz.getEnumConstants()) {
            if (value.toApiString().equals(code))
                return value;
        }

        return null;
    }
}
