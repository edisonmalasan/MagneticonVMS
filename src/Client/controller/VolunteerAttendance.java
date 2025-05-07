package Client.controller;

import common.dao.AttendanceDAO;
import common.models.Attendance;
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
import java.util.List;

public class VolunteerAttendance {
    @FXML
    private Label volId;
    @FXML
    private Label volName;

    @FXML
    private TableView<Attendance> table;
    @FXML
    private TableColumn<Attendance, String> colService;
    @FXML
    private TableColumn<Attendance, LocalDate> colDate;
    @FXML
    private TableColumn<Attendance, LocalTime> colOut;
    @FXML
    private TableColumn<Attendance, LocalTime> colIn;
    @FXML
    private TableColumn<Attendance, String> colStat;
    @FXML
    private Button backBttn;

    private String currentVolunteerId;
    private String currentVolunteerName;

    public void initialize() {
        setupTableColumns();
        backBttn.setOnAction(event -> handleBack());
    }

    private void setupTableColumns() {
        colService.setCellValueFactory(new PropertyValueFactory<>("servid"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colOut.setCellValueFactory(new PropertyValueFactory<>("timein"));
        colIn.setCellValueFactory(new PropertyValueFactory<>("timeout"));
        colStat.setCellValueFactory(new PropertyValueFactory<>("attendstat"));
    }

    public void setVolunteerData(String volunteerId, String volunteerName) {
        this.currentVolunteerId = volunteerId;
        this.currentVolunteerName = volunteerName;

        updateVolunteerInfo();
        loadAttendanceRecords();
    }

    private void updateVolunteerInfo() {
        volId.setText(currentVolunteerId);
        volName.setText(currentVolunteerName);
    }

    private void loadAttendanceRecords() {
        try {
            List<Attendance> records = AttendanceDAO.getAttendanceForVolunteer(currentVolunteerId);
            ObservableList<Attendance> observableRecords = FXCollections.observableArrayList(records);
            table.setItems(observableRecords);
        } catch (Exception e) {
            showErrorAlert("Failed to load attendance records", e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleBack() {
        try {
            Stage currentStage = (Stage) backBttn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Client/view/VolunteerDashboard.fxml"));
            Parent root = loader.load();
            VolunteerDashboard mainMenuController = loader.getController();
            mainMenuController.setStage(currentStage);
            currentStage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load");
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setStage(Stage currentStage) {

    }
}