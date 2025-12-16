package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.json.JSONObject;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginBtn;

    @FXML
    public void login() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Preencha todos os campos.");
            return;
        }

        try {
            // 1. Enviar pedido à API
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);

            JSONObject response = ApiClient.post("/api/auth/login", json);

            if (response.has("error")) {
                errorLabel.setText(response.getString("error"));
                return;
            }

            // 2. Extrair dados
            String id = String.valueOf(response.get("id"));
            String nome = response.optString("nome", "Utilizador");
            
            // Detetar Role (procura por "role" ou "tipo")
            String role;
            if (response.has("role")) {
                role = response.getString("role").toUpperCase();
            } else {
                role = response.optString("tipo", "").toUpperCase();
            }

            // Passamos o email que o utilizador escreveu no campo de texto
            UserSession.setUser(id, nome, email, role);

            // Guardar ID da empresa se for representante
            if (response.has("companyId")) {
                UserSession.setEmpresaId(String.valueOf(response.get("companyId")));
            }
            
            System.out.println("Login OK: " + nome + " (" + role + ")");

            // 4. Redirecionamento (Switch com todos os casos)
            switch (role) {
                case "ESTUDANTE":
                    SceneManager.changeScene("dashboard_estudante.fxml");
                    break;

                case "REPRESENTANTE":
                case "EMPRESA":
                    SceneManager.changeScene("dashboard_representante.fxml");
                    break;

                case "COORDENADOR":
                case "ADMIN":
                    SceneManager.changeScene("dashboard_coordenador.fxml");
                    break;

                default:
                    errorLabel.setText("Erro: Perfil desconhecido (" + role + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Login falhou. Verifique as credenciais.");
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("welcome.fxml");
    }
}