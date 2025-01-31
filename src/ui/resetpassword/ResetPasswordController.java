package ui.resetpassword;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import encryption.ClientSideEncryption;
import exception.SelectException;
import exception.UpdateException;
import factories.SignableFactory;
import java.util.Base64;
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
import ui.login.LogInController;

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
        resetUserPass = new User();
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
            try {
                resetUserPass.setEmail(email);
                User isUserExist = SignableFactory.getSignable().checkExist(resetUserPass, User.class);
                // Only proceed if user exists and is active
                if (isUserExist != null && isUserExist.isActivo()) {
                    LOGGER.info("Verification code sent to email: " + email);
                    SignableFactory.getSignable().resetPassword(resetUserPass);
                    showAlert(Alert.AlertType.INFORMATION, "Code Sent", "A verification code has been sent to " + email);
                } else {
                    LOGGER.info("Email not found: " + email);
                    showAlert(Alert.AlertType.ERROR, "Error", "This email is not registered in our system.");
                }
            } catch (javax.persistence.NoResultException e) {
                LOGGER.info("Email not found: " + email);
                showAlert(Alert.AlertType.ERROR, "Error", "This email is not registered in our system.");
            } catch (Exception e) {
                //LOGGER.sever("Error in password reset process", e);
                showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.");
            }
        }
    }

    private void handleVerifyCode() {
        String codeVerification = verificationCodeField.getText().trim();
        resetUserPass.setVerifcationCode(codeVerification);
        try {
            System.out.println(resetUserPass.toString());
            User userIsCorrectCode = SignableFactory.getSignable().verifyCode(resetUserPass, User.class);
            System.out.println("-------------------------" + userIsCorrectCode.isActivo());
            if (userIsCorrectCode.isActivo()) {
                LOGGER.info("Verification successful.");
                passwordSection.setVisible(true);
                showAlert(Alert.AlertType.INFORMATION, "Code Verified", "You can now set a new password.");
            } else {
                LOGGER.warning("Verification failed.");
                showAlert(Alert.AlertType.ERROR, "Verification Failed", "The code you entered is incorrect.");
            }
        } catch (SelectException ex) {
            LOGGER.warning("Verification failed.");
            // showAlert(Alert.AlertType.ERROR, "Verification Failed", "The code you entered is incorrect.");
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
            // to add  crypto her
            // Call ClientSideEncryption to encrypt the message
            byte[] encryptedData = null;
            try {
                encryptedData = ClientSideEncryption.encrypt(newPassword);
            } catch (Exception ex) {
                Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Convert to Base64 for easy printing (optional)
            String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedData);
            resetUserPass.setPassword(encryptedBase64);
            //resetUserPass.setPassword(newPassword);
            try {
                SignableFactory.getSignable().updatePassword(resetUserPass);
            } catch (UpdateException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

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
