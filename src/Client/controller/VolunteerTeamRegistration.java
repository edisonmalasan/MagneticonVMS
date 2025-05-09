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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class VolunteerTeamRegistration implements Initializable {
    @FXML private Label volID;
    @FXML private Label volName;
    @FXML private ComboBox<String> choiceDD;
    @FXML private Label displayTeamNo;
    @FXML private Label displayTeamDesc;
    @FXML private Button joinBttn;
    @FXML private Button cancelBttn;

    private String currentVolunteerId;
    private Stage currentStage;
    private List<Team> availableTeams;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBox();
        setupButtonActions();
    }

    private void setupComboBox() {
        choiceDD.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> displayTeamDetails(newValue));
    }

    private void setupButtonActions() {
        joinBttn.setOnAction(event -> handleJoinTeam());
        cancelBttn.setOnAction(event -> handleCancel());
    }

    public void setVolunteerData(String volunteerId) {
        this.currentVolunteerId = volunteerId;
        loadVolunteerDetails();
        loadAvailableTeams();
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    private void loadVolunteerDetails() {
        try {
            Volunteer volunteer = VolunteerDAO.getVolunteerById(currentVolunteerId);
            if (volunteer != null) {
                volID.setText(volunteer.getVolid());
                volName.setText(volunteer.getFname() + " " + volunteer.getLname());
            }
        } catch (SQLException e) {
            showError("Data Error", "Failed to load volunteer details");
            e.printStackTrace();
        }
    }

    private void loadAvailableTeams() {
        try {
            availableTeams = TeamDAO.getAvailableTeams();
            if (availableTeams != null && !availableTeams.isEmpty()) {
                choiceDD.setItems(FXCollections.observableArrayList(
                        availableTeams.stream().map(Team::getTname).toList()
                ));
                choiceDD.getSelectionModel().selectFirst();
            } else {
                displayTeamNo.setText("No teams available");
                displayTeamDesc.setText("There are currently no teams accepting new members");
                choiceDD.setDisable(true);
                joinBttn.setDisable(true);
            }
        } catch (SQLException e) {
            showError("Data Error", "Failed to load available teams");
            e.printStackTrace();
        }
    }

    private void displayTeamDetails(String teamName) {
        if (teamName == null || teamName.isEmpty()) return;

        availableTeams.stream()
                .filter(team -> team.getTname().equals(teamName))
                .findFirst()
                .ifPresent(team -> {
                    displayTeamNo.setText("Team: " + team.getTname());
                    displayTeamDesc.setText(team.getTdesc() != null ?
                            team.getTdesc() : "No description available");
                });
    }

    private void handleJoinTeam() {
        String selectedTeam = choiceDD.getSelectionModel().getSelectedItem();
        if (selectedTeam == null || selectedTeam.isEmpty()) {
            showError("Selection Error", "Please select a team to join");
            return;
        }

        try {
            boolean success = TeamDAO.addVolunteerToTeam(currentVolunteerId, selectedTeam);
            if (success) {
                showInfo("Success", "You have successfully joined " + selectedTeam);
                returnToDashboard();
            } else {
                showError("Registration Error", "Failed to join the selected team");
            }
        } catch (SQLException e) {
            showError("Database Error", "Failed to register for team: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleCancel() {
        returnToDashboard();
    }

    private void returnToDashboard() {
        try {
            Stage currentStage = (Stage) cancelBttn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/view/VolunteerDashboard.fxml"));
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

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}