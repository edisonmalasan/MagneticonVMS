package Admin.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ServiceTabController {
    @FXML
    private TableView<String> serviceTable;
    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ComboBox<String> teamComboBox;
    @FXML
    private Button serviceSaveButton;
    @FXML
    private Button serviceClearButton;

    public void initialize() {
        setStatusComboBox();
    }

    private void setStatusComboBox() {
        statusComboBox.getItems().addAll("Assigned", "Completed", "Ongoing");
    }

    private void handleSave() {

    }

    private void handleClear(){

    }
}
