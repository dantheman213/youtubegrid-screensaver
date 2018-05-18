package eagleview.data;

public class OsDetect {
    public enum OperatingSystem {
        WINDOWS_FAMILY,
        MACOS_FAMILY,
        LINUX_FAMILY,
        OTHER
    }

    private static OperatingSystem detectedOperatingSystem;

    public static OperatingSystem detect() {
        if(detectedOperatingSystem == null) {
            String rawOsProp = System.getProperty("os.name").toLowerCase();
            //System.out.println(rawOsProp);

            if(rawOsProp.contains("win")) {
                detectedOperatingSystem = OperatingSystem.WINDOWS_FAMILY;
            } else if(rawOsProp.contains("mac") || rawOsProp.contains("osx")|| rawOsProp.contains("os x")) {
                detectedOperatingSystem = OperatingSystem.MACOS_FAMILY;
            } else if(rawOsProp.contains("nix") || rawOsProp.contains("nux") || rawOsProp.contains("aix")) {
                detectedOperatingSystem = OperatingSystem.LINUX_FAMILY;
            } else {
                detectedOperatingSystem = OperatingSystem.OTHER;
            }
        }

        return detectedOperatingSystem;
    }
}
