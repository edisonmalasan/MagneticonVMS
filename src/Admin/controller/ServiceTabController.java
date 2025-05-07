package Admin.controller;

import common.dao.ServiceDAO;
import common.dao.TeamDAO;
import common.models.Service;
import common.models.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.UUID;

public class ServiceTabController {
    @FXML private TableColumn<Service, String> assignedTeamColumn;
    @FXML private TextArea descriptionArea;
    @FXML private TableColumn<Service, String> descriptionColumn;
    @FXML private TextField nameField;
    @FXML private TableColumn<Service, String> serviceIdColumn;
    @FXML private TableColumn<Service, String> serviceNameColumn;
    @FXML private Button serviceSaveButton;
    @FXML private TableView<Service> serviceTable;
    @FXML private TableColumn<Service, String> statusColumn;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private ComboBox<String> teamComboBox;
    @FXML private Button serviceClearButton;

    private final TeamDAO teamDAO = new TeamDAO();
    private final ServiceDAO serviceDAO = new ServiceDAO();
    private ObservableList<Service> services;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupEventHandlers();
        setupValidationListeners();
        loadServices();
    }

    private void setupEventHandlers() {
        serviceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
                serviceSaveButton.setText("Update");
            } else {
                clearForm();
                serviceSaveButton.setText("Create");
            }
        });

    }

    private void setupTableColumns(){
        serviceIdColumn.setCellValueFactory(new PropertyValueFactory<>("servid"));
        serviceNameColumn.setCellValueFactory(new PropertyValueFactory<>("sname"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("sdesc"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("sstat"));
        assignedTeamColumn.setCellValueFactory(new PropertyValueFactory<>("teamid"));
    }

    private void populateForm(Service service) {
        nameField.setText(service.getSname());
        descriptionArea.setText(service.getSdesc());
        statusComboBox.setValue(service.getSstat());
        teamComboBox.setValue(service.getTeamid());
    }

    private void clearForm() {
        nameField.clear();
        descriptionArea.clear();
        statusComboBox.setValue(null);
        teamComboBox.setValue(null);
        resetValidationStyles();
    }

    private void setupValidationListeners() {
        nameField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        descriptionArea.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        statusComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        teamComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateInputs());

        nameField.setTooltip(new Tooltip("Service name is required"));
        descriptionArea.setTooltip(new Tooltip("Service description is required"));
        statusComboBox.setTooltip(new Tooltip("Select a status"));
        teamComboBox.setTooltip(new Tooltip("Select a team"));
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInputs()) {
            showAlert("Validation Error", "Please fill in all required fields");
            return;
        }

        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        String status = statusComboBox.getValue();
        String teamId = teamComboBox.getValue();

        if (serviceTable.getSelectionModel().getSelectedItem() != null) {
            updateExistingService(name, description, status, teamId);
        } else {
            createNewService(name, description, status, teamId);
        }
    }

    private void updateExistingService(String name, String description, String status, String teamId) {
        Service selected = serviceTable.getSelectionModel().getSelectedItem();
        selected.setSname(name);
        selected.setSdesc(description);
        selected.setSstat(status);
        selected.setTeamid(teamId);

        if (serviceDAO.updateService(selected)) {
            showAlert("Success", "Service updated successfully");
            serviceTable.refresh();
            clearForm();
        } else {
            showAlert("Error", "Failed to update service");
        }
    }

    private void createNewService(String name, String description, String status, String teamId) {
        Service newService = new Service();
        String newID = serviceDAO.generateNewServiceId();

        newService.setServid(newID);
        newService.setSname(name);
        newService.setSdesc(description);
        newService.setSstat(status);
        newService.setTeamid(teamId);

        if (serviceDAO.createService(newService)) {
            showAlert("Success", "New service created successfully");
            loadServices();
            clearForm();
        } else {
            showAlert("Error", "Failed to create service");
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (nameField.getText().trim().isEmpty()) {
            nameField.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            isValid = false;
        } else {
            nameField.setStyle("");
        }

        if (descriptionArea.getText().trim().isEmpty()) {
            descriptionArea.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            isValid = false;
        } else {
            descriptionArea.setStyle("");
        }

        if (statusComboBox.getValue() == null) {
            statusComboBox.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            isValid = false;
        } else {
            statusComboBox.setStyle("");
        }

        if (teamComboBox.getValue() == null) {
            teamComboBox.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            isValid = false;
        } else {
            teamComboBox.setStyle("");
        }

        return isValid;
    }

    private void resetValidationStyles() {
        nameField.setStyle("");
        descriptionArea.setStyle("");
        statusComboBox.setStyle("");
        teamComboBox.setStyle("");
    }

    private void loadServices() {
        List<Service> serviceList = serviceDAO.getAllServices();
        List<Team> teamList = teamDAO.getAllTeams();
        services = FXCollections.observableArrayList(serviceList);
        serviceTable.setItems(services);

        statusComboBox.getItems().clear();
        statusComboBox.getItems().addAll("Ongoing", "Assigned", "Completed");

        //map unique teamID's to combo box
        teamComboBox.getItems().clear();
        teamList.stream()
                .map(Team::getTeamid)
                .distinct()
                .forEach(teamComboBox.getItems()::add);
    }

    @FXML
    private void handleClear(ActionEvent event) {
        serviceTable.getSelectionModel().clearSelection();
        clearForm();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}