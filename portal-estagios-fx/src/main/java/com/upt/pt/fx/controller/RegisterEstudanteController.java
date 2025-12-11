package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONObject;

public class RegisterEstudanteController {

    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML private TextField cursoIdField;
    @FXML private TextField numeroEstudanteField;
    @FXML private TextField anoMatriculaField;

    @FXML private Label errorLabel;

    @FXML
    public void registar() {
        try {
            JSONObject json = new JSONObject();
            json.put("nome", nomeField.getText());
            json.put("email", emailField.getText());
            json.put("password", passwordField.getText());
            json.put("tipo", "ESTUDANTE");

            json.put("cursoId", cursoIdField.getText());
            json.put("numeroEstudante", numeroEstudanteField.getText());
            json.put("anoMatricula", Integer.parseInt(anoMatriculaField.getText()));

            ApiClient.post("/api/auth/register", json);

            SceneManager.changeScene("login.fxml");

        } catch (Exception e) {
            errorLabel.setText("Erro no registo do estudante!");
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("register.fxml");
    }
}
