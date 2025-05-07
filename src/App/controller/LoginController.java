package App.controller;

import Client.controller.VolunteerDashboard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField loginEmail;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private Hyperlink registerLink;

    public void initialize(){
        registerLink.setOnAction(event -> handleRegister());
    }

    public void handleRegister(){
        try {
            Stage currentStage = (Stage) registerLink.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/view/register.fxml"));
            Parent root = loader.load();
            RegisterController registerController = loader.getController();
            registerController.setStage(currentStage);
            currentStage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load");
        }
    }
    public void setStage(Stage currentStage) {
    }
}
