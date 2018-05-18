package eagleview.data;

import eagleview.models.SettingsModel;

public class Config {
    public static SettingsModel settings;

    public Config() throws Exception {
        if(settings == null) {
            settings = new SettingsModel();
        }
    }
}
