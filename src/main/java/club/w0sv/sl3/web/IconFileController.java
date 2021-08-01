package club.w0sv.sl3.web;

import club.w0sv.sl3.AprsSymbol;
import club.w0sv.sl3.gui.IconManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/iconFiles")
public class IconFileController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private IconManager iconManager;

    private final int iconSize = 24;

    @RequestMapping(value = "{fileName}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> file(@PathVariable("fileName") String fileName) throws IOException {
        try {
            var iconFile = new ClassPathResource("iconFiles/" + fileName);
            byte[] bytes = StreamUtils.copyToByteArray(iconFile.getInputStream());

            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytes);
        }
        catch (FileNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "aprs/primary.png", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    @Cacheable("APRS-primary")
    public ResponseEntity<byte[]> primaryAprsIcons() throws IOException {
        BufferedImage image = createAprsIconFile('/', 16);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return ResponseEntity.ok(baos.toByteArray());
    }

    @RequestMapping(value = "aprs/alternate.png", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    @Cacheable(value = "APRS-alternate")
    public ResponseEntity<byte[]> alternateAprsIcons() throws IOException {
        BufferedImage image = createAprsIconFile('\\', 16);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return ResponseEntity.ok(baos.toByteArray());
    }

    private BufferedImage createAprsIconFile(char tableIndicator, int iconsPerRow) {
        BufferedImage image = new BufferedImage(iconSize * iconsPerRow, iconSize * 6, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = null;
        try {
            logger.trace("constructing APRS IconFile for {} table", tableIndicator);
            g = image.createGraphics();
            int row, col;
            for (int i = AprsSymbol.FIRST_SYMBOL_CHAR; i <= AprsSymbol.LAST_SYMBOL_CHAR; i++) {
                row = (i - AprsSymbol.FIRST_SYMBOL_CHAR) / iconsPerRow;
                col = (i - AprsSymbol.FIRST_SYMBOL_CHAR) % iconsPerRow;
                Optional<Icon> symbol = iconManager.getIcon(AprsSymbol.from(tableIndicator, (char) i));
                if (symbol.isPresent()) {
                    g.drawImage(((ImageIcon) symbol.get()).getImage(), col * iconSize, row * iconSize, null);
                }
            }
            return image;
        }
        finally {
            if (g != null)
                g.dispose();
        }
    }

    @RequestMapping(value = "aprs/overlays.png", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    @Cacheable("APRS-overlays")
    public ResponseEntity<byte[]> aprsIconOverlays() throws IOException {
        BufferedImage image = createAprsOverlayIconFile();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return ResponseEntity.ok(baos.toByteArray());
    }
    
    private BufferedImage createAprsOverlayIconFile() {
        logger.trace("constructing APRS overlay IconFile");
        final String overlayCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final int iconsPerRow = overlayCharacters.length();
        final int rows = AprsSymbol.LAST_SYMBOL_CHAR - AprsSymbol.FIRST_SYMBOL_CHAR;
        BufferedImage image = new BufferedImage(iconSize * iconsPerRow, iconSize * rows, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = null;
        try {
            g = image.createGraphics();
            int row;
            for (int s = AprsSymbol.FIRST_SYMBOL_CHAR; s <= AprsSymbol.LAST_SYMBOL_CHAR; s++) {
                row = s - AprsSymbol.FIRST_SYMBOL_CHAR;
                for (int col = 0; col < overlayCharacters.length(); col++) {
                    char overlayChar = overlayCharacters.charAt(col);
                    Optional<Icon> symbol = iconManager.getIcon(AprsSymbol.from(overlayChar, (char) s));
                    if (symbol.isPresent()) {
                        g.drawImage(((ImageIcon) symbol.get()).getImage(), col * iconSize, row * iconSize, null);
                    }
                }
            }
            return image;
        }
        finally {
            if (g != null)
                g.dispose();
        }
    }
    
    public IconManager getIconManager() {
        return iconManager;
    }

    @Autowired
    public void setIconManager(IconManager iconManager) {
        this.iconManager = iconManager;
    }
}
