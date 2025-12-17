package componente3.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import componente3.SceneManager;
import componente3.service.ApiClient;
import componente3.session.UserSession;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

// Repare que já não importamos PropostaFX aqui

public class ListarPropostasController {

    // 1. Mudamos o tipo da Tabela e Colunas para JSONObject
    @FXML private TableView<JSONObject> tabela;
    @FXML private TableColumn<JSONObject, String> colTitulo;
    @FXML private TableColumn<JSONObject, String> colEstado;
    @FXML private TableColumn<JSONObject, Integer> colVagas;
    @FXML private TableColumn<JSONObject, Integer> colDuracao;

    @FXML
    public void initialize() {
        configurarColunas();
        carregar();
    }

    private void configurarColunas() {
        // 
        
        // Título
        colTitulo.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().optString("titulo", "Sem Título")));

        // Estado (Confirme se a API envia "status" ou "estado")
        colEstado.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().optString("status", "-")));

        // Vagas (Inteiro)
        colVagas.setCellValueFactory(data -> 
            new SimpleIntegerProperty(data.getValue().optInt("numeroVagas", 0)).asObject());

        // Duração (Inteiro)
        colDuracao.setCellValueFactory(data -> 
            new SimpleIntegerProperty(data.getValue().optInt("duracaoMeses", 0)).asObject());
    }

    private void carregar() {
        try {
            String repId = UserSession.getId();
            
            // Vai buscar o array à API
            JSONArray arr = ApiClient.getArray("/api/propostas/representante/" + repId);

            ObservableList<JSONObject> lista = FXCollections.observableArrayList();

            // 2. Simplificação: Não criamos objetos Java, guardamos o JSON direto
            for (int i = 0; i < arr.length(); i++) {
                lista.add(arr.getJSONObject(i));
            }

            tabela.setItems(lista);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao carregar propostas: " + e.getMessage());
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_representante.fxml");
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(mensagem);
        alert.show();
    }
}
