package sw.praktikum.spinfood;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import sw.praktikum.spinfood.model.*;
import sw.praktikum.spinfood.model.tools.AgeGroupStringConverter;
import sw.praktikum.spinfood.model.tools.FoodPreferenceStringConverter;
import sw.praktikum.spinfood.model.tools.GenderStringConverter;


import java.io.File;
import java.io.IOException;


public class Controller {
    ObservableList<Person> personList;
    ObservableList<Couple> coupleList;
    ObservableList<Group> groupList;
    File chosenDirOutput = new File("Dokumentation");
    String fileName;
    @FXML
    private TextField filenameTextfield = new TextField();
    @FXML
    private Button saveButton = new Button();
    @FXML
    private ChoiceBox<Strictness> strictnessChange = new ChoiceBox<>();
    @FXML
    private Button uploadCSV = new Button();
    @FXML
    private Button calculate = new Button();
    @FXML
    private Button saveGroups = new Button();
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
    private TableColumn<Person, Kitchen> personKitchen = new TableColumn<>();
    @FXML
    private TableColumn<Person, Double> personKitchenLongitude = new TableColumn<>();
    @FXML
    private TableColumn<Person, Double> personKitchenLatitude = new TableColumn<>();
    @FXML
    private TableColumn<Person, Double> personKitchenStory = new TableColumn<>();
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

    @FXML
    private Button redo = new Button();
    @FXML
    private Button undo = new Button();

