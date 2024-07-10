package sw.praktikum.spinfood;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.IntegerStringConverter;
import sw.praktikum.spinfood.model.*;
import sw.praktikum.spinfood.model.tools.AgeGroupStringConverter;
import sw.praktikum.spinfood.model.tools.FoodPreferenceStringConverter;
import sw.praktikum.spinfood.model.tools.GenderStringConverter;
import sw.praktikum.spinfood.model.tools.LanguageSetting;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Controller {
    ObservableList<Person> currentPersonList;
    ObservableList<Couple> currentCoupleList;
    ObservableList<Group> currentGroupList;
    ObservableList<Manager> managersList;
    Person selectedPersonInList;
    @FXML
    private TableRow<Person> selectedPerson;
    File chosenDirOutput = new File("Dokumentation");
    String fileName;
    @FXML
    private ComboBox<LanguageSetting.Language> languageMenu = new ComboBox<>();
    @FXML
    private TextField filenameTextfield = new TextField();
    @FXML
    private ChoiceBox<Strictness> strictnessChange = new ChoiceBox<>();
    @FXML
    private Button uploadLocationButton = new Button();
    @FXML
    private Button uploadCSVButton = new Button();
    @FXML
    private Button calculateButton = new Button();
    @FXML
    private Button saveGroupsButton = new Button();
    @FXML
    private Button calculationConfigButton = new Button();
    @FXML
    private Button removeParticipantButton = new Button();
    @FXML
    private Button createCouplesGroupsTab = new Button();
    @FXML
    private Button deleteCouplesGroupsTab = new Button();
    @FXML
    private ComboBox<Manager> couplesGroupsMenu = new ComboBox<>();
    @FXML
    private Tab participantsTabSel = new Tab();
    @FXML
    private Tab couplesTabSel = new Tab();
    @FXML
    private Tab groupsTabSel = new Tab();
    @FXML
    private Label foodPrefWeightLabel = new Label();
    @FXML
    private Label ageGroupWeightLabel = new Label();
    @FXML
    private Label genderWeightLabel = new Label();
    @FXML
    private Label distanceWeightLabel = new Label();
    @FXML
    private Label optimalDistanceLabel = new Label();
    @FXML
    private Label strictnessLevelLabel = new Label();
    @FXML
    private Button configurationSubmitButton = new Button();
    @FXML
    private Button chooseDirButton = new Button();
    @FXML
    private Label filenameTextfieldLabel = new Label();
    @FXML
    private Button finalSaveButton = new Button();


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
    private TableColumn<Person, Kitchen> coupleKitchen1 = new TableColumn<>();
    @FXML
    private TableColumn<Person, Kitchen> coupleKitchen2 = new TableColumn<>();
    @FXML
    private TableColumn<Couple, Kitchen> coupleKitchen1Longitude = new TableColumn<>();
    @FXML
    private TableColumn<Couple, Kitchen> coupleKitchen1Latitude = new TableColumn<>();
    @FXML
    private TableColumn<Couple, Kitchen> coupleKitchen1Story = new TableColumn<>();
    @FXML
    private TableColumn<Couple, Kitchen> coupleKitchen2Longitude = new TableColumn<>();
    @FXML
    private TableColumn<Couple, Kitchen> coupleKitchen2Latitude = new TableColumn<>();
    @FXML
    private TableColumn<Couple, Kitchen> coupleKitchen2Story = new TableColumn<>();
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
        // personID.setCellFactory(TextFieldTableCell.forTableColumn());

        personName.setCellValueFactory(new PropertyValueFactory<>("name"));
        personName.setCellFactory(TextFieldTableCell.forTableColumn());
        personName.setOnEditCommit(personStringCellEditEvent -> {
            Person person = personStringCellEditEvent.getRowValue();
            person.setName(personStringCellEditEvent.getNewValue());
        });
        //personKitchen.setCellValueFactory(new PropertyValueFactory<>("kitchen"));

        personKitchenLongitude.setCellValueFactory(new PropertyValueFactory<>("kitchenLongitude"));

        personKitchenLatitude.setCellValueFactory(new PropertyValueFactory<>("kitchenLatitude"));

        personKitchenStory.setCellValueFactory(new PropertyValueFactory<>("kitchenStory"));

        personFoodPref.setCellValueFactory(new PropertyValueFactory<>("foodPreference"));
        personFoodPref.setCellFactory(TextFieldTableCell.forTableColumn(new FoodPreferenceStringConverter()));
        personFoodPref.setOnEditCommit(personFoodPrefCellEditEvent -> {
            Person person = personFoodPrefCellEditEvent.getRowValue();
            person.setFoodPreference(personFoodPrefCellEditEvent.getNewValue());
        });
        personAgeRange.setCellValueFactory(new PropertyValueFactory<>("age"));
        personAgeRange.setCellFactory(TextFieldTableCell.forTableColumn(new AgeGroupStringConverter()));
        personAgeRange.setOnEditCommit(personAgeRangeCellEditEvent -> {
            Person person = personAgeRangeCellEditEvent.getRowValue();
            person.setAge(personAgeRangeCellEditEvent.getNewValue());
        });
        personGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        personGender.setCellFactory(TextFieldTableCell.forTableColumn(new GenderStringConverter()));
        personGender.setOnEditCommit(personGenderValueCellEditEvent -> {
            Person person = personGenderValueCellEditEvent.getRowValue();
            person.setGender(personGenderValueCellEditEvent.getNewValue());
        });
        personRegWithPartner.setCellValueFactory(new PropertyValueFactory<>("lockedIn"));
        personRegWithPartner.setCellFactory(TextFieldTableCell.forTableColumn(new BooleanStringConverter()));
        personRegWithPartner.setOnEditCommit(personBooleanCellEditEvent -> {
            Person person = personBooleanCellEditEvent.getRowValue();
            person.setLockedIn(personBooleanCellEditEvent.getNewValue());
        });

        // Table for Couple tab
        coupleID.setCellValueFactory(new PropertyValueFactory<>("iD"));
        coupleID.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        couplePerson1.setCellValueFactory(new PropertyValueFactory<>("Person1ID"));
        couplePerson2.setCellValueFactory(new PropertyValueFactory<>("Person2ID"));
        coupleKitchen1Longitude.setCellValueFactory(new PropertyValueFactory<>("Kitchen1Longitude"));
        coupleKitchen1Latitude.setCellValueFactory(new PropertyValueFactory<>("Kitchen1Latitude"));
        coupleKitchen1Story.setCellValueFactory(new PropertyValueFactory<>("Kitchen1Story"));
        coupleKitchen2Longitude.setCellValueFactory(new PropertyValueFactory<>("Kitchen2Longitude"));
        coupleKitchen2Latitude.setCellValueFactory(new PropertyValueFactory<>("Kitchen2Latitude"));
        coupleKitchen2Story.setCellValueFactory(new PropertyValueFactory<>("Kitchen2Story"));
        coupleFoodPref.setCellValueFactory(new PropertyValueFactory<>("FoodPref"));
        coupleWhoseKitchen.setCellValueFactory(new PropertyValueFactory<>("whoseKitchen"));

        // Table for Group tab
        groupID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        groupHost.setCellValueFactory(new PropertyValueFactory<>("hostID"));
        groupGuest1.setCellValueFactory(new PropertyValueFactory<>("guest1ID"));
        groupGuest2.setCellValueFactory(new PropertyValueFactory<>("guest2ID"));
        groupFoodPref.setCellValueFactory(new PropertyValueFactory<>("foodPreference"));
        groupCourse.setCellValueFactory(new PropertyValueFactory<>("course"));

        // Strictness level
        strictnessChange.getItems().add(Strictness.A);
        strictnessChange.getItems().add(Strictness.B);
        strictnessChange.getItems().add(Strictness.C);
        strictnessChange.setValue(Strictness.B);

        managersList = FXCollections.observableArrayList(new Manager());
        couplesGroupsMenu.setItems(managersList);

        List<LanguageSetting.Language> languages = new ArrayList<>();
        languages.add(LanguageSetting.Language.ENGLISH);
        languages.add(LanguageSetting.Language.GERMAN);
        languageMenu.setItems(FXCollections.observableArrayList(languages));
        languageMenu.setPromptText("German");
        languageMenu.setValue(LanguageSetting.Language.GERMAN);


        personTab.setRowFactory(tv -> {
            TableRow<Person> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    selectedPersonInList = row.getItem();
                    System.out.println(selectedPersonInList);
                }
            });
            return row;
        });

    }
    @FXML
    private void handleSelectedLanguage() {
        Map<Integer, String> labels = LanguageSetting.getSelectLanguage(languageMenu.getValue());
        uploadLocationButton.setText(labels.get(0));
        uploadCSVButton.setText(labels.get(1));
        calculateButton.setText(labels.get(2));
        saveGroupsButton.setText(labels.get(3));
        calculationConfigButton.setText(labels.get(4));
        removeParticipantButton.setText(labels.get(5));
        createCouplesGroupsTab.setText(labels.get(6));
        deleteCouplesGroupsTab.setText(labels.get(7));
        couplesGroupsMenu.setPromptText(labels.get(8));
        participantsTabSel.setText(labels.get(9));
        couplesTabSel.setText(labels.get(10));
        groupsTabSel.setText(labels.get(11));
        personID.setText(labels.get(12));
        personName.setText(labels.get(13));
        personKitchen.setText(labels.get(14));
        personKitchenLongitude.setText(labels.get(15));
        coupleKitchen1Longitude.setText(labels.get(15));
        coupleKitchen2Longitude.setText(labels.get(15));
        personKitchenLatitude.setText(labels.get(16));
        coupleKitchen1Latitude.setText(labels.get(16));
        coupleKitchen2Latitude.setText(labels.get(16));
        personKitchenStory.setText(labels.get(17));
        coupleKitchen1Story.setText(labels.get(17));
        coupleKitchen2Story.setText(labels.get(17));
        personFoodPref.setText(labels.get(18));
        coupleFoodPref.setText(labels.get(18));
        groupFoodPref.setText(labels.get(18));
        personAgeRange.setText(labels.get(19));
        personGender.setText(labels.get(20));
        personRegWithPartner.setText(labels.get(21));
        coupleID.setText(labels.get(22));
        couplePerson1.setText(labels.get(23));
        couplePerson2.setText(labels.get(24));
        coupleKitchen1.setText(labels.get(25));
        coupleKitchen2.setText(labels.get(26));
        coupleWhoseKitchen.setText(labels.get(27));
        groupID.setText(labels.get(28));
        groupHost.setText(labels.get(29));
        groupGuest1.setText(labels.get(30));
        groupGuest2.setText(labels.get(31));
        groupCourse.setText(labels.get(32));
        foodPrefWeightLabel.setText(labels.get(33));
        ageGroupWeightLabel.setText(labels.get(34));
        genderWeightLabel.setText(labels.get(35));
        distanceWeightLabel.setText(labels.get(36));
        optimalDistanceLabel.setText(labels.get(37));
        strictnessLevelLabel.setText(labels.get(38));
        chooseDirButton.setText(labels.get(39));
        filenameTextfieldLabel.setText(labels.get(40));
        configurationSubmitButton.setText(labels.get(41));
        finalSaveButton.setText(labels.get(41));

    }

    @FXML
    private void handleOpenWindow() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PopupWindow.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Berechnungseinstellungen");
            stage.setScene(new Scene(root));
            stage.setAlwaysOnTop(true);
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
            currentPersonList = FXCollections.observableArrayList(Manager.getInstance().getAllPersonList());
            personTab.setItems(currentPersonList);
            calculateButton.setDisable(false);
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
            uploadCSVButton.setDisable(false);
        }
    }

    @FXML private void handleCalculate() {
        Manager.getInstance().calcAll();
        currentCoupleList = FXCollections.observableArrayList(Manager.getInstance().getCouples());
        coupleTab.setItems(currentCoupleList);
        currentGroupList = FXCollections.observableArrayList(Manager.getInstance().getGroups());
        groupTab.setItems(currentGroupList);
        saveGroupsButton.setDisable(false);
    }

    @FXML private void handleUndo() {
        if (!Manager.getInstance().getPrev().isEmpty()) {
            Manager removedManager = Manager.getInstance();
            Manager.getInstance().setToPrev();
            managersList.set(managersList.indexOf(removedManager), Manager.getInstance());
        }
    }
    @FXML private void handleRedo() {
        if (!Manager.getInstance().getFuture().isEmpty()) {
            Manager removedManager = Manager.getInstance();
            Manager.getInstance().setToFuture();
            managersList.set(managersList.indexOf(removedManager), Manager.getInstance());
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
        Manager.getInstance().setConfig(foodPrefSpinner.getValue(),
                ageGroupSpinner.getValue(),
                genderSpinner.getValue(),
                distanceSpinner.getValue(),
                optimalDistanceSpinner.getValue(),
                strictnessChange.getValue());
        Stage stage = (Stage) foodPrefSpinner.getScene().getWindow();
        stage.close();
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

    @FXML private void handleSaveGroupsWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SaveFileWindow.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Wähle ein Ziel");
            stage.setScene(new Scene(root));
            stage.setAlwaysOnTop(true);
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
        Stage stage = (Stage) finalSaveButton.getScene().getWindow();
        stage.close();
    }
    @FXML private void handleNewCouplesGroupsTab(){
        Manager manager = new Manager();
        managersList.add(manager);
        manager.setAllPersonList(currentPersonList);
    }
    @FXML private void handleDeleteCouplesGroupsTab(){
        if (managersList.size() > 1) {
            managersList.remove(Manager.getInstance());
            managersList.get(0).setInstance();
        }
    }
    @FXML private void handleSwitchCouplesGroupsTab(){
        couplesGroupsMenu.getValue().setInstance();
        currentCoupleList = FXCollections.observableArrayList(Manager.getInstance().getCouples());
        currentGroupList = FXCollections.observableArrayList(Manager.getInstance().getGroups());
        coupleTab.setItems(currentCoupleList);
        groupTab.setItems(currentGroupList);

    }
    @FXML private void handleRemoveParticipant() {
        managersList.forEach(manager -> manager.removePerson(selectedPersonInList));
        currentPersonList.remove(selectedPersonInList);

    }

}