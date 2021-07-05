package club.w0sv.sl3;

import club.w0sv.util.AprsId;
import club.w0sv.util.GeoPoint;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;
import javax.measure.quantity.Speed;
import java.time.ZonedDateTime;

public class TrackingEntry {
    private AprsId aprsId;
    /** time when target first reported this (current) position. */
    private ZonedDateTime arrived;
    private ZonedDateTime latestTime;
    
    private GeoPoint location;
    private Quantity<Length> altitude;
    private Quantity<Angle> course;
    private Quantity<Speed> speed;
    
    private String message;
    private String symbolCode;

    public AprsId getAprsId() {
        return aprsId;
    }

    public void setAprsId(AprsId aprsId) {
        this.aprsId = aprsId;
    }

    public ZonedDateTime getArrived() {
        return arrived;
    }

    public void setArrived(ZonedDateTime arrived) {
        this.arrived = arrived;
    }

    public ZonedDateTime getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(ZonedDateTime latestTime) {
        this.latestTime = latestTime;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public Quantity<Length> getAltitude() {
        return altitude;
    }

    public void setAltitude(Quantity<Length> altitude) {
        this.altitude = altitude;
    }

    public Quantity<Angle> getCourse() {
        return course;
    }

    public void setCourse(Quantity<Angle> course) {
        this.course = course;
    }

    public Quantity<Speed> getSpeed() {
        return speed;
    }

    public void setSpeed(Quantity<Speed> speed) {
        this.speed = speed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSymbolCode() {
        return symbolCode;
    }

    public void setSymbolCode(String symbolCode) {
        this.symbolCode = symbolCode;
    }

    @Override
    public String toString() {
        return "TrackingEntry{" +
                "aprsId=" + aprsId +
                ", latestTime=" + latestTime +
                ", location=" + location +
                ", message='" + message + '\'' +
                '}';
    }
}
