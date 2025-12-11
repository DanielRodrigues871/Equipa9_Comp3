package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONObject;

public class RegistarCursoController {

    @FXML private TextField nomeField;
    @FXML private TextField siglaField;
    @FXML private TextField areaField;
    @FXML private TextField duracaoField;

    @FXML private Label errorLabel;

    @FXML
    public void registar() {
        try {
            JSONObject json = new JSONObject();
            json.put("nome", nomeField.getText());
            json.put("sigla", siglaField.getText());
            json.put("area", areaField.getText());
            json.put("duracaoAnos", Integer.parseInt(duracaoField.getText()));

            ApiClient.post("/api/cursos", json);

            SceneManager.changeScene("dashboard_coordenador.fxml");

        } catch (Exception e) {
            errorLabel.setText("Erro ao registar curso!");
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_coordenador.fxml");
    }
}
