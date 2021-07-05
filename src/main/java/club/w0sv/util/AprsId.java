package club.w0sv.util;

import java.util.Objects;

public final class AprsId {
    private final CallSign callSign;
    private final int ssid;

    public AprsId(String callSign, int ssid) {
        this(CallSign.of(callSign), ssid);
    }
    
    public AprsId(CallSign callSign, int ssid) {
        this.callSign = callSign;
        this.ssid = ssid;
    }

    public CallSign getCallSign() {
        return callSign;
    }

    public int getSsid() {
        return ssid;
    }

    /**
     * Returns a new {@code AprsId} with the same call sign but with the SSID set to {@code ssid}.
     * @param ssid
     * @return
     */
    public AprsId withSSID(int ssid) {
        if (ssid == this.ssid)
            return this;
        
        return new AprsId(callSign, ssid);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AprsId aprsId = (AprsId) o;
        return ssid == aprsId.ssid &&
                callSign.getCall().equals(aprsId.callSign.getCall());
    }

    @Override
    public int hashCode() {
        return Objects.hash(callSign, ssid);
    }

    @Override
    public String toString() {
        return callSign.getCall() + "-" + ssid;
    }


    public static AprsId of(String str) {
        int i = str.indexOf('-');
        if (i < 0)
            return new AprsId(CallSign.of(str),9);
        else
            return new AprsId(CallSign.of(str.substring(0,i)),Integer.parseInt(str.substring(i+1)));
    }
}
