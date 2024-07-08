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
import sw.praktikum.spinfood.model.*;


import java.io.File;
import java.io.IOException;


public class Controller {
    ObservableList<Person> personList;
    ObservableList<Couple> coupleList;
    ObservableList<Group> groupList;
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
    private TableColumn<Person, FoodPreference.FoodPref> personFoodPref = new TableColumn<>();
    @FXML
    private TableColumn<Person, AgeGroup.AgeRange> personAgeRange = new TableColumn<>();
    @FXML
    private TableColumn<Person, Gender.genderValue> personGender = new TableColumn<>();
    @FXML
    private TableColumn<Person, Boolean> personRegWithPartner = new TableColumn<>();
    @FXML
    private TableView<Couple> coupleTab = new TableView<>();
    @FXML
    private TableColumn<Couple, Integer> coupleID = new TableColumn<>();
    @FXML
    private TableColumn<Couple, Person> couplePerson1 = new TableColumn<>();
    @FXML
    private TableColumn<Couple, Person> couplePerson2 = new TableColumn<>();
    @FXML
    private TableColumn<Couple, Kitchen> coupleKitchen1 = new TableColumn<>();
    @FXML
    private TableColumn<Couple, Kitchen> coupleKitchen2 = new TableColumn<>();
    @FXML
    private TableColumn<Couple, FoodPreference.FoodPref> coupleFoodPref = new TableColumn<>();
    @FXML
    private TableColumn<Couple, Boolean> coupleWhoseKitchen = new TableColumn<>();
    @FXML
    private TableView<Group> groupTab = new TableView<>();
    @FXML
    private TableColumn<Group, Integer> groupID = new TableColumn<>();
    @FXML
    private TableColumn<Group, Couple> groupHost = new TableColumn<>();
    @FXML
    private TableColumn<Group, Couple> groupGuest1 = new TableColumn<>();
    @FXML
    private TableColumn<Group, Couple> groupGuest2 = new TableColumn<>();
    @FXML
    private TableColumn<Group, FoodPreference.FoodPref> groupFoodPref = new TableColumn<>();
    @FXML
    private TableColumn<Group, Course> groupCourse = new TableColumn<>();

    public void initialize() {
        foodPrefSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, Manager.getInstance().getFoodPrefWeight()));
        ageGroupSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,Double.MAX_VALUE,Manager.getInstance().getAVGAgeRangeWeight()));
        genderSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,Double.MAX_VALUE,Manager.getInstance().getAVGGenderDIVWeight()));
        distanceSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,Double.MAX_VALUE,Manager.getInstance().getDistanceWeight()));
        optimalDistanceSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,Double.MAX_VALUE,Manager.getInstance().getOptimalDistance()));
        // Table for Person tab
        personID.setCellValueFactory(new PropertyValueFactory<>("iD"));
        personName.setCellValueFactory(new PropertyValueFactory<>("name"));
        personFoodPref.setCellValueFactory(new PropertyValueFactory<>("foodPreference"));
        personAgeRange.setCellValueFactory(new PropertyValueFactory<>("age"));
        personGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        personRegWithPartner.setCellValueFactory(new PropertyValueFactory<>("lockedIn"));


        // Table for Couple tab
        coupleID.setCellValueFactory(new PropertyValueFactory<>("iD"));
        couplePerson1.setCellValueFactory(new PropertyValueFactory<>("Person1"));
        couplePerson2.setCellValueFactory(new PropertyValueFactory<>("Person2"));
        coupleKitchen1.setCellValueFactory(new PropertyValueFactory<>("Kitchen1"));
        coupleKitchen2.setCellValueFactory(new PropertyValueFactory<>("Kitchen2"));
        coupleFoodPref.setCellValueFactory(new PropertyValueFactory<>("FoodPref"));
        coupleWhoseKitchen.setCellValueFactory(new PropertyValueFactory<>("whoseKitchen"));

        // Table for Group tab
        groupID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        groupHost.setCellValueFactory(new PropertyValueFactory<>("hosts"));
        groupGuest1.setCellValueFactory(new PropertyValueFactory<>("guest1"));
        groupGuest2.setCellValueFactory(new PropertyValueFactory<>("guest2"));
        groupFoodPref.setCellValueFactory(new PropertyValueFactory<>("foodPreference"));
        groupCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
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
        fileChooser.setTitle("Choose the CSV with the participants file");
        fileChooser.setInitialDirectory(new File("Dokumentation"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            Manager.getInstance().inputPeople(selectedFile.getPath());
            personList = FXCollections.observableArrayList(Manager.getInstance().getAllPersonList());
            System.out.println(personList.get(0).toString());
            personTab.setItems(personList);
        }
    }

    @FXML private void handleLocation() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose the location file");
        fileChooser.setInitialDirectory(new File("Dokumentation"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            Manager.getInstance().inputLocation(selectedFile.getPath());
        }
    }

    @FXML private void handleCalculate() {
        Manager.getInstance().calcAll();
        coupleList = FXCollections.observableArrayList(Manager.getInstance().getCouples());
        coupleTab.setItems(coupleList);
        groupList = FXCollections.observableArrayList(Manager.getInstance().getGroups());
        groupTab.setItems(groupList);
        coupleList.forEach(elem -> System.out.println(elem.getKitchen2()));
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