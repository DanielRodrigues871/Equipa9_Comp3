package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONObject;

public class CriarOfertaCoordenadorController {

    @FXML private TextField tituloField;
    @FXML private TextArea descricaoArea;
    @FXML private TextArea requisitosArea;
    @FXML private TextField localizacaoField;
    @FXML private TextField duracaoField;
    @FXML private TextField vagasField;

    @FXML private Label errorLabel;

    @FXML
    public void criar() {
        try {
            JSONObject json = new JSONObject();
            json.put("titulo", tituloField.getText());
            json.put("descricao", descricaoArea.getText());
            json.put("requisitos", requisitosArea.getText());
            json.put("localizacao", localizacaoField.getText());
            json.put("duracaoMeses", Integer.parseInt(duracaoField.getText()));
            json.put("vagasDisponiveis", Integer.parseInt(vagasField.getText()));

            String coordId = UserSession.getId();

            // Chamada API
            ApiClient.post("/api/ofertas?coordenadorId=" + coordId, json);

            SceneManager.changeScene("dashboard_coordenador.fxml");

        } catch (Exception e) {
            errorLabel.setText("Erro ao criar oferta!");
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_coordenador.fxml");
    }
}
