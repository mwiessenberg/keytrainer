package eu.wiessenberg.keytrainer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.io.File;

public class MediaPlayer {
    private Clip clip;

    public void stopAllLoops() {
        if (clip != null && clip.isOpen()) {
            clip.close();
        }
    }
    public void playLoop(File file) {
        try {
            clip = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));

//            clip.addLineListener(event -> {
//                if (event.getType() == LineEvent.Type.STOP) {
//                    System.out.println("replaying loop");
//                    clip.start();
//                }
//            });

            clip.open(AudioSystem.getAudioInputStream(file));
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch (Exception exc) {
            exc.printStackTrace(System.err);
        }
    }
}
