package club.w0sv.util;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;

/** A latitude and longitude pair. */
public final class GeoPoint {
    private final Quantity<Angle> latitude;
    private final Quantity<Angle> longitude;
    
    public GeoPoint(Quantity<Angle> lat, Quantity<Angle> lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    public Quantity<Angle> getLatitude() {
        return latitude;
    }

    public Quantity<Angle> getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "<"+ latitude + "," + longitude + ">";
    }
}
