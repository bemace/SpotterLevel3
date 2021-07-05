package club.w0sv.aprsfi;

import club.w0sv.aprsfi.Result;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class ResultJsonDeserializer extends JsonDeserializer<Result> {
    @Override
    public Result deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String code = p.getValueAsString();
        if (code == null)
            return null;
        
        for (Result result : Result.values()) {
            if (result.toApiString().equals(code))
                return result;
        }
        
        return null;
    }
}
