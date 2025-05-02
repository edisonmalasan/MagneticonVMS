package Admin.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ScheduleTabController {
    @FXML
    private Button clearButton;

    @FXML
    private TableColumn<?, ?> endDateColumn;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Button saveButton;

    @FXML
    private TableColumn<?, ?> scheduleIdColumn;

    @FXML
    private TextField scheduleIdField;

    @FXML
    private TableView<?> scheduleTable;

    @FXML
    private TableColumn<?, ?> serviceColumn;

    @FXML
    private ComboBox<?> serviceComboBox;

    @FXML
    private TableColumn<?, ?> startDateColumn;

    @FXML
    private DatePicker startDatePicker;

}
