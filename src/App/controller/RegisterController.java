package App.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController {

    @FXML
    private ComboBox<String> sexComboBox;

    public void initialize() {
        setSexComboBox();
    }

    private void setSexComboBox() {
        sexComboBox.getItems().addAll("Male", "Female");
    }

    public void setStage(Stage currentStage) {
    }
}