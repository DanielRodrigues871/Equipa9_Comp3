package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DashboardRepresentanteController {

    @FXML
    private AnchorPane contentPane;   // mesmo fx:id do FXML

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        if (UserSession.getNome() != null) {
            welcomeLabel.setText("Bem-vindo, " + UserSession.getNome());
        }
    }

    // métodos dos botões do menu 

    @FXML
    public void showCriarProposta() {
        loadView("representante_criar_proposta.fxml");  
    }

    @FXML
    public void showMinhasPropostas() {
        loadView("representante_propostas.fxml");        
    }

    @FXML
    public void showGestaoEmpresas() {
        loadView("representante_empresa.fxml");        
    }

    @FXML
    public void showNotificacoes() {
        loadView("representante_notificacoes.fxml");     
    }

    @FXML
    public void logout() {
        UserSession.logout();
        SceneManager.changeScene("welcome.fxml");        
    }

    // método auxiliar igual ao do coordenador

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + fxmlFile));
            Node view = loader.load();

            contentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

        } catch (IOException e) {
            System.err.println("ERRO: Não foi possível carregar a vista: " + fxmlFile);
            e.printStackTrace();
            welcomeLabel.setText("Erro: O ecrã '" + fxmlFile + "' ainda não foi criado.");
        }
    }
}