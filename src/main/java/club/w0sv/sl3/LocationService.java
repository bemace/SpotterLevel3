package club.w0sv.sl3;

import club.w0sv.aprsfi.AprsEntry;
import club.w0sv.aprsfi.AprsFiClient;
import club.w0sv.sl3.event.TrackingUpdateEvent;
import club.w0sv.sl3.roster.RosterService;
import club.w0sv.util.AprsId;
import club.w0sv.util.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class LocationService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ApplicationEventPublisher publisher;

    private Duration updateInterval = Duration.ofSeconds(60);

    private RosterService rosterService;
    private AprsFiClient client;
    private Map<AprsId, TrackingEntry> trackingEntries = Collections.synchronizedMap(new HashMap<>());
    private ZonedDateTime lastUpdated;

    public LocationService(@Autowired AprsFiClient client, @Autowired ApplicationEventPublisher publisher) {
        this.client = client;
        this.publisher = publisher;
    }


    public Collection<TrackingEntry> getEntries() {
        return Collections.unmodifiableCollection(trackingEntries.values());
    }

    public void updateIfDue() throws AprsLookupException {
        ZonedDateTime now = ZonedDateTime.now();
        if (lastUpdated == null) {
            logger.info("updating locations -- no previous updates");
        }
        else if (lastUpdated.plus(updateInterval).isBefore(now)) {
            logger.info("updating locations -- last updated {}", lastUpdated);
        }
        else {
            logger.debug("not updating -- last updated {}", lastUpdated);
            return;
        }

        locate();
    }

    public void locate() throws AprsLookupException {
        try {
            ZonedDateTime updateStarted = ZonedDateTime.now();
            for (AprsEntry entry : client.findLocations(rosterService.getAprsIds())) {
                logger.trace("{} ({}) - <{},{}> as of {}", entry.getDisplayName(), entry.getName(), entry.getLatitude(), entry.getLongitude(), entry.getLasttime());

                AprsId id = AprsId.of(entry.getName());
                TrackingEntry t = trackingEntries.get(id);
                if (t == null) {
                    t = new TrackingEntry();
                    t.setAprsId(id);
                }
                t.setLocation(new GeoPoint(entry.getLatitude(), entry.getLongitude()));
                t.setAltitude(entry.getAltitude());
                t.setCourse(entry.getCourse());
                t.setSpeed(entry.getSpeed());
                t.setLatestTime(entry.getLasttime().atZone(ZoneId.systemDefault()));
                t.setArrived(entry.getTime().atZone(ZoneId.systemDefault()));
                t.setMessage(entry.getComment());
                t.setAprsSymbol(AprsSymbol.from(entry.getSymbol().charAt(0),entry.getSymbol().charAt(1)));
                trackingEntries.put(id, t);
                logger.debug("{} {}", id, t);

                publisher.publishEvent(new TrackingUpdateEvent(this, t));
            }

            lastUpdated = updateStarted;
        }
        catch (Exception ex) {
            throw new AprsLookupException("APRS lookup failed", ex);
        }
    }

    public RosterService getRosterService() {
        return rosterService;
    }

    @Autowired
    public void setRosterService(RosterService rosterService) {
        this.rosterService = rosterService;
    }
}
