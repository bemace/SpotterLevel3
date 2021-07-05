package club.w0sv.sl3.event;

import club.w0sv.util.AprsId;
import club.w0sv.sl3.roster.RosterStatus;
import org.springframework.context.ApplicationEvent;

public class RosterChangeEvent extends ApplicationEvent {
    public static enum ChangeType {
        ADD,
        UPDATE,
        REMOVE;
    }
    
    private final AprsId aprsId;
    private final RosterStatus oldStatus;
    private final RosterStatus newStatus;
    private final ChangeType changeType;
    
    public RosterChangeEvent(Object source, AprsId aprsId, RosterStatus oldStatus, RosterStatus newStatus) {
        super(source);
        this.aprsId = aprsId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        
        if (oldStatus == null && newStatus == null)
            throw new IllegalArgumentException("oldStatus and newStatus cannot both be null");
        if (oldStatus == null)
            changeType = ChangeType.ADD;
        else if (newStatus == null)
            changeType = ChangeType.REMOVE;
        else
            changeType = ChangeType.UPDATE;
    }
    
    public ChangeType getChangeType() {
        return changeType;
    }

    public AprsId getAprsId() {
        return aprsId;
    }

    public RosterStatus getOldStatus() {
        return oldStatus;
    }

    public RosterStatus getNewStatus() {
        return newStatus;
    }

    @Override
    public String toString() {
        return "RosterEvent{" +
                "callsignWithSSID=" + aprsId +
                ", oldStatus=" + oldStatus +
                ", newStatus=" + newStatus +
                ", changeType=" + changeType +
                '}';
    }
}
