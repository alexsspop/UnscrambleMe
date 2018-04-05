package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow extends Application {

    public static void main(String[] args) {
        System.out.println();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("MainWindow.fxml")));
        primaryStage.setTitle("Title");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
