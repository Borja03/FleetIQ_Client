/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.util.prefs.Preferences;
import java.util.List;
import java.util.ArrayList;


import javafx.scene.Scene;
import java.util.prefs.Preferences;
import java.util.WeakHashMap;
import java.util.Map;

public class ThemeManager {
    private static final String PREF_KEY = "app_theme";
    private static final ThemeManager instance = new ThemeManager();
    private final Preferences prefs;
    private Theme currentTheme;
    
    // Use WeakHashMap to automatically remove scenes when windows are closed
    private final Map<Scene, Boolean> activeScenes = new WeakHashMap<>();
    
    public enum Theme {
        LIGHT("/style/style.css"),
        DARK("/style/dark-theme.css");
        
        private final String cssPath;
        
        Theme(String cssPath) {
            this.cssPath = cssPath;
        }
        
        public String getCssPath() {
            return cssPath;
        }
    }
    
    private ThemeManager() {
        prefs = Preferences.userNodeForPackage(ThemeManager.class);
        String savedTheme = prefs.get(PREF_KEY, Theme.LIGHT.name());
        currentTheme = Theme.valueOf(savedTheme);
    }
    
    public static ThemeManager getInstance() {
        return instance;
    }
    
    // Apply theme to a new scene and start tracking it
    public void applyTheme(Scene scene) {
        if (scene == null) return;
        
        // Add scene to active scenes
        activeScenes.put(scene, true);
        
        // Apply current theme
        applyThemeToScene(scene);
    }
    
    // Change theme for all active scenes
    public void setTheme(Theme theme) {
        currentTheme = theme;
        // Save to preferences
        prefs.put(PREF_KEY, theme.name());
        
        // Update all active scenes immediately
        for (Scene scene : activeScenes.keySet()) {
            applyThemeToScene(scene);
        }
    }
    
    private void applyThemeToScene(Scene scene) {
        if (scene == null) return;
        
        scene.getStylesheets().clear();
        scene.getStylesheets().add(
            getClass().getResource(currentTheme.getCssPath()).toExternalForm()
        );
    }
    
    public Theme getCurrentTheme() {
        return currentTheme;
    }
}