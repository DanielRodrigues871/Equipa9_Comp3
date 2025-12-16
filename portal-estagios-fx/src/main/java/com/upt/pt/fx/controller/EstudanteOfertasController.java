package com.upt.pt.fx.controller;

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

        // 2. Empresa (DTO: empresaNome - CORREÇÃO AQUI)
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
            // Chamamos o endpoint que filtra por status APROVADA
            // Certifique-se que tem ofertas com status 'APROVADA' na BD
            JSONArray jsonArray = ApiClient.getArray("/api/ofertas/status/APROVADO");
            
            // DEBUG: Ver o que chega
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
            // IDs
            String ofertaId = ofertaSelecionada.getString("id"); // ou .optString("id")
            String estudanteId = UserSession.getId(); 

            // --- CORREÇÃO AQUI ---
            // O seu Controller exige RequestParam (?estudanteId=...&ofertaId=...)
            // e também exige um Body (CandidaturaDTO), mesmo que vá vazio.
            
            String endpoint = String.format("/api/candidaturas?estudanteId=%s&ofertaId=%s", 
                                            estudanteId, ofertaId);
            
            // Enviamos um JSON vazio porque o @RequestBody é obrigatório no Spring, 
            // mas os dados importantes vão na URL.
            JSONObject jsonBody = new JSONObject(); 
            
            // Envia candidatura
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

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}