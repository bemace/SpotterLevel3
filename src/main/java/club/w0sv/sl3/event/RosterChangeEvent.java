package club.w0sv.sl3.event;

import club.w0sv.sl3.roster.RosterEntry;
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
    private final RosterEntry oldEntry;
    private final RosterEntry newEntry;
    private final ChangeType changeType;
    
    public RosterChangeEvent(Object source, AprsId aprsId, RosterEntry oldEntry, RosterEntry newEntry) {
        super(source);
        this.aprsId = aprsId;
        this.oldEntry = oldEntry;
        this.newEntry = newEntry;
        
        if (oldEntry == null && newEntry == null)
            throw new IllegalArgumentException("oldStatus and newEntry cannot both be null");
        if (oldEntry == null)
            changeType = ChangeType.ADD;
        else if (newEntry == null)
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

    public RosterEntry getOldEntry() {
        return oldEntry;
    }

    public RosterEntry getNewEntry() {
        return newEntry;
    }

    @Override
    public String toString() {
        return "RosterEvent{" +
                "callsignWithSSID=" + aprsId +
                ", oldEntry=" + oldEntry +
                ", newEntry=" + newEntry +
                ", changeType=" + changeType +
                '}';
    }
}
