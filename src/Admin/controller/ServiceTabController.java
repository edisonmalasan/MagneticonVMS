package Admin.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ServiceTabController {

    @FXML
    private TableColumn<?, ?> assignedTeamColumn;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TableColumn<?, ?> descriptionColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TableColumn<?, ?> scheduleColumn;

    @FXML
    private Button serviceClearButton;

    @FXML
    private TableColumn<?, ?> serviceIdColumn;

    @FXML
    private TableColumn<?, ?> serviceNameColumn;

    @FXML
    private Button serviceSaveButton;

    @FXML
    private TableView<String> serviceTable;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private ComboBox<String> teamComboBox;

    @FXML
    void handle(ActionEvent event) {

    }

    @FXML
    void handleClear(ActionEvent event) {

    }

    @FXML
    void handleSave(ActionEvent event) {

    }

}
