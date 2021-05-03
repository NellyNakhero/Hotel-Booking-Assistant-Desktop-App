/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booking.assistant.ui.AddRoom;

import booking.assistant.database.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Nelly
 */
public class AddRoom extends Application {

    public static void main(String[] args) {
         launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("AddRoom.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
        
        DatabaseHandler.getInstance();
    
    }
    
}
