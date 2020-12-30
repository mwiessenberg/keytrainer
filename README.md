# Key Trainer

This application will confront you with a random key signature and loops a backing track with it. After some (configurable)
time period, a new key is randomly chosen, playing a different key accordingly.
The currently played key is displayed big on screen, and 10 seconds before the new key will be played, the next key will
be displayed as an announcement.

# Prerequisites
JDK 1.8 or higher should be installed
 
# Build
run `mvn clean package` to build the project. The result is a self-executable jar file, generated in `target/`

# Run
`java -jar keytrainer.jar`

# Loops
I've added a couple of loops that will be used as the backing track (in `src/resources/loops`), but you can add your own.
Make sure you follow this format, so the application will pick the correct loop for the active key:
    `<key>_<title or discription of the song>_<bpm>`
