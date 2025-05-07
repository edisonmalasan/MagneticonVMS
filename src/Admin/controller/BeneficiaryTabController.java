package Admin.controller;

import common.dao.*;
import common.models.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.*;
import java.util.stream.Collectors;

public class BeneficiaryTabController {
    @FXML private TableView<BeneficiaryGroup> beneficiaryTable;
    @FXML private TableColumn<BeneficiaryGroup, String> idColumn;
    @FXML private TableColumn<BeneficiaryGroup, String> groupNameColumn;
    @FXML private TableColumn<BeneficiaryGroup, String> descriptionColumn;
    @FXML private TextField groupNameField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> beneficiaryGroupComboBox;
    @FXML private ComboBox<String> serviceComboBox;
    @FXML private Button saveButton;
    @FXML private Button clearButton;
    @FXML private Button assignButton;

    private final BeneficiaryGroupDAO beneficiaryGroupDAO = new BeneficiaryGroupDAO();
    private final BeneficiaryDAO beneficiaryDAO = new BeneficiaryDAO();
    private final ServiceDAO serviceDAO = new ServiceDAO();
    private ObservableList<BeneficiaryGroup> beneficiaryGroups;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupSelectionHandlers();
        setupValidation();
        loadInitialData();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("benid"));
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<>("bengroup"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("bendesc"));
    }

    private void setupSelectionHandlers() {
        beneficiaryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateForm(newVal);
                saveButton.setText("Update");
            } else {
                clearForm();
                saveButton.setText("Create");
            }
        });
    }

    private void setupValidation() {
        groupNameField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        descriptionArea.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());

        groupNameField.setTooltip(new Tooltip("Group name is required"));
        descriptionArea.setTooltip(new Tooltip("Description is required"));
    }

    private void loadInitialData() {
        loadBeneficiaryGroups();
        loadServices();
    }

    private void loadBeneficiaryGroups() {
        List<BeneficiaryGroup> groups = beneficiaryGroupDAO.getAllBeneficiaryGroups();
        beneficiaryGroups = FXCollections.observableArrayList(groups);
        beneficiaryTable.setItems(beneficiaryGroups);

        //get unique bengroups
        beneficiaryGroupComboBox.getItems().setAll(
                groups.stream()
                        .map(BeneficiaryGroup::getBengroup)
                        .distinct()
                        .collect(Collectors.toList())
        );
    }

    private void loadServices() {
        serviceComboBox.getItems().setAll(
                serviceDAO.getAllServices().stream()
                        .map(Service::getSname)
                        .collect(Collectors.toList())
        );
    }

    private void populateForm(BeneficiaryGroup group) {
        groupNameField.setText(group.getBengroup());
        descriptionArea.setText(group.getBendesc());
    }

    @FXML
    private void handleSaveGroup(ActionEvent event) {
        if (!validateInputs()) {
            showAlert("Validation error", "Please fill in all required fields");
            return;
        }

        String groupName = groupNameField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (beneficiaryTable.getSelectionModel().isEmpty()) {
            createNewGroup(groupName, description);
        } else {
            updateExistingGroup(groupName, description);
        }
    }

    private void createNewGroup(String groupName, String description) {
        String newID = beneficiaryGroupDAO.generateNewBeneficiaryID();

        BeneficiaryGroup newGroup = new BeneficiaryGroup(
                newID,
                groupName,
                description
        );

        if (beneficiaryGroupDAO.createBeneficiaryGroup(newGroup)) {
            showAlert("Success", "New beneficiary group created successfully");
            loadBeneficiaryGroups();
            clearForm();
        } else {
            showAlert("Error", "Failed to create beneficiary group");
        }
    }

    private void updateExistingGroup(String groupName, String description) {
        BeneficiaryGroup selected = beneficiaryTable.getSelectionModel().getSelectedItem();
        selected.setBengroup(groupName);
        selected.setBendesc(description);

        if (beneficiaryGroupDAO.updateBeneficiaryGroup(selected)) {
            showAlert("Success", "Beneficiary group updated successfully");
            beneficiaryTable.refresh();
            clearForm();
        } else {
            showAlert("Error", "Failed to update beneficiary group");
        }
    }

    @FXML
    private void handleAssignBeneficiary(ActionEvent event) {
        if (!validateAssignmentInputs()) {
            showAlert("Validation Error", "Please select both service and beneficiary group");
            return;
        }

        Optional<Service> service = findSelectedService();
        Optional<BeneficiaryGroup> group = findSelectedGroup();

        if (service.isPresent() && group.isPresent()) {
            if (beneficiaryDAO.assignBeneficiaryToService(
                    service.get().getServid(),
                    group.get().getBenid())) {
                showAlert("Success", "Assignment successful");
            } else {
                showAlert("Error", "Assignment failed");
            }
        }
    }

    @FXML
    private void handleRemoveBeneficiary(ActionEvent event) {
        if (!validateAssignmentInputs()) {
            showAlert("Validation Error", "Please select both service and beneficiary group");
            return;
        }

        Optional<Service> service = findSelectedService();
        Optional<BeneficiaryGroup> group = findSelectedGroup();

        if (service.isPresent() && group.isPresent()) {
            if (beneficiaryDAO.removeBeneficiaryFromService(
                    service.get().getServid(),
                    group.get().getBenid())) {
                showAlert("Success", "Removal successful");
            } else {
                showAlert("Error", "Removal failed");
            }
        }
    }

    private Optional<Service> findSelectedService() {
        return serviceDAO.getAllServices().stream()
                .filter(s -> s.getSname().equals(serviceComboBox.getValue()))
                .findFirst();
    }

    private Optional<BeneficiaryGroup> findSelectedGroup() {
        return beneficiaryGroups.stream()
                .filter(g -> g.getBengroup().equals(beneficiaryGroupComboBox.getValue()))
                .findFirst();
    }

    private boolean validateAssignmentInputs() {
        return serviceComboBox.getValue() != null &&
                beneficiaryGroupComboBox.getValue() != null;
    }

    @FXML
    private void handleClearGroup(ActionEvent event) {
        beneficiaryTable.getSelectionModel().clearSelection();
        clearForm();
    }

    private void clearForm() {
        groupNameField.clear();
        descriptionArea.clear();
        resetValidationStyles();
    }

    private boolean validateInputs() {
        boolean isValid = !groupNameField.getText().trim().isEmpty() &&
                !descriptionArea.getText().trim().isEmpty();

        groupNameField.setStyle(isValid ? "" : "-fx-border-color: red; -fx-border-width: 1px;");
        descriptionArea.setStyle(isValid ? "" : "-fx-border-color: red; -fx-border-width: 1px;");

        return isValid;
    }

    private void resetValidationStyles() {
        groupNameField.setStyle("");
        descriptionArea.setStyle("");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}