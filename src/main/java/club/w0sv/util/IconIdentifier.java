package club.w0sv.util;

public class IconIdentifier {
    private String iconFilePath;
    private int iconNumber;
    
    public IconIdentifier(String iconFilePath, int iconNumber) {
        this.iconFilePath = iconFilePath;
        this.iconNumber = iconNumber;
    }

    public String getIconFilePath() {
        return iconFilePath;
    }
    
    public int getIconNumber() {
        return iconNumber;
    }
}
