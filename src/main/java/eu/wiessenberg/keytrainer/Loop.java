package eu.wiessenberg.keytrainer;

import java.io.File;
import java.util.Random;

public class Loop {
    private File file;
    private String key;
    private String name;
    private String bpm;
    private LoopProperties properties;

    private Loop(File file, LoopProperties properties, String key, String name, String bpm) {
        this.file = file;
        this.properties = properties;
        this.key = key;
        this.name = name;
        this.bpm = bpm;
    }

    public LoopProperties getProperties() {
        return properties;
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

    String getBpm() {
        return bpm;
    }

    private static Loop fromFile(File file) {
        String[] parts = file.getName().split("[._]+");
        return new Loop(file, getLoopProperties(file), parts[0], parts[1], parts[2]);
    }

    private static LoopProperties getLoopProperties(File loopFile) {
        File propertiesFile = new File(loopFile.getParent() + File.separator + loopFile.getName().substring(0, loopFile.getName().indexOf('.')) + ".properties");
        return propertiesFile.exists() ? LoopProperties.fromPropertyFile(propertiesFile): null;
    }

    public static Loop getRandomLoop(File path, String key) {
        File[] files = path.listFiles(file -> file.getName().startsWith(key) && file.getName().endsWith(".wav"));
        System.out.println("Found " + files.length + " loops for key " + key);
        if (files.length > 0) {
            File randomFile = files[Math.abs(new Random().nextInt()) % files.length];
            return fromFile(randomFile);
        }
        return null;
    }
}
