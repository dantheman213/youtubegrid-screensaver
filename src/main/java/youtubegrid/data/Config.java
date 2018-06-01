package youtubegrid.data;

import youtubegrid.models.SettingsModel;

public class Config {
    public static SettingsModel data;

    public Config() throws Exception {
        if(data == null) {
            data = new SettingsModel();
        }
    }
}
