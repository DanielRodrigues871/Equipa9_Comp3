package componente3.controller;

import componente3.service.ApiClient;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.json.JSONArray;
import org.json.JSONObject;

public class CoordenadorEmpresasController {

    @FXML private TableView<JSONObject> tabelaEmpresas;
    @FXML private TableColumn<JSONObject, String> colNome;
    @FXML private TableColumn<JSONObject, String> colEmail;
    @FXML private TableColumn<JSONObject, String> colNif;
    @FXML private TableColumn<JSONObject, String> colArea;
    @FXML private TableColumn<JSONObject, String> colEstado;

    @FXML
    public void initialize() {
        configurarColunas();
        carregarEmpresas();
    }

    private void configurarColunas() {
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("nome")));
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("email")));
        colNif.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("nif")));
        colArea.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("areaAtuacao")));
        
        // Mostra "ATIVO" ou "INATIVO" baseado no booleano
        colEstado.setCellValueFactory(data -> {
            boolean ativo = data.getValue().optBoolean("ativo", true);
            return new SimpleStringProperty(ativo ? "ATIVO" : "INATIVO");
        });
    }

    private void carregarEmpresas() {
        try {
            JSONArray arr = ApiClient.getArray("/api/empresas");
            ObservableList<JSONObject> lista = FXCollections.observableArrayList();
            for (int i = 0; i < arr.length(); i++) lista.add(arr.getJSONObject(i));
            tabelaEmpresas.setItems(lista);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void ativar() { alterarEstado("ativar"); }

    @FXML
    public void desativar() { alterarEstado("desativar"); }

    private void alterarEstado(String acao) {
        JSONObject selecionada = tabelaEmpresas.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            showAlert("Selecione uma empresa.");
            return;
        }
        try {
            String id = selecionada.getString("id");
            // API: POST /api/empresas/{id}/ativar
            ApiClient.post("/api/empresas/" + id + "/" + acao, new JSONObject());
            carregarEmpresas(); // Atualiza a tabela
            showAlert("Estado alterado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro ao alterar estado.");
        }
    }
    
    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION); a.setContentText(msg); a.show();
    }
}
