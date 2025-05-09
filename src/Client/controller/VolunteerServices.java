package Client.controller;

import common.dao.ServiceDAO;
import common.dao.VolunteerDAO;
import common.models.Service;
import common.models.Volunteer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class VolunteerServices implements Initializable {
    @FXML
    private Label volId;
    @FXML
    private Label volName;
    @FXML
    private Label volTeam;
    @FXML
    private Label servTitle;
    @FXML
    private Label servDesc;
    @FXML
    private Label servStat;
    @FXML
    private Button backBttn;

    private Volunteer currentVolunteer;
    private String currentVolunteerId;
    private Stage currentStage;
    private List<Service> volunteerServices;
    private int currentServiceIndex = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupButtonActions();
    }

    private void setupButtonActions() {
        backBttn.setOnAction(event -> handleBack());
    }

    public void setCurrentVolunteer(Volunteer volunteer) {
        this.currentVolunteer = volunteer;
        displayVolunteerInfo(volunteer);
        loadVolunteerServices();
    }

    private void displayVolunteerInfo(Volunteer volunteer) {
        volId.setText(volunteer.getVolid());
        volName.setText(volunteer.getFname() + " " + volunteer.getLname());
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    private void loadVolunteerServices() {
        try {
            Service service = (Service) ServiceDAO.getServicesForVolunteer(currentVolunteerId);
            if (!service.isEmpty()) {
                displayService(currentServiceIndex);
            } else {
                servTitle.setText("No services assigned");
                servDesc.setText("");
                servStat.setText("");
            }
        } catch (SQLException e) {
            showError("Data Error", "Failed to load volunteer services");
            e.printStackTrace();
        }
    }

    private void displayService(int index) {
        if (index >= 0 && index < volunteerServices.size()) {
            Service service = volunteerServices.get(index);
            servTitle.setText(service.getSname());
            servDesc.setText(service.getSdesc() != null ? service.getSdesc() : "No description available");
            servStat.setText(service.getSstat() != null ? service.getSstat() : "Status unknown");
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
}