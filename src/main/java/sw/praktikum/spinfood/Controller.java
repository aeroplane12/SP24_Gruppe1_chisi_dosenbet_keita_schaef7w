package sw.praktikum.spinfood;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sw.praktikum.spinfood.model.Couple;
import sw.praktikum.spinfood.model.Group;
import sw.praktikum.spinfood.model.Manager;
import sw.praktikum.spinfood.model.Person;


import java.io.File;
import java.io.IOException;


public class Controller {
    ObservableList<Person> personList;
    @FXML
    private Spinner<Double> foodPrefSpinner = new Spinner<>();
    @FXML
    private Spinner<Double> ageGroupSpinner = new Spinner<>();
    @FXML
    private Spinner<Double> genderSpinner = new Spinner<>();
    @FXML
    private Spinner<Double> distanceSpinner = new Spinner<>();
    @FXML
    private Spinner<Double> optimalDistanceSpinner = new Spinner<>();
    @FXML
    private TableView<Person> personTab = new TableView<>();
    @FXML
    private TableColumn<Person, String> personID = new TableColumn<>();
    @FXML
    private TableColumn<Person, String> personName = new TableColumn<>();
    @FXML
    private TableColumn<Person, String> personFoodPref = new TableColumn<>();
    @FXML
    private TableColumn<Person, String> personAgeRange = new TableColumn<>();
    @FXML
    private TableColumn<Person, String> personGender = new TableColumn<>();
    @FXML
    private TableColumn<Person, String> personRegWithPartner = new TableColumn<>();
    @FXML
    private TableView<Couple> coupleTab = new TableView<>();
    @FXML
    private TableColumn<Couple, String> coupleID = new TableColumn<>();
    @FXML
    private TableColumn<Couple, String> couplePerson1ID = new TableColumn<>();
    @FXML
    private TableColumn<Couple, String> couplePerson2ID = new TableColumn<>();
    @FXML
    private TableColumn<Couple, String> coupleFoodPref = new TableColumn<>();
    @FXML
    private TableColumn<Couple, String> coupleWhoseKitchen = new TableColumn<>();
    @FXML
    private TableView<Group> groupTab = new TableView<>();

    public void initialize() {
        foodPrefSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, Manager.getInstance().getFoodPrefWeight()));
        ageGroupSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,Double.MAX_VALUE,Manager.getInstance().getAVGAgeRangeWeight()));
        genderSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,Double.MAX_VALUE,Manager.getInstance().getAVGGenderDIVWeight()));
        distanceSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,Double.MAX_VALUE,Manager.getInstance().getDistanceWeight()));
        optimalDistanceSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,Double.MAX_VALUE,Manager.getInstance().getOptimalDistance()));
        // Table for Person tab
        personID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        personName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        personFoodPref.setCellValueFactory(new PropertyValueFactory<>("FoodPreference"));
        personAgeRange.setCellValueFactory(new PropertyValueFactory<>("AgeRange"));
        personGender.setCellValueFactory(new PropertyValueFactory<>("Gender"));
        personRegWithPartner.setCellValueFactory(new PropertyValueFactory<>("LockedIn"));
        // Table for Couple tab
        coupleID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        couplePerson1ID.setCellValueFactory(new PropertyValueFactory<>("Person1().getID"));
        couplePerson2ID.setCellValueFactory(new PropertyValueFactory<>("Person2().getID"));
        coupleFoodPref.setCellValueFactory(new PropertyValueFactory<>("FoodPref().toString"));
        coupleWhoseKitchen.setCellValueFactory(new PropertyValueFactory<>("WhoseKitchen"));

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
            personList = FXCollections.observableArrayList(Manager.getInstance().getAllPersonList());
            personTab.setItems(personList);
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
        personList = FXCollections.observableArrayList(Manager.getInstance().getAllPersonList());
        personTab.setItems(personList);
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
    private boolean validateDouble(String text) {
        if (text.isEmpty()) {
            return true;
        }
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    @FXML private void handleConfigurationSubmit() {
        Manager.getInstance().setConfig(foodPrefSpinner.getValue(), ageGroupSpinner.getValue(), genderSpinner.getValue(), distanceSpinner.getValue(), optimalDistanceSpinner.getValue());
    }

    @FXML private void handleInvalidTextInputFoodPref() {
        if (!validateDouble(foodPrefSpinner.getEditor().getText())) {
            foodPrefSpinner.getEditor().setText(foodPrefSpinner.getValue().toString());
        }
    }
    @FXML private void handleInvalidTextInputAgeGroup() {
        if (!validateDouble(ageGroupSpinner.getEditor().getText())) {
            ageGroupSpinner.getEditor().setText(ageGroupSpinner.getValue().toString());
        }
    }
    @FXML private void handleInvalidTextInputGender() {
        if (!validateDouble(genderSpinner.getEditor().getText())) {
            genderSpinner.getEditor().setText(genderSpinner.getValue().toString());
        }
    }

    @FXML private void handleInvalidTextInputDistanceWeight() {
        if (!validateDouble(distanceSpinner.getEditor().getText())) {
            distanceSpinner.getEditor().setText(distanceSpinner.getValue().toString());
        }
    }
    @FXML private void handleInvalidTextInputOptimalDistance() {
        if (!validateDouble(optimalDistanceSpinner.getEditor().getText())) {
            optimalDistanceSpinner.getEditor().setText(optimalDistanceSpinner.getValue().toString());
        }
    }
    @FXML private void handleRefreshTable() {

    }

}
