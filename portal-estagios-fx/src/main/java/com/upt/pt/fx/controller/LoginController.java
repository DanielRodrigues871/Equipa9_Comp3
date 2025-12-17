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
            // 1. Enviar Login para a API
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);

            JSONObject response = ApiClient.post("/api/auth/login", json);

            // Debug na consola para ver o que chega
            System.out.println("DEBUG LOGIN: " + response.toString());

            if (response.has("error")) {
                errorLabel.setText(response.getString("error"));
                return;
            }

            // 2. Guardar dados básicos do Utilizador
            String id = String.valueOf(response.get("id"));
            String nome = response.optString("nome", "Utilizador");
            
            String role;
            if (response.has("role")) role = response.getString("role").toUpperCase();
            else role = response.optString("tipo", "").toUpperCase();

            UserSession.setUser(id, nome, email, role);

            // =================================================================
            // 3. LÓGICA PARA RECUPERAR ID DA EMPRESA (CRUCIAL!)
            // =================================================================
            String empresaIdEncontrado = null;

            // Passo A: Tentar ler diretamente da resposta do Login
            if (response.has("companyId")) empresaIdEncontrado = String.valueOf(response.get("companyId"));
            else if (response.has("empresaId")) empresaIdEncontrado = String.valueOf(response.get("empresaId"));
            else if (response.has("empresa_id")) empresaIdEncontrado = String.valueOf(response.get("empresa_id"));

            // Passo B: Se não veio no Login e é Representante, fazer pedido extra à API
            if ((empresaIdEncontrado == null || empresaIdEncontrado.equals("null")) 
                 && (role.equals("REPRESENTANTE") || role.equals("EMPRESA"))) {
                
                System.out.println("⚠️ ID Empresa não veio no login. Tentando buscar perfil do representante...");
                try {
                    // Faz GET /api/representantes/{idUsuario} para ver os detalhes
                    JSONObject repDetalhes = ApiClient.getJson("/api/representantes/" + id); // ou use endpoint correto da sua API
                    
                    if (repDetalhes.has("empresaId")) {
                        empresaIdEncontrado = String.valueOf(repDetalhes.get("empresaId"));
                    } else if (repDetalhes.has("empresa")) {
                        // Caso a empresa venha como objeto aninhado
                        JSONObject empObj = repDetalhes.getJSONObject("empresa");
                        empresaIdEncontrado = String.valueOf(empObj.get("id"));
                    }
                } catch (Exception ex) {
                    System.err.println("❌ Falha ao tentar recuperar empresa via API: " + ex.getMessage());
                }
            }

            // Passo C: Guardar na Sessão se encontrou
            if (empresaIdEncontrado != null && !empresaIdEncontrado.equals("null")) {
                UserSession.setEmpresaId(empresaIdEncontrado);
                System.out.println("✅ SESSÃO: Empresa ID guardado com sucesso: " + empresaIdEncontrado);
            } else {
                System.err.println("❌ ERRO CRÍTICO: Não foi possível identificar a empresa deste utilizador.");
            }
            // =================================================================

            // 4. Redirecionamento
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
            errorLabel.setText("Login falhou. Verifique consola.");
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("welcome.fxml");
    }
}