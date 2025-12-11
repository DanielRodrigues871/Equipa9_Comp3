package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.json.JSONObject;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    public void login() {
        try {
            JSONObject json = new JSONObject();
            json.put("email", emailField.getText());
            json.put("password", passwordField.getText());

            JSONObject resp = ApiClient.post("/api/auth/login", json);

            // Guardar sessão
            UserSession.setUser(
                    resp.getString("idUtilizador"),
                    resp.getString("nome"),
                    resp.getString("email"),
                    resp.getString("tipo")
            );
            
            UserSession.setEmpresaId(resp.optString("empresaId", null));
            
            // Se for REPRESENTANTE e o backend enviar empresaId
            if (resp.getString("tipo").equals("REPRESENTANTE") && resp.has("empresaId")) {
                UserSession.setEmpresaId(resp.getString("empresaId"));
                }

            // Navegar conforme o tipo
            switch (resp.getString("tipo")) {
                case "ESTUDANTE":
                    SceneManager.changeScene("dashboard_estudante.fxml");
                    break;

                case "COORDENADOR":
                    SceneManager.changeScene("dashboard_coordenador.fxml");
                    break;

                case "REPRESENTANTE":
                    SceneManager.changeScene("dashboard_representante.fxml");
                    break;

                default:
                    errorLabel.setText("Tipo de utilizador desconhecido!");
                    break;
            }

        } catch (Exception e) {
            errorLabel.setText("Credenciais inválidas!");
        }
    }   

    @FXML
    public void goToRegister() {
        SceneManager.changeScene("register.fxml");
    }
}
