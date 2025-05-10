package Client.controller;

import common.dao.TeamDAO;
import common.dao.VolunteerDAO;
import common.models.Team;
import common.models.Volunteer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class VolunteerTeams implements Initializable {
    @FXML private Label volId;
    @FXML private Label volName;
    @FXML private Label volTeam;
    @FXML private ComboBox<String> choiceDD;
    @FXML private TableView<Volunteer> table;
    @FXML private TableColumn<Volunteer, String> colVolId;
    @FXML private TableColumn<Volunteer, String> colLName;
    @FXML private TableColumn<Volunteer, String> colFName;
    @FXML private TableColumn<Volunteer, String> colAddress;
    @FXML private TableColumn<Volunteer, String> colPhone;
    @FXML private TableColumn<Volunteer, String> colEmail;
    @FXML private TableColumn<Volunteer, String> colBday;
    @FXML private TableColumn<Volunteer, String> colSex;
    @FXML private TableColumn<Volunteer, String> colStat;
    @FXML private Button backBttn;

    private Volunteer currentVolunteer;
    private String currentVolunteerId;
    private Stage currentStage;
    private List<Team> volunteerTeams;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupComboBox();
        setupButtonActions();
    }

    private void setupTableColumns() {
        colVolId.setCellValueFactory(new PropertyValueFactory<>("volid"));
        colLName.setCellValueFactory(new PropertyValueFactory<>("lname"));
        colFName.setCellValueFactory(new PropertyValueFactory<>("fname"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colBday.setCellValueFactory(new PropertyValueFactory<>("bday"));
        colSex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        colStat.setCellValueFactory(new PropertyValueFactory<>("volstat"));
    }

    private void setupComboBox() {
        choiceDD.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> loadTeamMembers(newValue));
    }

    private void setupButtonActions() {
        backBttn.setOnAction(event -> handleBack());
    }

    public void setCurrentVolunteer(Volunteer volunteer) {
        this.currentVolunteer = volunteer;
        this.currentVolunteerId = volunteer.getVolid();
        displayVolunteerInfo(volunteer);
        loadVolunteerTeams();
    }

    private void displayVolunteerInfo(Volunteer volunteer) {
        volId.setText(volunteer.getVolid());
        volName.setText(volunteer.getFname() + " " + volunteer.getLname());
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    private void loadVolunteerTeams() {
        try {
            volunteerTeams = TeamDAO.getTeamsForVolunteer(currentVolunteerId);
            if (volunteerTeams != null && !volunteerTeams.isEmpty()) {
                choiceDD.setItems(FXCollections.observableArrayList(
                        volunteerTeams.stream().map(Team::getTname).toList()
                ));
                choiceDD.getSelectionModel().selectFirst();
            } else {
                table.setPlaceholder(new Label("You are not assigned to any teams"));
                choiceDD.setDisable(true);
            }
        } catch (SQLException e) {
            showError("Data Error", "Failed to load volunteer teams");
            e.printStackTrace();
        }
    }

    private void loadTeamMembers(String teamName) {
        if (teamName == null || teamName.isEmpty()) {
            table.setPlaceholder(new Label("Please select a team"));
            return;
        }

        // Show loading state
        table.setPlaceholder(new Label("Loading team members..."));

        try {
            List<Volunteer> members = TeamDAO.getTeamMembers(teamName);

            if (members.isEmpty()) {
                table.setPlaceholder(new Label("No members found in this team"));
            } else {
                table.setItems(FXCollections.observableArrayList(members));
            }

        } catch (SQLException e) {
            showError("Error Failed " , "Failed to load team members for team" );
            table.setPlaceholder(new Label("Error loading team members"));
            showError("Load Error",
                    "Failed to load team members.\n" +
                            "Error: " + e.getMessage());
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