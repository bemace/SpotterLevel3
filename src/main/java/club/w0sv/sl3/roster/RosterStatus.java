package club.w0sv.sl3.roster;

import club.w0sv.util.Displayable;

public enum RosterStatus implements Displayable {
    CHECKED_IN("Checked In"),
    CHECKED_OUT("Checked Out");
    
    private final String display;
    
    RosterStatus(String display) {
        this.display = display;
    }

    @Override
    public String toDisplayString() {
        return display;
    }
}
