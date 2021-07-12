package club.w0sv.sl3.roster;

import club.w0sv.util.AprsId;
import club.w0sv.sl3.event.RosterChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * The authoritative roster.
 */
@Service
public class RosterService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ApplicationEventPublisher publisher;

    private Map<AprsId, RosterEntry> entries = Collections.synchronizedMap(new HashMap<>());

    public RosterService(@Autowired ApplicationEventPublisher publisher) {
        this.publisher = publisher;
        addOrUpdate(new RosterEntry(AprsId.of("W9AX-9"), RosterStatus.CHECKED_IN));
        addOrUpdate(new RosterEntry(AprsId.of("KU5MC-9"), RosterStatus.CHECKED_IN));
        addOrUpdate(new RosterEntry(AprsId.of("K9MP-9"), RosterStatus.CHECKED_IN));
        addOrUpdate(new RosterEntry(AprsId.of("N0TAC-2"), RosterStatus.CHECKED_IN));
        addOrUpdate(new RosterEntry(AprsId.of("KD0NNI-tour"), RosterStatus.CHECKED_IN));
        addOrUpdate(new RosterEntry(AprsId.of("WJ0U-10"), RosterStatus.CHECKED_IN));
        addOrUpdate(new RosterEntry(AprsId.of("KE0UWL-1"), RosterStatus.CHECKED_OUT));
        addOrUpdate(new RosterEntry(AprsId.of("KC0TAF-1-i"), RosterStatus.CHECKED_OUT));
        addOrUpdate(new RosterEntry(AprsId.of("KC0UEA-i"), RosterStatus.CHECKED_OUT));
    }

    public Collection<RosterEntry> getEntries() {
        return Collections.unmodifiableCollection(entries.values());
    }

    public Set<AprsId> getAprsIds() {
        return Collections.unmodifiableSet(entries.keySet());
    }

    public void addOrUpdate(RosterEntry entry) {
        if (entry.getAprsId() == null)
            throw new IllegalArgumentException("callsign parameter cannot be null");
        if (entry.getStatus() == null)
            throw new IllegalArgumentException("status parameter cannot be null");

        RosterEntry existingEntry = entries.get(entry.getAprsId());
        entries.put(entry.getAprsId(), entry);
        logger.debug("{} {} ({})", existingEntry == null ? "ADD" : "UPDATE", entry.getAprsId(), entry.getStatus());
        publisher.publishEvent(new RosterChangeEvent(this, entry.getAprsId(), existingEntry, entry));
    }

    public void remove(AprsId what) {
        RosterEntry oldEntry = entries.remove(what);
        if (oldEntry == null) {
            logger.trace("{} not found in roster", what);
        }
        else {
            logger.debug("{} removed", what);
            publisher.publishEvent(new RosterChangeEvent(this, what, oldEntry, null));
        }
    }
}
