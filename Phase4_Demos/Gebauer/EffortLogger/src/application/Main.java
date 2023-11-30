package application;
//required imports
import application.user;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException; // This is to handle the IOException for file operations
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.concurrent.Task;
import javafx.scene.input.KeyEvent;


public class Main extends Application { //Gannon Gebauer
	//creates primary stage
    public Stage primaryStage;
    public Main mainApp;
    public static int daysFor2FASetup = 7;
    public static boolean is2FASetup = false;
    private static String CURRENT_VERSION = "1.0.0";


    public static void main(String[] args) { //Gannon Gebauer
        launch(args);
    }
    
    //function that controlls the scene
    @Override
    public void start(Stage primaryStage) { //Gannon Gebauer
    	//variable that controlls the scene
        this.primaryStage = primaryStage;
        showMainScene();
    }
    public Stage getPrimaryStage() { //Hassan Moustafa
        return primaryStage;
    }

    //Provides the intial scene for the EffortLogger screen
    public void showMainScene() { //Gannon Gebauer
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
    public boolean isNewPatchAvailable() { // Ahmed Kilani
        return !CURRENT_VERSION.equals(LatestVersionInfo.getLatestVersion());
    }

    public void applySecurityPatch() { // Ahmed Kilani
        if (isNewPatchAvailable()) {
            System.out.println("New security patch found and applied!");
            // After applying the update, update the current version
            CURRENT_VERSION = LatestVersionInfo.getLatestVersion();
        } else {
            System.out.println("Your system is up to date.");
        }
    }



    //the controller class for interacting with the UI
    public static class MainController { //Gannon Gebauer
    	//variable allows association with the Main function
        public Main mainApp;
        public boolean isPopupShown = false;
        public Timer inactivityTimer;
        public Label inactivityLabel; // Add this label to the FXML file and link
        public static boolean is2FASetup = false; // flag to check if 2FA has been set up
        @FXML
        public Hyperlink setup2FALink;
        @FXML
        public Text successText;
        @FXML
        public Text daysRemainingText;

        //Input from the textfields
        public TextField usernameTextField;
        public TextField passwordTextField;
        //User object
        public user person;
        private boolean updateNotificationShown = false;
        public static void setDaysFor2FASetup(int days) {
            Main.daysFor2FASetup = days;
        }

        public static int getDaysFor2FASetup() {
            return Main.daysFor2FASetup;
        }

        public static void setIs2FASetup(boolean isSetup) {
            Main.is2FASetup = isSetup;
        }

        //allows variable change from outside the function
        public void init(Main mainApp) {
            this.mainApp = mainApp;
            daysRemainingText.setText("You have " + daysFor2FASetup + " days to set up 2-FA.");

            if(is2FASetup) {
                daysRemainingText.setVisible(false);
                setup2FALink.setVisible(false);
            }
            // Mouse movement listener to reset inactivity timer - Hassan Moustafa
            mainApp.getPrimaryStage().getScene().addEventFilter(MouseEvent.MOUSE_MOVED, event -> resetInactivityTimer());
            // Key pressed listener to reset inactivity timer
            mainApp.getPrimaryStage().getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> resetInactivityTimer());

            setupInactivityWarning(); // Setup inactivity warning - Hassan Moustafa
        }
        public void setupInactivityWarning() { // Setup inactivity timer - Hassan Moustafa
            inactivityTimer = new Timer();
            inactivityTimer.schedule(new TimerTask() {
                int countdown = 6; //change fof testing

                @Override
                public void run() { // Inactivity timer task - Hassan Moustafa
                    Platform.runLater(() -> {
                        countdown--;
                        if (countdown <= 0) {
                            showBiometricErrorPopup(); // Show popup on inactivity - Hassan Moustafa
                        } else {
                            inactivityLabel.setText(countdown + " seconds until inactivity warning"); // Update label - Hassan Moustafa
                        }
                    });
                }
            }, 0, 1000);
        }

        public void resetInactivityTimer() { // Reset inactivity timer - Hassan Moustafa
            if (inactivityTimer != null) {
                inactivityTimer.cancel();
                setupInactivityWarning();
            }
        }

        public void showBiometricErrorPopup() { // Display the biometric error popup - Hassan Moustafa
            if (!isPopupShown) {
                isPopupShown = true;
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Biometric Error");
                alert.setHeaderText("Account Flagged");
                alert.setContentText("Your account has been flagged due to a biometric error. Please contact IT.");
                alert.showAndWait(); // Wait for user response - Hassan Moustafa

                isPopupShown = false; // Reset popup shown flag - Hassan Moustafa
                resetInactivityTimer(); // Restart inactivity timer after popup - Hassan Moustafa
            }
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
                if (!updateNotificationShown) { //Ahmed Kilani
                    checkForSecurityUpdates();
                    updateNotificationShown = true; // Set the flag to true after showing update notification
                }
            } else {
//            	mainApp.showMainScene(); // Handle unsuccessful login
            }
        }

