package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class RepresentanteNotificacoesController {

    @FXML private ListView<String> listaNotificacoes;

    @FXML
    public void initialize() {
        carregarNotificacoes();
    }

    private void carregarNotificacoes() {
        // Por enquanto, vamos colocar dados fictícios para testar o ecrã.
        // Futuramente, pode ligar isto ao ApiClient.getArray("/api/notificacoes/...")
        
        ObservableList<String> notificacoes = FXCollections.observableArrayList(
            "✅ A sua proposta 'Dev Java' foi aprovada pelo Coordenador.",
            "ℹ️ Novo candidato na oferta 'Estágio Frontend'.",
            "⚠️ Lembrete: Atualize os dados da sua empresa."
        );
        
        listaNotificacoes.setItems(notificacoes);
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_representante.fxml");
    }
}