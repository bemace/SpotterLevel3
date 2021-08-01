package club.w0sv.grlevel3;

public class IconFileRef {
    private static final int MAX_FILES = 8;
    private static final IconFileRef[] instances = new IconFileRef[MAX_FILES];
    
    private final int fileNumber;
    
    private IconFileRef(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    public int getFileNumber() {
        return fileNumber;
    }
    
    public static IconFileRef valueOf(int fileNumber) {
        if (fileNumber < 1 || fileNumber > MAX_FILES)
            throw new IllegalArgumentException("IconFile fileNumbers must be between 1 and " + MAX_FILES);
        
        return instances[fileNumber-1];
    }
    
    static {
        for (int i = 1; i <= MAX_FILES; i++)
            instances[i-1] = new IconFileRef(i);
    }
}
