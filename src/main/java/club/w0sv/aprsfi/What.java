package club.w0sv.aprsfi;

import club.w0sv.util.ApiValue;

public enum What implements ApiValue {
    LOCATION("loc");
    
    private final String apiValue;
    
    What(String apiValue) {
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
