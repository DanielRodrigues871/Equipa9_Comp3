package componente3;

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
        // Certifique-se que o ficheiro welcome.fxml (o código da resposta anterior)
        // está guardado dentro de: src/main/resources/views/
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("/views/welcome.fxml"));
        
        Scene scene = new Scene(fxml.load());

        // 3. Configurações da Janela
        stage.setTitle("Portal de Estágios");
        stage.setScene(scene);

        // --- AQUI ESTÁ A MUDANÇA PARA RESOLUÇÃO DE COMPUTADOR ---
        
        // Opção A (Recomendada): Abre a janela maximizada (ecrã inteiro)
        stage.setMaximized(true);
        
        // Opção B (Segurança): Define um tamanho mínimo para evitar que a janela fique demasiado pequena
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
