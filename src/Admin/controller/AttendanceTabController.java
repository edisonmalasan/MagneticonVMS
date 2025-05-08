package Admin.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AttendanceTabController {

    @FXML
    private TableColumn<?, ?> attendanceIdColumn;

    @FXML
    private TableView<?> attendanceTable;

    @FXML
    private Button clearButton;

    @FXML
    private TableColumn<?, ?> dateColumn;

    @FXML
    private DatePicker datePicker;

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
    private TableColumn<?, ?> timeInColumn;

    @FXML
    private TextField timeInField;

    @FXML
    private TableColumn<?, ?> timeOutColumn;

    @FXML
    private TextField timeOutField;

    @FXML
    private TableColumn<?, ?> volunteerColumn;

    @FXML
    private ComboBox<?> volunteerComboBox;

    @FXML
    void handleClear(ActionEvent event) {

    }

    @FXML
    void handleSave(ActionEvent event) {

    }

}
