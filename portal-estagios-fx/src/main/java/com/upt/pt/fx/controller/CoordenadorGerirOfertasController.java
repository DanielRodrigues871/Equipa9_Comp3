package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
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

public class CoordenadorGerirOfertasController {

    @FXML private TableView<JSONObject> tabelaOfertas;
    @FXML private TableColumn<JSONObject, String> colTitulo;
    @FXML private TableColumn<JSONObject, String> colEmpresa;
    @FXML private TableColumn<JSONObject, String> colEstado;
    @FXML private TableColumn<JSONObject, String> colVagas;

    @FXML
    public void initialize() {
        configurarColunas();
        carregarTodas();
    }

    private void configurarColunas() {
        colTitulo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("titulo", "-")));
        
        // Ajuste: Verifica se 'empresaNome' vem direto ou dentro de um objeto 'empresa'
        colEmpresa.setCellValueFactory(data -> {
            JSONObject json = data.getValue();
            if (json.has("empresaNome")) return new SimpleStringProperty(json.getString("empresaNome"));
            if (json.has("empresa")) return new SimpleStringProperty(json.getJSONObject("empresa").optString("nome", "-"));
            return new SimpleStringProperty("-");
        });

        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("status", "-")));
        colVagas.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().optInt("numeroVagas", 0))));
    }

    private void carregarTodas() {
        try {
            JSONArray jsonArray = ApiClient.getArray("/api/ofertas"); // Pega TODAS as ofertas
            ObservableList<JSONObject> lista = FXCollections.observableArrayList();
            
            for (int i = 0; i < jsonArray.length(); i++) {
                lista.add(jsonArray.getJSONObject(i));
            }
            
            tabelaOfertas.setItems(lista);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível carregar as ofertas.");
        }
    }

    @FXML
    public void verDetalhes() {
        JSONObject selecionada = tabelaOfertas.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            showAlert("Aviso", "Selecione uma oferta na tabela para ver os detalhes.");
            return;
        }

        // Construir o texto com todos os detalhes
        StringBuilder detalhes = new StringBuilder();
        detalhes.append("Título: ").append(selecionada.optString("titulo")).append("\n");
        detalhes.append("Empresa: ").append(selecionada.optString("empresaNome", "N/A")).append("\n");
        detalhes.append("Estado: ").append(selecionada.optString("status")).append("\n");
        detalhes.append("Vagas: ").append(selecionada.optInt("numeroVagas")).append("\n");
        detalhes.append("Salário: ").append(selecionada.optDouble("salario", 0.0)).append(" €\n");
        detalhes.append("Localização: ").append(selecionada.optString("localizacao", "N/A")).append("\n\n");
        
        detalhes.append("--- Descrição ---\n");
        detalhes.append(selecionada.optString("descricao", "Sem descrição.")).append("\n\n");
        
        detalhes.append("--- Requisitos ---\n");
        detalhes.append(selecionada.optString("requisitos", "Nenhum requisito especificado."));

        showDetalhesAlert("Detalhes da Oferta", detalhes.toString());
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_coordenador.fxml");
    }

    // --- Métodos Auxiliares para Alertas ---

    private void showAlert(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // Cria um alerta especial com uma caixa de texto grande para caber a descrição
    private void showDetalhesAlert(String titulo, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText("Resumo da oferta:");

        TextArea textArea = new TextArea(conteudo);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        // Define a área de texto expandível como o conteúdo do diálogo
        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setExpanded(true); // Abre já expandido
        alert.showAndWait();
    }
}