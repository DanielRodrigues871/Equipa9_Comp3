package com.upt.pt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PortalFXMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // 1. Configurar o SceneManager (preservando o seu código atual)
        SceneManager.setStage(stage);

        // 2. Carregar o FXML
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("/views/welcome.fxml"));
        
        Scene scene = new Scene(fxml.load());

        // 3. Configurações da Janela
        stage.setTitle("Portal de Estágios");
        stage.setScene(scene);

       
        
        // Abre a janela maximizada (ecrã inteiro)
        stage.setMaximized(true);
        
        // Define um tamanho mínimo para evitar que a janela fique demasiado pequena
        stage.setMinWidth(1024);
        stage.setMinHeight(720);

        // 4. Mostrar a janela
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

/*
@../images/Gemini_Generated_Image_9m5poe9m5poe9m5p.png
*/

/*
 * UPDATE utilizador 
 * SET password = '$2a$10$WOr2.pQp59bwUC8vv/BM5.4DQRfZe3qLM5CFLnlWZ/tzw6VWI8lN6' 
 * WHERE email = 'jose@upt.pt';
 */
