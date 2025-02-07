package utils;

import javafx.scene.Scene;
import java.util.prefs.Preferences;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * The ThemeManager class is responsible for managing the application's theme.
 * It allows switching between different themes (e.g., light and dark themes)
 * and applies the selected theme to all active scenes. The theme selection is
 * persisted across application restarts using Java's Preferences API.
 */
public class ThemeManager {
    // Preference key for storing theme selection

    private static final String PREF_KEY = "app_theme";
    // Singleton instance
    private static final ThemeManager instance = new ThemeManager();
    // Preferences object to save and load theme
    private final Preferences prefs;
    // The current theme to be applied
    private Theme currentTheme;

    // Use WeakHashMap to automatically remove scenes when windows are closed
    private final Map<Scene, Boolean> activeScenes = new WeakHashMap<>();

    /**
     * Enum representing the available themes in the application. Each theme has
     * a corresponding CSS file path.
     */
    public enum Theme {
        LIGHT("/style/style.css"), // Light theme CSS
        DARK("/style/dark-theme.css"); // Dark theme CSS
        // Path to the CSS file for the theme
        private final String cssPath;

        /**
         * Constructor to associate the CSS file path with the theme.
         *
         * @param cssPath The CSS file path for the theme.
         */
        Theme(String cssPath) {
            this.cssPath = cssPath;
        }

        /**
         * Returns the CSS file path for the theme.
         *
         * @return The path to the theme's CSS file.
         */
        public String getCssPath() {
            return cssPath;
        }
    }

    /**
     * Private constructor to initialize the ThemeManager instance and load the
     * saved theme. The default theme is set to LIGHT if no theme is found in
     * preferences.
     */
    private ThemeManager() {
        prefs = Preferences.userNodeForPackage(ThemeManager.class);
        // Retrieve saved theme or use default (LIGHT)
        String savedTheme = prefs.get(PREF_KEY, Theme.LIGHT.name());
        currentTheme = Theme.valueOf(savedTheme);
    }

    /**
     * Returns the singleton instance of the ThemeManager.
     *
     * @return The instance of the ThemeManager.
     */
    public static ThemeManager getInstance() {
        return instance;
    }

    /**
     * Applies the current theme to a new scene and starts tracking it. This
     * method should be called whenever a new scene is created to ensure the
     * correct theme is applied.
     *
     * @param scene The scene to which the theme will be applied.
     */
    public void applyTheme(Scene scene) {
        if (scene == null) {
            return;
        }

        // Add scene to active scenes
        activeScenes.put(scene, true);

        // Apply the current theme to the scene
        applyThemeToScene(scene);
    }

    /**
     * Changes the theme for all active scenes. Once the theme is changed, it is
     * applied to all scenes that are currently being tracked by the
     * ThemeManager.
     *
     * @param theme The new theme to apply.
     */
    public void setTheme(Theme theme) {
        currentTheme = theme;
        // Save the new theme to preferences
        prefs.put(PREF_KEY, theme.name());

        // Update all active scenes immediately with the new theme
        for (Scene scene : activeScenes.keySet()) {
            applyThemeToScene(scene);
        }
    }

    /**
     * Applies the current theme's styles to a specific scene. This method is
     * called whenever a scene is created or when the theme is changed.
     *
     * @param scene The scene to apply the theme to.
     */
    private void applyThemeToScene(Scene scene) {
        if (scene == null) {
            return;
        }

        scene.getStylesheets().clear(); // Clear any previously applied styles
        scene.getStylesheets().add(
                        getClass().getResource(currentTheme.getCssPath()).toExternalForm()
        ); // Add the current theme's stylesheet
    }

    /**
     * Returns the current theme being applied to the application.
     *
     * @return The current theme.
     */
    public Theme getCurrentTheme() {
        return currentTheme;
    }
}
