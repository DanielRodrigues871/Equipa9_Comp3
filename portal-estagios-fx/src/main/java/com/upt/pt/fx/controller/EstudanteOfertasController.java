package com.upt.pt.fx.controller;

import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Optional;

public class EstudanteOfertasController {

    @FXML private TableView<JSONObject> tabelaOfertas;
    @FXML private TableColumn<JSONObject, String> colTitulo;
    @FXML private TableColumn<JSONObject, String> colEmpresa;
    @FXML private TableColumn<JSONObject, String> colLocal;
    @FXML private TableColumn<JSONObject, String> colVagas;

    @FXML
    public void initialize() {
        configurarColunas();
        carregarOfertas();
    }

    private void configurarColunas() {
        colTitulo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("titulo", "-")));
        
        colEmpresa.setCellValueFactory(data -> {
            JSONObject emp = data.getValue().optJSONObject("empresa");
            return new SimpleStringProperty(emp != null ? emp.optString("nome") : data.getValue().optString("empresaNome", "-"));
        });

        colLocal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("localizacao", "-")));
        colVagas.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().optInt("vagasDisponiveis"))));
    }

    private void carregarOfertas() {
        try {
            JSONArray jsonArray = ApiClient.getArray("/api/ofertas/status/APROVADA");
            ObservableList<JSONObject> lista = FXCollections.observableArrayList();
            
            for (int i = 0; i < jsonArray.length(); i++) {
                lista.add(jsonArray.getJSONObject(i));
            }
            tabelaOfertas.setItems(lista);
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar as ofertas.");
        }
    }

    @FXML
    public void candidatar() {
        JSONObject oferta = tabelaOfertas.getSelectionModel().getSelectedItem();

        if (oferta == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione uma oferta primeiro.");
            return;
        }

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Nova Candidatura");
        dialog.setHeaderText("Candidatar a: " + oferta.optString("titulo"));

        ButtonType enviarBtnType = new ButtonType("Enviar Candidatura", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(enviarBtnType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextArea cartaArea = new TextArea();
        cartaArea.setPromptText("Escreva aqui a sua carta de motivação...");
        cartaArea.setWrapText(true);
        cartaArea.setPrefHeight(150);
        cartaArea.setPrefWidth(400);

        grid.add(new Label("Carta de Motivação:"), 0, 0);
        grid.add(cartaArea, 0, 1);

        GridPane.setVgrow(cartaArea, Priority.ALWAYS);
        GridPane.setHgrow(cartaArea, Priority.ALWAYS);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == enviarBtnType) {
                return cartaArea.getText();
            }
            return null;
        });

        Optional<String> resultado = dialog.showAndWait();

        resultado.ifPresent(carta -> {
            enviarCandidaturaAPI(oferta.optString("id"), carta);
        });
    }

    private void enviarCandidaturaAPI(String ofertaId, String cartaMotivacao) {
        try {
            String estudanteId = UserSession.getId();

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("cartaMotivacao", cartaMotivacao);

            String endpoint = "/api/candidaturas?estudanteId=" + estudanteId + "&ofertaId=" + ofertaId;

            JSONObject resposta = ApiClient.post(endpoint, jsonBody);

            if (resposta.has("id")) {
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Candidatura submetida com sucesso!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "O servidor não devolveu o ID da candidatura.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType tipo, String titulo, String msg) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}