package club.w0sv.sl3.event;

import club.w0sv.util.AprsId;
import club.w0sv.sl3.TrackingEntry;
import org.springframework.context.ApplicationEvent;

public class TrackingUpdateEvent extends ApplicationEvent {
    private TrackingEntry data;
    
    public TrackingUpdateEvent(Object source, TrackingEntry data) {
        super(source);
        this.data = data;
    }

    public AprsId getTarget() {
        return data.getAprsId();
    }

    public TrackingEntry getData() {
        return data;
    }

    @Override
    public String toString() {
        return "TrackingUpdateEvent{" +
                "target=" + getTarget() +
                ", data=" + data +
                '}';
    }
}
