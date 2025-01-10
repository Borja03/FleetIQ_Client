package ui.signup;




import exception.ConnectionException;
import exception.EmptyFieldException;
import exception.InvalidCityFormatException;
import exception.InvalidEmailFormatException;
import exception.InvalidPasswordFormatException;
import exception.InvalidStreetFormatException;
import exception.InvalidZipFormatException;
import exception.ServerErrorException;
import exception.UserAlreadyExistsException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;
import ui.login.LogInController;

/**
 * SignUpController handles the user interactions for the sign-up window. It
 * manages the UI components and their associated actions.
 * Author: Adrian y Omar
 */
        

public class SignUpController {

    // Logger for logging events
    private static final Logger LOGGER = Logger.getLogger(SignUpController.class.getName());

    // UI Components
    @FXML
    private Button btn_show_password; // Button to toggle password visibility

    @FXML
    private Button btn_signup; // Button to submit the sign-up form

    @FXML
    private CheckBox chb_active; // Checkbox to indicate if the user is active

    @FXML
    private Hyperlink hl_login; // Hyperlink to navigate to the login screen

    @FXML
    private ImageView imgCity; // Image view for the city input field

    @FXML
    private ImageView imgEmail; // Image view for the email input field

    @FXML
    private ImageView imgKey; // Image view for the password input field

    @FXML
    private ImageView imgLock; // Image view for the password lock icon

    @FXML
    private ImageView imgShowPassword; // Image view for showing password visibility

    @FXML
    private ImageView imgStreet; // Image view for the street input field

    @FXML
    private ImageView imgUser; // Image view for the user name input field

    @FXML
    private ImageView imgZIP; // Image view for the ZIP code input field

    @FXML
    private Label lbl_error; // Label to display error messages

    @FXML
    private PasswordField pf_password; // Password field for user password

    @FXML
    private PasswordField pf_password_confirm; // Password field for confirming user password

    @FXML
    private TextField tf_city; // Text field for user city input

    @FXML
    private TextField tf_email; // Text field for user email input

    @FXML
    private TextField tf_name; // Text field for user name input

    @FXML
    private TextField tf_password; // Text field for user password input

    @FXML
    private TextField tf_password_confirm; // Text field for confirming user password input

    @FXML
    private TextField tf_street; // Text field for user street input

    @FXML
    private TextField tf_zip; // Text field for user ZIP code input

    @FXML
    private VBox vbx_card; // Vertical box layout for UI components

    private Stage stage; // Current stage for the sign-up window

    private boolean isPasswordVisible = false; // Flag to check if the password is visible

    /**
     * Gets the current stage of the SignUpController.
     *
     * @return the current stage
     */
    public Stage getStage() {
        return stage;
    }

   
    /**
     * Sets the current stage of the SignUpController.
     *
     * @param stage the stage to be set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private ContextMenu contextMenu; // Context menu for additional actions
   // private String currentTheme = "light"; // Current theme of the application

    /**
     * Initializes the sign-up stage with the given root layout.
     *
     * @param root the parent layout for the sign-up window
     */
    
    public void initStage(Parent root) {
        LOGGER.info("Initialising Sign Up window.");

        Scene scene = new Scene(root);
        // Set the stage properties
        stage.setScene(scene);
        stage.setTitle("Sign Up");
        stage.setResizable(false);
        // stage.initModality(Modality.APPLICATION_MODAL);
        stage.centerOnScreen();

        stage.getIcons().add(new Image("/image/fleet_icon.png"));
   
        
        tf_password.setVisible(false);
        tf_password_confirm.setVisible(false);

        btn_signup.setOnAction(this::handleSignUpButtonAction);
        hl_login.setOnAction(this::handleLoginHyperlinkAction);
        btn_show_password.setOnAction(this::handlePasswordImageButtonAction);
        stage.setOnCloseRequest(this::handleOnActionExit);

        // changes for exam
             
        // active chackbox
        chb_active.setSelected(true);
        // listener for focus
        tf_email.focusedProperty().addListener(this::focusChanged);
        pf_password.focusedProperty().addListener(this::focusChanged);
        tf_password.focusedProperty().addListener(this::focusChanged);
        pf_password_confirm.focusedProperty().addListener(this::focusChanged);
        tf_password_confirm.focusedProperty().addListener(this::focusChanged);
        tf_name.focusedProperty().addListener(this::focusChanged);
        tf_street.focusedProperty().addListener(this::focusChanged);
        tf_city.focusedProperty().addListener(this::focusChanged);
        tf_zip.focusedProperty().addListener(this::focusChanged);

      // Initialize context menu
        initializeContextMenu();

        // Add context menu to the scene
        root.setOnContextMenuRequested(this::showContextMenu);

        // Load default theme
        //currentTheme = loadThemePreference();
        loadTheme(LogInController.currentTheme);
        LOGGER.info("Window opened.");

        // Show the stage
        stage.show();
    }

