package eu.wiessenberg.keytrainer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LoopProperties {
    private String[] chordProgression;
    private String description;

    private LoopProperties(String[] chordProgression, String description) {
        this.chordProgression = chordProgression;
        this.description = description;
    }

    public String getChordProgression() {
        String progession = "";
        for (int i=0; i<chordProgression.length; i++) {
            progession += chordProgression[i];
            if (i < chordProgression.length - 1) {
                progession += " -> ";
            }
        }
        return progession;
    }

    public String getDescription() {
        return description;
    }

    static LoopProperties fromPropertyFile(File file) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(file)) {
            properties.load(inputStream);

            String chordProgression = properties.getProperty("chord.progression");
            String description = properties.getProperty("description");
            return new LoopProperties(chordProgression.split(","), description);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
