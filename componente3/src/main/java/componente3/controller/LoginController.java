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

    // Estes nomes TÊM de ser iguais aos fx:id no seu FXML
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginBtn;

    // Este é o método chamado pelo onAction="#login" do botão
    @FXML
    public void login() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // 1. Validação básica
        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Preencha todos os campos.");
            return;
        }

        try {
            // 2. Preparar dados para a API
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);

            // 3. Enviar para o servidor (ajuste o endpoint "/login" se necessário)
            // Se usar modo de teste sem API, veja a nota abaixo*
            JSONObject response = ApiClient.post("/api/auth/login", json);

            // 4. Verificar se houve erro
            if (response.has("error")) {
                errorLabel.setText(response.getString("error"));
                return;
            }

            // 5. Ler os dados do utilizador da resposta JSON
            // (Assumindo que a API devolve: id, name, email, role)
            String id = String.valueOf(response.get("id"));
            String nome = response.getString("nome");
            String role = response.getString("tipo").toUpperCase(); // Ex: "ESTUDANTE"

            // 6. Guardar na Sessão (para usar nos dashboards)
            UserSession.setUser(id, nome, email, role);

            // Se for empresa, guardar ID específico se existir
            if (role.equals("EMPRESA") && response.has("companyId")) {
                UserSession.setEmpresaId(String.valueOf(response.get("companyId")));
            }

            // =========================================================
            // 7. AQUI ESTÁ A LÓGICA DE REDIRECIONAMENTO
            // =========================================================
            switch (role) {
                case "ESTUDANTE":
                case "STUDENT": 
                    // Abre o dashboard do estudante
                    SceneManager.changeScene("dashboard_estudante.fxml");
                    break;

                case "EMPRESA":
                case "COMPANY":
                    // Abre o dashboard da empresa
                    SceneManager.changeScene("dashboard_representante.fxml");
                    break;

                case "COORDENADOR":
                case "COORDINATOR":
                case "ADMIN":
                    // Abre o dashboard do coordenador
                    SceneManager.changeScene("dashboard_coordenador.fxml");
                    break;

                default:
                    errorLabel.setText("Perfil de utilizador desconhecido: " + role);
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erro de conexão ou login inválido.");
        }
    }

    // Chamado pelo onAction="#voltar"
    @FXML
    public void voltar() {
        SceneManager.changeScene("welcome.fxml");
    }
}