    /**
     * Initializes and manages the context menu for the SignUpController.
     */
    private void initializeContextMenu() {
        contextMenu = new ContextMenu();

        MenuItem lightMode = new MenuItem("Light Mode");
        MenuItem darkMode = new MenuItem("Dark Mode");
        MenuItem clearFields = new MenuItem("Clear Fields");

        lightMode.setOnAction(e -> switchTheme("light"));
        darkMode.setOnAction(e -> switchTheme("dark"));
        clearFields.setOnAction(e -> clearAllFields());

        contextMenu.getItems().addAll(lightMode, darkMode, clearFields);
    }

    /**
     * Displays the context menu at the specified screen coordinates.
     *
     * @param event the context menu event containing the screen coordinates
     */
    private void showContextMenu(ContextMenuEvent event) {
        contextMenu.show(vbx_card, event.getScreenX(), event.getScreenY());
    }

    /**
     * Saves the user's selected theme preference to a properties file.
     *
     * @param theme the theme to be saved
     */
   private void saveThemePreference(String theme) {
    try {
        Properties props = new Properties();
        props.setProperty("theme", theme);
        props.store(new FileOutputStream("src/config/config_theme.properties"), "Theme Settings");
    } catch (IOException e) {
      LOGGER.severe("Error saving theme preference: " + e.getMessage());
    }
}

    /**
     * Loads the user's theme preference from a properties file.
     *
     * @return the loaded theme preference, defaults to "light" if not found
     */


/**
 * Loads the saved theme preference from a configuration file using ResourceBundle.
 * If no preference is found, returns the default theme "light".
 *
 * @return the saved theme preference, or "light" if no preference is found
 */
private String loadThemePreference() {
    try {
        ResourceBundle bundle = ResourceBundle.getBundle("config/config_theme");
        return bundle.getString("theme");
    } catch (Exception e) {
        // Log an error message if loading the theme preference fails
    }

    return "light";
}
    /**
     * Switches the application theme and saves the preference.
     *
     * @param theme the new theme to be applied
     */
    private void switchTheme(String theme) {
        LogInController.currentTheme = theme;
        loadTheme(LogInController.currentTheme);
        saveThemePreference(LogInController.currentTheme);
    }

    /**
     * Loads the specified theme by applying the appropriate styles and images.
     *
     * @param theme the theme to be loaded
     */
    private void loadTheme(String theme) {
        Scene scene = stage.getScene();
        scene.getStylesheets().clear();

        if (theme.equals("dark")) {
            // Code for dark theme
            String cssFile = "/style/dark-styles.css";
            scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
            imgEmail.setImage(new Image(getClass().getResourceAsStream("/image/envelope-solid-white.png")));
            imgLock.setImage(new Image(getClass().getResourceAsStream("/image/lock-solid-white.png")));
            imgKey.setImage(new Image(getClass().getResourceAsStream("/image/key-solid-white.png")));
            imgUser.setImage(new Image(getClass().getResourceAsStream("/image/user-solid-white.png")));
            imgStreet.setImage(new Image(getClass().getResourceAsStream("/image/location-dot-solid-white.png")));
            imgCity.setImage(new Image(getClass().getResourceAsStream("/image/city-solid-white.png")));
            imgZIP.setImage(new Image(getClass().getResourceAsStream("/image/imgZIP-white.png")));
            contextMenu.getStyleClass().add("context-menu-dark");

            // Additional actions for dark theme
        } else if (theme.equals("light")) {
            // Code for light theme
            String cssFile = "/style/CSSglobal.css";
            scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
            imgEmail.setImage(new Image(getClass().getResourceAsStream("/image/envelope-solid.png")));
            imgLock.setImage(new Image(getClass().getResourceAsStream("/image/lock-solid.png")));
            imgKey.setImage(new Image(getClass().getResourceAsStream("/image/key-solid.png")));
            imgUser.setImage(new Image(getClass().getResourceAsStream("/image/user-solid.png")));
            imgStreet.setImage(new Image(getClass().getResourceAsStream("/image/location-dot-solid.png")));
            imgCity.setImage(new Image(getClass().getResourceAsStream("/image/city-solid.png")));
            imgZIP.setImage(new Image(getClass().getResourceAsStream("/image/imgZIP.png")));
            contextMenu.getStyleClass().remove("context-menu-dark");

            // Additional actions for light theme
        }
    }

