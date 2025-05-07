package Admin.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private Button logoutButton;
    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/view/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logoutButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load the login screen: " + e.getMessage());
        }
    }


}
