package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardEstudanteController {

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Bem-vindo, " + UserSession.getNome() + "!");
    }

    // ================== NAVEGAÇÃO ==================

    @FXML
    public void verOfertas() {
        SceneManager.changeScene("consultar_ofertas.fxml");
    }

    @FXML
    public void verCandidaturas() {
        SceneManager.changeScene("minhas_candidaturas.fxml");
    }

    @FXML
    public void candidatar() {
        // Redireciona para ofertas aprovadas
        SceneManager.changeScene("consultar_ofertas.fxml");
    }

    @FXML
    public void verNotificacoes() {
        SceneManager.changeScene("notificacoes.fxml");
    }

    // ================== LOGOUT ==================

    @FXML
    public void logout() {
        UserSession.logout();
        SceneManager.changeScene("welcome.fxml");
    }
}
