package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONObject;

public class RegisterCoordenadorController {

    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML private TextField departamentoIdField;

    @FXML private Label errorLabel;

    @FXML
    public void registar() {
        try {
            JSONObject json = new JSONObject();
            json.put("nome", nomeField.getText());
            json.put("email", emailField.getText());
            json.put("password", passwordField.getText());
            json.put("tipo", "COORDENADOR");

            json.put("departamentoId", departamentoIdField.getText());

            ApiClient.post("/api/auth/register", json);

            SceneManager.changeScene("login.fxml");

        } catch (Exception e) {
            errorLabel.setText("Erro no registo do coordenador!");
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("register.fxml");
    }
}
