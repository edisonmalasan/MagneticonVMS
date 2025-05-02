package Admin.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TeamTabController {
    @FXML
    private Button clearButton;

    @FXML
    private Button saveButton;

    @FXML
    private TextArea teamDescriptionArea;

    @FXML
    private TableColumn<?, ?> teamDescriptionColumn;

    @FXML
    private TableColumn<?, ?> teamIDColumn;

    @FXML
    private TextField teamNameArea;

    @FXML
    private TableColumn<?, ?> teamNameColumn;

    @FXML
    private TableView<?> teamTable;
}
