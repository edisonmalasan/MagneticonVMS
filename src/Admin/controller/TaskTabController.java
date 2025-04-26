package Admin.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.w3c.dom.Text;

public class TaskTabController {
    @FXML
    private Button clearButton;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TableColumn<?, ?> descriptionColumn;

    @FXML
    private Button saveButton;

    @FXML
    private TableColumn<?, ?> serviceColumn;

    @FXML
    private ComboBox<?> serviceComboBox;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private ComboBox<?> statusComboBox;

    @FXML
    private TableView<?> taskTable;

    @FXML
    private TableColumn<?, ?> volunteerColumn;

    @FXML
    private ComboBox<?> volunteerComboBox;

    @FXML
    void handleClear(ActionEvent event) {

    }
}
