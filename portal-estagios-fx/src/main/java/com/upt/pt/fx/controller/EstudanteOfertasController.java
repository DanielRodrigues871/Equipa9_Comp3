package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.beans.property.SimpleIntegerProperty;
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

public class EstudanteOfertasController {

    @FXML private TableView<JSONObject> tabelaOfertas;
    @FXML private TableColumn<JSONObject, String> colTitulo;
    @FXML private TableColumn<JSONObject, String> colEmpresa;
    @FXML private TableColumn<JSONObject, String> colTipo;
    @FXML private TableColumn<JSONObject, Integer> colDuracao;
    @FXML private TableColumn<JSONObject, Integer> colVagas;

    @FXML
    public void initialize() {
        configurarColunas();
        carregarOfertas();
    }

    private void configurarColunas() {
        // 1. Título (DTO: titulo)
        colTitulo.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().optString("titulo", "Sem Título")));

        // 2. Empresa (DTO: empresaNome)
        colEmpresa.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().optString("empresaNome", "Anónimo")));

        // 3. Tipo (DTO: tipo)
        colTipo.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().optString("tipo", "-")));

        // 4. Duração (DTO: duracaoMeses)
        colDuracao.setCellValueFactory(data -> 
            new SimpleIntegerProperty(data.getValue().optInt("duracaoMeses", 0)).asObject());

        // 5. Vagas (DTO: numeroVagas)
        colVagas.setCellValueFactory(data -> 
            new SimpleIntegerProperty(data.getValue().optInt("numeroVagas", 0)).asObject());
    }

    @FXML
    public void carregarOfertas() {
        try {
            JSONArray jsonArray = ApiClient.getArray("/api/ofertas/status/APROVADO");
            System.out.println("Ofertas recebidas: " + jsonArray.length());

            ObservableList<JSONObject> lista = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                lista.add(jsonArray.getJSONObject(i));
            }
            tabelaOfertas.setItems(lista);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar ofertas.");
        }
    }

    @FXML
    public void candidatar() {
        JSONObject ofertaSelecionada = tabelaOfertas.getSelectionModel().getSelectedItem();
        
        if (ofertaSelecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma oferta na lista primeiro.");
            return;
        }

        try {
            String ofertaId = ofertaSelecionada.getString("id");
            String estudanteId = UserSession.getId(); 

            String endpoint = String.format("/api/candidaturas?estudanteId=%s&ofertaId=%s", 
                                            estudanteId, ofertaId);
            
            JSONObject jsonBody = new JSONObject(); 
            JSONObject response = ApiClient.post(endpoint, jsonBody);

            if (response.has("id")) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Candidatura submetida com sucesso!");
            } else {
                 mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível candidatar.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Falha ao comunicar com o servidor.");
        }
    }

    // Ver Detalhes da Oferta Selecionada
    @FXML
    public void verDetalhes() {
        JSONObject selecionada = tabelaOfertas.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma oferta primeiro.");
            return;
        }

        StringBuilder detalhes = new StringBuilder();
        detalhes.append("Título: ").append(selecionada.optString("titulo")).append("\n");
        detalhes.append("Empresa: ").append(selecionada.optString("empresaNome", "N/A")).append("\n");
        detalhes.append("Tipo: ").append(selecionada.optString("tipo")).append("\n");
        detalhes.append("Duração: ").append(selecionada.optInt("duracaoMeses")).append(" meses\n");
        detalhes.append("Vagas: ").append(selecionada.optInt("numeroVagas")).append("\n");
        detalhes.append("Descrição: ").append(selecionada.optString("descricao", "Sem descrição."));

        showDetalhesAlert("Detalhes da Oferta", detalhes.toString());
    }

    // Voltar ao Dashboard
    @FXML
    public void voltarDashboard() {
        SceneManager.changeScene("dashboard_estudante.fxml");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    // Alerta expandível para detalhes longos
    private void showDetalhesAlert(String titulo, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);

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

        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }
}