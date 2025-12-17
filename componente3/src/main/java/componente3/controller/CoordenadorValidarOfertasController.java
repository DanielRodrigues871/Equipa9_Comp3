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

public class CoordenadorValidarOfertasController {

    @FXML private TableView<JSONObject> tabelaPendentes;
    @FXML private TableColumn<JSONObject, String> colTitulo;
    @FXML private TableColumn<JSONObject, String> colEmpresa;
    @FXML private TableColumn<JSONObject, String> colTipo;
    @FXML private TableColumn<JSONObject, String> colData;

    @FXML
    public void initialize() {
        configurarColunas();
        carregarOfertasPendentes();
    }

    private void configurarColunas() {
        colTitulo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("titulo", "-")));
        colEmpresa.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("empresaNome", "-"))); // Ajuste conforme seu DTO
        colTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("tipo", "-")));
        colData.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("dataPublicacao", "-")));
    }

    private void carregarOfertasPendentes() {
        try {
            // Chama API filtrando apenas por PENDENTE
            JSONArray jsonArray = ApiClient.getArray("/api/ofertas/status/PENDENTE");
            ObservableList<JSONObject> lista = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) lista.add(jsonArray.getJSONObject(i));
            tabelaPendentes.setItems(lista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void aprovarOferta() {
        processarAcao("aprovar");
    }

    @FXML
    public void rejeitarOferta() {
        processarAcao("rejeitar");
    }

    private void processarAcao(String acao) {
        JSONObject selecionada = tabelaPendentes.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            showAlert("Selecione uma oferta primeiro.");
            return;
        }
        try {
            String id = selecionada.getString("id");
            // POST /api/ofertas/{id}/aprovar ou /rejeitar
            ApiClient.post("/api/ofertas/" + id + "/" + acao, new JSONObject());
            showAlert("Oferta " + acao + "da com sucesso!");
            carregarOfertasPendentes(); // Atualiza a tabela
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro ao processar oferta.");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}
