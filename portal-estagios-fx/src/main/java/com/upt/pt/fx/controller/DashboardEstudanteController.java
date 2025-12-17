package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DashboardEstudanteController {

    @FXML
    private AnchorPane contentPane; // A área branca central que muda

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        // Mostra o nome do estudante logado
        if (UserSession.getNome() != null) {
            welcomeLabel.setText("Bem-vindo, " + UserSession.getNome());
        }
    }

    // MÉTODOS DOS BOTÕES (onAction)

    @FXML
    public void showOfertasAprovadas() {
        System.out.println("Botão clicado: Ofertas Aprovadas");
        loadView("estudante_ofertas_disponiveis.fxml");
    }

    @FXML
    public void showCandidatar() {
        System.out.println("Botão clicado: Candidatar a Oferta");
        loadView("estudante_ofertas_disponiveis.fxml");
    }

    // *** NOVOS MÉTODOS PARA PROPOSTAS ***
    @FXML
    public void showPropostasAprovadas() {
        System.out.println("Botão clicado: Propostas Aprovadas");
        loadView("estudante_propostas_aprovadas.fxml");
    }

    @FXML
    public void showCandidatarProposta() {
        System.out.println("Botão clicado: Candidatar a Proposta");
        loadView("estudante_propostas_aprovadas.fxml");
    }
    // ************************************

    @FXML
    public void showMinhasCandidaturas() {
        System.out.println("Botão clicado: Minhas Candidaturas");
        loadView("estudante_minhas_candidaturas.fxml");
    }

    @FXML
    public void showNotificacoes() {
        System.out.println("Botão clicado: Notificações");
        loadView("estudante_notificacoes.fxml");
    }

    @FXML
    public void logout() {
        // Limpa a sessão e volta ao login
        UserSession.logout();
        SceneManager.changeScene("login.fxml");
    }

    // MÉTODO PARA TROCAR O ECRÃ CENTRAL
    
    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + fxmlFile));
            Node view = loader.load();
            
            // Substitui o conteúdo central
            contentPane.getChildren().setAll(view);
            
            // Cola as bordas para ocupar tudo
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            
        } catch (IOException e) {
            // Se o ficheiro não existir, mostra erro na consola mas não fecha a app
            System.err.println("ERRO: Não encontrei o ficheiro " + fxmlFile);
            e.printStackTrace();
            
            // Opcional: Mostra erro visual ao utilizador
            welcomeLabel.setText("Erro: A vista '" + fxmlFile + "' ainda não foi criada.");
        }
    }
}