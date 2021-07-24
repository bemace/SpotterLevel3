package club.w0sv.grlevel3;

import club.w0sv.util.GeoPoint;
import club.w0sv.util.XYPoint;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import java.awt.*;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlaceFileWriter {
    private static final int MIN_FONT_NUMBER = 1;
    private static final int MAX_FONT_NUMBER = 8;
    private static final int MIN_ICON_FILE_NUMBER = 1;
    private static final int MAX_ICON_FILE_NUMBER = 8;

    private Writer writer;

    private AtomicInteger nextFontNumber = new AtomicInteger(MIN_FONT_NUMBER);
    private AtomicInteger nextIconFileNumber = new AtomicInteger(MIN_ICON_FILE_NUMBER);
    private boolean inObject = false;

    public PlaceFileWriter(Writer writer) {
        this.writer = writer;
    }

    public void setTitle(String title) throws IOException {
        // titles do not need to be quotes, so we write the whole line ourselves
        writeLine("Title: " + title);
    }

    public void setRefreshFrequency(Duration frequency) throws IOException {
        if (frequency.toSecondsPart() == 0)
            writeLine("Refresh", frequency.toMinutes());
        else
            writeLine("RefreshSeconds", frequency.toSeconds());
    }

    public void startObject(GeoPoint location) throws IOException {
        if (inObject)
            throw new IllegalStateException("cannot nest objects within other objects");

        skipLine();
        writeLine("Object", toDecimal(location.getLatitude()), toDecimal(location.getLongitude()));
        inObject = true;
    }

    public void endObject() throws IOException {
        if (!inObject)
            throw new IllegalStateException("there is no open object");

        inObject = false;
        writeLine("End:");
    }

    private void writeLine(String key, Object... values) throws IOException {
        writeLine((inObject ? "  " : "") + key + ": " + Stream.of(values).filter(v -> v != null).map(this::valueToString).collect(Collectors.joining(", ")));
    }

    private String valueToString(Object value) {
        if (value instanceof Number)
            return String.valueOf(value);
        else if (value instanceof HoverTextInput) {
            return ((HoverTextInput) value).encodeForPlaceFile();
        }
        else
            return "\"" + String.valueOf(value) + "\"";
    }

    /**
     * Adds a line to the place file which contains only the specified {@code text} followed by a line break.
     *
     * @param text
     * @throws IOException
     */
    protected void writeLine(String text) throws IOException {
        writer.write(text + "\r\n");
    }

    /**
     * Adds an empty line to the place file. This is only useful to make the place file easier to read
     * when someone is troubleshooting it.
     *
     * @throws IOException
     */
    protected void skipLine() throws IOException {
        writer.write("\r\n");
    }

    /**
     * @param heightInPixels
     * @param style
     * @param face
     * @return font number for using the newly-configured font in
     * {@link #addText(GeoPoint, int, String, String)} calls.
     */
    public int defineFont(int heightInPixels, FontStyle style, String face) throws IOException {
        int fontNumber = nextFontNumber.getAndIncrement();
        if (fontNumber > MAX_FONT_NUMBER)
            throw new IllegalStateException("too many fonts -- place files may contain a maximum of " + MAX_FONT_NUMBER + " fonts");

        // TODO: recognize duplicates and return existing
        writeLine("Font", fontNumber, heightInPixels, style.value, face);
        return fontNumber;
    }

    /**
     * Adds an icon file which can be used to show icons on the map. A maximum of eight icon files may be added
     * in a single place file.
     * <p>Icon files are composed of any number of icons of the same width and height,
     * all placed into a single image.
     *
     * @param uri        URI of a {@code PNG}, {@code JPG}, or {@code TIF} image which contains one or more icons
     * @param iconWidth
     * @param iconHeight
     * @param hotspot    offset for center of icons in this icon file.
     *                   X value must be between {@code 0} and {@code iconWidth - 1};
     *                   Y value must be between {@code 0} and {@code iconHeight - 1}.
     * @return the icon file number for referencing this icon file in an {@code addIcon} call.
     * @throws IOException
     */
    public int defineIconFile(URI uri, int iconWidth, int iconHeight, XYPoint hotspot) throws IOException {
        int iconFileNumber = nextIconFileNumber.getAndIncrement();
        if (iconFileNumber > MAX_ICON_FILE_NUMBER)
            throw new IndexOutOfBoundsException("too many icon files -- place files may contain a maximum of " + MAX_ICON_FILE_NUMBER + " icon files");

        // TODO: recognize duplicates and return existing
        writeLine("IconFile", iconFileNumber, iconWidth, iconHeight, hotspot.getX(), hotspot.getY(), uri);
        return iconFileNumber;
    }

    /**
     * @param iconFileNumber from earlier {@code addIconFile} call
     * @param iconNumber     number of the icon to draw, from {@code 1} to the number of icons in the icon file.
     *                       Icons in an icon file are numbered from left to right, top to bottom.
     * @param location       coordinates where the icon's hotspot is placed.
     * @param rotation       clockwise rotation of the icon (degrees)
     * @param hoverText      displayed when the mouse cursor hovers over the {@code <latitude,longitude>} of the icon
     * @see #defineIconFile(URI, int, int, XYPoint)
     */
    public void addIcon(int iconFileNumber, int iconNumber, GeoPoint location, Quantity<Angle> rotation,
                        String hoverText) throws IOException {
        checkNotInObject();
        writeLine("Icon", toDecimal(location.getLatitude()), toDecimal(location.getLongitude()), toDecimal(rotation), iconFileNumber, iconNumber, hoverText == null ? null : new HoverTextInput(hoverText));
    }

    /**
     *
     * @param iconFileNumber from earlier {@code addIconFile} call
     * @param iconNumber     number of the icon to draw, from {@code 1} to the number of icons in the icon file.
     *                       Icons in an icon file are numbered from left to right, top to bottom.
     * @param offset       
     * @param rotation       clockwise rotation of the icon (degrees)
     * @param hoverText      displayed when the mouse cursor hovers over the {@code <latitude,longitude>} of the icon
     * @see #defineIconFile(URI, int, int, XYPoint)
     */
    public void addObjectIcon(int iconFileNumber, int iconNumber, XYPoint offset, Quantity<Angle> rotation, String hoverText) throws IOException {
        checkInObject();
        writeLine("Icon", offset.getX(), offset.getY(), toDecimal(rotation), iconFileNumber, iconNumber, hoverText == null ? null : new HoverTextInput(hoverText));
    }
    /**
     * Sets the default color of subsequence Place statements.
     * Default color is white.
     *
     * @param value
     * @throws IOException
     */
    public void setColor(Color value) throws IOException {
        // no commas between color values, so we write the whole line here instead of using writeLine
        writeLine("Color: " + value.getRed() + " " + value.getGreen() + " " + value.getBlue());
    }

    /**
     * Adds a comment to the place file. Comments have no effect on what's displayed in GRLevel3,
     * but can be helpful when troubleshooting problems.
     *
     * @param comment
     */
    public void addComment(String comment) throws IOException {
        if (comment == null || comment.isEmpty())
            return;
        
        writeLine(Arrays.stream(comment.split("\r?\n")).map(c -> "; " + c).collect(Collectors.joining("\n")));
    }

    /**
     * @param location   coordinates for the center of the text string
     * @param fontNumber from earlier {@code configureFont} call
     * @param text       text to display
     * @param hoverText  displayed when the mouse cursor hovers over the {@code <latitude,longitude>} of the string
     * @see #defineFont(int, FontStyle, String)
     */
    public void addText(GeoPoint location, int fontNumber, String text, String hoverText) throws IOException {
        checkNotInObject();
        writeLine("Text", toDecimal(location.getLatitude()), toDecimal(location.getLongitude()), fontNumber, text, hoverText == null ? null : new HoverTextInput(hoverText));
    }

    /**
     * @param offset     offset in pixels from object's center
     * @param fontNumber
     * @param text
     * @param hoverText
     * @throws IOException
     */
    public void addObjectText(XYPoint offset, int fontNumber, String text, String hoverText) throws IOException {
        checkInObject();
        writeLine("Text", offset.getX(), offset.getY(), fontNumber, text, hoverText == null ? null : new HoverTextInput(hoverText));
    }

    private static BigDecimal toDecimal(Quantity<?> quantity) {
        return new BigDecimal(quantity.getValue().toString());
    }

    public static enum FontStyle {
        PLAIN(0),
        BOLD(1),
        ITALIC(2);

        private final int value;

        FontStyle(int value) {
            this.value = value;
        }
    }

    /**
     * @throws IllegalStateException if writer is not currently in an object
     */
    private void checkInObject() {
        if (!inObject)
            throw new IllegalStateException("no object open");

    }

    /**
     * @throws IllegalStateException if writer is currently inside an object.
     */
    private void checkNotInObject() {
        if (inObject)
            throw new IllegalStateException("currentl in an object");
    }
    
    private static class HoverTextInput {
        private final String raw;
        
        public HoverTextInput(String raw) {
            this.raw = raw;
        }
        
        public String encodeForPlaceFile() {
            if (raw == null || raw.isEmpty())
                return null;
            
            String encoded = raw;
            encoded = encoded.replace("\\", "\\\\");
            encoded = encoded.replaceAll("\\t", "\\\\t");
            encoded = encoded.replaceAll("\r?\n","\\\\n");
            return "\"" + encoded + "\"";
        }
    }
}
