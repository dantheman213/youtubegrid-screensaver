package eagleview.models;

import eagleview.data.Utilities;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SettingsModel {
    public String baseAppDir; // where the app dir is located
    public String videoCollectionDir; // where the video cache dir is located

    public int columnCount;
    public int rowCount;
    public List<VideoItemModel> videoCollection;

    public SettingsModel() throws Exception {
        if(videoCollection == null) {
            videoCollection = new ArrayList<VideoItemModel>();
        }

        if(StringUtils.isEmpty(baseAppDir)) {
            baseAppDir = Utilities.getApplicationPath();
            System.out.println(String.format("Application Base Directory: %s", baseAppDir));
        }

        if(StringUtils.isEmpty(videoCollectionDir)) {
            videoCollectionDir = String.format("%s/cache/videos", baseAppDir);
            System.out.println(String.format("Video Cache Directory: %s", videoCollectionDir));
        }

        // ... TBD change
        columnCount = 3;
        rowCount = 3;
    }
}
