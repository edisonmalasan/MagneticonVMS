package Admin.controller;

import common.dao.AttendanceDAO;
import common.dao.ServiceDAO;
import common.dao.VolunteerDAO;
import common.models.Attendance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AttendanceTabController {

    @FXML private TableColumn<Attendance, String> attendanceIdColumn;
    @FXML private TableView<Attendance> attendanceTable;
    @FXML private Button clearButton;
    @FXML private TableColumn<Attendance, LocalDate> dateColumn;
    @FXML private DatePicker datePicker;
    @FXML private Button saveButton;
    @FXML private TableColumn<Attendance, String> serviceColumn;
    @FXML private ComboBox<String> serviceComboBox;
    @FXML private TableColumn<Attendance, String> statusColumn;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TableColumn<Attendance, LocalTime> timeInColumn;
    @FXML private TextField timeInField;
    @FXML private TableColumn<Attendance, LocalTime> timeOutColumn;
    @FXML private TextField timeOutField;
    @FXML private TableColumn<Attendance, String> volunteerColumn;
    @FXML private ComboBox<String> volunteerComboBox;

    private final AttendanceDAO attendanceDAO = new AttendanceDAO();
    private final ServiceDAO serviceDAO = new ServiceDAO();
    private final VolunteerDAO volunteerDAO = new VolunteerDAO();
    private ObservableList<Attendance> attendances;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    public void initialize() throws SQLException {
        setupTableColumns();
        setupEventHandlers();
        setupValidationListeners();
        loadAttendances();
        populateComboBoxes();
    }

    private void setupTableColumns() {
        attendanceIdColumn.setCellValueFactory(new PropertyValueFactory<>("attendid"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("servid"));
        volunteerColumn.setCellValueFactory(new PropertyValueFactory<>("volid"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeInColumn.setCellValueFactory(new PropertyValueFactory<>("timein"));
        timeOutColumn.setCellValueFactory(new PropertyValueFactory<>("timeout"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("attendstat"));
    }

    private void setupEventHandlers() {
        attendanceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
                saveButton.setText("Update");
            } else {
                clearForm();
                saveButton.setText("Create");
            }
        });
    }

    private void populateForm(Attendance attendance) {
        serviceComboBox.setValue(attendance.getServid());
        volunteerComboBox.setValue(attendance.getVolid());
        datePicker.setValue(attendance.getDate());
        timeInField.setText(attendance.getTimein() != null ? attendance.getTimein().format(timeFormatter) : "");
        timeOutField.setText(attendance.getTimeout() != null ? attendance.getTimeout().format(timeFormatter) : "");
        statusComboBox.setValue(attendance.getAttendstat());
    }

    private void clearForm() {
        serviceComboBox.setValue(null);
        volunteerComboBox.setValue(null);
        datePicker.setValue(null);
        timeInField.clear();
        timeOutField.clear();
        statusComboBox.setValue(null);
        resetValidationStyles();
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInputs()) {
            showAlert("Validation Error", "Please fill in all required fields");
            return;
        }

        try {
            String serviceId = serviceComboBox.getValue();
            String volunteerId = volunteerComboBox.getValue();
            LocalDate date = datePicker.getValue();
            LocalTime timeIn = parseTime(timeInField.getText());
            LocalTime timeOut = parseTime(timeOutField.getText());
            String status = statusComboBox.getValue();

            if (timeOut != null && timeIn != null && timeOut.isBefore(timeIn)) {
                showAlert("Validation Error", "Time out must be after time in");
                return;
            }

            if (attendanceTable.getSelectionModel().getSelectedItem() != null) {
                updateExistingAttendance(serviceId, volunteerId, date, timeIn, timeOut, status);
            } else {
                createNewAttendance(serviceId, volunteerId, date, timeIn, timeOut, status);
            }
        } catch (DateTimeParseException | SQLException e) {
            showAlert("Validation Error", "Please enter time in HH:mm format (e.g., 09:00)");
        }
    }

    private LocalTime parseTime(String timeString) throws DateTimeParseException {
        if (timeString == null || timeString.trim().isEmpty()) {
            return null;
        }
        return LocalTime.parse(timeString.trim(), timeFormatter);
    }

    private void updateExistingAttendance(String serviceId, String volunteerId, LocalDate date,
                                          LocalTime timeIn, LocalTime timeOut, String status) throws SQLException {
        Attendance selected = attendanceTable.getSelectionModel().getSelectedItem();
        selected.setServid(serviceId);
        selected.setVolid(volunteerId);
        selected.setDate(date);
        selected.setTimein(timeIn);
        selected.setTimeout(timeOut);
        selected.setAttendstat(status);

        if (attendanceDAO.updateAttendance(selected)) {
            showAlert("Success", "Attendance updated successfully");
            attendanceTable.refresh();
            clearForm();
        } else {
            showAlert("Error", "Failed to update attendance");
        }
    }

    private void createNewAttendance(String serviceId, String volunteerId, LocalDate date,
                                     LocalTime timeIn, LocalTime timeOut, String status) throws SQLException {
        Attendance newAttendance = new Attendance();
        newAttendance.setServid(serviceId);
        newAttendance.setVolid(volunteerId);
        newAttendance.setDate(date);
        newAttendance.setTimein(timeIn);
        newAttendance.setTimeout(timeOut);
        newAttendance.setAttendstat(status);

        if (attendanceDAO.createAttendance(newAttendance)) {
            showAlert("Success", "New attendance record created successfully");
            loadAttendances();
            clearForm();
        } else {
            showAlert("Error", "Failed to create attendance record");
        }
    }

    private void setupValidationListeners() {
        serviceComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        volunteerComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        timeInField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        statusComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateInputs());

        serviceComboBox.setTooltip(new Tooltip("Select a service"));
        volunteerComboBox.setTooltip(new Tooltip("Select a volunteer"));
        datePicker.setTooltip(new Tooltip("Select attendance date"));
        timeInField.setTooltip(new Tooltip("Enter time in HH:mm format (e.g., 09:00)"));
        timeOutField.setTooltip(new Tooltip("Enter time in HH:mm format (e.g., 17:00)"));
        statusComboBox.setTooltip(new Tooltip("Select attendance status"));
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (serviceComboBox.getValue() == null) {
            serviceComboBox.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            isValid = false;
        } else {
            serviceComboBox.setStyle("");
        }

        if (volunteerComboBox.getValue() == null) {
            volunteerComboBox.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            isValid = false;
        } else {
            volunteerComboBox.setStyle("");
        }

        if (datePicker.getValue() == null) {
            datePicker.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            isValid = false;
        } else {
            datePicker.setStyle("");
        }

        if (timeInField.getText() == null || timeInField.getText().trim().isEmpty()) {
            timeInField.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            isValid = false;
        } else {
            try {
                parseTime(timeInField.getText());
                timeInField.setStyle("");
            } catch (DateTimeParseException e) {
                timeInField.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
                isValid = false;
            }
        }

        if (statusComboBox.getValue() == null) {
            statusComboBox.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            isValid = false;
        } else {
            statusComboBox.setStyle("");
        }

        return isValid;
    }

    private void resetValidationStyles() {
        serviceComboBox.setStyle("");
        volunteerComboBox.setStyle("");
        datePicker.setStyle("");
        timeInField.setStyle("");
        timeOutField.setStyle("");
        statusComboBox.setStyle("");
    }

    private void loadAttendances() throws SQLException {
        List<Attendance> attendanceList = attendanceDAO.getAllAttendances();
        attendances = FXCollections.observableArrayList(attendanceList);
        attendanceTable.setItems(attendances);
    }

    private void populateComboBoxes() {
        serviceDAO.getAllServices().stream()
                .map(service -> service.getServid())
                .distinct()
                .forEach(serviceComboBox.getItems()::add);

        volunteerDAO.getAllVolunteers().stream()
                .map(volunteer -> volunteer.getVolid())
                .distinct()
                .forEach(volunteerComboBox.getItems()::add);

        statusComboBox.getItems().addAll("Present", "Absent", "Late");
    }

    @FXML
    private void handleClear(ActionEvent event) {
        attendanceTable.getSelectionModel().clearSelection();
        clearForm();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}