package Admin.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class VolunteerTabController {

    @FXML
    private TableColumn<?, ?> addressColumn;

    @FXML
    private TextField addressField;

    @FXML
    private TableColumn<?, ?> birthdateColumn;

    @FXML
    private DatePicker birthdateField;

    @FXML
    private Button clearButton;

    @FXML
    private TableColumn<?, ?> emailColumn;

    @FXML
    private TextField emailField;

    @FXML
    private TableColumn<?, ?> firstNameColumn;

    @FXML
    private TextField firstNameField;

    @FXML
    private TableColumn<?, ?> lastNameColumn;

    @FXML
    private TextField lastNameField;

    @FXML
    private TableColumn<?, ?> phoneColumn;

    @FXML
    private TextField phoneField;

    @FXML
    private Button saveButton;

    @FXML
    private TableColumn<?, ?> sexColumn;

    @FXML
    private ComboBox<?> sexComboBox;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private ComboBox<?> statusComboBox;

    @FXML
    private TableColumn<?, ?> volidColumn;

    @FXML
    private TextField volunteerField;

    @FXML
    private TableView<?> volunteerTable;

    @FXML
    void handleClear(ActionEvent event) {

    }

    @FXML
    void handleSave(ActionEvent event) {

    }

}
