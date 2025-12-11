package com.upt.pt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PortalFXMain extends Application {

	@Override
	public void start(Stage stage) throws Exception {
	    SceneManager.setStage(stage);

	    FXMLLoader fxml = new FXMLLoader(getClass().getResource("/views/welcome.fxml"));
	    Scene scene = new Scene(fxml.load());
	    stage.setTitle("Portal de Estágios");
	    stage.setScene(scene);
	    stage.show();
	}



    public static void main(String[] args) {
        launch(args);
    }
}
