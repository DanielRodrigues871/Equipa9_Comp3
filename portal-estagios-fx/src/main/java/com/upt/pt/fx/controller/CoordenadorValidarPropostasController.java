package com.upt.pt.fx.controller;

import com.upt.pt.fx.service.ApiClient;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.json.JSONArray;
import org.json.JSONObject;

public class CoordenadorValidarPropostasController {

    @FXML private TableView<JSONObject> tabelaPendentes;
    @FXML private TableColumn<JSONObject, String> colId;
    @FXML private TableColumn<JSONObject, String> colTitulo;
    @FXML private TableColumn<JSONObject, String> colEmpresa;
    @FXML private TableColumn<JSONObject, String> colData;

    @FXML
    public void initialize() {
        configurarColunas();
        carregarPendentes();
    }

    private void configurarColunas() {
        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().optInt("id"))));
        colTitulo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("titulo", "-")));
        colEmpresa.setCellValueFactory(data -> {
            JSONObject emp = data.getValue().optJSONObject("empresa");
            return new SimpleStringProperty(emp != null ? emp.optString("nome", "N/A") : "N/A");
        });
        colData.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("dataCriacao", "-")));
    }

    private void carregarPendentes() {
        try {
            
            JSONArray jsonArray = ApiClient.getArray("/api/propostas");
            
            ObservableList<JSONObject> lista = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if ("PENDENTE".equalsIgnoreCase(obj.optString("status"))) {
                    lista.add(obj);
                }
            }
            tabelaPendentes.setItems(lista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void aprovar() {
        processar("aprovar");
    }

    @FXML
    public void rejeitar() {
        processar("rejeitar");
    }

    private void processar(String acao) {
        JSONObject item = tabelaPendentes.getSelectionModel().getSelectedItem();
        if (item == null) {
            showAlert("Selecione uma proposta primeiro.");
            return;
        }
        try {
            String id = String.valueOf(item.getInt("id"));
            ApiClient.post("/api/propostas/" + id + "/" + acao, new JSONObject());
            showAlert("Proposta " + acao + "da com sucesso!");
            carregarPendentes(); // Atualiza a tabela
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro ao processar.");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}