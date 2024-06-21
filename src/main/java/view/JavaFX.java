package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JavaFX extends Application {
    private ComboBox<String> inputComboBox;
    private ListView<String> outputListView;

    @Override
    public void start(Stage stage) {
        Button uploadCSVButton = new Button("Upload csv");
        Button locationButton = new Button("Location");
        Button calcButton = new Button("Calculate");
        Button personButton = new Button("Person");
        Button coupleButton = new Button("Couple");
        Button groupButton = new Button("Group");
        Button symbolButton1 = new Button("*");
        Button symbolButton2 = new Button("<-");
        Button symbolButton3 = new Button("->");
        Button symbolButton4 = new Button("*");

        inputComboBox = new ComboBox<>();

        outputListView = new ListView<>();

        HBox buttonBox1 = new HBox(10, uploadCSVButton, locationButton, calcButton);
        buttonBox1.setPadding(new Insets(10));

        HBox buttonBox2 = new HBox(20, personButton, coupleButton, groupButton);
        buttonBox1.setPadding(new Insets(10));

        VBox topBox = new VBox(10, buttonBox1, buttonBox2);
        topBox.setPadding(new Insets(10));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        GridPane.setConstraints(uploadCSVButton, 0, 0);
        GridPane.setConstraints(personButton, 0, 1);
        gridPane.getChildren().addAll(uploadCSVButton, personButton);

        GridPane.setConstraints(locationButton, 1, 0);
        GridPane.setConstraints(coupleButton, 1, 1);
        gridPane.getChildren().addAll(locationButton, coupleButton);

        GridPane.setConstraints(calcButton, 2, 0);
        GridPane.setConstraints(groupButton, 2, 1);
        gridPane.getChildren().addAll(calcButton, groupButton);

        GridPane.setConstraints(inputComboBox, 0, 2, 10, 1);
        gridPane.getChildren().addAll(inputComboBox);
        inputComboBox.setPrefWidth(2000);

        GridPane.setConstraints(outputListView, 0, 3, 3, 1);
        gridPane.getChildren().addAll(outputListView);

        GridPane.setConstraints(symbolButton1, 90, 0);
        GridPane.setConstraints(symbolButton2, 89, 1);
        GridPane.setConstraints(symbolButton3, 90, 1);
        GridPane.setConstraints(symbolButton4, 90, 2);
        gridPane.getChildren().addAll(symbolButton1, symbolButton2, symbolButton3, symbolButton4);

        symbolButton1.setOnAction(e -> {
            VBox newWindowLayout = createNewWindowLayout(stage);
            Stage newWindow = createNewWindow(stage, newWindowLayout);
            newWindow.show();
        });

        symbolButton4.setOnAction(e -> {
            VBox newWindowLayout = createInputFieldsLayout(stage);
            Stage newWindow = createNewWindow(stage, newWindowLayout);
            newWindow.show();
        });

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: lightgreen;");
        root.getChildren().add(gridPane);

        Scene scene = new Scene(root, 640, 480);

        stage.setTitle("Spinfood");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createNewWindowLayout(Stage stage) {
        Button buttonDE = new Button("DE");
        Button buttonEN = new Button("EN");

        buttonDE.setOnAction(e -> {
            System.out.println("DE button clicked");
            stage.close();
        });

        buttonEN.setOnAction(e -> {
            System.out.println("EN button clicked");
            stage.close();
        });

        VBox layout = new VBox(10, buttonDE, buttonEN);
        layout.setPadding(new Insets(20));
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        return layout;
    }

    private VBox createInputFieldsLayout(Stage stage) {
        Label foodPrefLabel = new Label("Food Preference:");
        Label ageGroupLabel = new Label("Age Group:");
        Label genderLabel = new Label("Gender:");
        Label maxNumberLabel = new Label("Maximal Number of Persons:");

        TextField foodPrefField = new TextField();
        TextField ageGroupField = new TextField();
        TextField genderField = new TextField();
        TextField maxNumberField = new TextField();

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                new HBox(10, foodPrefLabel, foodPrefField),
                new HBox(10, ageGroupLabel, ageGroupField),
                new HBox(10, genderLabel, genderField),
                new HBox(10, maxNumberLabel, maxNumberField)
        );

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            stage.close();
        });

        layout.getChildren().add(submitButton);

        layout.setPadding(new Insets(20));
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        return layout;
    }

    private Stage createNewWindow(Stage stage, VBox layout) {
        Stage newWindow = new Stage();
        newWindow.setTitle("New Window");
        Scene scene = new Scene(layout, 400, 400);
        newWindow.setScene(scene);
        return newWindow;
    }

    public static void main(String[] args) {
        launch();
    }

}