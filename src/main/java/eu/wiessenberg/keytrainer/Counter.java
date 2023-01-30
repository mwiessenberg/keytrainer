package eu.wiessenberg.keytrainer;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Counter {
    private long timeInSeconds = 0;
    private boolean running = false;
    private List<CounterListener> listeners = new ArrayList<>();
    private List<Long> triggers = new ArrayList<>();

    private Timer timer;

    public Counter(long timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public void addListener(CounterListener listener) {
        listeners.add(listener);
    }

    public void setTime(long timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public void start() {
        timer = new Timer(false);
        timer.schedule(new CounterTask(), 1000, 1000);
        running = true;
    }

    public void stop() {
        timer.cancel();
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void addTimeTrigger(long i) {
        triggers.add(i);
    }

    interface CounterListener {
        void onCountedDownToZero();
        void onTimeChanged(long secondsRemaining);
        void onTimeTrigger(long secondsRemaining);
    }

    private class CounterTask extends TimerTask {
        @Override
        public void run() {
            timeInSeconds--;
            for(CounterListener listener: listeners) {
                Platform.runLater(() -> listener.onTimeChanged(timeInSeconds));

                if (triggers.contains(timeInSeconds)) {
                    Platform.runLater(() -> listener.onTimeTrigger(timeInSeconds));
                }
            }

            if (timeInSeconds <= 0) {
                for(CounterListener listener: listeners) {
                    Platform.runLater(listener::onCountedDownToZero);

                }
                stop();
            }
        }
    }
}
