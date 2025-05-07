package Client;

import App.controller.LoginController;
import Client.controller.VolunteerDashboard;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class HelloWorld extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/view/VolunteerDashboard.fxml"));
        Parent root = loader.load();

        // Get the controller and pass the primary stage
        VolunteerDashboard loginController = loader.getController();
        loginController.setStage(primaryStage);

        // Set up the main stage
        primaryStage.setTitle("Volunteer Management System");

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Initialize database connection pool or other resources here if needed
        launch(args);
    }
}