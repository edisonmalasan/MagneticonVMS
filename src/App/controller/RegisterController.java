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
import java.time.LocalDate;
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
        registerButton.setOnAction(e -> {
            String fName = firstNameTextField.getText();
            String lName = lastNameTextField.getText();
            String address = addressTextField.getText();
            String phoneNo = phoneNumberTextField.getText();
            String email = emailTextField.getText();
            String pwd = passwordField.getText();
            String sex = sexComboBox.getValue();
            LocalDate bday = birthdateDatePicker.getValue();

            if (fName.isEmpty() || lName.isEmpty() || address.isEmpty() || phoneNo.isEmpty() || email.isEmpty() || pwd.isEmpty() || sex == null || bday == null) {
                showAlert("Missing Fields", "Pleas fill in all fields");
                return;
            }

            VolunteerDAO vDAO = new VolunteerDAO();

            if (vDAO.emailExists(email)) {
                showAlert("Duplicate Email", "An account with this email already exists.");
                return;
            }

            Volunteer vol = new Volunteer();
            vol.setVolid(String.valueOf(System.currentTimeMillis()));
            vol.setFname(fName);
            vol.setLname(lName);
            vol.setAddress(address);
            vol.setPhone(phoneNo);
            vol.setEmail(email);
            vol.setPassword(pwd);
            vol.setSex(sex);
            vol.setBirthday(bday);
            vol.setVolstat("Pending");

            boolean created = VolunteerDAO.createVolunteer(vol);
            if(created) {
                showSuccess("Registration Successful.","You are now a registered volunteer!");
                clearFields();
            } else {
                showAlert("Registration Failed.", "Ensure that all details are filled in :)");
            }
        });
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        firstNameTextField.clear();
        lastNameTextField.clear();
        addressTextField.clear();
        phoneNumberTextField.clear();
        emailTextField.clear();
        passwordField.clear();
        sexComboBox.getSelectionModel().clearSelection();
        birthdateDatePicker.setValue(null);
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
            LoginController mainMenuController = loader.getController();
            mainMenuController.setStage(currentStage);
            currentStage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load");
        }
    }
}