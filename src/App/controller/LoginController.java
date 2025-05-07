package App.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField loginEmail;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private Hyperlink registerLink;

    public void setStage(Stage currentStage) {

    }
}
