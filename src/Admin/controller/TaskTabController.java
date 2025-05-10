package Admin.controller;

import common.dao.TaskAssignmentDAO;
import common.dao.ServiceDAO;
import common.dao.VolunteerDAO;
import common.models.Service;
import common.models.TaskAssignment;
import common.models.Volunteer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.UUID;

public class TaskTabController {
    private static final String[] TASK_STATUSES = {"Completed", "In Progress"};

    @FXML private TableView<TaskAssignment> taskTable;
    @FXML private TableColumn<TaskAssignment, String> serviceColumn;
    @FXML private TableColumn<TaskAssignment, String> volunteerColumn;
    @FXML private TableColumn<TaskAssignment, String> statusColumn;
    @FXML private TableColumn<TaskAssignment, String> descriptionColumn;

    @FXML private ComboBox<String> serviceComboBox;
    @FXML private ComboBox<String> volunteerComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextArea descriptionArea;

    @FXML private Button saveButton;
    @FXML private Button clearButton;

    private final TaskAssignmentDAO taskDAO = new TaskAssignmentDAO();
    private final ServiceDAO serviceDAO = new ServiceDAO();
    private final VolunteerDAO volunteerDAO = new VolunteerDAO();

    private ObservableList<TaskAssignment> tasks;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupComboBoxes();
        setupEventHandlers();
        loadInitialData();
    }

    private void setupTableColumns() {
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("servid"));
        volunteerColumn.setCellValueFactory(new PropertyValueFactory<>("volid"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("taskstat"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("tadesc"));
    }

    private void setupComboBoxes() {
        statusComboBox.getItems().addAll(TASK_STATUSES);

        serviceComboBox.getItems().addAll(
                serviceDAO.getAllServices().stream()
                        .map(Service::getServid)
                        .toList()
        );

        volunteerComboBox.getItems().addAll(
                volunteerDAO.getAllVolunteers().stream()
                        .map(Volunteer::getVolid)
                        .toList()
        );
    }

    private void setupEventHandlers() {
        taskTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateForm(newVal);
                saveButton.setText("Update");
            } else {
                clearForm();
                saveButton.setText("Create");
            }
        });

        clearButton.setOnAction(this::handleClear);
        saveButton.setOnAction(this::handleSave);
    }

    private void loadInitialData() {
        List<TaskAssignment> taskList = taskDAO.getAllTasks();
        tasks = FXCollections.observableArrayList(taskList);
        taskTable.setItems(tasks);
    }

    private void populateForm(TaskAssignment task) {
        serviceComboBox.setValue(task.getServid());
        volunteerComboBox.setValue(task.getVolid());
        statusComboBox.setValue(task.getTaskstat());
        descriptionArea.setText(task.getTadesc());
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInputs()) {
            showAlert("Validation Error", "Please fill in all required fields");
            return;
        }

        String service = serviceComboBox.getValue();
        String volunteer = volunteerComboBox.getValue();
        String status = statusComboBox.getValue();
        String description = descriptionArea.getText().trim();

        if (taskTable.getSelectionModel().isEmpty()) {
            createNewTask(service, volunteer, status, description);
        } else {
            updateExistingTask(service, volunteer, status, description);
        }
    }

    private void createNewTask(String service, String volunteer, String status, String description) {
        TaskAssignment newTask = new TaskAssignment();

        newTask.setServid(service);
        newTask.setVolid(volunteer);
        newTask.setTaskstat(status);
        newTask.setTadesc(description);

        if (taskDAO.createTaskAssignment(newTask)) {
            showAlert("Success", "Task created successfully");
            loadInitialData();
            clearForm();
        } else {
            showAlert("Error", "Failed to create task");
        }
    }

    private void updateExistingTask(String service, String volunteer, String status, String description) {
        TaskAssignment selected = taskTable.getSelectionModel().getSelectedItem();
        selected.setServid(service);
        selected.setVolid(volunteer);
        selected.setTaskstat(status);
        selected.setTadesc(description);

        if (taskDAO.updateTaskAssignment(selected)) {
            showAlert("Success", "Task updated successfully");
            taskTable.refresh();
            clearForm();
        } else {
            showAlert("Error", "Failed to update task");
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        taskTable.getSelectionModel().clearSelection();
        clearForm();
    }

    private void clearForm() {
        serviceComboBox.setValue(null);
        volunteerComboBox.setValue(null);
        statusComboBox.setValue(null);
        descriptionArea.clear();
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (serviceComboBox.getValue() == null) {
            serviceComboBox.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            serviceComboBox.setStyle("");
        }

        if (volunteerComboBox.getValue() == null) {
            volunteerComboBox.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            volunteerComboBox.setStyle("");
        }

        if (descriptionArea.getText().trim().isEmpty()) {
            descriptionArea.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            descriptionArea.setStyle("");
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