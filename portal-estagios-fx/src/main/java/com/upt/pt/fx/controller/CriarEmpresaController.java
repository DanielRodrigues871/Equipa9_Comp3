package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONObject;

public class CriarEmpresaController {

    @FXML private TextField txtNome;
    @FXML private TextField txtNif;
    @FXML private TextField txtEmail;
    @FXML private TextField txtMorada;
    @FXML private Label lblErro;

    @FXML
    public void criar() {
        try {
            JSONObject body = new JSONObject();
            body.put("nome", txtNome.getText());
            body.put("nif", txtNif.getText());
            body.put("email", txtEmail.getText());
            body.put("morada", txtMorada.getText());
            body.put("ativa", true);

            ApiClient.post("/api/empresas", body);

            // depois de criar → volta ao registo
            SceneManager.changeScene("registo_representante.fxml");

        } catch (Exception e) {
            lblErro.setText("Erro ao criar empresa.");
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("registo_representante.fxml");
    }
}
