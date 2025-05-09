package App.controller;

import Client.controller.*;
import common.dao.VolunteerDAO;
import common.models.Volunteer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController {

    @FXML
    private ComboBox<String> sexComboBox;

    @FXML
    private Button backBttn, registerButton;

    @FXML
    private TextField firstNameTextField, lastNameTextField, addressTextField, phoneNumberTextField, emailTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private DatePicker birthdateDatePicker;
    private Stage currentStage;

    public void initialize() {
        setSexComboBox();
        handleRegister();
        backBttn.setOnAction(e -> handleBackBttn());


    }

//to fix
    private void handleRegister() {
        String fName = firstNameTextField.getText();
        String lName = lastNameTextField.getText();
        String address = addressTextField.getText();
        String phoneNo = phoneNumberTextField.getText();
        String email = emailTextField.getText();
        String pwd = passwordField.getText();
        String sex = sexComboBox.getAccessibleText();
        String bday = birthdateDatePicker.getAccessibleText();

        if (fName.isEmpty() || lName.isEmpty() || address.isEmpty() || phoneNo.isEmpty() || email.isEmpty() || pwd.isEmpty() || sex.isEmpty() || bday.isEmpty()) {
            showAlert("Missing Fields", "Pleas fill in all fields");
            return;
        }

        VolunteerDAO vDAO = new VolunteerDAO();
        Volunteer user = vDAO.createVolunteer(5123651);


    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    private void setSexComboBox() {
        sexComboBox.getItems().addAll("Male", "Female");
    }

    private void handleBackBttn() {
        try {
            Stage currentStage = (Stage) backBttn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/view/login.fxml"));
            Parent root = loader.load();
            VolunteerDashboard mainMenuController = loader.getController();
            mainMenuController.setStage(currentStage);
            currentStage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load");
        }
    }
}