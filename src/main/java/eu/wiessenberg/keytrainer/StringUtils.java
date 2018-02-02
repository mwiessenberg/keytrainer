package eu.wiessenberg.keytrainer;

public class StringUtils {
    public static String formatTime(long totalSeconds) {
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
}
