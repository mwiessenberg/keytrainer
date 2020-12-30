package eu.wiessenberg.keytrainer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    private FXMLLoader fxmlLoader;

    public Main() {
        fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("keytrainer.fxml"));

        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ((Controller)fxmlLoader.getController()).storeApplicationPreferences();
                mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Key Trainer");
        primaryStage.setScene(new Scene(root, 800, 600));

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
