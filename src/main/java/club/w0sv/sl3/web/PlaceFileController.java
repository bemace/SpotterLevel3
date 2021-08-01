package club.w0sv.sl3.web;

import club.w0sv.grlevel3.IconFileRef;
import club.w0sv.grlevel3.PlaceFileWriter;
//import club.w0sv.sl3.AprsIconSupplier;
import club.w0sv.sl3.AprsIconSupplier;
import club.w0sv.sl3.AprsLookupException;
import club.w0sv.sl3.LocationService;
import club.w0sv.sl3.TrackingEntry;
//import club.w0sv.util.IconIdentifier;
import club.w0sv.util.IconIdentifier;
import club.w0sv.util.QuantityUtil;
import club.w0sv.util.XYPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import systems.uom.common.USCustomary;
import tech.units.indriya.quantity.Quantities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.math.RoundingMode;
import java.net.URI;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Optional;

import static club.w0sv.sl3.AprsIconSupplier.primaryTablePath;

@Controller()
@RequestMapping(path = "/placeFiles")
public class PlaceFileController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private LocationService locationService;
    private AprsIconSupplier iconSupplier;

    public PlaceFileController(@Autowired LocationService locationService) {
        this.locationService = locationService;
        try {
            iconSupplier = new AprsIconSupplier();
        }
        catch (Exception ex) {
            logger.error("error loading icon supplier", ex);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/spotters", produces = "text/plain;charset=Windows-1252")
    @ResponseBody
    public String spotterList(HttpServletRequest request, HttpServletResponse response) throws IOException, AprsLookupException {
        response.setContentType("text/plain");
        locationService.updateIfDue();

        boolean showCallSigns = true;
        boolean showIcons = true;
        boolean useAprsSymbols = true;

        StringWriter out = new StringWriter();

        PlaceFileWriter writer = new PlaceFileWriter(out);
        writer.addComment("Spotter Locations");
        writer.addComment("Generated by " + getClass().getPackage().getImplementationTitle() + " " + getClass().getPackage().getImplementationVersion());
        writer.setTitle("Spotter Locations - SpotterLevel3");
        writer.setRefreshFrequency(Duration.ofMinutes(1));
        int basicFont = writer.defineFont(11, PlaceFileWriter.FontStyle.BOLD, "Courier New");

        IconFileRef aprsPrimary = null;
        IconFileRef aprsSecondary = null;
        IconFileRef coloredDots = null;

        if (showIcons) {
            if (useAprsSymbols) {
                aprsPrimary = writer.defineIconFile(AprsIconSupplier.primaryTablePath, 24, 24, new XYPoint(12, 12));
                aprsSecondary = writer.defineIconFile(AprsIconSupplier.alternateTablePath, 24, 24, new XYPoint(12, 12));
            }
            else {
                URI coloredDotsUri = ServletUriComponentsBuilder.fromContextPath(request).path("iconFiles/colored_dots.png").build().toUri();
                coloredDots = writer.defineIconFile(coloredDotsUri, 12, 12, new XYPoint(6, 6));
            }
        }

        for (TrackingEntry entry : locationService.getEntries()) {
            writer.startObject(entry.getLocation());
            String hoverText = entry.getAprsId().toString();
            if (entry.getCourse() != null) {
                hoverText += "\n" + entry.getCourse() + " @ " + QuantityUtil.setScale(entry.getSpeed().to(USCustomary.MILE_PER_HOUR), 0, RoundingMode.HALF_UP);
            }

            if (showCallSigns) {
                writer.addObjectText(new XYPoint(0, showIcons ? 14 : 0), basicFont, entry.getAprsId().toString(), hoverText);
            }
            if (showIcons) {
                if (useAprsSymbols) {
                    try {
                        IconIdentifier id = iconSupplier.resolveSymbolIcon(entry.getAprsSymbol());
                        Optional<IconFileRef> iconFileRef = writer.getIconFileRef(id.getIconFilePath());
                        if (iconFileRef.isEmpty())
                            logger.warn("IconFile ref not found for {}", id.getIconFilePath());
                        else
                            writer.addObjectIcon(iconFileRef.get(), id.getIconNumber(), XYPoint.NO_OFFSET, Quantities.getQuantity(0, USCustomary.DEGREE_ANGLE), hoverText);
                    }
                    catch (Exception ex) {
                        logger.error("error adding icon for {}", entry.getAprsId(), ex);
                    }
                }
                else
                    writer.addObjectIcon(coloredDots, 1, XYPoint.NO_OFFSET, Quantities.getQuantity(0, USCustomary.DEGREE_ANGLE), hoverText);
            }
            writer.endObject();
        }
        out.flush();
        out.close();

        return out.toString();
    }
}
