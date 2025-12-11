package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.json.JSONObject;

public class VerEmpresaController {

    @FXML private Label nomeLabel;
    @FXML private Label nifLabel;
    @FXML private Label areaLabel;

    @FXML
    public void initialize() {
        carregarEmpresa();
    }

    private void carregarEmpresa() {
        try {
            String id = UserSession.getEmpresaId();

            JSONObject obj = ApiClient.getObject("/api/empresas/" + id);

            nomeLabel.setText("Nome: " + obj.getString("nome"));
            nifLabel.setText("NIF: " + obj.getString("nif"));
            areaLabel.setText("Área: " + obj.getString("area"));
        }
        catch (Exception e) {
            nomeLabel.setText("Erro ao carregar empresa!");
            e.printStackTrace();
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("menu_empresas.fxml");
    }
}
