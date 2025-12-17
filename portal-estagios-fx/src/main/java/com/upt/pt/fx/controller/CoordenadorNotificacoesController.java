package com.upt.pt.fx.controller;

import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CoordenadorNotificacoesController {

    @FXML private TableView<JSONObject> tabelaNotificacoes;
    @FXML private TableColumn<JSONObject, Boolean> colSelecionar;
    @FXML private TableColumn<JSONObject, String> colTitulo;
    @FXML private TableColumn<JSONObject, String> colMensagem;
    @FXML private TableColumn<JSONObject, String> colData;
    @FXML private TableColumn<JSONObject, String> colEstado;
    @FXML private TableColumn<JSONObject, Void> colAcao;

    private Map<String, SimpleBooleanProperty> selectionMap = new HashMap<>();

    @FXML
    public void initialize() {
        configurarColunas();
        carregarNotificacoes();
    }
    
    @FXML
    public void atualizar() {
        carregarNotificacoes();
    }

    private void configurarColunas() {
        // 1. Checkbox
        colSelecionar.setCellValueFactory(data -> {
            String id = data.getValue().optString("id");
            selectionMap.putIfAbsent(id, new SimpleBooleanProperty(false));
            return selectionMap.get(id);
        });
        colSelecionar.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecionar));
        tabelaNotificacoes.setEditable(true);

        // 2. Título (Protegido contra null)
        colTitulo.setCellValueFactory(data -> {
            String t = data.getValue().optString("titulo");
            if (t == null || t.isEmpty() || t.equals("null")) t = "Nova Notificação";
            return new SimpleStringProperty(t);
        });

        // 3. Mensagem
        colMensagem.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("mensagem", "")));

        // 4. Data (Blindada contra erros de formato)
        colData.setCellValueFactory(data -> {
            JSONObject json = data.getValue();
            String rawDate = "";
            if (json.has("dataCriacao") && !json.isNull("dataCriacao")) {
                rawDate = json.getString("dataCriacao");
            } else if (json.has("data_criacao") && !json.isNull("data_criacao")) {
                rawDate = json.getString("data_criacao");
            }
            return new SimpleStringProperty(formatarData(rawDate));
        });

        // 5. Estado
        colEstado.setCellValueFactory(data -> {
            boolean lida = data.getValue().optBoolean("lida", false);
            int lidaInt = data.getValue().optInt("lida", 0);
            boolean isLida = lida || (lidaInt == 1);
            return new SimpleStringProperty(isLida ? "Lida" : "Nova");
        });
        
        colEstado.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Nova".equals(item)) {
                        setStyle("-fx-text-fill: #2980b9; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #7f8c8d;");
                    }
                }
            }
        });

        // 6. Botão
        colAcao.setCellFactory(param -> new TableCell<>() {
            private final Button btnLer = new Button("Marcar Lida");
            {
                btnLer.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 10px; -fx-cursor: hand;");
                btnLer.setOnAction(event -> {
                    JSONObject notif = getTableView().getItems().get(getIndex());
                    marcarIndividual(notif);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    JSONObject notif = getTableView().getItems().get(getIndex());
                    boolean lida = notif.optBoolean("lida", false) || notif.optInt("lida", 0) == 1;
                    if (!lida) setGraphic(btnLer);
                    else setGraphic(null);
                }
            }
        });
    }

    private void carregarNotificacoes() {
        try {
            selectionMap.clear();
            String idUser = UserSession.getId();
            
            JSONArray jsonArray = ApiClient.getArray("/api/notificacoes/coordenador/" + idUser);
            
            ObservableList<JSONObject> items = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                items.add(jsonArray.getJSONObject(i));
            }
            tabelaNotificacoes.setItems(items);
            tabelaNotificacoes.refresh();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Aviso", "Não foi possível carregar as notificações.");
        }
    }

    private String formatarData(String dataIso) {
        if (dataIso == null || dataIso.isEmpty() || dataIso.equals("-")) return "-";
        try {
            dataIso = dataIso.replace(" ", "T");
            if (dataIso.length() > 19) dataIso = dataIso.substring(0, 19);
            LocalDateTime data = LocalDateTime.parse(dataIso);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return data.format(formatter);
        } catch (Exception e) {
            return dataIso;
        }
    }

    private void marcarIndividual(JSONObject notif) {
        try {
            String id = notif.getString("id");
            ApiClient.post("/api/notificacoes/" + id + "/ler", new JSONObject());
            notif.put("lida", true);
            tabelaNotificacoes.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Erro ao atualizar.");
        }
    }

    @FXML
    public void marcarSelecionadasComoLidas() {
        boolean alguma = false;
        for (JSONObject notif : tabelaNotificacoes.getItems()) {
            String id = notif.optString("id");
            SimpleBooleanProperty checked = selectionMap.get(id);
            if (checked != null && checked.get()) {
                boolean jaLida = notif.optBoolean("lida", false) || notif.optInt("lida", 0) == 1;
                if (!jaLida) {
                    alguma = true;
                    marcarIndividual(notif);
                }
                checked.set(false);
            }
        }
        if (!alguma) showAlert("Info", "Nenhuma notificação nova selecionada.");
        else tabelaNotificacoes.refresh();
    }

    private void showAlert(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}