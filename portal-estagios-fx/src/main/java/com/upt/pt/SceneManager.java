package com.upt.pt;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void changeScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/views/" + fxml));
            Scene scene = new Scene(loader.load());
            
            primaryStage.setScene(scene);
            
            // Força a janela a ficar maximizada sempre que troca de página
            primaryStage.setMaximized(true); 
            
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}