        // Author: Aly Shiha
        // This method aligns with the design by providing a user interface for 2FA setup, ensuring secure access.
        // The implementation involves creating a pop-up window where the user can enter the 2FA code,
        // and it gets verified. On successful setup, relevant UI elements are updated, and the window is closed.
        @FXML
        public void setup2FA(ActionEvent event) {
            // Initialize a new pop-up window for the 2FA setup
            Stage popupStage = new Stage();

            // Ensure this window remains in the foreground until addressed
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Set the title for the window
            popupStage.setTitle("Set Up 2FA");

            // Prevent window size adjustments by the user
            popupStage.setResizable(false);

            // Disable the default window close function for better flow control
            popupStage.setOnCloseRequest(e -> e.consume());

            // Define a layout with a vertical box and set space between elements
            VBox layout = new VBox(10);

            // Add padding to the layout for a better visual appeal
            layout.setPadding(new Insets(10, 10, 10, 10));

            // Create UI elements: label prompt, input field, button, and result display
            Label infoLabel = new Label("Enter the 2FA code sent to you:");
            TextField codeField = new TextField();
            Button submitButton = new Button("Submit");
            Label resultLabel = new Label(); // Initialized empty, will display results based on user input

            // Generate a random 6-digit code
            Random random = new Random();
            String generatedCode = String.format("%06d", random.nextInt(1000000));

            // Save the generated code to a text file on the desktop
            try {
                String desktopPath = System.getProperty("user.home") + "/Desktop";
                File file = new File(desktopPath + "/2FACode.txt");
                FileWriter writer = new FileWriter(file);
                writer.write(generatedCode);
                writer.close();
                System.out.println("2FA code saved to Desktop as 2FACode.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Define button action to validate the 2FA code
            submitButton.setOnAction(e -> {
                if (codeField.getText().equals(generatedCode)) { // Check against the generated code
                    resultLabel.setText("2FA Setup Successfully!");
                    resultLabel.setTextFill(Color.GREEN); // Use green color for successful setup
                    is2FASetup = true; // Mark 2FA as set up

                    // Close the setup window after successful setup
                    popupStage.close();

                    // Hide any remaining setup prompts and show a success message
                    daysRemainingText.setVisible(false);
                    setup2FALink.setVisible(false);
                    successText.setVisible(true);
                    successText.toFront();

                    // Set a delay to hide the success message, enhancing UX
                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(actionEvent -> successText.setVisible(false));
                    pause.play();
                } else {
                    resultLabel.setText("Incorrect Code. Try again.");
                    resultLabel.setTextFill(Color.RED); // Use red color for errors
                }
            });

            // Add all UI elements to the layout
            layout.getChildren().addAll(infoLabel, codeField, submitButton, resultLabel);

            // Define scene dimensions and set it to the stage
            Scene scene = new Scene(layout, 300, 200);
            popupStage.setScene(scene);

            // Display the pop-up and wait for user interaction
            popupStage.showAndWait();
        }

        // Author: Aly Shiha
        // This method helps in risk reduction by ensuring the user sets up 2FA within a specified time frame.
        // It aligns with the design by decrementing the number of days remaining for 2FA setup and
        // forcing the setup if the time is up, enhancing security.
        public void decrement2FADays() {
            // Decrement the days counter only if it's greater than 0
            if (daysFor2FASetup > 0) {
                daysFor2FASetup--;

                // Display the updated days count to the user
                daysRemainingText.setText("You have " + daysFor2FASetup + " days to set up 2-FA.");

                // When the counter hits 0, prompt the user to set up 2FA immediately
                if (daysFor2FASetup == 0) {
                    System.out.println("Time's up! Please set up 2FA now.");
                    setup2FA(null);
                }
            }
        }

        // Author: Aly Shiha
        // This method aligns with the design by checking if 2FA setup is pending and initiating it if necessary,
        // which ensures that the user's account remains secure.
        public void check2FANeeded() {
            // Check if the time frame for 2FA setup is over and if 2FA isn't set up yet
            if (daysFor2FASetup == 0 && !is2FASetup) {
                setup2FA(null); // Initiate the 2FA setup process
            }
        }


        @FXML
        public void logout() {
            mainApp.showMainScene(); // Switch to login scene first
            decrement2FADays();  // Decrement the counter
            check2FANeeded(); // Then check if 2FA setup is needed
        }


        public boolean authenticateUser(String username, String password) { //Gannon Gebauer
            // Authentication for login data will be plaved here
            // For simplicity, let's assume any non-empty username and password is valid for this demo
            return username != null && !username.isEmpty() && password != null && !password.isEmpty();
        }
        public void checkForSecurityUpdates() { //Ahmed Kilani
            Alert checkingAlert = new Alert(AlertType.INFORMATION, "Checking for updates...");
            checkingAlert.show();

            new Thread(() -> {
                try {
                    // Simulating checking for updates
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Platform.runLater(() -> {
                    checkingAlert.close();
                    boolean updatesAvailable = mainApp.isNewPatchAvailable();

                    if (updatesAvailable) {
                        Alert updateAlert = new Alert(AlertType.CONFIRMATION, "A new update is available. Would you like to install it now?");
                        Optional<ButtonType> result = updateAlert.showAndWait();

                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            // Show progress bar in an alert dialog
                            Alert progressAlert = new Alert(AlertType.INFORMATION);
                            progressAlert.setTitle("Downloading Update");
                            progressAlert.setHeaderText("Downloading update in progress...");
                            ProgressBar progressBar = new ProgressBar(0);
                            progressAlert.getDialogPane().setContent(progressBar);
                            progressAlert.show();

                            Task<Void> task = new Task<Void>() {
                                @Override
                                protected Void call() throws Exception {
                                    for (int i = 0; i < 100; i++) {
                                        updateProgress(i, 99);
                                        Thread.sleep(50); // Simulate some work
                                    }
                                    return null;
                                }

                                @Override
                                protected void succeeded() {
                                    super.succeeded();
                                    progressAlert.close();
                                    mainApp.applySecurityPatch();
                                    Alert successAlert = new Alert(AlertType.INFORMATION, "The update was installed successfully.");
                                    successAlert.showAndWait();
                                }
                            };

                            progressBar.progressProperty().bind(task.progressProperty());
                            new Thread(task).start();
                        }
                    } else {
                        Alert upToDateAlert = new Alert(AlertType.INFORMATION, "Your system is up to date.");
                        upToDateAlert.showAndWait();
                    }
                });
            }).start();
        }

    }
}