package club.w0sv.aprsfi;

import club.w0sv.util.ApiValue;

public enum RecordClass implements ApiValue {
    APRS("a"),
    AIS("i"),
    WEB("w");
    
    private final String apiValue;
    
    RecordClass(String apiValue) {
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