    /**
     * Clears all input fields in the sign-up form and resets the error label.
     */
    private void clearAllFields() {
        LOGGER.info("Clearing all input fields.");
        tf_email.clear();
        pf_password.clear();
        tf_password.clear();
        pf_password_confirm.clear();
        tf_password_confirm.clear();
        tf_name.clear();
        tf_street.clear();
        tf_city.clear();
        tf_zip.clear();
        chb_active.setSelected(true);
        lbl_error.setText("");
    }

    /**
     * Handles the action of showing or hiding the password input fields.
     *
     * @param event the action event triggered by the password visibility button
     */
    private void handlePasswordImageButtonAction(ActionEvent event) {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            imgShowPassword.setImage(new Image(getClass().getResourceAsStream("/image/eye-slash-solid.png")));
            pf_password.setVisible(false);
            tf_password.setVisible(true);
            tf_password.setText(pf_password.getText());
            pf_password_confirm.setVisible(false);
            tf_password_confirm.setVisible(true);
            tf_password_confirm.setText(pf_password_confirm.getText());
        } else {
            imgShowPassword.setImage(new Image(getClass().getResourceAsStream("/image/eye-solid.png")));
            pf_password.setVisible(true);
            tf_password.setVisible(false);
            pf_password.setText(tf_password.getText());
            pf_password_confirm.setVisible(true);
            tf_password_confirm.setVisible(false);
            pf_password_confirm.setText(tf_password_confirm.getText());
        }
    }
    
    //
    private void focusChanged(ObservableValue observable ,Boolean oldValue ,Boolean newValue){
      if(newValue){
       lbl_error.setText("");

      }  
    }

    /**
     * Handles the action of the sign-up button, validating input fields and
     * performing the sign-up process.
     *
     * @param event the action event triggered by the sign-up button
     */
    private void handleSignUpButtonAction(ActionEvent event) {
        try {
            String password;
            String confirmPassword;
            String email = tf_email.getText();

            // Check which password fields are visible and use them
            if (pf_password.isVisible()) {
                password = pf_password.getText();
                confirmPassword = pf_password_confirm.getText();
            } else {
                password = tf_password.getText();
                confirmPassword = tf_password_confirm.getText();
            }

            String name = tf_name.getText();
            String street = tf_street.getText();
            String city = tf_city.getText();
            String zip = tf_zip.getText();
            boolean isActive = chb_active.isSelected();

            // Clear previous error messages            
            lbl_error.setText("");

            // Validate inputs
            validateInputs(email, password, confirmPassword, name, street, city, zip);

            // Proceed with sign-up logic
            performSignUp(email, password, name, 1, street, city, Integer.parseInt(zip), isActive);
            LOGGER.info("Performing signup");
        } catch (Exception e) {
            lbl_error.setText(e.getMessage());
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }

    /**
     * Validates the provided input fields for empty values and correct formats.
     *
     * @param email the email to validate
     * @param password the password to validate
     * @param confirmPassword the password confirmation to validate
     * @param name the name to validate
     * @param street the street to validate
     * @param city the city to validate
     * @param zip the zip code to validate
     * @throws EmptyFieldException if any required field is empty
     * @throws InvalidEmailFormatException if the email format is invalid
     * @throws InvalidPasswordFormatException if the password format is invalid
     * @throws InvalidCityFormatException if the city format is invalid
     * @throws InvalidZipFormatException if the zip code format is invalid
     * @throws InvalidStreetFormatException if the street format is invalid
     */
    private void validateInputs(String email, String password, String confirmPassword, String name, String street, String city, String zip)
                    throws EmptyFieldException, InvalidEmailFormatException, InvalidPasswordFormatException,
                    InvalidCityFormatException, InvalidZipFormatException, InvalidStreetFormatException {

        // Check empty fields
        checkEmptyFields(email, password, confirmPassword, name, street, city, zip);

        // Check fields format 
        checkFieldsFormat(email, password, confirmPassword, name, street, city, zip);
    }

    /**
     * Checks for empty fields among the provided input values.
     *
     * @param email the email to check
     * @param password the password to check
     * @param confirmPassword the password confirmation to check
     * @param name the name to check
     * @param street the street to check
     * @param city the city to check
     * @param zip the zip code to check
     * @throws EmptyFieldException if any field is empty
     */
    private void checkEmptyFields(String email, String password, String confirmPassword, String name, String street, String city, String zip) throws EmptyFieldException {
        // Validate email
        if (email == null || email.isEmpty()) {
            throw new EmptyFieldException("Email cannot be empty.");
        }
        // Validate password
        if (password == null || password.isEmpty()) {
            throw new EmptyFieldException("Password cannot be empty.");
        }
        // Validate password confirmation
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            throw new EmptyFieldException("Password confirmation cannot be empty.");
        }
        // Validate name
        if (name == null || name.isEmpty()) {
            throw new EmptyFieldException("Name cannot be empty.");
        }
        // Validate street
        if (street == null || street.isEmpty()) {
            throw new EmptyFieldException("Street cannot be empty.");
        }
        // Validate city
        if (city == null || city.isEmpty()) {
            throw new EmptyFieldException("City cannot be empty.");
        }
        // Validate zip
        if (zip == null || zip.isEmpty()) {
            throw new EmptyFieldException("Zip code cannot be empty.");
        }
    }

    /**
     * Checks the format of the provided input fields.
     *
     * @param email the email to check
     * @param password the password to check
     * @param confirmPassword the password confirmation to check
     * @param name the name to check
     * @param street the street to check
     * @param city the city to check
     * @param zip the zip code to check
     * @throws InvalidEmailFormatException if the email format is invalid
     * @throws InvalidPasswordFormatException if the password format is invalid
     * @throws InvalidCityFormatException if the city format is invalid
     * @throws InvalidZipFormatException if the zip code format is invalid
     * @throws InvalidStreetFormatException if the street format is invalid
     */
    private void checkFieldsFormat(String email, String password, String confirmPassword, String name, String street, String city, String zip)
                    throws InvalidEmailFormatException, InvalidPasswordFormatException,
                    InvalidCityFormatException, InvalidZipFormatException, InvalidStreetFormatException {

        // Regex pattern for a valid email format
        String emailRegex = "^[a-zA-Z0-9._%+-]+@([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$";
        if (!email.matches(emailRegex)) {
            throw new InvalidEmailFormatException("Email must be in a valid format (e.g., example@domain.com).");
        }

        // Validate password format
        if (!validatePassword(password)) {
            throw new InvalidPasswordFormatException("Password must be at least 6 characters, with lowercase, uppercase, numbers, and special characters.");
        }

        // Validate password match
        if (!password.equals(confirmPassword)) {
            throw new InvalidPasswordFormatException("Passwords do not match.");
        }

        // Check that city only contains letters (basic validation)
        if (!city.matches("[a-zA-Z\\s]+")) {
            throw new InvalidCityFormatException("City must only contain letters.");
        }

        // Check that zip contains exactly 5 digits
        if (!zip.matches("\\d{5}")) {
            throw new InvalidZipFormatException("Zip code must be exactly 5 digits.");
        }
    }

    /**
     * Validates the given password based on certain criteria.
     *
     * @param password the password to validate.
     * @return true if the password is valid; false otherwise.
     */
    private boolean validatePassword(String password) {
        if (password.length() < 6) {
            LOGGER.warning("Password validation failed: Length is less than 6 characters.");
            return false;
        }

        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }

        boolean isValid = hasUppercase && hasDigit && hasSpecialChar;
        if (!isValid) {
            LOGGER.warning("Password validation failed: Missing required characters.");
        }
        return isValid;
    }

    /**
     * Performs the sign-up process for a new user.
     *
     * @param email the email address of the user.
     * @param password the password entered by the user.
     * @param name the name of the user.
     * @param companyID the company ID associated with the user.
     * @param street the street address of the user.
     * @param city the city of the user.
     * @param zip the zip code of the user.
     * @param isActive the active status of the user.
     */
    private void performSignUp(String email, String password, String name, int companyID, String street, String city, int zip, boolean isActive) {
        User user = new User(email, password, name, isActive, companyID, street, city, zip);

//        try {
//            // Attempting to sign up the user
//            User nuevoUser = SignableFactory.getSignable().signUp(user);
//            // If the sign-up is successful
//            if (nuevoUser != null) {
//                showAlert();
//                LOGGER.info("User signed up successfully: " + email);
//            }
//        } catch (UserAlreadyExistsException e) {
//            // Handle duplicate email error
//            utils.showAlert("Error", "Email already exists. Please use another email.");
//            LOGGER.warning("Email already exists: " + email);
//        } catch (ServerErrorException e) {
//            // Handle server error
//            utils.showAlert("Error", "Server is not available at the moment. Please try again later.");
//            LOGGER.warning("Server error occurred during sign-up: " + e.getMessage());
//        } catch (ConnectionException e) {
//            // Handle connection exceptions
//            utils.showAlert("Error", "Problemas de conexión a la base de datos.");
//            LOGGER.warning("Connection error during sign-up: " + e.getMessage());
//        } catch (Exception e) {
//            // Handle unexpected errors
//            utils.showAlert("Error", "An unexpected error occurred: " + e.getMessage());
//            LOGGER.log(Level.SEVERE, "Unexpected error in performSignUp", e);
//        }
    }

    /**
     * Handles the action of the login hyperlink.
     *
     * @param event the action event triggered by the hyperlink.
     */
    private void handleLoginHyperlinkAction(ActionEvent event) {
        navigateToScreen("/ui/login/LogIn.fxml", "LogIn");
    }

        private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign-up Successful");
        alert.setHeaderText(null);
        alert.setContentText("Your account has been created successfully!");

        // Handle alert button click
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Navigate to another screen after the user clicks OK
                navigateToScreen("/ui/login/LogIn.fxml", "LogIn");
            }
        });
    }
    /**
     * Navigates to a specified screen.
     *
     * @param fxmlPath the path to the FXML file of the screen to load.
     * @param title the title of the new screen.
     */
    private void navigateToScreen(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            // Get the current stage


                LogInController controller = loader.getController();
                Stage newStage = new Stage();
                controller.setStage(newStage);
                controller.initialize(root);


            stage = (Stage) btn_signup.getScene().getWindow();
            stage.close();
            LOGGER.info("Stage closed successfully for: " + title);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load " + title + " screen: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the exit action of the application.
     *
     * @param event the exit event triggered by the user.
     */
    private void handleOnActionExit(Event event) {
        try {
            // Ask user for confirmation on exit
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure you want to exit the application?",
                            ButtonType.OK, ButtonType.CANCEL);
                      // Load an icon image for the alert
           // Load a custom icon for the window (not the alert itself)
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        Image iconImage = new Image(getClass().getResourceAsStream("/image/fleet_icon.png"));
        stage.getIcons().add(iconImage);  // Set the icon for the window

        // Set the icon as the graphic for the alert
            Optional<ButtonType> result = alert.showAndWait();
            // If OK to exit
            if (result.isPresent() && result.get() == ButtonType.OK) {
                 saveThemePreference(LogInController.currentTheme);
                Platform.exit();
                LOGGER.info("Application exited by user.");
            } else {
                event.consume();
            }
        } catch (Exception e) {
            String errorMsg = "Error exiting application: " + e.getMessage();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                            errorMsg,
                            ButtonType.OK);
   
      
            alert.showAndWait();
            LOGGER.log(Level.SEVERE, errorMsg);
        }
    }

}