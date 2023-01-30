package eu.wiessenberg.keytrainer;

import java.util.prefs.Preferences;

public class ApplicationPreferences {
    private static ApplicationPreferences instance;
    private Preferences prefs = Preferences.userNodeForPackage(Controller.class);

    private ApplicationPreferences() {
        // singleton; use getInstance()
    }

    public static ApplicationPreferences getInstance() {
        if (instance == null) {
            instance = new ApplicationPreferences();
        }
        return instance;

    }
    public boolean isKeyEnabled(String key) {
        return prefs.getBoolean(key, true);
    }

    public void storeKeyEnabled(String key, boolean enabled) {
        prefs.putBoolean(key, enabled);
    }

    public void storeMajor(boolean enabled) {
        prefs.putBoolean("major", enabled);
    }

    public void storeMinor(boolean enabled) {
        prefs.putBoolean("minor", enabled);
    }

    public void storeBackingTracks(boolean enabled) {
        prefs.putBoolean("backingTracks", enabled);
    }

    public boolean isBackingTracksEnabled() {
        return prefs.getBoolean("backingTracks", true);
    }

    public void storeLoop(boolean enabled) {
        prefs.putBoolean("loop", enabled);
    }

    public boolean isLoopEnabled() {
        return prefs.getBoolean("loop", true);
    }

    public boolean isMajorEnabled() {
        return prefs.getBoolean("major", true);
    }

    public boolean isMinorEnabled() {
        return prefs.getBoolean("minor", true);
    }

    public double getDuration() {
        return prefs.getDouble("duration", 30);
    }

    public void storeDuration(double duration) {
        prefs.putDouble("duration", duration);
    }
}
