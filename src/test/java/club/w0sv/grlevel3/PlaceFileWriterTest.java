package club.w0sv.grlevel3;

import club.w0sv.util.GeoPoint;
import club.w0sv.util.XYPoint;
import org.junit.jupiter.api.Test;
import systems.uom.common.USCustomary;
import tech.units.indriya.quantity.Quantities;

import java.awt.*;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceFileWriterTest {

    @Test
    public void basicSpotterList() throws IOException {
        StringWriter sw = new StringWriter();
        PlaceFileWriter writer = new PlaceFileWriter(sw);

        writer.addComment("W0SV Spotter list generated by SpotterLevel3");
        writer.setTitle("W0SV Spotters");
        writer.setRefreshFrequency(Duration.ofMinutes(1));
        
        writer.startObject(new GeoPoint(Quantities.getQuantity(new BigDecimal("45.56000"), USCustomary.DEGREE_ANGLE),
                Quantities.getQuantity(new BigDecimal("-94.22183"), USCustomary.DEGREE_ANGLE)));
        writer.addObjectText(XYPoint.NO_OFFSET, 0, "K9MP-9", Optional.of("ESE @ 1mph"));
        writer.endObject();
        
        writer.startObject(new GeoPoint(Quantities.getQuantity(new BigDecimal("45.48583"), USCustomary.DEGREE_ANGLE),
                Quantities.getQuantity(new BigDecimal("-94.15333"), USCustomary.DEGREE_ANGLE)));
        writer.addObjectText(XYPoint.NO_OFFSET, 0, "W9AX-9", Optional.of("N @ 23mph"));
        writer.endObject();

        writer.startObject(new GeoPoint(Quantities.getQuantity(new BigDecimal("45.58883"), USCustomary.DEGREE_ANGLE),
                Quantities.getQuantity(new BigDecimal("-94.13983"), USCustomary.DEGREE_ANGLE)));
        writer.addObjectText(XYPoint.NO_OFFSET, 0, "KU5MC-9", Optional.empty());
        writer.endObject();

        assertThat(sw.toString().trim().split("\r?\n")).containsExactly(readSampleFile("basic spotter list.txt"));
    }
    
    @Test
    public void iowaDotTrucks() throws IOException {
        StringWriter sw = new StringWriter();
        PlaceFileWriter writer = new PlaceFileWriter(sw);
        writer.setTitle("Iowa DOT Trucks @1817Z");
        writer.setRefreshFrequency(Duration.ofMinutes(5));
        writer.setColor(new Color(200, 200, 255));
        writer.defineIconFile(URI.create("https://mesonet.agron.iastate.edu/request/grx/arrows.png"), 15, 25, new XYPoint(8, 25));
        writer.defineFont(11, PlaceFileWriter.FontStyle.BOLD, "Courier New");

        assertThat(sw.toString().trim().split("\r?\n")).containsExactly(readSampleFile("iowaDotTrucks.txt"));
    }

    /**
     * 
     * @param name of a file in the {@code tests/resources/samplePlaceFiles/} directory
     * @return
     */
    private static String[] readSampleFile(String name) throws IOException {
        return Files.readString(Paths.get("src", "test","resources","samplePlaceFiles", name)).trim().split("\r?\n");
    }
}