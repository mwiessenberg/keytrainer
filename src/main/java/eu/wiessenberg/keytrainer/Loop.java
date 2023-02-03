package eu.wiessenberg.keytrainer;

import java.io.File;
import java.util.Locale;
import java.util.Random;

public class Loop {
    private final File file;
    private final String key;
    private final String name;

    private Loop(File file, String key, String name) {
        this.file = file;
        this.key = key;
        this.name = name;
    }

    File getFile() {
        return file;
    }

    public String getKey() {
        return key;
    }

    String getName() {
        return name;
    }

    private static Loop fromFile(File file) {
        String[] parts = file.getName().split("[._]+");
        return new Loop(file, parts[0], parts[1]);
    }

    public static Loop getRandomLoop(File path, String key) {
        File[] files = path.listFiles(file -> file.getName().toLowerCase().startsWith(key.toLowerCase()) && file.getName().endsWith(".wav"));
        System.out.println("Found " + files.length + " loops for key " + key);
        if (files.length > 0) {
            File randomFile = files[Math.abs(new Random().nextInt()) % files.length];
            return fromFile(randomFile);
        }
        return null;
    }
}
