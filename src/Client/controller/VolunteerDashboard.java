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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class VolunteerDashboard {
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
    private Button logoutBttn;

    private void loadVolunteer(){

    }

    private void handleTask(){

    }
    private void handleService(){

    }

    private void handleTeam(){

    }

    private void handleBeneficiaries(){

    }

    private void handleAttendance(){

    }

    private void handleLogout(){

    }

}
