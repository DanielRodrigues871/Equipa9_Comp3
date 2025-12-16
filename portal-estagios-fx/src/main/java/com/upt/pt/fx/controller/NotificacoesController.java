package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.model.NotificationFX;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

public class NotificacoesController {

    @FXML private TableView<NotificationFX> tabela;
    @FXML private TableColumn<NotificationFX, String> colMensagem;
    @FXML private TableColumn<NotificationFX, String> colData;
    @FXML private TableColumn<NotificationFX, Boolean> colLida;

    @FXML
    public void initialize() {
        colMensagem.setCellValueFactory(new PropertyValueFactory<>("mensagem"));
        colLida.setCellValueFactory(new PropertyValueFactory<>("lida"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));

        carregar();
    }

    //CARREGAR
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
                    ApiClient.put(
                            "/api/notificacoes/" + n.getId() + "/lida",
                            new JSONObject()
                    );
                }
            }
            carregar(); // refrescar tabela
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void voltar() {
        switch (UserSession.getTipo()) {
            case "ESTUDANTE":
                SceneManager.changeScene("dashboard_estudante.fxml");
                break;
            case "COORDENADOR":
                SceneManager.changeScene("dashboard_coordenador.fxml");
                break;
            case "REPRESENTANTE":
                SceneManager.changeScene("dashboard_representante.fxml");
                break;
        }
    }
    
    @FXML private Label badgeNotificacoes;

    private void atualizarBadge() {
        try {
            long count = ApiClient
                    .getObject("/api/notificacoes/utilizador/" 
                            + UserSession.getId() + "/nao-lidas")
                    .getLong("value");

            badgeNotificacoes.setVisible(count > 0);

        } catch (Exception e) {
            badgeNotificacoes.setVisible(false);
        }
    }

}
