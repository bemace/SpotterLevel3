package club.w0sv.sl3.roster;

import club.w0sv.util.AprsId;
import club.w0sv.sl3.event.RosterChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The authoritative roster.
 */
@Service
public class RosterService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ApplicationEventPublisher publisher;
    
    private Map<AprsId,RosterStatus> entries = Collections.synchronizedMap(new HashMap<>());
    
    public RosterService(@Autowired ApplicationEventPublisher publisher) {
        this.publisher = publisher;
        entries.put(AprsId.of("W9AX-9"), RosterStatus.CHECKED_IN);
        entries.put(AprsId.of("KU5MC-9"), RosterStatus.CHECKED_IN);
        entries.put(AprsId.of("K9MP-9"), RosterStatus.CHECKED_IN);
        entries.put(AprsId.of("N0TAC-2"), RosterStatus.CHECKED_IN);
        entries.put(AprsId.of("KD0NNI-tour"), RosterStatus.CHECKED_IN);
        entries.put(AprsId.of("WJ0U-10"), RosterStatus.CHECKED_IN);
        entries.put(AprsId.of("KE0UWL-1"), RosterStatus.CHECKED_OUT);
        entries.put(AprsId.of("KC0TAF-1-i"), RosterStatus.CHECKED_OUT);
        entries.put(AprsId.of("KC0UEA-i"), RosterStatus.CHECKED_OUT);
    }
    
    public Set<Map.Entry<AprsId,RosterStatus>> getEntries() {
        return Collections.unmodifiableSet(entries.entrySet());
    }
    
    public Set<AprsId> getAprsIds() {
        return Collections.unmodifiableSet(entries.keySet());
    }
    
    public void addOrUpdate(AprsId what, RosterStatus status) {
        if (what == null)
            throw new IllegalArgumentException("callsign parameter cannot be null");
        if (status == null)
            throw new IllegalArgumentException("status parameter cannot be null");
        
        RosterStatus oldStatus = entries.get(what);
        entries.put(what, status);
        logger.debug("{} {}", what, status);
        publisher.publishEvent(new RosterChangeEvent(this, what, oldStatus, status));
    }
    
    public void remove(AprsId what) {
        RosterStatus oldStatus = entries.remove(what);
        if (oldStatus != null) {
            logger.debug("{} removed", what);
            publisher.publishEvent(new RosterChangeEvent(this, what, oldStatus, null));
        }
    }
}
