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
        colVolId.setCellValueFactory(new PropertyValueFactory<>("volunteerId"));
        colLName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colFName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colBday.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        colSex.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colStat.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupComboBox() {
        choiceDD.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> loadTeamMembers(newValue));
    }

    private void setupButtonActions() {
        backBttn.setOnAction(event -> handleBack());
    }

    public void setVolunteerData(String volunteerId) {
        this.currentVolunteerId = volunteerId;
        loadVolunteerDetails();
        loadVolunteerTeams();
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
        } catch (SQLException e) {
            showError("Data Error", "Failed to load volunteer details");
            e.printStackTrace();
        }
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
        if (teamName == null || teamName.isEmpty()) return;

        try {
            List<Volunteer> members = TeamDAO.getTeamMembers(teamName);
            table.setItems(FXCollections.observableArrayList(members));
        } catch (SQLException e) {
            showError("Data Error", "Failed to load team members");
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

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}