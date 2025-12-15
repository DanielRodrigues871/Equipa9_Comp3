package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DashboardCoordenadorController {

    @FXML
    private AnchorPane contentPane; // A área central que muda

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        if (UserSession.getNome() != null) {
            welcomeLabel.setText("Bem-vindo, " + UserSession.getNome());
        }
    }

    // =======================================================
    // MÉTODOS LIGADOS AOS BOTÕES DO MENU (onAction)
    // =======================================================

    @FXML
    public void showValidarOfertas() {
        System.out.println("Clicou em Validar Ofertas");
        loadView("coordenador_validar_ofertas.fxml");
    }

    @FXML
    public void showTodasOfertas() {
        System.out.println("Clicou em Gerir Ofertas");
        loadView("coordenador_gerir_ofertas.fxml");
    }

    @FXML
    public void showCriarOferta() {
        System.out.println("Clicou em Criar Oferta");
        loadView("coordenador_criar_oferta.fxml");
    }

    @FXML
    public void showGestaoCandidaturas() {
        System.out.println("Clicou em Gerir Candidaturas");
        loadView("coordenador_candidaturas.fxml");
    }

    @FXML
    public void showGestaoAcademica() {
        System.out.println("Clicou em Gestão Académica");
        loadView("coordenador_academico.fxml");
    }

    @FXML
    public void showEmpresas() {
        System.out.println("Clicou em Empresas");
        loadView("coordenador_empresas.fxml");
    }

    @FXML
    public void showEstatisticas() {
        System.out.println("Clicou em Estatísticas");
        loadView("coordenador_estatisticas.fxml");
    }

    @FXML
    public void logout() {
        UserSession.logout();
        SceneManager.changeScene("login.fxml");
    }

    // =======================================================
    // MÉTODO AUXILIAR PARA TROCAR O CONTEÚDO CENTRAL
    // =======================================================
    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + fxmlFile));
            Node view = loader.load();
            
            // Limpa o centro e mete a nova vista
            contentPane.getChildren().setAll(view);
            
            // Estica a vista para ocupar o espaço todo
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            
        } catch (IOException e) {
            // Se o ficheiro ainda não existir, mostra o erro na consola mas não crasha a app
            System.err.println("ERRO: Não foi possível carregar a vista: " + fxmlFile);
            e.printStackTrace();
            
            // Opcional: Mostrar mensagem visual de erro
            welcomeLabel.setText("Erro: O ecrã '" + fxmlFile + "' ainda não foi criado.");
        }
    }
}