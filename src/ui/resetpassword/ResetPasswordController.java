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
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import models.User;
import ui.login.LogInController;
import ui.signup.SignUpController;

/**
 * Controller class for handling password reset functionality. This class
 * manages the password reset process including email verification, code
 * verification, and password update operations.
 */
public class ResetPasswordController {

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger(ResetPasswordController.class.getName());

    /**
     * The primary stage for the password reset window
     */
    private Stage stage;

    /**
     * Container for password-related input fields
     */
    @FXML
    private VBox passwordSection;

    /**
     * Field for entering email address
     */
    @FXML
    private JFXTextField emailField;

    /**
     * Field for entering verification code
     */
    @FXML
    private JFXTextField verificationCodeField;

    /**
     * Field for entering new password (hidden)
     */
    @FXML
    private JFXPasswordField newPasswordField;

    /**
     * Field for repeating new password (hidden)
     */
    @FXML
    private JFXPasswordField repeatPasswordField;

    /**
     * Button to trigger verification code sending
     */
    @FXML
    private JFXButton sendCodeBtn;

    /**
     * Button to verify entered code
     */
    @FXML
    private JFXButton verifyCodeBtn;

    /**
     * Button to toggle password visibility
     */
    @FXML
    private JFXButton showPasswordBtn;

    /**
     * Button to toggle repeated password visibility
     */
    @FXML
    private JFXButton showRepeatedPasswordBtn;

    /**
     * Button to confirm password update
     */
    @FXML
    private JFXButton updatePasswordBtn;

    /**
     * Field for showing password in plain text
     */
    @FXML
    private JFXTextField newPassTxt;

    /**
     * Field for showing repeated password in plain text
     */
    @FXML
    private JFXTextField newPassTxtRepeat;

    /**
     * Icon for repeated password visibility toggle
     */
    @FXML
    private ImageView repeatEyeImageView;

    /**
     * Icon for password visibility toggle
     */
    @FXML
    private ImageView eyeImageView;

    /**
     * User object for password reset operation
     */
    private User resetUserPass;

    /**
     * Flag indicating if password is visible
     */
    private boolean isPasswordVisible = false;

    /**
     * Flag indicating if repeated password is visible
     */
    private boolean isPasswordVisibleR = false;

    /**
     * Image for closed eye icon
     */
    private final Image eyeClosed = new Image(getClass().getResourceAsStream("/image/eye-solid.png"));

    /**
     * Image for open eye icon
     */
    private final Image eyeOpen = new Image(getClass().getResourceAsStream("/image/eye-slash-solid.png"));

    /**
     * Sets the stage for this controller.
     *
     * @param stage JavaFX stage to be used for this window
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the reset password window and sets up all necessary event
     * handlers. This method configures the window properties, initializes UI
     * components, and sets up event handling for all interactive elements.
     *
     * @param root The root node of the scene graph
     */
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
      LOGGER.info("Controller initialized successfully.");
        // Verify Button Event
        verifyCodeBtn.setOnAction(event -> handleVerifyCode());

        updatePasswordBtn.setOnAction(event -> handleUpdatePassword());
        // Set initial visibility of password fields
        newPassTxt.setVisible(false);
        newPassTxtRepeat.setVisible(false);

