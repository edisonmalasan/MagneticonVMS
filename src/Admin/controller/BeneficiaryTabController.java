package Admin.controller;

import common.dao.BeneficiaryDAO;
import common.dao.BeneficiaryGroupDAO;
import common.dao.ServiceDAO;
import common.models.BeneficiaryGroup;
import common.models.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.UUID;

public class BeneficiaryTabController {

    @FXML
    private Button assignButton;

    @FXML
    private ComboBox<String> beneficiaryGroupComboBox;

    @FXML
    private TableView<BeneficiaryGroup> beneficiaryTable;

    @FXML
    private Button clearButton;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TableColumn<?, ?> descriptionColumn;

    @FXML
    private TableColumn<?, ?> groupNameColumn;

    @FXML
    private TextField groupNameField;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private Button saveButton;

    @FXML
    private ComboBox<String> serviceComboBox;

    private BeneficiaryGroupDAO beneficiaryGroupDAO;
    private BeneficiaryDAO beneficiaryDAO;
    private ServiceDAO serviceDAO;
    private ObservableList<BeneficiaryGroup> beneficiaryGroups;

    @FXML
    public void initialize() {
        beneficiaryGroupDAO = new BeneficiaryGroupDAO();
        beneficiaryDAO = new BeneficiaryDAO();
        serviceDAO = new ServiceDAO();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("benid"));
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<>("bengroup"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("bendesc"));

        //loads selected details into txt field
        beneficiaryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                groupNameField.setText(newSelection.getBengroup());
                descriptionArea.setText(newSelection.getBendesc());
            }
        });

        //change btn based on mode
        beneficiaryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            saveButton.setText(newVal != null ? "Update" : "Save");
        });

        clearButton.setOnAction(e -> {
            beneficiaryTable.getSelectionModel().clearSelection();
            handleClearGroup(e);
        });

        groupNameField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());

        groupNameField.setTooltip(new Tooltip("Group name is required"));
        descriptionArea.setTooltip(new Tooltip("Description is required"));

        loadBeneficiaryGroups();
        loadServices();
    }

    private void loadBeneficiaryGroups() {
        List<BeneficiaryGroup> groups = beneficiaryGroupDAO.getAllBeneficiaryGroups();
        beneficiaryGroups = FXCollections.observableArrayList(groups);
        beneficiaryTable.setItems(beneficiaryGroups);

        beneficiaryGroupComboBox.getItems().clear();
        for (BeneficiaryGroup group : groups) {
            beneficiaryGroupComboBox.getItems().add(group.getBengroup());
        }
    }

    private void loadServices() {
        List<Service> services = serviceDAO.getAllServices();
        serviceComboBox.getItems().clear();
        for (Service service : services) {
            serviceComboBox.getItems().add(service.getSname());
        }
    }

    @FXML
    private void handleSaveGroup(ActionEvent event) {
        if (!validateInputs()) {
            showAlert("Validation Error", "Please fill in all required fields");
            return;
        }

        String groupName = groupNameField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (groupName.isEmpty() || description.isEmpty()) {
            showAlert("Validation Error", "Group name and description cannot be empty");
            return;
        }

        //check if updating an existing record
        if (beneficiaryTable.getSelectionModel().getSelectedItem() != null) {
            BeneficiaryGroup selected = beneficiaryTable.getSelectionModel().getSelectedItem();
            selected.setBengroup(groupName);
            selected.setBendesc(description);

            if (beneficiaryGroupDAO.updateBeneficiaryGroup(selected)) {
                showAlert("Success", "Beneficiary group updated successfully");
                beneficiaryTable.refresh(); //refresh table view
                handleClearGroup(null);
            } else {
                showAlert("Error", "Failed to update beneficiary group");
            }
        } else {
            BeneficiaryGroup newGroup = new BeneficiaryGroup();
            newGroup.setBenid(UUID.randomUUID().toString());
            newGroup.setBengroup(groupName);
            newGroup.setBendesc(description);

            if (beneficiaryGroupDAO.createBeneficiaryGroup(newGroup)) {
                showAlert("Success", "New beneficiary group created successfully");
                loadBeneficiaryGroups(); //reload data
                handleClearGroup(null);
            } else {
                showAlert("Error", "Failed to create beneficiary group");
            }
        }
    }

    @FXML
    private void handleAssignBeneficiary(ActionEvent event) {
        String serviceName = serviceComboBox.getValue();
        String groupName = beneficiaryGroupComboBox.getValue();

        if (serviceName == null || groupName == null) {
            showAlert("Validation Error", "Please select both service and beneficiary group");
            return;
        }

        Service selectedService = serviceDAO.getAllServices().stream()
                .filter(s -> s.getSname().equals(serviceName))
                .findFirst()
                .orElse(null);

        BeneficiaryGroup selectedGroup = beneficiaryGroups.stream()
                .filter(g -> g.getBengroup().equals(groupName))
                .findFirst()
                .orElse(null);

        if (selectedService != null && selectedGroup != null) {
            if (beneficiaryDAO.assignBeneficiaryToService(selectedService.getServid(), selectedGroup.getBenid())) {
                showAlert("Success", "Beneficiary group assigned to service successfully");
            } else {
                showAlert("Error", "Failed to assign beneficiary group to service");
            }
        }
    }

    @FXML
    private void handleRemoveBeneficiary(ActionEvent event) {
        String serviceName = serviceComboBox.getValue();
        String groupName = beneficiaryGroupComboBox.getValue();

        if (serviceName == null || groupName == null) {
            showAlert("Validation Error", "Please select both service and beneficiary group");
            return;
        }

        // Get the selected service and group
        Service selectedService = serviceDAO.getAllServices().stream()
                .filter(s -> s.getSname().equals(serviceName))
                .findFirst()
                .orElse(null);

        BeneficiaryGroup selectedGroup = beneficiaryGroups.stream()
                .filter(g -> g.getBengroup().equals(groupName))
                .findFirst()
                .orElse(null);

        if (selectedService != null && selectedGroup != null) {
            if (beneficiaryDAO.removeBeneficiaryFromService(selectedService.getServid(), selectedGroup.getBenid())) {
                showAlert("Success", "Beneficiary group removed from service successfully");
            } else {
                showAlert("Error", "Failed to remove beneficiary group from service");
            }
        }
    }

    @FXML
    private void handleClearGroup(ActionEvent event) {
        beneficiaryTable.getSelectionModel().clearSelection();
        groupNameField.clear();
        descriptionArea.clear();

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

    private boolean validateInputs() {
        boolean isValid = true;

        if (groupNameField.getText().trim().isEmpty()) {
            groupNameField.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            isValid = false;
        } else {
            groupNameField.setStyle("");
        }

        if (descriptionArea.getText().trim().isEmpty()) {
            descriptionArea.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            isValid = false;
        } else {
            descriptionArea.setStyle("");
        }

        return isValid;
    }

}
