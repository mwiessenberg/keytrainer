package eu.wiessenberg.keytrainer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.prefs.Preferences;

public class Controller implements Counter.CounterListener {
    @FXML private Slider timeSlider;

    @FXML private Label timeLabel;
    @FXML private Button startStopButton;

    @FXML private ToggleButton buttonAFlat;
    @FXML private ToggleButton buttonA;
    @FXML private ToggleButton buttonASharp;
    @FXML private ToggleButton buttonBFlat;
    @FXML private ToggleButton buttonB;
    @FXML private ToggleButton buttonBSharp;
    @FXML private ToggleButton buttonCFlat;
    @FXML private ToggleButton buttonC;
    @FXML private ToggleButton buttonCSharp;
    @FXML private ToggleButton buttonDFlat;
    @FXML private ToggleButton buttonD;
    @FXML private ToggleButton buttonDSharp;
    @FXML private ToggleButton buttonEFlat;
    @FXML private ToggleButton buttonE;
    @FXML private ToggleButton buttonESharp;
    @FXML private ToggleButton buttonFFlat;
    @FXML private ToggleButton buttonF;
    @FXML private ToggleButton buttonFSharp;
    @FXML private ToggleButton buttonGFlat;
    @FXML private ToggleButton buttonG;
    @FXML private ToggleButton buttonGSharp;
    @FXML private CheckBox major;
    @FXML private CheckBox minor;
    @FXML private Text countdown;
    @FXML private Text activeKey;
    @FXML private Text nextKey;
    @FXML private Text nowPlaying;
    @FXML private Text chordProgression;
    @FXML private Text loopDescription;
    private String nextKeyDescr;

    private long timerInSeconds = 30L;
    private Counter counter;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    private List<ToggleButton> allKeySignatures = new ArrayList<>();

    @FXML
    public void closeApplication() {
        Platform.exit();
    }

