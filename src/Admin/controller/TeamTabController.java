package Admin.controller;

import common.dao.TeamDAO;
import common.models.Team;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class TeamTabController {
    @FXML private TableView<Team> teamTable;
    @FXML private TableColumn<Team, String> teamIDColumn;
    @FXML private TableColumn<Team, String> teamNameColumn;
    @FXML private TableColumn<Team, String> teamDescriptionColumn;
    @FXML private TableColumn<Team, String> membersColumn;

    @FXML private TextField teamNameArea;
    @FXML private TextArea teamDescriptionArea;

    @FXML private Button saveButton;
    @FXML private Button clearButton;

    private final TeamDAO teamDAO = new TeamDAO();
    private ObservableList<Team> teams;

    @FXML
    public void initialize() throws SQLException {
        setupTableColumns();
        setupEventHandlers();
        loadTeamData();
    }

    private void setupTableColumns() {
        teamIDColumn.setCellValueFactory(new PropertyValueFactory<>("teamid"));
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("tname"));
        teamDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("tdesc"));
    }

    private void setupEventHandlers() {
        teamTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateForm(newVal);
                saveButton.setText("Update");
            } else {
                clearForm();
                saveButton.setText("Save");
            }
        });

        saveButton.setOnAction(this::handleSave);
        clearButton.setOnAction(this::handleClear);
    }

    // TODO: check if working
    private void loadTeamData() throws SQLException {
        List<Team> teamList = teamDAO.getAllTeamsWithMembers();
        teams = FXCollections.observableArrayList(teamList);

        teamIDColumn.setCellValueFactory(new PropertyValueFactory<>("teamid"));
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("tname"));
        teamDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("tdesc"));

        membersColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getMemberNames())
        );

        // multiline render
        membersColumn.setCellFactory(tc -> new TableCell<Team, String>() {
            private final Text text = new Text();
            {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(text);
                setPrefHeight(Control.USE_COMPUTED_SIZE);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    text.setText("");
                } else {
                    text.setText(item);
                    text.setWrappingWidth(membersColumn.getWidth() - 10);
                }
            }
        });

        teamTable.setItems(teams);
    }

    private void populateForm(Team team) {
        teamNameArea.setText(team.getTname());
        teamDescriptionArea.setText(team.getTdesc());
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInputs()) {
            showAlert("Validation Error", "Team name is required");
            return;
        }

        String teamName = teamNameArea.getText().trim();
        String teamDesc = teamDescriptionArea.getText().trim();

        if (teamTable.getSelectionModel().isEmpty()) {
            try {
                createNewTeam(teamName, teamDesc);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            updateExistingTeam(teamName, teamDesc);
        }
    }

    private void createNewTeam(String teamName, String teamDesc) throws SQLException {
        if (teamDAO.teamNameExists(teamName)) {
            showAlert("Error", "Team name already exists");
            return;
        }

        Team newTeam = new Team(
                teamDAO.generateNewTeamID(),
                teamName,
                teamDesc
        );

        if (teamDAO.createTeam(newTeam)) {
            showAlert("Success", "Team created successfully");
            loadTeamData();
            clearForm();
        } else {
            showAlert("Error", "Failed to create team");
        }
    }

    private void updateExistingTeam(String teamName, String teamDesc) {
        Team selectedTeam = teamTable.getSelectionModel().getSelectedItem();
        selectedTeam.setTname(teamName);
        selectedTeam.setTdesc(teamDesc);

        if (teamDAO.updateTeam(selectedTeam)) {
            showAlert("Success", "Team updated successfully");
            teamTable.refresh();
            clearForm();
        } else {
            showAlert("Error", "Failed to update team");
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        teamTable.getSelectionModel().clearSelection();
        clearForm();
    }

    private void clearForm() {
        teamNameArea.clear();
        teamDescriptionArea.clear();
        resetValidationStyles();
    }

    private boolean validateInputs() {
        boolean isValid = !teamNameArea.getText().trim().isEmpty();

        teamNameArea.setStyle(isValid ? "" : "-fx-border-color: red;");
        return isValid;
    }

    private void resetValidationStyles() {
        teamNameArea.setStyle("");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}