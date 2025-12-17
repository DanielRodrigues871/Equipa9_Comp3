package componente3.controller;

import componente3.SceneManager;
import componente3.service.ApiClient;
import componente3.session.UserSession;
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

            // 2. Guardar dados básicos do Utilizador - CORREÇÃO AQUI!
            String id = response.getString("idUtilizador"); // <- CORRIGIDO
            String nome = response.optString("nome", "Utilizador");
            
            // O backend está retornando "tipo", não "role"
            String role = response.getString("tipo").toUpperCase(); // <- CORRIGIDO

            UserSession.setUser(id, nome, email, role);

            // 3. LÓGICA PARA RECUPERAR ID DA EMPRESA
            String empresaIdEncontrado = null;

            // Verificar se o backend retorna empresaId na resposta do login
            if (response.has("empresaId")) {
                empresaIdEncontrado = response.getString("empresaId");
                System.out.println("✅ Empresa ID encontrado na resposta do login: " + empresaIdEncontrado);
            }

            // Se não veio no Login e é Representante, fazer pedido extra à API
            if (empresaIdEncontrado == null && role.equals("REPRESENTANTE")) {
                System.out.println("⚠️ Empresa ID não veio no login. Tentando buscar perfil do representante...");
                try {
                    // Faz GET para obter detalhes do representante
                    JSONObject repDetalhes = ApiClient.getJson("/api/representantes/" + id);
                    
                    if (repDetalhes.has("empresaId")) {
                        empresaIdEncontrado = repDetalhes.getString("empresaId");
                    } else if (repDetalhes.has("empresa")) {
                        // Caso a empresa venha como objeto aninhado
                        JSONObject empObj = repDetalhes.getJSONObject("empresa");
                        empresaIdEncontrado = empObj.getString("id");
                    }
                } catch (Exception ex) {
                    System.err.println("❌ Falha ao tentar recuperar empresa via API: " + ex.getMessage());
                    errorLabel.setText("Erro ao buscar dados da empresa.");
                    return;
                }
            }

            // Guardar na Sessão se encontrou
            if (empresaIdEncontrado != null && !empresaIdEncontrado.equals("null")) {
                UserSession.setEmpresaId(empresaIdEncontrado);
                System.out.println("✅ SESSÃO: Empresa ID guardado: " + empresaIdEncontrado);
            } else if (role.equals("REPRESENTANTE")) {
                System.err.println("❌ ERRO: Representante não tem empresa associada!");
                errorLabel.setText("Representante não tem empresa associada!");
                return;
            }

            // 4. Redirecionamento
            switch (role) {
                case "ESTUDANTE":
                    SceneManager.changeScene("dashboard_estudante.fxml");
                    break;
                case "REPRESENTANTE":
                    SceneManager.changeScene("dashboard_representante.fxml");
                    break;
                case "COORDENADOR":
                    SceneManager.changeScene("dashboard_coordenador.fxml");
                    break;
                default:
                    errorLabel.setText("Erro: Perfil desconhecido (" + role + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Credenciais inválidas!");
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("welcome.fxml");
    }
}