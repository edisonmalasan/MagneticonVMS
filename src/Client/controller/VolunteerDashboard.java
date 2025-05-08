package Client.controller;

import App.controller.LoginController;
import common.dao.VolunteerDAO;
import common.models.Volunteer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class VolunteerDashboard {
    private Volunteer currentVolunteer;
    @FXML
    private Label volId;
    @FXML
    private Label volName;
    @FXML
    private Label volTeam;
    @FXML
    private Button myTasksBttn;
    @FXML
    private Button servicesBttn;
    @FXML
    private Button myTeamBttn;
    @FXML
    private Button beneficiariesBttn;
    @FXML
    private Button attendanceBttn;
    @FXML
    private Button logoutButton;

    private String currentVolunteerId;
    private Stage currentStage;

    public void initialize() {
        setupButtonActions();
    }

    private void setupButtonActions() {
        myTasksBttn.setOnAction(e -> navigateTo("Client/view/VolunteerTaskList.fxml", VolunteerTaskList.class));
        servicesBttn.setOnAction(e -> navigateTo("Client/view/VolunteerServices.fxml", VolunteerServices.class));
        myTeamBttn.setOnAction(e -> navigateTo("Client/view/VolunteerTeams.fxml", VolunteerTeams.class));
        beneficiariesBttn.setOnAction(e -> navigateTo("Client/view/VolunteerServiceBeneficiary.fxml", VolunteerServiceBeneficiary.class));
        attendanceBttn.setOnAction(e -> navigateTo("Client/view/VolunteerAttendance.fxml", VolunteerAttendance.class));
        logoutButton.setOnAction(e -> navigateTo("App/view/Login.fxml", LoginController.class));
    }

    private <T> void navigateTo(String fxmlPath, Class<T> controllerClass) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
            Parent root = loader.load();

            currentStage.setScene(new Scene(root));
            currentStage.show();
        } catch (IOException e) {
            showError("Navigation Error", "Failed to load view: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public void setVolunteerData(String volunteerId) {
        this.currentVolunteerId = volunteerId;
        loadVolunteerDetails();
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    private void loadVolunteerDetails() {
        try {
            Volunteer volunteer = VolunteerDAO.getVolunteerById(currentVolunteerId);
            if (volunteer != null) {
                volId.setText(volunteer.getVolid());
                volName.setText(volunteer.getFname() + " " + volunteer.getLname());
            }
        } catch (Exception e) {
            showError("Data Error", "Failed to load volunteer details");
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setCurrentVolunteer(Volunteer volunteer) {
        this.currentVolunteer = volunteer;
    }
}
