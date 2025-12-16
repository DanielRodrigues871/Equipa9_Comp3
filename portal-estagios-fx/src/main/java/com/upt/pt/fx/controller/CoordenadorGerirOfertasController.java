package com.upt.pt.fx.controller;

import com.upt.pt.fx.service.ApiClient;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.json.JSONArray;
import org.json.JSONObject;

public class CoordenadorGerirOfertasController {
    @FXML private TableView<JSONObject> tabelaOfertas;
    @FXML private TableColumn<JSONObject, String> colTitulo;
    @FXML private TableColumn<JSONObject, String> colEmpresa;
    @FXML private TableColumn<JSONObject, String> colEstado;
    @FXML private TableColumn<JSONObject, String> colVagas;

    @FXML
    public void initialize() {
        colTitulo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("titulo", "-")));
        colEmpresa.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("empresaNome", "-")));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("status", "-")));
        colVagas.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().optInt("numeroVagas", 0))));

        carregarTodas();
    }

    private void carregarTodas() {
        try {
            JSONArray jsonArray = ApiClient.getArray("/api/ofertas"); // Pega TODAS
            ObservableList<JSONObject> lista = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) lista.add(jsonArray.getJSONObject(i));
            tabelaOfertas.setItems(lista);
        } catch (Exception e) { e.printStackTrace(); }
    }
}