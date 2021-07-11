package club.w0sv.util;

import java.util.Objects;

public final class AprsId {
    private static final String DEFAULT_SSID = "9";

    private final CallSign callSign;
    private final String ssid;

    public AprsId(String callSign, int ssid) {
        this(callSign, String.valueOf(ssid));
    }

    public AprsId(String callSign, String ssid) {
        this(CallSign.of(callSign), ssid);
    }

    public AprsId(CallSign callSign, String ssid) {
        this.callSign = callSign;
        this.ssid = ssid;
    }

    public CallSign getCallSign() {
        return callSign;
    }

    public String getSsid() {
        return ssid;
    }

    /**
     * Returns a new {@code AprsId} with the same call sign but with the SSID set to {@code ssid}.
     *
     * @param ssid
     * @return
     */
    public AprsId withSSID(String ssid) {
        if (ssid == this.ssid)
            return this;

        return new AprsId(callSign, ssid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AprsId aprsId = (AprsId) o;
        return callSign.equals(aprsId.callSign) &&
                ssid.equals(aprsId.ssid);
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
            return new AprsId(CallSign.of(str), DEFAULT_SSID);
        else
            return new AprsId(CallSign.of(str.substring(0, i)), str.substring(i + 1));
    }
}
