package componente3.controller;

import componente3.service.ApiClient;
import componente3.session.UserSession;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.json.JSONArray;
import org.json.JSONObject;

public class EstudanteMinhasCandidaturasController {

    @FXML 
    private TableView<JSONObject> tabelaCandidaturas;
    
    @FXML 
    private TableColumn<JSONObject, String> colOferta;
    
    @FXML 
    private TableColumn<JSONObject, String> colEmpresa;
    
    @FXML 
    private TableColumn<JSONObject, String> colData;
    
    @FXML 
    private TableColumn<JSONObject, String> colEstado;

    @FXML
    public void initialize() {
        configurarColunas();
        carregarCandidaturas();
    }

    private void configurarColunas() {
        // 1. Título da Oferta
        colOferta.setCellValueFactory(data -> {
            JSONObject json = data.getValue();
            // Verifica se os dados vêm num objeto "oferta" aninhado (DTO completo)
            if (json.has("oferta") && json.get("oferta") instanceof JSONObject) {
                return new SimpleStringProperty(json.getJSONObject("oferta").optString("titulo", "Sem Título"));
            }
            // Ou se vem "flat" (DTO simplificado)
            return new SimpleStringProperty(json.optString("ofertaTitulo", "Sem Título"));
        });

        // 2. Nome da Empresa
        colEmpresa.setCellValueFactory(data -> {
            JSONObject json = data.getValue();
            // Tenta buscar dentro de oferta -> empresaNome
            if (json.has("oferta") && json.getJSONObject("oferta").has("empresaNome")) {
                return new SimpleStringProperty(json.getJSONObject("oferta").optString("empresaNome", "-"));
            }
            // Fallback
            return new SimpleStringProperty(json.optString("empresaNome", "-"));
        });

        // 3. Data da Candidatura
        colData.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().optString("dataCandidatura", "-")));

        // 4. Estado (PENDENTE, APROVADA, REJEITADA)
        colEstado.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().optString("estado", "Desconhecido")));
    }

    private void carregarCandidaturas() {
        try {
            String estudanteId = UserSession.getId();
            
            // CHAMADA À API
            // Certifique-se que o backend tem este endpoint
            JSONArray jsonArray = ApiClient.getArray("/api/candidaturas/estudante/" + estudanteId);

            System.out.println("Candidaturas encontradas: " + jsonArray.length());

            ObservableList<JSONObject> lista = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                lista.add(jsonArray.getJSONObject(i));
            }
            
            tabelaCandidaturas.setItems(lista);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar candidaturas do aluno.");
        }
    }
}
