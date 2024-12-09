/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Borja
 */


public class RutaController {

    
    @FXML
    public void initialize() {
    }
    
         @FXML
    private void handleLogOutClick(MouseEvent event) {
        // Aquí puedes implementar lo que quieres que pase cuando se haga clic en el logOut
        System.out.println("Clic en el LogOut!");
        
        // Ejemplo: cerrar la ventana o hacer logout
        // Stage stage = (Stage) logOut.getScene().getWindow();
        // stage.close();
    }
    
}
