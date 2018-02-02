package eu.wiessenberg.keytrainer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller implements Counter.CounterListener {
    @FXML private Slider timeSlider;

    @FXML private Label timeLabel;
    @FXML private Button startStopButton;

    @FXML private ToggleButton buttonBFlat;
    @FXML private ToggleButton buttonB;
    @FXML private ToggleButton buttonA;
    @FXML private ToggleButton buttonC;
    @FXML private ToggleButton buttonCSharp;
    @FXML private ToggleButton buttonD;
    @FXML private ToggleButton buttonE;
    @FXML private ToggleButton buttonEFlat;
    @FXML private ToggleButton buttonF;
    @FXML private ToggleButton buttonFSharp;
    @FXML private ToggleButton buttonG;
    @FXML private ToggleButton buttonAFlat;
    @FXML private CheckBox major;
    @FXML private CheckBox minor;
    @FXML private Text countdown;
    @FXML private Text activeKey;
    @FXML private Text nextKey;
    @FXML private Text nowPlaying;
    private String nextKeyDescr;

    private long timerInSeconds = 30L;
    private Counter counter;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    private List<ToggleButton> allKeySignatures = new ArrayList<>();

    @FXML
    public void closeApplication() {
        System.exit(0);
    }

    @FXML
    public void initialize() {
        allKeySignatures.add(buttonA);
        allKeySignatures.add(buttonBFlat);
        allKeySignatures.add(buttonB);
        allKeySignatures.add(buttonC);
        allKeySignatures.add(buttonCSharp);
        allKeySignatures.add(buttonD);
        allKeySignatures.add(buttonEFlat);
        allKeySignatures.add(buttonE);
        allKeySignatures.add(buttonF);
        allKeySignatures.add(buttonFSharp);
        allKeySignatures.add(buttonG);
        allKeySignatures.add(buttonAFlat);

        timeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            timerInSeconds = Math.round(newValue.doubleValue());
            updateTimeLabel(timerInSeconds);

            if (counter != null) {
                counter.setTime(timerInSeconds);
            }

        });

        updateTimeLabel(timerInSeconds);
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
        } else {
            return major.isSelected() ? "Major": "minor";
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
        System.out.println("actived: " + activatedKeys.size() + ", next one: " + nextKeyIndex);
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
        File file = new File("./loops");
        System.out.println(file.getAbsolutePath());
        Loop loop = Loop.getRandomLoop(file, activeKey.getText());
        if (loop != null) {
            mediaPlayer.playLoop(loop.getFile());
            updateNowPlayingLabel(loop);
        } else {
            System.out.println("No loop found for key " + activeKey.getText());
        }
    }

    private void updateNowPlayingLabel(Loop loop) {
        if (loop != null) {
            nowPlaying.setText(String.format("Now playing: \"" + loop.getName() + "\", " + loop.getBpm() + " bpm"));
        } else {
            nowPlaying.setText(null);
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

}
