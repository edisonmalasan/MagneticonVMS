package Client.controller;

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
    private Button backBttn;


    public void initialize(){
        backBttn.setOnAction(event -> handleBack());
    }
    private void loadVolunteer() {

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
}
