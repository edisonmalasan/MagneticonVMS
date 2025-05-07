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

    @FXML
    private ComboBox<String> roleComboBox;

    public void initialize() {
        initializeComboBox();
    }

    private void initializeComboBox(){
        setSexComboBox();
        setRoleComboBox();
    }

    private void setSexComboBox() {
        sexComboBox.getItems().addAll("Male", "Female", "Other");
    }

    private void setRoleComboBox() {
        roleComboBox.getItems().addAll("Member", "Admin");
    }


    public void setStage(Stage currentStage) {
    }
}