package com.upt.pt.fx.controller;

import com.upt.pt.fx.model.NotificationFX;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class NotificacoesController {

    @FXML private AnchorPane contentPane;
    @FXML private TableView<NotificationFX> tabela;
    @FXML private TableColumn<NotificationFX, String> colMensagem;
    @FXML private TableColumn<NotificationFX, String> colData;
    @FXML private TableColumn<NotificationFX, Boolean> colLida;
    @FXML private Label badgeNotificacoes;

    @FXML
    public void initialize() {
        colMensagem.setCellValueFactory(new PropertyValueFactory<>("mensagem"));
        colLida.setCellValueFactory(new PropertyValueFactory<>("lida"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        carregar();
        atualizarBadge();
    }

    private void carregar() {
        try {
            String userId = UserSession.getId();
            JSONArray arr = ApiClient.getArray("/api/notificacoes/utilizador/" + userId);
            ObservableList<NotificationFX> lista = FXCollections.observableArrayList();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject n = arr.getJSONObject(i);
                lista.add(new NotificationFX(
                    n.getString("id"),
                    n.getString("mensagem"),
                    n.getBoolean("lida"),
                    n.getString("dataCriacao")
                ));
            }
            tabela.setItems(lista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void marcarTodas() {
        try {
            for (NotificationFX n : tabela.getItems()) {
                if (!n.isLida()) {
                    ApiClient.put("/api/notificacoes/" + n.getId() + "/lida", new JSONObject());
                }
            }
            carregar();
            atualizarBadge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void voltar() {
        String dashboard;
        switch (UserSession.getTipo()) {
            case "ESTUDANTE":
                dashboard = "dashboard_estudante.fxml";
                break;
            case "COORDENADOR":
                dashboard = "dashboard_coordenador.fxml";
                break;
            case "REPRESENTANTE":
                dashboard = "dashboard_representante.fxml";
                break;
            default:
                dashboard = "welcome.fxml";
                break;
        }
        loadView(dashboard);
    }


    private void atualizarBadge() {
        try {
            long count = ApiClient.getObject("/api/notificacoes/utilizador/" 
                + UserSession.getId() + "/nao-lidas").getLong("value");
            badgeNotificacoes.setVisible(count > 0);
        } catch (Exception e) {
            badgeNotificacoes.setVisible(false);
        }
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + fxmlFile));
            Node view = loader.load();
            contentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) {
            System.err.println("ERRO ao carregar: " + fxmlFile);
            e.printStackTrace();
        }
    }
}
