package Admin.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BeneficiaryTabController {

    @FXML
    private Button assignButton;

    @FXML
    private ComboBox<?> beneficiaryGroupComboBox;

    @FXML
    private TableView<?> beneficiaryTable;

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
    private ComboBox<?> serviceComboBox;
}
