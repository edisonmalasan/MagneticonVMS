package Admin.controller;

import common.dao.ServiceDAO;
import common.dao.ServiceScheduleDAO;
import common.models.Service;
import common.models.ServiceSchedule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ScheduleTabController {
    @FXML private Button clearButton;
    @FXML private TableColumn<ServiceSchedule, LocalDate> endDateColumn;
    @FXML private DatePicker endDatePicker;
    @FXML private Button saveButton;
    @FXML private TableColumn<ServiceSchedule, String> scheduleIdColumn;
    @FXML private TextField scheduleIdField;
    @FXML private TableView<ServiceSchedule> scheduleTable;
    @FXML private TableColumn<ServiceSchedule, String> serviceColumn;
    @FXML private ComboBox<String> serviceComboBox;
    @FXML private TableColumn<ServiceSchedule, LocalDate> startDateColumn;
    @FXML private DatePicker startDatePicker;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TableColumn<?, ?> statusColumn;


    private final ServiceScheduleDAO serviceScheduleDAO = new ServiceScheduleDAO();
    private final ServiceDAO serviceDAO = new ServiceDAO();
    private ObservableList<ServiceSchedule> serviceSchedules;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupEventHandlers();
        setupValidationListeners();
        loadServiceSchedules();
    }

    private void setupEventHandlers() {
        scheduleTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
                saveButton.setText("Update");
            } else {
                clearForm();
                saveButton.setText("Create");
            }
        });
    }

    private void setupTableColumns() {
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("servid"));
        scheduleIdColumn.setCellValueFactory(new PropertyValueFactory<>("schedid"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
    }


    private void populateForm(ServiceSchedule schedule) {
        scheduleIdField.setText(schedule.getSchedid());
        serviceComboBox.setValue(schedule.getServid());
        statusComboBox.setValue(schedule.getStatus());
        startDatePicker.setValue(schedule.getStart());
        endDatePicker.setValue(schedule.getEnd());
    }

    private void clearForm() {
        scheduleIdField.clear();
        serviceComboBox.setValue(null);
        statusComboBox.setValue(null);
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        resetValidationStyles();
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInputs()) {
            showAlert("Validation Error", "Please fill in all required fields");
            return;
        }

        String scheduleID = scheduleIdField.getText().trim();
        String serviceID = serviceComboBox.getValue();
        String status = statusComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate.isAfter(endDate)) {
            showAlert("Validation Error", "End date must be after start date");
            return;
        }

        if (scheduleTable.getSelectionModel().getSelectedItem() != null) {
            updateExistingSchedule(scheduleID, serviceID, status, startDate, endDate);
        } else {
            createNewSchedule(scheduleID, serviceID, status, startDate, endDate);
        }
    }

    private void updateExistingSchedule(String scheduleID, String serviceID, String status, LocalDate startDate, LocalDate endDate) {
        ServiceSchedule selected = scheduleTable.getSelectionModel().getSelectedItem();
        selected.setServid(serviceID);
        selected.setSchedid(scheduleID);
        selected.setStatus(status);
        selected.setStart(startDate);
        selected.setEnd(endDate);

        if (serviceScheduleDAO.hasScheduleConflict(serviceID, status, startDate, endDate, scheduleID)) {
            showAlert("Conflict Detected", "This schedule conflicts with an existing one");
            return;
        }

        if (serviceScheduleDAO.updateServiceSchedule(selected)) {
            showAlert("Success", "Schedule updated successfully");
            scheduleTable.refresh();
            clearForm();
        } else {
            showAlert("Error", "Failed to update schedule");
        }
    }

    private void createNewSchedule(String scheduleID, String serviceID, String status, LocalDate startDate, LocalDate endDate) {
        ServiceSchedule newSchedule = new ServiceSchedule();
        String newID = serviceScheduleDAO.generateNewScheduleID();

        newSchedule.setServid(serviceID);
        newSchedule.setSchedid(scheduleID.isEmpty() ? newID : scheduleID);
        newSchedule.setStatus(status);
        newSchedule.setStart(startDate);
        newSchedule.setEnd(endDate);

        if (serviceScheduleDAO.hasScheduleConflict(serviceID, status, startDate, endDate, null)) {
            showAlert("Conflict Detected", "This schedule conflicts with an existing one");
            return;
        }

        if (serviceScheduleDAO.createServiceSchedule(newSchedule)) {
            showAlert("Success", "New schedule created successfully");
            loadServiceSchedules();
            clearForm();
        } else {
            showAlert("Error", "Failed to create schedule");
        }
    }

    private void setupValidationListeners() {
        scheduleIdField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        serviceComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        statusComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        startDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        endDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> validateInputs());

        scheduleIdField.setTooltip(new Tooltip("Schedule ID (leave empty to auto-generate)"));
        serviceComboBox.setTooltip(new Tooltip("Select a service"));
        statusComboBox.setTooltip(new Tooltip("Select a status"));
        startDatePicker.setTooltip(new Tooltip("Select start date"));
        endDatePicker.setTooltip(new Tooltip("Select end date (must be after start date)"));
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (serviceComboBox.getValue() == null) {
            serviceComboBox.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            isValid = false;
        } else {
            serviceComboBox.setStyle("");
        }

        if (statusComboBox.getValue() == null) {
            statusComboBox.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            isValid = false;
        } else {
            statusComboBox.setStyle("");
        }

        if (startDatePicker.getValue() == null) {
            startDatePicker.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            isValid = false;
        } else {
            startDatePicker.setStyle("");
        }

        if (endDatePicker.getValue() == null) {
            endDatePicker.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            isValid = false;
        } else {
            endDatePicker.setStyle("");
        }

        return isValid;
    }

    private void resetValidationStyles() {
        scheduleIdField.setStyle("");
        statusComboBox.setStyle("");
        serviceComboBox.setStyle("");
        startDatePicker.setStyle("");
        endDatePicker.setStyle("");
    }

    private void loadServiceSchedules() {
        List<ServiceSchedule> schedules = serviceScheduleDAO.getAllServiceSchedule();
        List<Service> services = serviceDAO.getAllServices();
        serviceSchedules = FXCollections.observableArrayList(schedules);
        scheduleTable.setItems(serviceSchedules);

        //populate combo box
        serviceComboBox.getItems().clear();
        services.stream()
                .map(Service::getServid)
                .distinct()
                .forEach(serviceComboBox.getItems()::add);

        statusComboBox.getItems().clear();
        schedules.stream()
                .map(ServiceSchedule::getStatus)
                .distinct()
                .forEach(statusComboBox.getItems()::add);
    }

    @FXML
    private void handleClear(ActionEvent event) {
        scheduleTable.getSelectionModel().clearSelection();
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