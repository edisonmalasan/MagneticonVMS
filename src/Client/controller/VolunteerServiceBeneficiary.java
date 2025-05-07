package Client.controller;

import common.dao.ServiceDAO;
import common.dao.VolunteerDAO;
import common.models.Attendance;
import common.models.Service;
import common.models.Volunteer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;

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
    private ComboBox<String> serviceComboBox;


    @FXML
    private Button backBttn;

    private String currentVolunteerId;
    private Stage currentStage;
    private Stage stage;

    public void initialize(){
        setupEventHandlers();
    }
    private void setupEventHandlers() {
        backBttn.setOnAction(event -> handleBack());
        serviceComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> loadServiceDetails(newValue));
    }

    public void setVolunteerData(String volunteerId) {
        this.currentVolunteerId = volunteerId;
        loadVolunteerDetails();
        loadServiceOptions();
    }

    private void loadVolunteerDetails() {
        try {
            Volunteer volunteer = VolunteerDAO.getVolunteerById(currentVolunteerId);
            if (volunteer != null) {
                volId.setText(volunteer.getVolid());
                volName.setText(volunteer.getFname() + " " + volunteer.getLname());

            }
        } catch (SQLException e) {
            showError("Data Error", "Failed to load volunteer details");
            e.printStackTrace();
        }
    }

    private void loadServiceOptions() {
        try {
            List<String> services = ServiceDAO.getServicesForVolunteer(currentVolunteerId);
            serviceComboBox.setItems(FXCollections.observableArrayList(services));
            if (!services.isEmpty()) {
                serviceComboBox.getSelectionModel().selectFirst();
            }
        } catch (SQLException e) {
            showError("Data Error", "Failed to load service options");
            e.printStackTrace();
        }
    }

    private void loadServiceDetails(String serviceName) {
        if (serviceName == null || serviceName.isEmpty()) return;

        try {
            Service details = ServiceDAO.getServiceDetails(serviceName, currentVolunteerId);
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
            Stage currentStage = (Stage) backBttn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Client/view/VolunteerDashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            currentStage.setScene(scene);
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load");
        }
    }

    public void setStage(Stage currentStage) {
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
