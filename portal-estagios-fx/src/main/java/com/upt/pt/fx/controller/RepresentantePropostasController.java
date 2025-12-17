package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

public class RepresentantePropostasController {

    @FXML private ListView<String> listaPropostas;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        carregar();
    }

    private void carregar() {
        try {
            String repId = UserSession.getId();
            
            // Vai buscar as propostas deste representante
            JSONArray array = ApiClient.getArray("/api/propostas/representante/" + repId);

            ObservableList<String> items = FXCollections.observableArrayList();

            if (array.length() == 0) {
                items.add("Nenhuma proposta encontrada.");
            } else {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);

                  
                    String id = String.valueOf(obj.get("id")); 
                    
                    String titulo = obj.getString("titulo");
                    String status = obj.optString("status", "N/A"); // optString previne erros se for null

                    // Formatar o texto da lista
                    String texto = String.format("[%s] %s - %s", id, titulo, status);
                    items.add(texto);
                }
            }

            listaPropostas.setItems(items);
            
            // Adicionar evento de clique para editar (Opcional, mas útil para o futuro)
            listaPropostas.setOnMouseClicked(event -> {
                String selected = listaPropostas.getSelectionModel().getSelectedItem();
                if (selected != null && !selected.startsWith("Nenhuma")) {
                    // Extrair ID: "[1] Titulo..." -> "1"
                    String idStr = selected.substring(selected.indexOf("[") + 1, selected.indexOf("]"));
                    UserSession.setPropostaEditarId(idStr);
                    // SceneManager.changeScene("editar_proposta.fxml"); // Futuro
                    System.out.println("Selecionou ID: " + idStr);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Erro ao carregar propostas.");
        }
    }

    @FXML
    public void criarNova() {
        SceneManager.changeScene("representante_criar_proposta.fxml");
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_representante.fxml");
    }
}