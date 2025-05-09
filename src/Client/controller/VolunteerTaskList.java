package Client.controller;

import common.dao.ServiceDAO;
import common.dao.TaskAssignmentDAO;
import common.dao.TaskAssignmentDAO;
import common.dao.VolunteerDAO;
import common.models.TaskAssignment;
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

public class VolunteerTaskList implements Initializable {
    @FXML private Label volId;
    @FXML private Label volName;
    @FXML private Label volTeam;
    @FXML private ComboBox<String> serviceDD;
    @FXML private ComboBox<String> taskStat;
    @FXML private Label taskDesc;
    @FXML private Button saveBttn;
    @FXML private Button backBttn;

    private Volunteer currentVolunteer;
    private String currentVolunteerId;
    private Stage currentStage;
    private List<Task> currentTasks;
    private Task currentTask;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBoxes();
        setupButtonActions();
    }

    private void setupComboBoxes() {
        // Task status options
        taskStat.setItems(FXCollections.observableArrayList(
                "Not Started", "In Progress", "Completed", "Blocked"
        ));
    }

    private void setupButtonActions() {
        backBttn.setOnAction(event -> handleBack());
        saveBttn.setOnAction(event -> handleSave());
        serviceDD.setOnAction(event -> loadTasksForService());
    }
    public void setCurrentVolunteer(Volunteer volunteer) {
        this.currentVolunteer = volunteer;
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
            List<String> services = new ServiceDAO().getServicesForVolunteer(currentVolunteerId);
            serviceDD.setItems(FXCollections.observableArrayList(services));
            if (!services.isEmpty()) {
                serviceDD.getSelectionModel().selectFirst();
                loadTasksForService();
            }
        } catch (SQLException e) {
            showError("Data Error", "Failed to load service options");
            e.printStackTrace();
        }
    }

    private void loadTasksForService() {
        String selectedService = serviceDD.getSelectionModel().getSelectedItem();
        if (selectedService == null || selectedService.isEmpty()) return;

        try {
            List<TaskAssignment> currentTasks = TaskAssignmentDAO.getTasksForVolunteerService(currentVolunteerId, selectedService);
            if (!currentTasks.isEmpty()) {
                displayTask(currentTasks.get(0));
            } else {
                taskDesc.setText("No tasks assigned for this service");
                taskStat.setValue(null);
                taskStat.setDisable(true);
            }
        } catch (SQLException e) {
            showError("Data Error", "Failed to load tasks");
            e.printStackTrace();
        }
    }

    private void displayTask(TaskAssignment task) {
        taskDesc.setText(task.getTadesc());
        taskStat.setValue(task.getTaskstat());
        taskStat.setDisable(false);
    }

    private void handleSave() {

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