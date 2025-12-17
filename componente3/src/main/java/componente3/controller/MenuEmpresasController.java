package componente3.controller;

import componente3.SceneManager;
import componente3.session.UserSession;
import javafx.fxml.FXML;

public class MenuEmpresasController {

    @FXML
    public void verEmpresa() {
        SceneManager.changeScene("ver_empresa.fxml");
    }

    @FXML
    public void editarEmpresa() {
        SceneManager.changeScene("editar_empresa.fxml");
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_representante.fxml");
    }
}
