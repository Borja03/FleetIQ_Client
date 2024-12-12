package ui.profile;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.User;
import ui.login.LogInController;
import utils.UtilsMethods;

/**
 * The MainController handles the main functionalities of the main screen of the
 * application. It includes actions for showing and hiding the password, as well
 * as logging out. This class is linked to the main FXML view where user
 * interactions are handled.
 *
 * <p>
 * This controller allows the user to view the password in plain text, hide the
 * password, and log out to the login screen.</p>
 *
 * @author Borja
 */
public class MainController {

    // FXML components to display user details
    @FXML
    private TextField emailField;  // TextField to display user's email

    @FXML
    private TextField nameField;   // TextField to display user's name

    @FXML
    private TextField addressField; // TextField to display user's address

    // Password-related fields
    @FXML
    private PasswordField passwordField; // The masked password input

    @FXML
    private TextField plainPasswordField; // The visible plain text input (used when the password is shown)

    @FXML
    private Button logOutButton; // LogOut button

    @FXML
    private Button eyeButton; // Eye button for showing/hiding password

    @FXML
    private ImageView eyeImageView; // Eye icon for password visibility

    @FXML
    private Pane mainPane;

    // Image paths for the eye icons
    private final Image eyeClosed = new Image(getClass().getResourceAsStream("/image/eye-solid.png"));
    private final Image eyeOpen = new Image(getClass().getResourceAsStream("/image/eye-slash-solid.png"));

    private boolean passwordIsVisible = false;

    // Context menus for copying selected text
    private ContextMenu contextMenu;

    // Stage to manage the current window
    private Stage stage;

    // Getter and Setter for Stage
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static final Logger logger = Logger.getLogger(MainController.class.getName());

    /**
     * Initializes the controller with the provided root element and the user
     * data.
     *
     * @param root The root element of the scene.
     * @param user The user object containing the user's details.
     */
    public void initStage(Parent root, User user) {
        logger.info("Initializing MainController stage.");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Main");
        stage.setResizable(false);
       // stage.getIcons().add(new Image("/Images/userIcon.png"));

        // Set initial visibility of password fields
        passwordField.setVisible(true);
        plainPasswordField.setVisible(false);
        eyeImageView.setImage(eyeClosed);

        // Initialize the context menu for copy functionality
        createContextMenu();

        // Link event handlers
        logOutButton.setOnAction(this::logOut);
        eyeButton.setOnAction(this::togglePasswordVisibility);

        // Set user details in text fields
        emailField.setText(user.getEmail());
        nameField.setText(user.getName());

        // Concatenate street, city, and zip for the address field
        String address = String.format("%s, %s, %d", user.getStreet(), user.getCity(), user.getZip());
        addressField.setText(address);

        // Set the user's password (ensure that this is a secure way of handling passwords)
        passwordField.setText(user.getPassword()); // Asegúrate de que tengas acceso a la contraseña aquí

        // menu
        // Initialize context menu
        initializeContextMenu();

        // Add context menu to the scene
        root.setOnContextMenuRequested(this::showContextMenu);

        // Load default theme
        //currentTheme = loadThemePreference();
        loadTheme(LogInController.currentTheme);

        logger.info("MainController initialized.");
        stage.show();
    }

    /**
     * Creates a context menu with a copy option for selected text.
     */
    private void createContextMenu() {
        contextMenu = new ContextMenu();
        MenuItem copyItem = new MenuItem("Copy");
        MenuItem lightMode = new MenuItem("Light Mode");
        MenuItem darkMode = new MenuItem("Dark Mode");

        lightMode.setOnAction(e -> switchTheme("light"));
        darkMode.setOnAction(e -> switchTheme("dark"));

        // Set action for the copy menu item
        copyItem.setOnAction(this::handleCopyAction);

        // Add the copy option to the context menu
        contextMenu.getItems().add(copyItem);

        // Attach the context menu to relevant text fields
        attachContextMenuToTextField(passwordField);
        attachContextMenuToTextField(plainPasswordField);
    }

