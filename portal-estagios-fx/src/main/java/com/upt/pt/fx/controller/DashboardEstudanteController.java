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

    @FXML
    public void verOfertas() {
        SceneManager.changeScene("consultar_ofertas.fxml");
    }


    @FXML
    public void verCandidaturas() {
        System.out.println("→ GET /api/candidaturas/estudante/" + UserSession.getId());
    }

    @FXML
    public void candidatar() {
        System.out.println("→ POST /api/candidaturas?estudanteId=... etc");
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
