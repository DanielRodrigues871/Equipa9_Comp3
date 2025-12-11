package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.json.JSONObject;

public class VerCandidaturaController {

    @FXML private Label estudanteLabel;
    @FXML private Label estadoLabel;
    @FXML private Label dataLabel;
    @FXML private TextArea cartaArea;

    private String candidaturaId;

    @FXML
    public void initialize() {
        candidaturaId = UserSession.getCandidaturaSelecionada();
        carregar();
    }

    private void carregar() {
        try {
            JSONObject obj = ApiClient.getObject("/api/candidaturas/" + candidaturaId);

            estudanteLabel.setText("Estudante: " + obj.getString("estudanteNome"));
            estadoLabel.setText("Estado: " + obj.getString("estado"));
            dataLabel.setText("Data: " + obj.getString("data"));
            cartaArea.setText(obj.getString("cartaMotivacao"));

        } catch (Exception e) {
            estudanteLabel.setText("Erro ao carregar.");
        }
    }

    @FXML
    public void aprovar() {
        try {
            ApiClient.post("/api/candidaturas/" + candidaturaId + "/aprovar", new JSONObject());
            voltar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void rejeitar() {
        try {
            ApiClient.post("/api/candidaturas/" + candidaturaId + "/rejeitar", new JSONObject());
            voltar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("listar_candidaturas.fxml");
    }
}