    /**
     * Displays the context menu at the specified screen coordinates where the
     * event occurred.
     *
     * @param event the event containing the screen coordinates to display the
     * context menu
     */
    private void showContextMenu(ContextMenuEvent event) {
        contextMenu.show(mainPane, event.getScreenX(), event.getScreenY());
    }

    /**
     * Saves the specified theme preference to a configuration file.
     *
     * @param theme the theme preference to save
     */

private void saveThemePreference(String theme) {
    try {
        Properties props = new Properties();
        props.setProperty("theme", theme);
        props.store(new FileOutputStream("src/config/config_theme.properties"), "Theme Settings");
    } catch (IOException e) {
        logger.severe("Error saving theme preference: " + e.getMessage());
    }
}
    /**
     * Loads the saved theme preference from a configuration file. If no
     * preference is found, returns the default theme "light".
     *
     * @return the saved theme preference, or "light" if no preference is found
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
     * Switches to the specified theme by loading it and saving the preference.
     *
     * @param theme the theme to switch to
     */
    private void switchTheme(String theme) {
        // Update the current theme to the specified theme
        LogInController.currentTheme = theme;

        // Load the specified theme settings into the application
        loadTheme(LogInController.currentTheme);

        // Save the specified theme preference to the configuration file
        saveThemePreference(LogInController.currentTheme);
    }

    /**
     * Loads the specified theme by applying the corresponding CSS stylesheet
     * and adjusting style settings for the context menu.
     *
     * @param theme the theme to load, either "dark" or "light"
     */
    private void loadTheme(String theme) {
        // Retrieve the current scene from the stage
        Scene scene = stage.getScene();

        // Clear any existing stylesheets from the scene
        scene.getStylesheets().clear();

        // Check if the specified theme is "dark"
        if (theme.equals("dark")) {
            // Define the CSS file for the dark theme
            String cssFile = "/style/dark-styles.css";

            // Add the dark theme stylesheet to the scene
            scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());

            // Apply dark styling to the context menu
            contextMenu.getStyleClass().add("context-menu-dark");

            // Add any other dark theme-specific actions here
        } // Check if the specified theme is "light"
        else if (theme.equals("light")) {
            // Define the CSS file for the light theme
            String cssFile = "/style/CSSglobal.css";

            // Add the light theme stylesheet to the scene
            scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());

            // Remove dark styling from the context menu
            contextMenu.getStyleClass().remove("context-menu-dark");

