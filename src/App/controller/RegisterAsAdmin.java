package App.controller;

import common.dao.AdminDAO;
import common.dao.VolunteerDAO;
import common.models.Admin;
import common.models.Volunteer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

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
    private Button registerButton, backBttn;

    @FXML
    private ComboBox<String> sexComboBox;

    @FXML
    private TextField skillsTextField;

    private Stage currentStage;

    public void initialize() {
        backBttn.setOnAction(e -> handleBackBttn());
        setSexComboBox();
        handleRegister();
    }

    private void setSexComboBox() {
        sexComboBox.getItems().addAll("Male", "Female");
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
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

    //
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

    private void handleRegister() {
        registerButton.setOnAction(e -> {
            String fName = firstNameTextField.getText();
            String lName = lastNameTextField.getText();
            String address = addressTextField.getText();
            String phoneNo = phoneNumberTextField.getText();
            String email = emailTextField.getText();
            String pwd = passwordField.getText();
            String sex = sexComboBox.getValue();
            String skills = skillsTextField.getText();
            String certification = certificationTextField.getText();
            LocalDate bday = birthdateDatePicker.getValue();

            if (fName.isEmpty() || lName.isEmpty() || address.isEmpty() || phoneNo.isEmpty() ||
                    email.isEmpty() || pwd.isEmpty() || sex == null || bday == null) {
                showAlert("Missing Fields", "Please fill in all fields");
                return;
            }

            VolunteerDAO volunteerDAO = new VolunteerDAO();
            if (volunteerDAO.emailExists(email)) {
                showAlert("Registration Failed", "Email already exists.");
                return;
            }

            Volunteer volunteer = new Volunteer();
            String volid = volunteerDAO.generateNewVolunteerID();
            volunteer.setVolid(volid);
            volunteer.setFname(fName);
            volunteer.setLname(lName);
            volunteer.setAddress(address);
            volunteer.setPhone(phoneNo);
            volunteer.setEmail(email);
            volunteer.setPassword(pwd);
            volunteer.setBirthday(bday);
            volunteer.setSex(sex);
            volunteer.setVolstat("Active");
            volunteer.setRole("Admin");

            boolean volunteerCreated = VolunteerDAO.createVolunteer(volunteer);

            if (!volunteerCreated) {
                showAlert("Registration Failed", "Could not register volunteer.");
                return;
            }

            Admin admin = new Admin();
            admin.setVolid(volid);
            admin.setSkills(skills);
            admin.setCertification(certification);

            boolean created = AdminDAO.createAdmin(admin);

            if (created) {
                showSuccess("Registration Successful", "You are now a registered admin!");

                try {
                    Stage stage = (Stage) registerButton.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin/view/AdminDashboard.fxml"));
                    Parent root = loader.load();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (Exception ec) {
                    ec.printStackTrace();
                    showAlert("Error", "Ensure that all details are filled in :)");
                }
            } else {
                showAlert("Registration Failed", "Could not create admin record.");
            }
        });
    }
}