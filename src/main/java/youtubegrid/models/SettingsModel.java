package youtubegrid.models;

import youtubegrid.data.OsDetect;
import youtubegrid.data.Utilities;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SettingsModel {
    public String baseAppDir; // where the app dir is located
    public String videoCollectionDir; // where the video cache dir is located

    public String binDir;
    public String youtubeDlBin;

    public int columnCount;
    public int rowCount;
    public List<String> videoCollection;

    public SettingsModel() throws Exception {
        if(StringUtils.isEmpty(baseAppDir)) {
            baseAppDir = Utilities.getApplicationPath();
            System.out.println(String.format("Application Base Directory: %s", baseAppDir));
        }

        if(StringUtils.isEmpty(videoCollectionDir)) {
            videoCollectionDir = String.format("%s%scache%svideos", baseAppDir, File.separator, File.separator);
            System.out.println(String.format("Video Cache Directory: %s", videoCollectionDir));
        }

        if(StringUtils.isEmpty(binDir)) {
            binDir = String.format("%s%sbin", baseAppDir, File.separator);
            System.out.println(String.format("Ext Binary Directory: %s", binDir));
        }

        if(StringUtils.isEmpty(youtubeDlBin)) {
            String osSuffix;
            switch(OsDetect.detect()) {
                case WINDOWS_FAMILY:
                    osSuffix = ".exe";
                    break;
                case MACOS_FAMILY:
                case LINUX_FAMILY:
                    osSuffix = "";
                    break;
                case OTHER:
                default:
                    throw new Exception("Unknown operating system");
            }

            youtubeDlBin = String.format("%s%syoutube-dl%s", binDir, File.separator, osSuffix);
            System.out.println(String.format("youtube-dl bin: %s", youtubeDlBin));
        }

        // ... TBD change
        columnCount = 3;
        rowCount = 3;
    }

    public void updateVideoCollection() {
        videoCollection = Utilities.getVideoCollection();
    }
}