            // Add any other light theme-specific actions here
        }
    }

    /**
     * Handles the copy action from the context menu.
     *
     * @param event The action event triggered by the context menu.
     */
    private void handleCopyAction(ActionEvent event) {
        copySelectedText();
    }

    /**
     * Attaches the context menu to a TextField or PasswordField.
     *
     * @param textField The text field to which the context menu will be
     * attached.
     */
    private void attachContextMenuToTextField(TextField textField) {
        textField.setContextMenu(contextMenu);
    }

    //menu and theme
    /**
     * Initializes the context menu with options to switch between light and
     * dark themes.
     */
    private void initializeContextMenu() {
        // Create a new ContextMenu instance
        contextMenu = new ContextMenu();

        // Create menu items for light and dark modes
        MenuItem lightMode = new MenuItem("Light Mode");
        MenuItem darkMode = new MenuItem("Dark Mode");

        // Set the action for light mode to switch to the light theme
        lightMode.setOnAction(e -> switchTheme("light"));

        // Set the action for dark mode to switch to the dark theme
        darkMode.setOnAction(e -> switchTheme("dark"));

        // Add the menu items to the context menu
        contextMenu.getItems().addAll(lightMode, darkMode);
    }

    /**
     * Copies the selected text from the visible password field to the system
     * clipboard, if any text is selected.
     */
    private void copySelectedText() {
        // Initialize a variable to hold the selected text
        String selectedText = "";

        // Check if the password field is visible and has selected text
        if (passwordField.isVisible() && passwordField.getSelectedText() != null) {
            // Retrieve the selected text from the password field
            selectedText = passwordField.getSelectedText();
        } // Check if the plain password field is visible and has selected text
        else if (plainPasswordField.isVisible() && plainPasswordField.getSelectedText() != null) {
            // Retrieve the selected text from the plain password field
            selectedText = plainPasswordField.getSelectedText();
        }

        // If there is selected text, copy it to the clipboard
        if (!selectedText.isEmpty()) {
            // Get the system clipboard
            Clipboard clipboard = Clipboard.getSystemClipboard();

            // Create a ClipboardContent object to hold the selected text
            ClipboardContent content = new ClipboardContent();

            // Set the string content for the clipboard
            content.putString(selectedText);

            // Set the clipboard content to the selected text
            clipboard.setContent(content);
        }
    }

    /**
     * Handles the log out action and navigates to the login screen.
     *
     * @param event The action event triggered by the log out button.
     */
    @FXML
    private void logOut(ActionEvent event) {
        navigateToScreen("/ui/login/LogIn.fxml", "LogIn");
    }

    /**
     * Toggles the password visibility between masked and plain text.
     *
     * @param event The action event triggered by the eye button.
     */
    @FXML
    private void togglePasswordVisibility(ActionEvent event) {
    // Check if the password is currently visible
    if (passwordIsVisible) {
        // Show the password field (masked) and hide the plain password field
        passwordField.setVisible(true);
        plainPasswordField.setVisible(false);
        
        // Change the eye icon to indicate the password is hidden
        eyeImageView.setImage(eyeClosed);
    } else {
        // Hide the password field (masked) and show the plain password field
        passwordField.setVisible(false);
        plainPasswordField.setVisible(true);
        
        // Set the plain password field to show the actual password text
        plainPasswordField.setText(passwordField.getText()); // Show the real password
        
        // Change the eye icon to indicate the password is visible
        eyeImageView.setImage(eyeOpen);
    }
    
    // Toggle the visibility state for the next action
    passwordIsVisible = !passwordIsVisible;
}

    /**
     * Navigates to a different screen based on the provided FXML path and
     * title.
     *
     * @param fxmlPath The path to the FXML file for the new screen.
     * @param windowTitle The title for the new window.
     */
    private void navigateToScreen(String fxmlPath, String windowTitle) {
    try {
        // Load the FXML file using the FXMLLoader
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        
        // Create the root node from the loaded FXML
        Parent root = loader.load();
        
        // Get the controller associated with the loaded FXML
        LogInController controller = loader.getController();
        
        // Close the current stage (window)
        stage.close();
        
        // Create a new stage (window) for the next screen
        Stage newStage = new Stage();
        
        // Pass the new stage to the controller for further initialization
        controller.setStage(newStage);
        
        // Initialize the controller with the loaded root node
        controller.initialize(root);
    } catch (Exception e) {
        // Log an error if the screen fails to load
        logger.log(Level.SEVERE, "Failed to load {0} screen: " + e.getMessage(), windowTitle);
    }
}

    /**
     * Handles the exit action with confirmation dialog.
     *
     * @param event The event triggered when closing the stage.
     */
   private void handleOnActionExit(Event event) {
    try {
        // Create a confirmation alert to ask the user if they want to exit
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to exit the application?",
                ButtonType.OK, ButtonType.CANCEL);
        
        // Show the alert and wait for the user's response
        Optional<ButtonType> result = alert.showAndWait();
        
        // Check if the user confirmed the exit action
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // If confirmed, exit the application
                    saveThemePreference(LogInController.currentTheme);

            Platform.exit();
        } else {
            // If not confirmed, consume the event to prevent exit
            event.consume();
        }
    } catch (Exception e) {
        // If an error occurs while attempting to exit, show an error alert
        String errorMsg = "Error exiting application: " + e.getMessage();
        Alert alert = new Alert(Alert.AlertType.ERROR,
                errorMsg,
                ButtonType.OK);
        
        // Display the error alert
        alert.showAndWait();
    }
}
}

