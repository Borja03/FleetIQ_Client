package ui.resetpassword;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import exception.SelectException;
import factories.SignableFactory;
import java.util.logging.Level;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.logging.Logger;
import javafx.stage.Modality;
import models.User;

public class ResetPasswordController {

    private static final Logger LOGGER = Logger.getLogger(ResetPasswordController.class.getName());
    private Stage stage;

    @FXML
    private VBox passwordSection;
    @FXML
    private JFXTextField emailField;
    @FXML
    private JFXTextField verificationCodeField;
    @FXML
    private JFXPasswordField newPasswordField;
    @FXML
    private JFXPasswordField repeatPasswordField;
    @FXML
    private JFXButton sendCodeBtn;
    @FXML
    private JFXButton verifyCodeBtn;
    @FXML
    private JFXButton showPasswordBtn;
    @FXML
    private JFXButton showRepeatedPasswordBtn;
    @FXML
    private JFXButton updatePasswordBtn;
    @FXML
    private ImageView passwordEyeIcon;
    @FXML
    private ImageView repeatedPasswordEyeIcon;
    
    private User resetUserPass;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root) {
        LOGGER.info("Initialising Reset Password window.");
        Scene scene = new Scene(root);
        passwordSection.setVisible(false);
        stage.setScene(scene);
        stage.setTitle("Reset Password");
        stage.setResizable(false);
        stage.centerOnScreen();
        resetUserPass= new User();
        stage.getIcons().add(new Image("/image/fleet_icon.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        sendCodeBtn.setOnAction(event -> handleSendCode());
        System.out.println("Controller initialized successfully.");
        // Verify Button Event
        verifyCodeBtn.setOnAction(event -> handleVerifyCode());

        updatePasswordBtn.setOnAction(event -> handleUpdatePassword());

        stage.show();
    }

    private void handleSendCode() {
        String email = emailField.getText().trim();
        if (email.isEmpty() || !email.contains("@")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
        } else {
            LOGGER.info("Verification code sent to email: " + email);
            showAlert(Alert.AlertType.INFORMATION, "Code Sent", "A verification code has been sent to " + email);
            resetUserPass.setEmail(email);
            SignableFactory.getSignable().resetPassword(resetUserPass);

        }
    }

    private void handleVerifyCode() {
        String codeVerification = verificationCodeField.getText().trim();
        resetUserPass.setVerifcationCode(codeVerification);
        try {
            SignableFactory.getSignable().verifyCode(resetUserPass);
               LOGGER.info("Verification successful.");
            passwordSection.setVisible(true); 
            showAlert(Alert.AlertType.INFORMATION, "Code Verified", "You can now set a new password.");
        } catch (SelectException ex) {
            LOGGER.warning("Verification failed.");
            showAlert(Alert.AlertType.ERROR, "Verification Failed", "The code you entered is incorrect.");
        }  
    }

    private void handleUpdatePassword() {
        String newPassword = newPasswordField.getText();
        String repeatPassword = repeatPasswordField.getText();

        if (newPassword.isEmpty() || repeatPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Empty Fields", "Both password fields must be filled.");
        } else if (!newPassword.equals(repeatPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match. Please try again.");
        } else {
            LOGGER.info("Password updated successfully.");
            showAlert(Alert.AlertType.INFORMATION, "Success", "Your password has been updated successfully!");
            // Simulate password update logic here.

            stage.close();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
