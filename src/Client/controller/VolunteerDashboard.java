package Client.controller;

import App.controller.LoginController;
import common.dao.VolunteerDAO;
import common.models.Volunteer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class VolunteerDashboard {

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

    private Volunteer currentVolunteer;
    private String currentVolunteerId;
    private Stage currentStage;

    @FXML
    public void initialize() {
        setupButtonActions();
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    public void setCurrentVolunteer(Volunteer volunteer) {
        this.currentVolunteer = volunteer;
        this.currentVolunteerId = volunteer.getVolid(); // Optional, if needed elsewhere
        displayVolunteerInfo(volunteer);
    }

    private void displayVolunteerInfo(Volunteer volunteer) {
        volId.setText(volunteer.getVolid());
        volName.setText(volunteer.getFname() + " " + volunteer.getLname());
    }

    private void setupButtonActions() {
        myTasksBttn.setOnAction(e -> navigateTo("/Client/view/VolunteerTaskList.fxml", VolunteerTaskList.class));
        myTeamBttn.setOnAction(e -> navigateTo("/Client/view/VolunteerTeams.fxml", VolunteerTeams.class));
        servicesBttn.setOnAction(e -> navigateTo("/Client/view/VolunteerServices.fxml", VolunteerServices.class));
        beneficiariesBttn.setOnAction(e -> navigateTo("/Client/view/VolunteerServiceBeneficiary.fxml", VolunteerServiceBeneficiary.class));
        attendanceBttn.setOnAction(e -> navigateTo("/Client/view/VolunteerAttendance.fxml", VolunteerAttendance.class));
        logoutButton.setOnAction(e -> navigateTo("/App/view/Login.fxml", LoginController.class));
    }

    private <T> void navigateTo(String fxmlPath, Class<T> controllerClass) {
        try {
            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                showError("Navigation Error", "FXML file not found: " + fxmlPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            T controller = loader.getController();

            // Inject currentVolunteer if needed
            if (controller instanceof VolunteerServiceBeneficiary) {
                ((VolunteerServiceBeneficiary) controller).setCurrentVolunteer(currentVolunteer);
                ((VolunteerServiceBeneficiary) controller).setStage(currentStage);
            } else if (controller instanceof VolunteerServices) {
                ((VolunteerServices) controller).setCurrentVolunteer(currentVolunteer);
                ((VolunteerServices) controller).setStage(currentStage);
            }else if (controller instanceof VolunteerAttendance) {
                ((VolunteerAttendance) controller).setCurrentVolunteer(currentVolunteer);
                ((VolunteerAttendance) controller).setStage(currentStage);
            }else if (controller instanceof VolunteerTaskList) {
                ((VolunteerTaskList) controller).setCurrentVolunteer(currentVolunteer);
                ((VolunteerTaskList) controller).setStage(currentStage);
            }else if (controller instanceof VolunteerTeams) {
                ((VolunteerTeams) controller).setCurrentVolunteer(currentVolunteer);
                ((VolunteerTeams) controller).setStage(currentStage);
            }


            Stage stage = currentStage;
            if (stage == null) {
                stage = (Stage) ((Node) myTasksBttn).getScene().getWindow();
                currentStage = stage;
            }

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Navigation Error", "Failed to load view: " + fxmlPath);
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
}
