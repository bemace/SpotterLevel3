package club.w0sv.aprsfi;

import club.w0sv.util.ApiValue;

public enum RecordType implements ApiValue {
    AIS("a"),
    APRS_STATION("l"),
    APRS_ITEM("i"),
    APRS_OBJECT("o"),
    WEATHER_STATION("w");
    
    private final String apiValue;
    
    RecordType(String apiValue) {
        this.apiValue = apiValue;
    }

    @Override
    public String toDisplayString() {
        return name();
    }

    @Override
    public String toApiString() {
        return apiValue;
    }
}
