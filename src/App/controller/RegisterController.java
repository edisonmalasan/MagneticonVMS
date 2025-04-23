package App.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private ComboBox<String> sexComboBox;

    @FXML
    private ComboBox<String> roleComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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


}