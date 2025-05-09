package Client.controller;

import common.dao.ServiceDAO;
import common.dao.VolunteerDAO;
import common.models.Service;
import common.models.Volunteer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class VolunteerServiceBeneficiary {
    @FXML
    private Label headerText;
    @FXML
    private Label volDeets;
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
    private Label benGroupTitle;
    @FXML
    private Label benGroupDesc;
    @FXML
    private ComboBox<String> choiceDD;
    @FXML
    private Button backBttn;

    private Volunteer currentVolunteer;
    private Stage currentStage;

    public void initialize() {
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        backBttn.setOnAction(event -> handleBack());
        choiceDD.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> loadServiceDetails(newValue));
    }

    public void setCurrentVolunteer(Volunteer volunteer) {
        this.currentVolunteer = volunteer;
        displayVolunteerInfo(volunteer);
        loadServiceOptions();
    }

    private void displayVolunteerInfo(Volunteer volunteer) {
        volId.setText(volunteer.getVolid());
        volName.setText(volunteer.getFname() + " " + volunteer.getLname());
    }

    private void loadServiceOptions() {
        try {
            List<String> services = ServiceDAO.getServicesForVolunteer(currentVolunteer.getVolid());
            choiceDD.setItems(FXCollections.observableArrayList(services));
            if (!services.isEmpty()) {
                choiceDD.getSelectionModel().selectFirst();
            }
        } catch (SQLException e) {
            showError("Data Error", "Failed to load service options");
            e.printStackTrace();
        }
    }

    private void loadServiceDetails(String serviceName) {
        if (serviceName == null || serviceName.isEmpty()) return;

        try {
            Service details = ServiceDAO.getServiceDetails(serviceName, currentVolunteer.getVolid());
            if (details != null) {
                servTitle.setText(details.getServid());
                servDesc.setText(details.getSdesc());
            }
        } catch (SQLException e) {
            showError("Data Error", "Failed to load service details");
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

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
