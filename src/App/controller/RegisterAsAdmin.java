package App.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterAsAdmin {

    @FXML
    private TextField addressTextField;

    @FXML
    private DatePicker birthdateDatePicker;

    @FXML
    private TextField certificationTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private Button registerButton;

    @FXML
    private ComboBox<String> sexComboBox;

    @FXML
    private TextField skillsTextField;


    public void initialize() {
        setSexComboBox();
    }

    private void setSexComboBox() {
        sexComboBox.getItems().addAll("Male", "Female");
    }

    public void setStage(Stage currentStage) {
    }
}
