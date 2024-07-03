package sw.praktikum.spinfood;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sw.praktikum.spinfood.model.Manager;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private Spinner<Double> foodPrefSpinner = new Spinner<>();
    @FXML
    private Spinner<Double> ageGroupSpinner = new Spinner<>();
    @FXML
    private Spinner<Double> genderSpinner = new Spinner<>();
    @FXML
    private Spinner<Double> amountPplSpinner = new Spinner<>();



    public void initialize() {

        foodPrefSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, 0));
        ageGroupSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,Double.MAX_VALUE,0));
        genderSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,Double.MAX_VALUE,0));
        amountPplSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,Double.MAX_VALUE,0));
    }

    @FXML
    private void handleOpenWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PopupWindow.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Popup Window");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUploadCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a file");
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            Manager.getInstance().inputPeople(selectedFile.getPath());
        }
    }

    @FXML private void handleLocation() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a file");
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            Manager.getInstance().inputLocation(selectedFile.getPath());
        }
    }

    @FXML private void handleCalculate() {
        Manager.getInstance().calcAll();
    }

    @FXML private void handlePersonTab() {

    }

    @FXML private void handleCoupleTab() {

    }

    @FXML private void handleGroupTab() {

    }

    @FXML private void handleUndo() {
        if (!Manager.getInstance().getPrev().isEmpty()) {
            Manager.getInstance().setToPrev();
        }
    }
    @FXML private void handleRedo() {
        if (!Manager.getInstance().getFuture().isEmpty()) {
            Manager.getInstance().setToFuture();
        }
    }
    @FXML private void handleLanguageGerman() {

    }
    @FXML private void handleLanguageEnglish() {

    }
    private boolean validate(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    @FXML private void handleConfigurationSubmit() {

    }

    @FXML private void handleInvalidTextInputFoodPref() {
        if (!validate(foodPrefSpinner.getEditor().getText())) {
            foodPrefSpinner.getEditor().setText(foodPrefSpinner.getValue().toString());
        }
    }
    @FXML private void handleInvalidTextInputAgeGroup() {
        if (!validate(ageGroupSpinner.getEditor().getText())) {
            ageGroupSpinner.getEditor().setText(ageGroupSpinner.getValue().toString());
        }
    }
    @FXML private void handleInvalidTextInputGender() {
        if (!validate(genderSpinner.getEditor().getText())) {
            genderSpinner.getEditor().setText(genderSpinner.getValue().toString());
        }
    }

    @FXML private void handleInvalidTextInputMaxPpl() {
        if (!validate(amountPplSpinner.getEditor().getText())) {
            amountPplSpinner.getEditor().setText(amountPplSpinner.getValue().toString());
        }
    }

}
