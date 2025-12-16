package com.upt.pt.fx.controller;

import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.json.JSONObject;

import java.io.IOException;

public class VerEmpresaController {

    @FXML private AnchorPane contentPane;
    @FXML private Label nomeLabel;
    @FXML private Label nifLabel;
    @FXML private Label areaLabel;  // ← ADICIONADO (estava no FXML)

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
        } catch (Exception e) {
            nomeLabel.setText("Erro ao carregar empresa!");
            e.printStackTrace();
        }
    }

    @FXML
    public void arquivarEmpresa() {
        try {
            String id = UserSession.getEmpresaId();
            ApiClient.delete("/api/empresas/" + id);
            loadView("coordenador_menu_empresas.fxml");  // ← MUDADO
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void voltar() {
        loadView("coordenador_menu_empresas.fxml");  // ← MUDADO
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + fxmlFile));
            Node view = loader.load();
            contentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) {
            System.err.println("ERRO ao carregar: " + fxmlFile);
            e.printStackTrace();
        }
    }
}
