package application;
//required imports
import application.user;


import java.io.IOException;

import javafx.application.Application;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;




/*
 * 
 * Main defines what the user will be viewing through the use of .fxml files and setting the stage.
 * 
 */
public class Main extends Application { //Gannon Gebauer
	//creates primary stage
    private Stage primaryStage;

    public static void main(String[] args) { //Gannon Gebauer
        launch(args);
    }
    
    //function that conrtolls the scene
    @Override
    public void start(Stage primaryStage) { //Gannon Gebauer
    	//variable that controlls the scene
        this.primaryStage = primaryStage;
        showMainScene();
    }
    
    //Provides the intial scene for the EffortLogger screen
    private void showMainScene() { //Gannon Gebauer
        try {
        	//Calls the login.fxml file to create the scene
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            
            //Sets scene parameters
            Scene scene = new Scene(root);
            primaryStage.setTitle("Login Demo-Gebauer,Gannon");
            primaryStage.setScene(scene);
            primaryStage.show();
            
            
            MainController mainController = loader.getController();
            // Pass a reference to the main app to the controller
            mainController.init(this);  
        } catch (Exception e) {
        	//Catches exceptions and return the trace
            e.printStackTrace();
        }
    }
    
    //function to show the Home screen
    public void showHomeScene() { //Gannon Gebauer
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();
            
            //sets the new scene
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
            
            
            
            MainController mainController = loader.getController();
            // Pass a reference to the main app to the controller
            mainController.init(this);  
        } catch (Exception e) {
        	//Catches exceptions and return the trace
            e.printStackTrace();
        }
    }
    

    /*
     * 
     * MainController is the contoller for the main function telling it what page to display and when.
     * Also interacts with other functions as the controller should.
     * 
     */    
    public static class MainController { //Gannon Gebauer
    	//variable allows association with the Main function
        private Main mainApp;
        //Input from the textfields
        public TextField usernameTextField;
        public TextField passwordTextField;
        //User object
        private user person;
        
        //allows variable change from outside the function
        public void init(Main mainApp) {
            this.mainApp = mainApp;
        }

        @FXML
        public void login() { //Gannon Gebauer
            // Handle button click
            String username = usernameTextField.getText();
            //Prints out for testing purposes
            System.out.println("Username: " + username);
            
            String password = passwordTextField.getText();
            //Prints out for testing purposes
            System.out.println("Password: " + password);
            
            if (authenticateUser(username, password)) {
            	mainApp.showHomeScene(); // Switch to another scene
            } else {
            	mainApp.showMainScene(); // Handle unsuccessful login
            }            
        }
        
        @FXML
        public void logout() { //Gannon Gebauer
            // Handle logout button click
            mainApp.showMainScene(); // Switch to login scene or another appropriate scene
        }
        
        private boolean authenticateUser(String username, String password) { //Gannon Gebauer
            // Authentication for login data will be plaved here
            // For simplicity, let's assume any non-empty username and password is valid for this demo
            return username != null && !username.isEmpty() && password != null && !password.isEmpty();
        }
        
    }
    
    
}


