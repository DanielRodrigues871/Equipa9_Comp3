package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.model.EmpresaFX;
import com.upt.pt.fx.service.ApiClient;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.ObservableList;



import org.json.JSONArray;
import org.json.JSONObject;

public class ListarEmpresasController {

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
                // assume que o DTO tem campo "status" ou então usa "ativa"
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
        SceneManager.changeScene("menu_empresas.fxml");
    }
}
