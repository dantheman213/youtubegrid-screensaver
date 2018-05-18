package eagleview.models;

import eagleview.data.Utilities;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SettingsModel {
    private static String baseAppDir; // where the app dir is located
    private static String videoCollectionDir; // where the video cache dir is located

    public static int columnCount;
    public static int rowCount;
    public static List<VideoItemModel> videoCollection;

    public SettingsModel() throws Exception {
        if(videoCollection == null) {
            videoCollection = new ArrayList<VideoItemModel>();
        }

        if(StringUtils.isEmpty(baseAppDir)) {
            baseAppDir = Utilities.getApplicationPath();
        }

        if(StringUtils.isEmpty(videoCollectionDir)) {
            videoCollectionDir = String.format("%s/cache/videos", baseAppDir);
        }

        // ... TBD change
        columnCount = 3;
        rowCount = 3;
    }
}