        showPasswordBtn.setOnAction(this::handlePasswordBtnShowHideAction);
        showRepeatedPasswordBtn.setOnAction(this::handlePasswordBtnRepeatShowHideAction);
        stage.show();
    }

    /**
     * Handles the show/hide password functionality for the new password field.
     * Toggles between showing the password as plain text or hidden characters.
     *
     * @param event The action event triggered by the show/hide password button
     */
    private void handlePasswordBtnShowHideAction(ActionEvent event) {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            eyeImageView.setImage(new Image(getClass().getResourceAsStream("/image/eye-slash-solid.png")));
            newPasswordField.setVisible(false);
            newPassTxt.setVisible(true);
            newPassTxt.setText(newPasswordField.getText());
        } else {
            eyeImageView.setImage(new Image(getClass().getResourceAsStream("/image/eye-solid.png")));
            newPasswordField.setVisible(true);
            newPassTxt.setVisible(false);
            newPasswordField.setText(newPassTxt.getText());
        }
    }

    /**
     * Handles the show/hide password functionality for the repeated password
     * field. Toggles between showing the password as plain text or hidden
     * characters.
     *
     * @param event The action event triggered by the show/hide password button
     */
    private void handlePasswordBtnRepeatShowHideAction(ActionEvent event) {
        isPasswordVisibleR = !isPasswordVisibleR;
        if (isPasswordVisibleR) {
            repeatEyeImageView.setImage(new Image(getClass().getResourceAsStream("/image/eye-slash-solid.png")));
            repeatPasswordField.setVisible(false);
            newPassTxtRepeat.setVisible(true);
            newPassTxtRepeat.setText(repeatPasswordField.getText());
        } else {
            repeatEyeImageView.setImage(new Image(getClass().getResourceAsStream("/image/eye-solid.png")));
            repeatPasswordField.setVisible(true);
            newPassTxtRepeat.setVisible(false);
            repeatPasswordField.setText(newPassTxtRepeat.getText());
        }
    }

    /**
     * Handles the process of sending a verification code to the user's email.
     * Validates the email format and existence in the system before sending the
     * code.
     */
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

    /**
     * Handles the verification of the code entered by the user. Encrypts the
     * verification code and validates it against the server.
     */
    private void handleVerifyCode() {
        String codeVerification = verificationCodeField.getText().trim();

        String encryptedcodeVerification = null;
        try {
            //  User user = new User(email, password, name, isActive, street, city, zip);
            // Call ClientSideEncryption to encrypt the message
            LOGGER.log(Level.INFO, "Verification code before encrypting : " + codeVerification);
            encryptedcodeVerification = ClientSideEncryption.encrypt(codeVerification);
            LOGGER.log(Level.INFO, "Verification code after encrypting : " + encryptedcodeVerification);
            resetUserPass.setVerifcationCode(encryptedcodeVerification);

        } catch (Exception ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            LOGGER.info(resetUserPass.toString());
            User userIsCorrectCode = SignableFactory.getSignable().verifyCode(resetUserPass, User.class);
             LOGGER.info("-------------------------" + userIsCorrectCode.isActivo());
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

    /**
     * Handles the password update process. Validates password matching,
     * encrypts the new password, and updates it in the system.
     */
    private void handleUpdatePassword() {
        // Extract passwords only from visible fields
        String newPassword = newPasswordField.isVisible() ? newPasswordField.getText() : repeatPasswordField.getText();
        String repeatPassword = repeatPasswordField.isVisible() ? repeatPasswordField.getText() : newPasswordField.getText();

        // Validate only the visible fields
        if ((newPasswordField.isVisible() && newPassword.isEmpty())
                        || (repeatPasswordField.isVisible() && repeatPassword.isEmpty())) {
            showAlert(Alert.AlertType.ERROR, "Empty Fields", "Both password fields must be filled.");
            return;
        }

        // Check if passwords match
        if (!newPassword.equals(repeatPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match. Please try again.");
            return;
        }

        LOGGER.info("Password updated successfully.");
        showAlert(Alert.AlertType.INFORMATION, "Success", "Your password has been updated successfully!");

        // Encrypt password
        String encryptedPassword = null;
        try {
            String mPassword = newPasswordField.isVisible() ? newPasswordField.getText() : repeatPasswordField.getText();

            LOGGER.log(Level.INFO, "Password before encrypting: " + mPassword);
            encryptedPassword = ClientSideEncryption.encrypt(mPassword);
            LOGGER.log(Level.INFO, "Password after encrypting: " + encryptedPassword);

            resetUserPass.setPassword(encryptedPassword);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Encryption error", ex);
            return;
        }

        // Update password
        try {
            SignableFactory.getSignable().updatePassword(resetUserPass);
        } catch (UpdateException ex) {
            LOGGER.log(Level.SEVERE, "Password update failed", ex);
            return;
        }

        stage.close();
    }

    /**
     * Utility method to show alert dialogs to the user.
     *
     * @param type The type of alert to show (ERROR, INFORMATION, etc.)
     * @param title The title of the alert dialog
     * @param message The main message to display in the alert
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
