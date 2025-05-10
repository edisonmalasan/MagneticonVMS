package App.controller;

import Admin.controller.AdminDashboardController;
import Client.controller.VolunteerDashboard;
import common.dao.VolunteerDAO;
import common.models.Volunteer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    private Button loginButton;

    @FXML
    private TextField loginEmail;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private Hyperlink registerAdmin;

    @FXML
    private Hyperlink registerVolunteer;

    public void initialize() {
        registerVolunteer.setOnAction(this::handleVolunteerLink);
        registerAdmin.setOnAction(this::handleAdminLink);
    }

    @FXML
    void handleAdminLink(ActionEvent event) {
        try {
            Stage stage = (Stage) registerAdmin.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/view/registerAsAdmin.fxml"));
            Parent root = loader.load();
            RegisterController registerController = loader.getController();
            registerController.setStage(stage);
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load");
        }
    }

    @FXML
    void handleVolunteerLink(ActionEvent event) {
        try {
            Stage stage = (Stage) registerVolunteer.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/view/register.fxml"));
            Parent root = loader.load();
            RegisterController registerController = loader.getController();
            registerController.setStage(stage);
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load");
        }
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String email = loginEmail.getText();
        String password = loginPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Missing Fields", "Please enter both email and password.");
            return;
        }

        VolunteerDAO volunteerDAO = new VolunteerDAO();
        Volunteer user = volunteerDAO.authenticate(email, password);

        if (user != null) {
            try {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                FXMLLoader loader;
                Parent root;


                if ("Admin".equalsIgnoreCase(user.getRole())) {
                    loader = new FXMLLoader(getClass().getResource("/Admin/view/AdminDashboard.fxml"));
                    root = loader.load();

                    AdminDashboardController adminDashboardController = loader.getController();
                    adminDashboardController.setCurrentAdmin(user);
                } else {
                    loader = new FXMLLoader(getClass().getResource("/Client/view/VolunteerDashboard.fxml"));
                    root = loader.load();

                    VolunteerDashboard volunteerDashboard = loader.getController();
                    volunteerDashboard.setCurrentVolunteer(user);
                }

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showAlert("Login Failed", "Invalid email or password.");
        }

    }

    @FXML
    void handleClick(ActionEvent event){
        loginButton = (Button) event.getSource();
        loginButton.setTextFill(Color.WHITE);
    }


    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
