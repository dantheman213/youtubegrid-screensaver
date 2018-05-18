package eagleview.models;

import java.util.List;

public class SettingsModel {
    private static String baseAppDir; // where the app dir is located
    private static String videoCollectionDir; // where the video cache dir is located

    public static int columnCount;
    public static int rowCount;
    public static List<String> videoCollectionUrls; // YouTube URLs

    public SettingsModel() throws Exception {
        // ...
        
    }
}
