package club.w0sv.util;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CallSign {
    
    private final String call;
    /** part after slash, if any */
    private final Optional<String> indicator;

    /**
     * 
     * @param call base call sign with no other indicators
     * @param indicator part after a {@code /}, if any. Can be {@code null}.
     */
    public CallSign(String call, String indicator) {
        this.call = call.toUpperCase();
        this.indicator = Optional.ofNullable(indicator);
    }
    
    public String getCall() {
        return call;
    }
    
    public Optional<String> getIndicator() {
        return indicator;
    }
    
    public CallSign withIndicator(String indicator) {
        if (this.indicator == null && indicator == null)
            return this;
        if (this.indicator != null && this.indicator.equals(indicator))
            return this;
        
        return new CallSign(call, indicator);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallSign callSign = (CallSign) o;
        return call.equals(callSign.call);
    }

    @Override
    public int hashCode() {
        return Objects.hash(call);
    }

    @Override
    public String toString() {
        if (indicator.isPresent())
            return call + "/" + indicator;
        else
            return call;
    }

    public static CallSign of(String str) {
        str = str.trim();
        int i = str.indexOf('/');
        if (i == 0 || i == str.length() - 1)
            throw new IllegalArgumentException(str + " is not a valid call sign");
        
        String call;
        String indicator = null;
        
        if (i > 0) {
            call = str.substring(0,i);
            indicator = str.substring(i+1);
        }
        else
            call = str;
        
        if (!isValidCallSign(call))
            throw new IllegalArgumentException(str + " is not a valid call sign");
        
        return new CallSign(call, indicator);
    }
    
    private static Pattern callSignPattern = Pattern.compile("\\w+\\d\\w+");
    
    private static boolean isValidCallSign(String str) {
        Matcher m = callSignPattern.matcher(str);
        return m.matches();
    }
}
