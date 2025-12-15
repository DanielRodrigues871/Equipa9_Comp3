package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONObject;

public class EditarEmpresaController {

    @FXML private TextField nomeField;
    @FXML private TextField areaField;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        carregarDados();
    }

    private void carregarDados() {
        try {
            JSONObject emp = ApiClient.getObject("/api/empresas/" + UserSession.getEmpresaId());
            nomeField.setText(emp.getString("nome"));
            areaField.setText(emp.getString("area"));
        }
        catch (Exception e) {
            errorLabel.setText("Erro ao carregar dados.");
        }
    }

    @FXML
    public void guardar() {
        try {
            JSONObject json = new JSONObject();
            json.put("nome", nomeField.getText());
            json.put("area", areaField.getText());

            ApiClient.put("/api/empresas/" + UserSession.getEmpresaId(), json);

            SceneManager.changeScene("menu_empresas.fxml");
        }
        catch (Exception e) {
            errorLabel.setText("Erro ao guardar.");
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("menu_empresas.fxml");
    }
}
