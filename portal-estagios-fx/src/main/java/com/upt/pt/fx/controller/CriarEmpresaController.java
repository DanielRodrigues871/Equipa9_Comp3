package com.upt.pt.fx.controller;

import com.upt.pt.fx.service.ApiClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.json.JSONObject;

import java.io.IOException;

public class CriarEmpresaController {

    @FXML private AnchorPane contentPane;
    @FXML private TextField txtNome;
    @FXML private TextField txtNif;
    @FXML private TextField txtEmail;
    @FXML private TextField txtMorada;
    @FXML private Label lblErro;

    @FXML
    public void criar() {
        try {
            JSONObject body = new JSONObject();
            body.put("nome", txtNome.getText());
            body.put("nif", txtNif.getText());
            body.put("email", txtEmail.getText());
            body.put("morada", txtMorada.getText());
            body.put("ativa", true);

            ApiClient.post("/api/empresas", body);
            lblErro.setText("Empresa criada com sucesso!");
            txtNome.clear(); txtNif.clear(); txtEmail.clear(); txtMorada.clear(); // limpa campos

        } catch (Exception e) {
            lblErro.setText("Erro ao criar empresa.");
            e.printStackTrace();
        }
    }

    @FXML
    public void voltar() {
        loadView("registo_representante.fxml");
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
