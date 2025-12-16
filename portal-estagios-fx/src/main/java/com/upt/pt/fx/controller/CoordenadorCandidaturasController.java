package com.upt.pt.fx.controller;

import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Optional;

public class CoordenadorCandidaturasController {

    @FXML private TableView<JSONObject> tabelaCandidaturas;
    @FXML private TableColumn<JSONObject, String> colEstudante;
    @FXML private TableColumn<JSONObject, String> colOferta;
    @FXML private TableColumn<JSONObject, String> colData;
    @FXML private TableColumn<JSONObject, String> colEstado;

    // Filtros
    @FXML private TextField txtFiltroOferta;
    @FXML private TextField txtFiltroEstudante;

    @FXML
    public void initialize() {
        configurarColunas();
        carregarTodas(); // Por defeito carrega todas
    }

    private void configuringColunas() {
        colEstudante.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("estudanteNome", "N/A")));
        colOferta.setCellValueFactory(data -> {
            JSONObject json = data.getValue();
            // Verifica se vem aninhado ou flat
            if(json.has("oferta") && json.get("oferta") instanceof JSONObject) 
                return new SimpleStringProperty(json.getJSONObject("oferta").optString("titulo", "-"));
            return new SimpleStringProperty(json.optString("ofertaTitulo", "-"));
        });
        colData.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("dataCandidatura", "-")));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("estado", "-")));
    }
    
    private void configurarColunas() {
         colEstudante.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("estudanteNome", "N/A")));
        colOferta.setCellValueFactory(data -> {
            JSONObject json = data.getValue();
            // Verifica se vem aninhado ou flat
            if(json.has("oferta") && json.get("oferta") instanceof JSONObject) 
                return new SimpleStringProperty(json.getJSONObject("oferta").optString("titulo", "-"));
            return new SimpleStringProperty(json.optString("ofertaTitulo", "-"));
        });
        colData.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("dataCandidatura", "-")));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("estado", "-")));
    }

    // --- LÓGICA DE CARREGAMENTO E FILTROS ---

    private void carregarTodas() {
        carregarEndpoint("/api/candidaturas");
    }

    @FXML
    public void aplicarFiltros() {
        String ofertaId = txtFiltroOferta.getText().trim();
        String estId = txtFiltroEstudante.getText().trim();

        if (!ofertaId.isEmpty()) {
            // Prioridade ao filtro de oferta
            carregarEndpoint("/api/candidaturas/oferta/" + ofertaId);
        } else if (!estId.isEmpty()) {
            // Se não tem oferta, tenta estudante
            carregarEndpoint("/api/candidaturas/estudante/" + estId);
        } else {
            // Se ambos vazios, carrega tudo
            carregarTodas();
        }
    }

    @FXML
    public void limparFiltros() {
        txtFiltroOferta.clear();
        txtFiltroEstudante.clear();
        carregarTodas();
    }

    private void carregarEndpoint(String endpoint) {
        try {
            JSONArray jsonArray = ApiClient.getArray(endpoint);
            ObservableList<JSONObject> lista = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) lista.add(jsonArray.getJSONObject(i));
            tabelaCandidaturas.setItems(lista);
        } catch (Exception e) {
            e.printStackTrace();
            // Se der erro (ex: ID não existe), limpa a tabela
            tabelaCandidaturas.setItems(FXCollections.observableArrayList());
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao carregar dados. Verifique os IDs.");
        }
    }

    // --- NOVA CANDIDATURA MANUAL ---

    @FXML
    public void criarCandidaturaManual() {
        // Cria um Dialog personalizado
        Dialog<JSONObject> dialog = new Dialog<>();
        dialog.setTitle("Nova Candidatura Manual");
        dialog.setHeaderText("Insira os IDs para criar a candidatura");

        ButtonType btnCriar = new ButtonType("Criar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnCriar, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtEstudante = new TextField();
        txtEstudante.setPromptText("ID do Estudante");
        TextField txtOferta = new TextField();
        txtOferta.setPromptText("ID da Oferta");
        TextArea txtCarta = new TextArea();
        txtCarta.setPromptText("Carta de Motivação");
        txtCarta.setPrefHeight(100);

        grid.add(new Label("Estudante ID:"), 0, 0);
        grid.add(txtEstudante, 1, 0);
        grid.add(new Label("Oferta ID:"), 0, 1);
        grid.add(txtOferta, 1, 1);
        grid.add(new Label("Carta:"), 0, 2);
        grid.add(txtCarta, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Converte o resultado
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnCriar) {
                JSONObject json = new JSONObject();
                json.put("estudanteId", txtEstudante.getText());
                json.put("ofertaId", txtOferta.getText());
                json.put("carta", txtCarta.getText());
                return json;
            }
            return null;
        });

        Optional<JSONObject> result = dialog.showAndWait();

        result.ifPresent(dados -> {
            try {
                String estId = dados.getString("estudanteId");
                String ofId = dados.getString("ofertaId");
                String carta = dados.getString("carta");

                String endpoint = String.format("/api/candidaturas?estudanteId=%s&ofertaId=%s", estId, ofId);
                
                JSONObject body = new JSONObject();
                body.put("cartaMotivacao", carta);

                ApiClient.post(endpoint, body);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Candidatura criada com sucesso!");
                carregarTodas(); // Atualiza a tabela

            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Erro ao criar candidatura: " + e.getMessage());
            }
        });
    }

    // --- AÇÕES DE WORKFLOW (Aprovar/Rejeitar) ---

    @FXML
    public void colocarEmAnalise() { mudarEstado("analise", null); }

    @FXML
    public void aprovar() { 
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText("Observações (Opcional)");
        td.showAndWait().ifPresent(obs -> mudarEstado("aprovar", obs));
    }

    @FXML
    public void rejeitar() {
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText("Motivo (Obrigatório)");
        td.showAndWait().ifPresent(obs -> {
            if(!obs.isBlank()) mudarEstado("rejeitar", obs);
        });
    }

    private void mudarEstado(String acao, String obs) {
        JSONObject selecionada = tabelaCandidaturas.getSelectionModel().getSelectedItem();
        if(selecionada == null) return;
        try {
            String id = selecionada.getString("id");
            String coord = UserSession.getId();
            String url = "/api/candidaturas/" + id + "/" + acao + "?coordenadorId=" + coord;
            JSONObject body = new JSONObject();
            if(obs != null) body.put("observacoes", obs);
            ApiClient.post(url, body);
            aplicarFiltros(); // Recarrega mantendo o filtro atual se houver
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String msg) {
        Alert a = new Alert(tipo); a.setContentText(msg); a.show();
    }
}