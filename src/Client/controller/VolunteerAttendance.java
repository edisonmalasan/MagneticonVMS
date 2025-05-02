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
import java.util.ArrayList;
import java.util.List;


public class VolunteerAttendance {
    @FXML
    private Label volId;
    @FXML
    private Label volName;
    @FXML
    private Label volTeam;

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

    public void initialize(){
        colService.setCellValueFactory(new PropertyValueFactory<>("servid"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colOut.setCellValueFactory(new PropertyValueFactory<>("timein"));
        colIn.setCellValueFactory(new PropertyValueFactory<>("timeout"));
        colStat.setCellValueFactory(new PropertyValueFactory<>("attendstat"));

        backBttn.setOnAction(event -> handleBack());
    }


    public void setVolunteerData(String volunteerId, String volunteerName) {
        this.currentVolunteerId = volunteerId;
        this.currentVolunteerName = volunteerName;

        // Update UI
        volId.setText(volunteerId);
        volName.setText(volunteerName);

        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {

    }

    private void loadVolunteerTeams(){

    }

    private void loadAttendanceRecords() {
        try {
            List<Attendance> records = AttendanceDAO.getAttendance(currentVolunteerId);
            table.getItems().setAll(FXCollections.observableArrayList(records));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Loading Database");
        }
    }

    private void handleBack() {
        try {

            Stage currentStage = (Stage) backBttn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Client/view/VolunteerDashboard.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            currentStage.setScene(scene);
            currentStage.setTitle("Dashboard");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load");
        }

    }

}

