package Admin.controller;

import common.dao.VolunteerDAO;
import common.models.Volunteer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class VolunteerTabController {
    private static final String[] GENDER_OPTIONS = {"Male", "Female", "Other"};
    private static final String[] STATUS_OPTIONS = {"Active", "Inactive", "Pending"};

    @FXML private TableView<Volunteer> volunteerTable;
    @FXML private TableColumn<Volunteer, String> volidColumn;
    @FXML private TableColumn<Volunteer, String> firstNameColumn;
    @FXML private TableColumn<Volunteer, String> lastNameColumn;
    @FXML private TableColumn<Volunteer, String> addressColumn;
    @FXML private TableColumn<Volunteer, String> phoneColumn;
    @FXML private TableColumn<Volunteer, String> emailColumn;
    @FXML private TableColumn<Volunteer, Date> birthdateColumn;
    @FXML private TableColumn<Volunteer, String> sexColumn;
    @FXML private TableColumn<Volunteer, String> statusColumn;

    @FXML private TextField volunteerField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private DatePicker birthdateField;
    @FXML private ComboBox<String> sexComboBox;
    @FXML private ComboBox<String> statusComboBox;

    @FXML private Button saveButton;
    @FXML private Button clearButton;

    private final VolunteerDAO volunteerDAO = new VolunteerDAO();
    private ObservableList<Volunteer> volunteers;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupComboBoxes();
        setupEventHandlers();
        loadVolunteerData();
    }

    private void setupTableColumns() {
        volidColumn.setCellValueFactory(new PropertyValueFactory<>("volid"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("fname"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lname"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        birthdateColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        sexColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("volstat"));
    }

    private void setupComboBoxes() {
        sexComboBox.getItems().addAll(GENDER_OPTIONS);
        statusComboBox.getItems().addAll(STATUS_OPTIONS);
    }

    private void setupEventHandlers() {
        volunteerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateForm(newVal);
                saveButton.setText("Update");
            } else {
                clearForm();
                saveButton.setText("Save");
            }
        });

        saveButton.setOnAction(this::handleSave);
        clearButton.setOnAction(this::handleClear);
    }

    private void loadVolunteerData() {
        List<Volunteer> volunteerList = volunteerDAO.getAllVolunteers();
        volunteers = FXCollections.observableArrayList(volunteerList);
        volunteerTable.setItems(volunteers);
    }

    private void populateForm(Volunteer volunteer) {
        volunteerField.setText(volunteer.getVolid());
        firstNameField.setText(volunteer.getFname());
        lastNameField.setText(volunteer.getLname());
        addressField.setText(volunteer.getAddress());
        phoneField.setText(volunteer.getPhone());
        emailField.setText(volunteer.getEmail());
        birthdateField.setValue(volunteer.getBirthday());
        sexComboBox.setValue(volunteer.getSex());
        statusComboBox.setValue(volunteer.getVolstat());
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInputs()) {
            showAlert("Validation Error", "Please fill in all required fields");
            return;
        }

        Volunteer volunteer = createVolunteerFromForm();

        if (volunteerTable.getSelectionModel().isEmpty()) {
            createNewVolunteer(volunteer);
        } else {
            updateExistingVolunteer(volunteer);
        }
    }

    private Volunteer createVolunteerFromForm() {
        Volunteer volunteer = new Volunteer();
        String newID = volunteerDAO .generateNewVolunteerID();
        volunteer.setVolid(volunteerField.getText().isEmpty() ?
               newID : volunteerField.getText());
        volunteer.setFname(firstNameField.getText());
        volunteer.setLname(lastNameField.getText());
        volunteer.setAddress(addressField.getText());
        volunteer.setPhone(phoneField.getText());
        volunteer.setEmail(emailField.getText());
        volunteer.setBirthday(Date.valueOf(birthdateField.getValue()).toLocalDate());
        volunteer.setSex(sexComboBox.getValue());
        volunteer.setVolstat(statusComboBox.getValue());
        volunteer.setPassword("defaultPassword");
        volunteer.setRole("Volunteer");

        return volunteer;
    }

    private void createNewVolunteer(Volunteer volunteer) {
        if (volunteerDAO.emailExists(volunteer.getEmail())) {
            showAlert("Error", "Email already exists");
            return;
        }

        if (VolunteerDAO.createVolunteer(volunteer)) {
            showAlert("Success", "Volunteer created successfully");
            loadVolunteerData();
            clearForm();
        } else {
            showAlert("Error", "Failed to create volunteer");
        }
    }

    private void updateExistingVolunteer(Volunteer volunteer) {
        volunteer.setVolid(volunteerTable.getSelectionModel().getSelectedItem().getVolid());

        if (volunteerDAO.updateVolunteer(volunteer)) {
            showAlert("Success", "Volunteer updated successfully");
            volunteerTable.refresh();
            clearForm();
        } else {
            showAlert("Error", "Failed to update volunteer");
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        volunteerTable.getSelectionModel().clearSelection();
        clearForm();
    }

    private void clearForm() {
        volunteerField.clear();
        firstNameField.clear();
        lastNameField.clear();
        addressField.clear();
        phoneField.clear();
        emailField.clear();
        birthdateField.setValue(null);
        sexComboBox.setValue(null);
        statusComboBox.setValue(null);
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (firstNameField.getText().trim().isEmpty()) {
            firstNameField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            firstNameField.setStyle("");
        }

        if (lastNameField.getText().trim().isEmpty()) {
            lastNameField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            lastNameField.setStyle("");
        }

        if (emailField.getText().trim().isEmpty()) {
            emailField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            emailField.setStyle("");
        }

        if (birthdateField.getValue() == null) {
            birthdateField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            birthdateField.setStyle("");
        }

        return isValid;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}