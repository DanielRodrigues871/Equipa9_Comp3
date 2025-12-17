package componente3.controller;

import componente3.SceneManager;
import componente3.model.EmpresaOption; // Vamos precisar desta classe (ver abaixo)
import componente3.service.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class RegisterRepresentanteController {

    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML private ComboBox<EmpresaOption> empresaCombo;
    
    @FXML private TextField cargoField;
    @FXML private TextField telefoneField;

    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        carregarEmpresas();
    }

    private void carregarEmpresas() {
        try {
            // Busca a lista de empresas à API
            JSONArray arr = ApiClient.getArray("/api/empresas");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject emp = arr.getJSONObject(i);
                // Adiciona à ComboBox (ID escondido, Nome visível)
                empresaCombo.getItems().add(new EmpresaOption(emp.getString("id"), emp.getString("nome")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (errorLabel != null) errorLabel.setText("Erro ao carregar lista de empresas.");
        }
    }

    @FXML
    public void registar() {
        if (errorLabel != null) errorLabel.setText("");

        try {
            // Validações Básicas
            if (nomeField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                if (errorLabel != null) errorLabel.setText("Preencha os dados obrigatórios.");
                return;
            }

            // Validar se escolheu empresa
            EmpresaOption empresaSelecionada = empresaCombo.getValue();
            if (empresaSelecionada == null) {
                if (errorLabel != null) errorLabel.setText("Selecione a sua empresa.");
                return;
            }

            JSONObject json = new JSONObject();
            json.put("nome", nomeField.getText());
            json.put("email", emailField.getText());
            json.put("password", passwordField.getText());
            json.put("tipo", "REPRESENTANTE");

            // --- MUDANÇA: Usar o ID do objeto selecionado na Combo ---
            json.put("empresaId", empresaSelecionada.getId());
            
            json.put("cargo", cargoField.getText());
            json.put("telefone", telefoneField.getText());

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