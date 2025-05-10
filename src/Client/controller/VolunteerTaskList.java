package Client.controller;

import common.dao.ServiceDAO;
import common.dao.TaskAssignmentDAO;
import common.dao.TaskAssignmentDAO;
import common.dao.VolunteerDAO;
import common.models.TaskAssignment;
import common.models.Volunteer;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class VolunteerTaskList implements Initializable {
    @FXML private Label volId;
    @FXML private Label volName;
    @FXML private Label volTeam;
    @FXML private ComboBox<String> serviceDD;
    @FXML private ComboBox<String> taskStat;
    @FXML private Label servid;
    @FXML private Button saveBttn;
    @FXML private Button backBttn;

    String VolunteerId;
    private Volunteer currentVolunteer;
    private String currentVolunteerId;
    private Stage currentStage;
    private TaskAssignment currentTask;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBoxes();
        setupButtonActions();
        serviceDD.setItems(FXCollections.observableArrayList());
        saveBttn.setDisable(true);
    }

    private void setupComboBoxes() {

        taskStat.setItems(FXCollections.observableArrayList(
                "Not Started", "In Progress", "Completed", "Blocked"
        ));
        taskStat.setDisable(true);
    }

    private void setupButtonActions() {
        backBttn.setOnAction(event -> handleBack());
        saveBttn.setOnAction(event -> handleSave());
        serviceDD.setOnAction(event -> loadTasksForService());
    }
    public void setCurrentVolunteer(Volunteer volunteer) {
        this.currentVolunteer = volunteer;
        this.currentVolunteerId = volunteer.getVolid();
        displayVolunteerInfo(volunteer);
        loadServiceOptions();
        loadTasksForService();
    }

    private void displayVolunteerInfo(Volunteer volunteer) {
        volId.setText(volunteer.getVolid());
        volName.setText(volunteer.getFname() + " " + volunteer.getLname());
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    private void loadServiceOptions() {
        try {
            List<String> services = ServiceDAO.getServicesForVolunteer(currentVolunteerId);
            if (services == null || services.isEmpty()) {
                showError("No Services", "No services found for this volunteer.");
            } else {
                serviceDD.setItems(FXCollections.observableArrayList(services));
                if (!services.isEmpty()) {
                    serviceDD.getSelectionModel().selectFirst();  // Select the first service
                    loadTasksForService();  // Load tasks for the selected service
                }
            }
        } catch (SQLException e) {
            showError("Data Error", "Failed to load service options");
            e.printStackTrace();
        }
    }

    private void loadTasksForService() {
        // Get the selected service from the ComboBox
        String selectedService = serviceDD.getSelectionModel().getSelectedItem();

        // Check if the selected service is valid
        if (selectedService == null || selectedService.isEmpty()) {
            showError("No Service Selected", "Please select a service.");
            return;
        }

        try {
            List<TaskAssignment> tasks = TaskAssignmentDAO.getTasksForVolunteerService(currentVolunteerId, selectedService);

            if (tasks != null && !tasks.isEmpty()) {
                currentTask = tasks.get(0); // Store the current task
                displayTask(currentTask);
                saveBttn.setDisable(false);
            } else {
                showInfo("No Tasks", "No tasks found for this service.");
            }
        } catch (SQLException e) {
            showError("Data Error", "Failed to load tasks");
            e.printStackTrace();
        }
    }

    private void displayTask(TaskAssignment task) {
        // Display the task description and set the task status
        servid.setText(task.getServid());
        taskStat.setValue(task.getTaskstat());
        taskStat.setDisable(false);  // Enable task status ComboBox
    }

    private void handleSave() {
        String selectedTaskStatus = taskStat.getSelectionModel().getSelectedItem();
        if (selectedTaskStatus == null || selectedTaskStatus.isEmpty()) {
            showError("Invalid Status", "Please select a valid task status.");
            return;
        }

        try {
            TaskAssignmentDAO.updateTaskStatus(
                    currentTask.getServid(),
                    currentTask.getVolid(),
                    selectedTaskStatus
            );

            currentTask.setTaskstat(selectedTaskStatus);
            showInfo("Success", "Task status updated successfully.");

        } catch (SQLException e) {
            showError("Update Error", "Failed to update task status: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleBack() {
        try {
            Stage stage = (Stage) backBttn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/view/VolunteerDashboard.fxml"));
            Parent root = loader.load();

            VolunteerDashboard dashboardController = loader.getController();
            dashboardController.setStage(stage);
            dashboardController.setCurrentVolunteer(currentVolunteer);

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Navigation Error", "Failed to load Volunteer Dashboard");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}