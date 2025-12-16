package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.model.DepartamentoOption;
import com.upt.pt.fx.service.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class RegisterCoordenadorController {

    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML private ComboBox<DepartamentoOption> departamentoCombo;

    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        carregarDepartamentos();
    }

    private void carregarDepartamentos() {
        try {
            JSONArray arr = ApiClient.getArray("/api/departamentos");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject d = arr.getJSONObject(i);
                departamentoCombo.getItems().add(
                        new DepartamentoOption(
                                d.getString("id"),
                                d.getString("nome")
                        )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void registar() {
        try {
            DepartamentoOption dep = departamentoCombo.getValue();
            if (dep == null) {
                errorLabel.setText("Selecione um departamento");
                return;
            }

            JSONObject json = new JSONObject();
            json.put("nome", nomeField.getText());
            json.put("email", emailField.getText());
            json.put("password", passwordField.getText());
            json.put("tipo", "COORDENADOR");

            json.put("departamentoId", dep.getId());

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
