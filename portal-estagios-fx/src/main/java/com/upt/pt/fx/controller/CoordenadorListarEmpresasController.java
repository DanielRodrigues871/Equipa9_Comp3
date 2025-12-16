package com.upt.pt.fx.controller;

import com.upt.pt.fx.model.EmpresaFX;
import com.upt.pt.fx.service.ApiClient;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class CoordenadorListarEmpresasController {  // ← NOME MUDADO

    @FXML private AnchorPane contentPane;
    @FXML private TableView<EmpresaFX> empresasTable;
    @FXML private TableColumn<EmpresaFX, String> nomeCol;
    @FXML private TableColumn<EmpresaFX, String> nifCol;
    @FXML private TableColumn<EmpresaFX, String> statusCol;
    @FXML private Label mensagemLabel;

    @FXML
    public void initialize() {
        nomeCol.setCellValueFactory(data -> data.getValue().nomeProperty());
        nifCol.setCellValueFactory(data -> data.getValue().nifProperty());
        statusCol.setCellValueFactory(data -> data.getValue().statusProperty());
        recarregar();
    }

    @FXML
    public void recarregar() {
        try {
            JSONArray arr = ApiClient.getArray("/api/empresas");
            ObservableList<EmpresaFX> lista = FXCollections.observableArrayList();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String id = obj.getString("id");
                String nome = obj.getString("nome");
                String nif = obj.getString("nif");
                String status = obj.optString("status",
                        obj.optBoolean("ativa", true) ? "ATIVA" : "INATIVA");
                lista.add(new EmpresaFX(id, nome, nif, status));
            }
            empresasTable.setItems(lista);
            mensagemLabel.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            mensagemLabel.setText("Erro ao carregar empresas.");
        }
    }

    @FXML
    public void ativarSelecionada() {
        EmpresaFX emp = empresasTable.getSelectionModel().getSelectedItem();
        if (emp == null) {
            mensagemLabel.setText("Selecione uma empresa.");
            return;
        }
        try {
            ApiClient.post("/api/empresas/" + emp.getId() + "/ativar", null);
            recarregar();
            mensagemLabel.setText("Empresa ativada.");
        } catch (Exception e) {
            e.printStackTrace();
            mensagemLabel.setText("Erro ao ativar.");
        }
    }

    @FXML
    public void desativarSelecionada() {
        EmpresaFX emp = empresasTable.getSelectionModel().getSelectedItem();
        if (emp == null) {
            mensagemLabel.setText("Selecione uma empresa.");
            return;
        }
        try {
            ApiClient.post("/api/empresas/" + emp.getId() + "/desativar", null);
            recarregar();
            mensagemLabel.setText("Empresa desativada.");
        } catch (Exception e) {
            e.printStackTrace();
            mensagemLabel.setText("Erro ao desativar.");
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
