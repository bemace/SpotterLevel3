package club.w0sv.sl3.config;

import java.awt.*;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("placefile")
public class PlaceFileConfig {
    
    private String urlSlug = "spotters";
    private ObjectDisplayType objectDisplayType = ObjectDisplayType.TEXT_AND_ICON;
    private Color textColor = Color.WHITE;
    private IconType iconType = IconType.APRS;

    public String getUrlSlug() {
        return urlSlug;
    }

    public void setUrlSlug(String urlSlug) {
        this.urlSlug = urlSlug;
    }

    public ObjectDisplayType getObjectDisplayType() {
        return objectDisplayType;
    }

    public void setObjectDisplayType(ObjectDisplayType objectDisplayType) {
        this.objectDisplayType = objectDisplayType;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public IconType getIconType() {
        return iconType;
    }

    public void setIconType(IconType iconType) {
        this.iconType = iconType;
    }

    public static enum ObjectDisplayType {
        TEXT,
        ICON,
        TEXT_AND_ICON;
        
        public boolean includesText() {
            return this == TEXT || this == TEXT_AND_ICON;
        }
        
        public boolean includesIcon() {
            return this == ICON || this == TEXT_AND_ICON;
        }
    }
    
    public static enum IconType {
        DOTS,
        APRS;
    }
}
