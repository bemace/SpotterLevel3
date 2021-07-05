package club.w0sv.sl3;

import club.w0sv.aprsfi.AprsFiClient;
import club.w0sv.aprsfi.AprsEntry;
import club.w0sv.sl3.config.AprsFiConfig;
import club.w0sv.sl3.event.TrackingUpdateEvent;
import club.w0sv.util.AprsId;
import club.w0sv.util.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;

@Service
public class AprsService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ApplicationEventPublisher publisher;

    private RestTemplateBuilder restTemplateBuilder;
    private AprsFiConfig aprsfiConfig;
    private AprsFiClient client;
    private Map<AprsId, TrackingEntry> trackingEntries = Collections.synchronizedMap(new HashMap<>());

    public AprsService(@Autowired AprsFiConfig aprsfiConfig, @Autowired ApplicationEventPublisher publisher, @Autowired RestTemplateBuilder restTemplateBuilder) {
        this.aprsfiConfig = aprsfiConfig;
        this.publisher = publisher;
        this.restTemplateBuilder = restTemplateBuilder;
        client = new AprsFiClient(aprsfiConfig.getApikey(), restTemplateBuilder);
    }


    public Collection<TrackingEntry> getEntries() {
        return Collections.unmodifiableCollection(trackingEntries.values());
    }

    public void locate(Set<AprsId> ids) throws AprsLookupException {
        try {
            for (AprsEntry entry : client.findLocations(ids)) {
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
                t.setSymbolCode(entry.getSymbol());
                trackingEntries.put(id, t);
                logger.debug("{} {}", id, t);

                publisher.publishEvent(new TrackingUpdateEvent(this, t));
            }
        }
        catch (Exception ex) {
            throw new AprsLookupException("APRS lookup failed", ex);
        }
    }
}