    @FXML
    public void initialize() {
        allKeySignatures.add(buttonAFlat);
        allKeySignatures.add(buttonA);
        allKeySignatures.add(buttonASharp);
        allKeySignatures.add(buttonBFlat);
        allKeySignatures.add(buttonB);
        allKeySignatures.add(buttonBSharp);
        allKeySignatures.add(buttonCFlat);
        allKeySignatures.add(buttonC);
        allKeySignatures.add(buttonCSharp);
        allKeySignatures.add(buttonDFlat);
        allKeySignatures.add(buttonD);
        allKeySignatures.add(buttonDSharp);
        allKeySignatures.add(buttonEFlat);
        allKeySignatures.add(buttonE);
        allKeySignatures.add(buttonESharp);
        allKeySignatures.add(buttonFFlat);
        allKeySignatures.add(buttonF);
        allKeySignatures.add(buttonFSharp);
        allKeySignatures.add(buttonGFlat);
        allKeySignatures.add(buttonG);
        allKeySignatures.add(buttonGSharp);

        timeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            timerInSeconds = Math.round(newValue.doubleValue());
            updateTimeLabel(timerInSeconds);

            if (counter != null) {
                counter.setTime(timerInSeconds);
            }

        });

        updateTimeLabel(timerInSeconds);

        restoreApplicationPreferences();
    }

    private String getNextKey() {
        return getNextKeySignature() + " " + getNextMinorOrMajor();
    }

    private void updateKeyLabel(String nextKeySignature) {
        activeKey.setText(nextKeySignature);
    }

    private String getNextMinorOrMajor() {
        boolean pickMajor = new Random().nextBoolean();

        if (major.isSelected() && minor.isSelected()) {
            return pickMajor ? "Major": "minor";
        } else if (major.isSelected()){
            return "Major";
        } else if (minor.isSelected()) {
            return "Minor";
        } else {
            return "";
        }
    }

    private String getNextKeySignature() {
        List<ToggleButton> activatedKeys = new ArrayList<>();
        for (ToggleButton key: allKeySignatures) {
            if (key.isSelected()) {
                activatedKeys.add(key);
            }
        }

        int nextKeyIndex = activatedKeys.size() > 1 ? (new Random().nextInt() & Integer.MAX_VALUE) % (activatedKeys.size() - 1): 0;
        ToggleButton nextKey = activatedKeys.get(nextKeyIndex);
        return nextKey.getText();
    }


    private void updateTimeLabel(long newValue) {
        timeLabel.setText(String.format("%s secs", newValue));
    }

    @FXML
    private void startOrStop() {
        if (counter != null && counter.isRunning()) {
            counter.stop();
            mediaPlayer.stopAllLoops();
            setStartStopButtonAsStopped();
            updateNowPlayingLabel(null);
        } else {
            nextKeyDescr = getNextKey();
            updateKeyLabel(nextKeyDescr);
            start();
        }
    }

    private void setStartStopButtonAsStopped() {
        startStopButton.setText("Start");
    }

    private void setStartStopButtonAsStarted() {
        startStopButton.setText("Stop");
    }

    private void start() {
        counter = new Counter(timerInSeconds);
        counter.addListener(this);
        counter.addTimeTrigger(10);
        counter.start();

        activeKey.setId("key");

        setStartStopButtonAsStarted();

        mediaPlayer.stopAllLoops();
        playLoop();
    }

    private void playLoop() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("loops/test");
        System.out.println("looking for loops in " + url);
        if (url != null) {
            Loop loop = Loop.getRandomLoop(new File(url.getFile()), activeKey.getText());
            updateNowPlayingLabel(loop);
            updateLoopProperties(loop);

            if (loop != null) {
                mediaPlayer.playLoop(loop.getFile());
            } else {
                System.out.println("No loop found for key " + activeKey.getText());
            }
        } else {
            System.err.println("looking for loops in " + url);
        }
    }

    private void updateNowPlayingLabel(Loop loop) {
        if (loop != null) {
            nowPlaying.setText(String.format("Now playing: \"%s\", %s bpm", loop.getName(), loop.getBpm()));
        } else {
            nowPlaying.setText(null);
        }
    }

    private void updateLoopProperties(Loop loop) {
        if (loop != null && loop.getProperties() != null) {
            chordProgression.setText(String.format("Chord progression: %s", loop.getProperties().getChordProgression()));
            loopDescription.setText(loop.getProperties().getDescription());
        } else {
            chordProgression.setText(null);
            loopDescription.setText(null);
        }
    }

    @Override
    public void onCountedDownToZero() {
        updateKeyLabel(nextKeyDescr);
        nextKey.setText(null);
        start();
    }

    @Override
    public void onTimeChanged(long secondsRemaining) {
        updateCountdown(secondsRemaining);
    }

    private void updateCountdown(long secondsRemaining) {
        countdown.setText(StringUtils.formatTime(secondsRemaining));
    }

    @Override
    public void onTimeTrigger(long secondsRemaining) {
        nextKeyDescr = getNextKey();
        nextKey.setText("Up next: " + nextKeyDescr);
        activeKey.setId("key-timesup");
    }

    private void restoreApplicationPreferences() {
        System.out.println("restoring application properties");
        ApplicationPreferences prefs = ApplicationPreferences.getInstance();

        for (ToggleButton toggle: allKeySignatures) {
            toggle.setSelected(prefs.isKeyEnabled(toggle.getText()));
        }

        minor.setSelected(prefs.isMinorEnabled());
        major.setSelected(prefs.isMajorEnabled());

        timeSlider.setValue(prefs.getDuration());
    }

    public void storeApplicationPreferences() {
        System.out.println("storing application properties");
        ApplicationPreferences prefs = ApplicationPreferences.getInstance();

        for (ToggleButton toggle: allKeySignatures) {
            prefs.storeKeyEnabled(toggle.getText(), toggle.isSelected());
        }

        prefs.storeMinor(minor.isSelected());
        prefs.storeMajor(major.isSelected());

        prefs.storeDuration(timeSlider.getValue());
    }
}
