package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.json.JSONObject;

public class CandidatarController {

    @FXML private TextArea cartaArea;
    @FXML private Label errorLabel;

    @FXML
    public void submeter() {
        try {
            String estudanteId = UserSession.getId();
            String ofertaId = UserSession.getOfertaSelecionada();

            if (estudanteId == null || ofertaId == null) {
                errorLabel.setText("Erro interno: IDs em falta!");
                return;
            }

            JSONObject json = new JSONObject();
            json.put("cartaMotivacao", cartaArea.getText());

            ApiClient.post("/api/candidaturas?estudanteId=" + estudanteId +
                           "&ofertaId=" + ofertaId, json);

            SceneManager.changeScene("dashboard_estudante.fxml");

        } catch (Exception e) {
            errorLabel.setText("Erro ao enviar candidatura!");
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("consultar_ofertas.fxml");
    }
}
