package club.w0sv.aprsfi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;
import javax.measure.quantity.Speed;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown=true)
public class AprsEntry {
    @JsonProperty("class")
    @JsonDeserialize(using=RecordClassDeserializer.class)
    private RecordClass recordClass;
    private String name;
    @JsonProperty("showname")
    private String displayName;
    @JsonProperty("type")
    @JsonDeserialize(using=RecordTypeJsonDeserializer.class)
    private RecordType type;
    @JsonProperty("time")
    private Instant time;
    @JsonProperty("lasttime")
    private Instant lasttime;
    @JsonProperty("lat")
    @JsonDeserialize(using= DegreeAngleDeserializer.class)
    private Quantity<Angle> latitude;
    @JsonProperty("lng")
    @JsonDeserialize(using= DegreeAngleDeserializer.class)
    private Quantity<Angle> longitude;
    @JsonProperty("course")
    @JsonDeserialize(using= DegreeAngleDeserializer.class)
    private Quantity<Angle> course;
    @JsonProperty("speed")
    @JsonDeserialize(using=KphSpeedDeserializer.class)
    private Quantity<Speed> speed;
    @JsonProperty("altitude")
    @JsonDeserialize(using=MetersLengthDeserializer.class) 
    private Quantity<Length> altitude;
    private String symbol;
    private String srccall;
    private String dstcall;
    private String comment;
    private String path;
    private String phg;
    private String status;
    @JsonProperty("status_lasttime")
    private Instant statusLastReceived;

    public RecordClass getRecordClass() {
        return recordClass;
    }

    public void setRecordClass(RecordClass recordClass) {
        this.recordClass = recordClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public RecordType getType() {
        return type;
    }

    public void setType(RecordType type) {
        this.type = type;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public Instant getLasttime() {
        return lasttime;
    }

    public void setLasttime(Instant lasttime) {
        this.lasttime = lasttime;
    }

    public Quantity<Angle> getLatitude() {
        return latitude;
    }

    public void setLatitude(Quantity<Angle> latitude) {
        this.latitude = latitude;
    }

    public Quantity<Angle> getLongitude() {
        return longitude;
    }

    public void setLongitude(Quantity<Angle> longitude) {
        this.longitude = longitude;
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

    public Quantity<Length> getAltitude() {
        return altitude;
    }

    public void setAltitude(Quantity<Length> altitude) {
        this.altitude = altitude;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSrccall() {
        return srccall;
    }

    public void setSrccall(String srccall) {
        this.srccall = srccall;
    }

    public String getDstcall() {
        return dstcall;
    }

    public void setDstcall(String dstcall) {
        this.dstcall = dstcall;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPhg() {
        return phg;
    }

    public void setPhg(String phg) {
        this.phg = phg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getStatusLastReceived() {
        return statusLastReceived;
    }

    public void setStatusLastReceived(Instant statusLastReceived) {
        this.statusLastReceived = statusLastReceived;
    }
}
