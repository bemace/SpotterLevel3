package club.w0sv.aprsfi;

import club.w0sv.util.ApiValue;

public enum Result implements ApiValue {
    
    OK("ok"),
    FAIL("fail");
    
    private String api;
    
    Result(String apiValue) {
        this.api = apiValue;
    }

    @Override
    public String toDisplayString() {
        return name();
    }

    @Override
    public String toApiString() {
        return api;
    }
}