    public void initialize() {
        foodPrefSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, Manager.getInstance().getFoodPrefWeight()));
        ageGroupSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,Double.MAX_VALUE,Manager.getInstance().getAVGAgeRangeWeight()));
        genderSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,Double.MAX_VALUE,Manager.getInstance().getAVGGenderDIVWeight()));
        distanceSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,Double.MAX_VALUE,Manager.getInstance().getDistanceWeight()));
        optimalDistanceSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,Double.MAX_VALUE,Manager.getInstance().getOptimalDistance()));

        // Table for Person tab
        personID.setCellValueFactory(new PropertyValueFactory<>("iD"));
        // personID.setCellFactory(TextFieldTableCell.forTableColumn());

        personName.setCellValueFactory(new PropertyValueFactory<>("name"));
        personName.setCellFactory(TextFieldTableCell.forTableColumn());
        personName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Person, String> personStringCellEditEvent) {
                Person person = personStringCellEditEvent.getRowValue();
                person.setName(personStringCellEditEvent.getNewValue());
            }
        });
        //personKitchen.setCellValueFactory(new PropertyValueFactory<>("kitchen"));

        personKitchenLongitude.setCellValueFactory(new PropertyValueFactory<>("kitchenLongitude"));

        personKitchenLatitude.setCellValueFactory(new PropertyValueFactory<>("kitchenLatitude"));

        personKitchenStory.setCellValueFactory(new PropertyValueFactory<>("kitchenStory"));

        personFoodPref.setCellValueFactory(new PropertyValueFactory<>("foodPreference"));
        personFoodPref.setCellFactory(TextFieldTableCell.forTableColumn(new FoodPreferenceStringConverter()));
        personFoodPref.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, FoodPreference.FoodPref>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Person, FoodPreference.FoodPref> personFoodPrefCellEditEvent) {
                Person person = personFoodPrefCellEditEvent.getRowValue();
                person.setFoodPreference(personFoodPrefCellEditEvent.getNewValue());
            }
        });
        personAgeRange.setCellValueFactory(new PropertyValueFactory<>("age"));
        personAgeRange.setCellFactory(TextFieldTableCell.forTableColumn(new AgeGroupStringConverter()));
        personAgeRange.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, AgeGroup.AgeRange>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Person, AgeGroup.AgeRange> personAgeRangeCellEditEvent) {
                Person person = personAgeRangeCellEditEvent.getRowValue();
                person.setAge(personAgeRangeCellEditEvent.getNewValue());
            }
        });
        personGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        personGender.setCellFactory(TextFieldTableCell.forTableColumn(new GenderStringConverter()));
        personGender.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, Gender.genderValue>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Person, Gender.genderValue> personGenderValueCellEditEvent) {
                Person person = personGenderValueCellEditEvent.getRowValue();
                person.setGender(personGenderValueCellEditEvent.getNewValue());
            }
        });
        personRegWithPartner.setCellValueFactory(new PropertyValueFactory<>("lockedIn"));
        personRegWithPartner.setCellFactory(TextFieldTableCell.forTableColumn(new BooleanStringConverter()));
        personRegWithPartner.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, Boolean>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Person, Boolean> personBooleanCellEditEvent) {
                Person person = personBooleanCellEditEvent.getRowValue();
                person.setLockedIn(personBooleanCellEditEvent.getNewValue());
            }
        });

        // Table for Couple tab
        coupleID.setCellValueFactory(new PropertyValueFactory<>("iD"));
        coupleID.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
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

        // Strictness level
        strictnessChange.getItems().add(Strictness.A);
        strictnessChange.getItems().add(Strictness.B);
        strictnessChange.getItems().add(Strictness.C);
        strictnessChange.setValue(Strictness.B);

        // undo / redo Buttons
        undo.setDisable(true);
        redo.setDisable(true);

    }

    @FXML
    private void handleOpenWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PopupWindow.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Configuration");
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
            personTab.setItems(personList);
            calculate.setDisable(false);
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
            uploadCSV.setDisable(false);
        }
    }

    @FXML private void handleCalculate() {
        Manager.getInstance().calcAll();
        coupleList = FXCollections.observableArrayList(Manager.getInstance().getCouples());
        coupleTab.setItems(coupleList);
        groupList = FXCollections.observableArrayList(Manager.getInstance().getGroups());
        groupTab.setItems(groupList);
        saveGroups.setDisable(false);
        undo.setDisable(Manager.getPrev().isEmpty());
        redo.setDisable(Manager.getFuture().isEmpty());
    }

    @FXML private void handleUndo() {
        if (!Manager.getPrev().isEmpty()) {
            Manager.setToPrev();
            coupleList = FXCollections.observableArrayList(Manager.getInstance().getCouples());
            coupleTab.setItems(coupleList);
            groupList = FXCollections.observableArrayList(Manager.getInstance().getGroups());
            groupTab.setItems(groupList);
        }
        undo.setDisable(Manager.getPrev().isEmpty());
        redo.setDisable(Manager.getFuture().isEmpty());
    }
    @FXML private void handleRedo() {
        if (!Manager.getFuture().isEmpty()) {
            Manager.setToFuture();
            coupleList = FXCollections.observableArrayList(Manager.getInstance().getCouples());
            coupleTab.setItems(coupleList);
            groupList = FXCollections.observableArrayList(Manager.getInstance().getGroups());
            groupTab.setItems(groupList);
        }
        undo.setDisable(Manager.getPrev().isEmpty());
        redo.setDisable(Manager.getFuture().isEmpty());
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
        Manager.getInstance().setConfig(foodPrefSpinner.getValue(),
                ageGroupSpinner.getValue(),
                genderSpinner.getValue(),
                distanceSpinner.getValue(),
                optimalDistanceSpinner.getValue(),
                strictnessChange.getValue());
        Stage stage = (Stage) foodPrefSpinner.getScene().getWindow();
        stage.close();
        undo.setDisable(Manager.getPrev().isEmpty());
        redo.setDisable(Manager.getFuture().isEmpty());
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
    @FXML private void handleSaveGroupsWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SaveFileWindow.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Choose save directory");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML private void handleChooseDir() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Chose directory");
        directoryChooser.setInitialDirectory(new File("Dokumentation"));
        chosenDirOutput = directoryChooser.showDialog(new Stage());
    }
    @FXML private void handleOutputFilename() {
        fileName = filenameTextfield.getCharacters().toString();
    }
    @FXML private void handleSaveButton() {
        if (fileName == null) {
            Manager.getInstance().saveGroupsToFile(chosenDirOutput.getAbsoluteFile() + "\\" + "calcGroupsOutput.csv");
        } else {
            Manager.getInstance().saveGroupsToFile(chosenDirOutput.getAbsoluteFile() + "\\" + fileName + ".csv");
        }
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}