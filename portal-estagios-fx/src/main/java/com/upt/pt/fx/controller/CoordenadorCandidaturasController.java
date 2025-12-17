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
import javafx.util.StringConverter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Optional;

public class CoordenadorCandidaturasController {

    @FXML private TableView<JSONObject> tabelaCandidaturas;
    @FXML private TableColumn<JSONObject, String> colEstudante;
    @FXML private TableColumn<JSONObject, String> colOferta;
    @FXML private TableColumn<JSONObject, String> colData;
    @FXML private TableColumn<JSONObject, String> colEstado;

    @FXML private ComboBox<JSONObject> cbFiltroOferta;
    @FXML private ComboBox<JSONObject> cbFiltroEstudante;

    @FXML
    public void initialize() {
        configurarColunas();
        configurarFiltros(); 
        carregarTodas();    
    }

    private void configuringColunas() {
        colEstudante.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("estudanteNome", "N/A")));
        colOferta.setCellValueFactory(data -> {
            JSONObject json = data.getValue();
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
            if(json.has("oferta") && json.get("oferta") instanceof JSONObject) 
                return new SimpleStringProperty(json.getJSONObject("oferta").optString("titulo", "-"));
            return new SimpleStringProperty(json.optString("ofertaTitulo", "-"));
        });
        colData.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("dataCandidatura", "-")));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("estado", "-")));
    }

    // Configuração das ComboBox de Filtro 
    private void configurarFiltros() {
        // Converter JSON para Texto Bonito na Oferta
        cbFiltroOferta.setConverter(new StringConverter<>() {
            @Override
            public String toString(JSONObject o) {
                if (o == null) return null;
                String empresa = o.optString("empresaNome", "");
                if (o.has("empresa") && o.get("empresa") instanceof JSONObject) {
                    empresa = o.getJSONObject("empresa").optString("nome");
                }
                return o.optString("titulo", "?") + (empresa.isEmpty() ? "" : " (" + empresa + ")");
            }
            @Override public JSONObject fromString(String s) { return null; }
        });

        // Converter JSON para Texto Bonito no Estudante
        cbFiltroEstudante.setConverter(new StringConverter<>() {
            @Override
            public String toString(JSONObject e) {
                if (e == null) return null;
                return e.optString("nome", "?") + " (" + e.optString("numeroEstudante", "?") + ")";
            }
            @Override public JSONObject fromString(String s) { return null; }
        });

        // Carregar dados da API para as combos
        carregarListasFiltro();
    }

    private void carregarListasFiltro() {
        try {
            // Ofertas
            JSONArray arrOfertas = ApiClient.getArray("/api/ofertas");
            ObservableList<JSONObject> listOfertas = FXCollections.observableArrayList();
            for(int i=0; i<arrOfertas.length(); i++) listOfertas.add(arrOfertas.getJSONObject(i));
            cbFiltroOferta.setItems(listOfertas);

            // Estudantes
            JSONArray arrEstudantes = ApiClient.getArray("/api/estudantes");
            ObservableList<JSONObject> listEstudantes = FXCollections.observableArrayList();
            for(int i=0; i<arrEstudantes.length(); i++) listEstudantes.add(arrEstudantes.getJSONObject(i));
            cbFiltroEstudante.setItems(listEstudantes);

        } catch (Exception e) {
            System.err.println("Aviso: Não foi possível carregar listas para os filtros.");
            e.printStackTrace();
        }
    }

    // --- LÓGICA DE APLICAR FILTROS ---

    private void carregarTodas() {
        carregarEndpoint("/api/candidaturas");
    }

    @FXML
    public void aplicarFiltros() {
        JSONObject ofertaSel = cbFiltroOferta.getValue();
        JSONObject estudanteSel = cbFiltroEstudante.getValue();

        if (ofertaSel != null) {
            // Filtra por Oferta
            String ofertaId = ofertaSel.getString("id");
            carregarEndpoint("/api/candidaturas/oferta/" + ofertaId);
            
            // Limpa a outra combo para não confundir visualmente
            cbFiltroEstudante.getSelectionModel().clearSelection(); 
            
        } else if (estudanteSel != null) {
            // Filtra por Estudante
            String estId = estudanteSel.getString("id");
            carregarEndpoint("/api/candidaturas/estudante/" + estId);
            
        } else {
            // Nenhum selecionado -> Carrega tudo
            carregarTodas();
        }
    }

    @FXML
    public void limparFiltros() {
        cbFiltroOferta.getSelectionModel().clearSelection();
        cbFiltroEstudante.getSelectionModel().clearSelection();
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
            tabelaCandidaturas.setItems(FXCollections.observableArrayList());
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao carregar dados filtrados.");
        }
    }

    @FXML
    public void criarCandidaturaManual() {
        
        Dialog<JSONObject> dialog = new Dialog<>();
        dialog.setTitle("Nova Candidatura Manual");
        dialog.setHeaderText("Selecione o Estudante e a Oferta");
        ButtonType btnCriar = new ButtonType("Criar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnCriar, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<JSONObject> cbEstPopup = new ComboBox<>(cbFiltroEstudante.getItems());
        ComboBox<JSONObject> cbOfPopup = new ComboBox<>(cbFiltroOferta.getItems());   
        cbEstPopup.setConverter(cbFiltroEstudante.getConverter());
        cbOfPopup.setConverter(cbFiltroOferta.getConverter());
        cbEstPopup.setPrefWidth(300); cbOfPopup.setPrefWidth(300);

        TextArea txtCarta = new TextArea();
        txtCarta.setPromptText("Carta de Motivação");
        txtCarta.setPrefHeight(100);

        grid.add(new Label("Estudante:"), 0, 0); grid.add(cbEstPopup, 1, 0);
        grid.add(new Label("Oferta:"), 0, 1);    grid.add(cbOfPopup, 1, 1);
        grid.add(new Label("Carta:"), 0, 2);     grid.add(txtCarta, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(btn -> {
            if (btn == btnCriar && cbEstPopup.getValue() != null && cbOfPopup.getValue() != null) {
                JSONObject j = new JSONObject();
                j.put("estudanteId", cbEstPopup.getValue().getString("id"));
                j.put("ofertaId", cbOfPopup.getValue().getString("id"));
                j.put("carta", txtCarta.getText());
                return j;
            } return null;
        });

        dialog.showAndWait().ifPresent(dados -> {
            try {
                String u = "/api/candidaturas?estudanteId=" + dados.getString("estudanteId") + "&ofertaId=" + dados.getString("ofertaId");
                JSONObject b = new JSONObject(); b.put("cartaMotivacao", dados.getString("carta"));
                ApiClient.post(u, b);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso!");
                carregarTodas();
            } catch(Exception e) { e.printStackTrace(); mostrarAlerta(Alert.AlertType.ERROR, "Erro: " + e.getMessage()); }
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
            aplicarFiltros(); 
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String msg) {
        Alert a = new Alert(tipo); a.setContentText(msg); a.show();
    }
}