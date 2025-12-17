package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.model.DepartamentoOption; // Certifique-se que tem esta classe criada
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
                // Assume que existe a classe DepartamentoOption(id, nome)
                departamentoCombo.getItems().add(
                        new DepartamentoOption(d.getString("id"), d.getString("nome"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (errorLabel != null) errorLabel.setText("Erro ao carregar departamentos.");
        }
    }

    @FXML
    public void registar() {
        if (errorLabel != null) errorLabel.setText("");

        try {
            // 1. Validações Básicas
            if (nomeField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                if (errorLabel != null) errorLabel.setText("Preencha todos os dados pessoais.");
                return;
            }

            // 2. Validar Seleção da ComboBox
            DepartamentoOption dep = departamentoCombo.getValue();
            if (dep == null) {
                if (errorLabel != null) errorLabel.setText("Selecione o seu departamento.");
                return;
            }

            // 3. Construir JSON
            JSONObject json = new JSONObject();
            json.put("nome", nomeField.getText());
            json.put("email", emailField.getText());
            json.put("password", passwordField.getText());
            json.put("tipo", "COORDENADOR");
            
            // Usar o ID do objeto selecionado na Combo
            json.put("departamentoId", dep.getId());
            
            // 4. Enviar
            ApiClient.post("/api/auth/register", json);
            SceneManager.changeScene("login.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            if (errorLabel != null) errorLabel.setText("Erro no registo: " + e.getMessage());
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("register.fxml");
    }
}