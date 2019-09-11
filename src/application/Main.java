package application;
	
import java.io.IOException;

import application.View.*;
import application.View.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Kergejõustiku kalkulaator");

        initRootLayout();

        showMainview();
    }
    
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

    /**
     * Shows the main overview inside the root layout.
     */
    public void showMainview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("mainView.fxml"));
            AnchorPane mainView = (AnchorPane) loader.load();
            
            // Set person overview into the center of root layout.
            rootLayout.setCenter(mainView);
            
            MainViewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setListener();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}