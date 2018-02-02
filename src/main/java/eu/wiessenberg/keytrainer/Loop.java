package eu.wiessenberg.keytrainer;

import java.io.File;
import java.util.Random;

public class Loop {
    private File file;
    private String key;
    private String name;
    private String bpm;

    private Loop(File file, String key, String name, String bpm) {
        this.file = file;
        this.key = key;
        this.name = name;
        this.bpm = bpm;
    }

    public File getFile() {
        return file;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getBpm() {
        return bpm;
    }

    public static Loop fromFile(File file) {
        String[] parts = file.getName().split("[._]+");
        System.out.println("key: " + parts[0] + ", name: " + parts[1] + ", bpm: " + parts[2]);

        return new Loop(file, parts[0], parts[1], parts[2]);
    }

    public static Loop getRandomLoop(File path, String key) {
        System.out.println(path.getName() + " startswith " + key + "? " + path.getName().startsWith(key));
        File[] files = path.listFiles(file -> file.getName().startsWith(key));
        System.out.println("Found " + files.length + " loops for key " + key);
        if (files.length > 0) {
            File randomFile = files[Math.abs(new Random().nextInt()) % files.length];
            return fromFile(randomFile);
        }
        return null;
    }
}
