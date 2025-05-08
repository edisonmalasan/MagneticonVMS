package App.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    }

}
