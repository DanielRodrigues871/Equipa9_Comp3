package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardCoordenadorController {

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Bem-vindo, Coordenador " + UserSession.getNome() + "!");
    }

    @FXML
    public void criarOferta() {
        SceneManager.changeScene("criar_oferta_coordenador.fxml");
    }


    @FXML
    public void editarOferta() {
        System.out.println("→ Abrir janela: Editar Oferta");
    }

    @FXML
    public void eliminarOferta() {
        System.out.println("→ DELETE /api/ofertas/{id}");
    }

    @FXML
    public void listarPendentes() {
        SceneManager.changeScene("listar_ofertas_pendentes.fxml");
    }


    @FXML
    public void aprovarOferta() {
        System.out.println("→ POST /api/ofertas/{id}/aprovar");
    }

    @FXML
    public void rejeitarOferta() {
        System.out.println("→ POST /api/ofertas/{id}/rejeitar");
    }

    @FXML
    public void listarTodas() {
        System.out.println("→ GET /api/ofertas");
    }

    @FXML
    public void verCandidaturas() {
        System.out.println("→ GET /api/candidaturas/oferta/{idOferta}");
    }

    @FXML
    public void gerirCandidatura() {
        System.out.println("→ POST /api/candidaturas/{id}/(analise|aprovar|rejeitar)");
    }

    @FXML
    public void registarCurso() {
        SceneManager.changeScene("registar_curso.fxml");
    }


    @FXML
    public void listarCursos() {
        SceneManager.changeScene("listar_cursos.fxml");
    }


    @FXML
    public void menuEmpresas() {
        System.out.println("→ Abrir sub-menu Empresa");
    }

    @FXML
    public void verEstatisticas() {
        SceneManager.changeScene("estatisticas.fxml");
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
