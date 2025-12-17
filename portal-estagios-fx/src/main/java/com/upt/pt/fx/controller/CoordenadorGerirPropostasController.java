package com.upt.pt.fx.controller;

import com.upt.pt.fx.service.ApiClient;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.json.JSONArray;
import org.json.JSONObject;

public class CoordenadorGerirPropostasController {

    @FXML private TableView<JSONObject> tabelaTodas;
    @FXML private TableColumn<JSONObject, String> colId;
    @FXML private TableColumn<JSONObject, String> colTitulo;
    @FXML private TableColumn<JSONObject, String> colEmpresa;
    @FXML private TableColumn<JSONObject, String> colEstado;
    @FXML private TableColumn<JSONObject, String> colData;

    @FXML
    public void initialize() {
        configurarColunas();
        carregarTodas();
    }

    private void configurarColunas() {
        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().optInt("id"))));
        
        colTitulo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("titulo", "-")));
        
        // Trata o objeto aninhado 'empresa'
        colEmpresa.setCellValueFactory(data -> {
            JSONObject emp = data.getValue().optJSONObject("empresa");
            return new SimpleStringProperty(emp != null ? emp.optString("nome", "N/A") : "N/A");
        });

        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("status", "-")));
        
        colData.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("dataCriacao", "-")));
    }

    private void carregarTodas() {
        try {
            // procura TODAS as propostas ao backend
            JSONArray jsonArray = ApiClient.getArray("/api/propostas");
            
            ObservableList<JSONObject> lista = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                lista.add(jsonArray.getJSONObject(i));
            }
            tabelaTodas.setItems(lista);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível carregar a lista de propostas.");
        }
    }

    @FXML
    public void verDetalhes() {
        JSONObject item = tabelaTodas.getSelectionModel().getSelectedItem();
        
        if (item == null) {
            showAlert("Aviso", "Por favor, selecione uma proposta na tabela para ver os detalhes.");
            return;
        }

        // Constrói o texto do popup
        StringBuilder sb = new StringBuilder();
        sb.append("Título: ").append(item.optString("titulo")).append("\n");
        sb.append("Empresa: ").append(item.optJSONObject("empresa") != null ? item.getJSONObject("empresa").optString("nome") : "N/A").append("\n");
        sb.append("Estado Atual: ").append(item.optString("status")).append("\n\n");
        
        sb.append("--- Descrição ---\n");
        sb.append(item.optString("descricao", "Sem descrição.")).append("\n\n");
        
        sb.append("--- Objetivos ---\n");
        sb.append(item.optString("objetivos", "Sem objetivos definidos."));

        // Cria um alerta com TextArea para o texto não ficar cortado
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalhes da Proposta");
        alert.setHeaderText("Detalhes completos da proposta #" + item.optInt("id"));

        TextArea textArea = new TextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }

    private void showAlert(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}