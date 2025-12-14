package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.model.CandidaturaFX;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MinhasCandidaturasController {

    @FXML private TableView<CandidaturaFX> tabela;
    @FXML private TableColumn<CandidaturaFX, String> colOferta;
    @FXML private TableColumn<CandidaturaFX, String> colEstado;
    @FXML private TableColumn<CandidaturaFX, String> colData;

    @FXML
    public void initialize() {
        colOferta.setCellValueFactory(new PropertyValueFactory<>("ofertaTitulo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));

        carregar();
    }

    private void carregar() {
        try {
            String estudanteId = UserSession.getId();

            JSONArray arr = ApiClient.getArray(
                    "/api/candidaturas/estudante/" + estudanteId
            );

            List<CandidaturaFX> lista = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject c = arr.getJSONObject(i);

                lista.add(new CandidaturaFX(
                        c.getString("id"),
                        c.getString("ofertaTitulo"),
                        c.getString("estado"),
                        c.getString("data")
                ));
            }

            tabela.setItems(FXCollections.observableArrayList(lista));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void verDetalhes() {
        CandidaturaFX sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        UserSession.setCandidaturaSelecionada(sel.getId());
        SceneManager.changeScene("ver_candidatura.fxml");
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_estudante.fxml");
    }
}
