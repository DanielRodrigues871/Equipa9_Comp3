package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.session.UserSession;
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
        switch (UserSession.getTipo()) {
            case "COORDENADOR":
                SceneManager.changeScene("dashboard_coordenador.fxml");
                break;
            case "REPRESENTANTE":
                SceneManager.changeScene("dashboard_representante.fxml");
                break;
        }
    }

}
