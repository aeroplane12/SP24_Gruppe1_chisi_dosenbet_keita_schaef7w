package sw.praktikum.spinfood;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sw.praktikum.spinfood.model.Manager;

import java.io.File;
import java.io.IOException;

public class View extends Application {
    private Controller controller;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(View.class.getResource("StartUpWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 740, 460);
        stage.setTitle("Spinfood");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public Controller getController() {
        return controller;
    }

}

