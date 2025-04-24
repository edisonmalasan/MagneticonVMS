package Admin.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ServiceTabController {

    @FXML
    private ComboBox<String> statusComboBox;

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
