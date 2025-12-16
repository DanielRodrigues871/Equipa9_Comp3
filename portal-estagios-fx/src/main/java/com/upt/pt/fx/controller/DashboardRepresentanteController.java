package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardRepresentanteController {

    @FXML private Label welcomeLabel;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Bem-vindo, " + UserSession.getNome());
    }

    @FXML
    public void criarProposta() {
        SceneManager.changeScene("criar_proposta.fxml");
    }

    @FXML
    public void verPropostas() {
        SceneManager.changeScene("listar_propostas.fxml");
    }

    @FXML
    public void menuEmpresas() {
        SceneManager.changeScene("menu_empresas.fxml");
    }

    @FXML
    public void verNotificacoes() {
        SceneManager.changeScene("notificacoes.fxml");
    }

    @FXML
    public void logout() {
        UserSession.logout();
        SceneManager.changeScene("welcome.fxml");
    }
}
