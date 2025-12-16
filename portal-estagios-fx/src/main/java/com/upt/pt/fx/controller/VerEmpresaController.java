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
    @FXML private Label emailLabel;     // ← ADICIONADO
    @FXML private Label moradaLabel;    // ← ADICIONADO
    @FXML private Label estadoLabel;    // ← ADICIONADO
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
            emailLabel.setText("Email: " + obj.optString("email", "Não definido"));
            moradaLabel.setText("Morada: " + obj.optString("morada", "Não definida"));
            areaLabel.setText("Área: " + obj.optString("area", "Não definida"));
            estadoLabel.setText("Estado: " + (obj.optBoolean("ativa", true) ? "ATIVA" : "INATIVA"));
            
        } catch (Exception e) {
            nomeLabel.setText("Erro ao carregar empresa!");
            e.printStackTrace();
        }
    }

    @FXML
    public void arquivarEmpresa() {
        // SÓ COORDENADOR pode arquivar (representante não tem este botão no FXML)
        if (!"COORDENADOR".equals(UserSession.getTipo())) {
            return; // ignora silenciosamente
        }
        
        try {
            String id = UserSession.getEmpresaId();
            ApiClient.delete("/api/empresas/" + id);
            loadView("coordenador_menu_empresas.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void editar() {
        loadView("editar_empresa.fxml");
    }

    @FXML
    public void voltar() {
        // Volta ao menu correto dependendo do tipo de user
        String menu = UserSession.getTipo().equals("COORDENADOR") 
            ? "coordenador_menu_empresas.fxml" 
            : "representante_menu_empresas.fxml";
        loadView(menu);
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
