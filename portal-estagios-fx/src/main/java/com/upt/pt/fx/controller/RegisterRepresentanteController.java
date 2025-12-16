package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONArray;
import org.json.JSONObject;
import com.upt.pt.fx.model.EmpresaOption;


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
            JSONArray arr = ApiClient.getArray("/api/empresas");
            empresaCombo.getItems().clear();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject e = arr.getJSONObject(i);
                empresaCombo.getItems().add(
                        new EmpresaOption(
                                e.getString("id"),
                                e.getString("nome")
                        )
                );
            }
        } catch (Exception e) {
            errorLabel.setText("Erro ao carregar empresas.");
        }
    }

    @FXML
    public void registar() {
        try {
            EmpresaOption empresa = empresaCombo.getValue();
            if (empresa == null) {
                errorLabel.setText("Selecione uma empresa.");
                return;
            }

            JSONObject json = new JSONObject();
            json.put("nome", nomeField.getText());
            json.put("email", emailField.getText());
            json.put("password", passwordField.getText());
            json.put("tipo", "REPRESENTANTE");

            json.put("empresaId", empresa.getId());
            json.put("cargo", cargoField.getText());
            json.put("telefone", telefoneField.getText());

            ApiClient.post("/api/auth/register", json);

            SceneManager.changeScene("login.fxml");

        } catch (Exception e) {
            errorLabel.setText("Erro no registo do representante!");
        }
    }

    @FXML
    public void criarEmpresa() {
        SceneManager.changeScene("criar_empresa.fxml");
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("register.fxml");
    }
}
