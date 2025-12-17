package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONObject;

public class RepresentanteEmpresaController {

    @FXML private TextField txtNome;
    @FXML private TextField txtNif;
    @FXML private TextField txtEmail;
    @FXML private TextField txtArea;
    
    @FXML private Label lblStatus; // Para mostrar mensagens de erro/sucesso

    @FXML
    public void initialize() {
        carregarDados();
    }

    private void carregarDados() {
        try {
            String empresaId = UserSession.getEmpresaId();
            if (empresaId == null) {
                lblStatus.setText("Erro: ID de empresa não encontrado na sessão.");
                return;
            }

            // Usa o novo método getJson que adicionámos ao ApiClient
            JSONObject json = ApiClient.getJson("/api/empresas/" + empresaId);

            txtNome.setText(json.optString("nome", ""));
            txtNif.setText(json.optString("nif", ""));
            txtEmail.setText(json.optString("email", ""));
            txtArea.setText(json.optString("areaAtuacao", ""));

        } catch (Exception e) {
            e.printStackTrace();
            lblStatus.setText("Erro ao carregar dados: " + e.getMessage());
            lblStatus.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void guardar() {
        try {
            String empresaId = UserSession.getEmpresaId();
            
            // 1. Criar JSON com os dados atualizados
            JSONObject json = new JSONObject();
            json.put("nome", txtNome.getText());
            json.put("nif", txtNif.getText());
            json.put("email", txtEmail.getText());
            json.put("areaAtuacao", txtArea.getText());

            // 2. Enviar pedido PUT para a API
            ApiClient.put("/api/empresas/" + empresaId, json);

            // 3. Feedback visual
            lblStatus.setText("Dados atualizados com sucesso!");
            lblStatus.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

        } catch (Exception e) {
            e.printStackTrace();
            lblStatus.setText("Erro ao guardar: " + e.getMessage());
            lblStatus.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_representante.fxml");
    }
}