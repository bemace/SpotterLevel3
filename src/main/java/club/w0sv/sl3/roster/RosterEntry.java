package club.w0sv.sl3.roster;

import club.w0sv.util.AprsId;

import java.util.Objects;

public class RosterEntry {
    private final AprsId aprsId;
    private RosterStatus status;

    public RosterEntry(AprsId aprsId, RosterStatus status) {
        this.aprsId = aprsId;
        this.status = status;
    }

    public AprsId getAprsId() {
        return aprsId;
    }

    public RosterStatus getStatus() {
        return status;
    }

    public void setStatus(RosterStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RosterEntry that = (RosterEntry) o;
        return aprsId.equals(that.aprsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aprsId);
    }

    @Override
    public String toString() {
        return aprsId + ":  " + status;
    }
}
