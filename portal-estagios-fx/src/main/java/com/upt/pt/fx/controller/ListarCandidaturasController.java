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

public class ListarCandidaturasController {

    @FXML private TableView<CandidaturaFX> tabela;
    @FXML private TableColumn<CandidaturaFX, String> colNome;
    @FXML private TableColumn<CandidaturaFX, String> colEstado;
    @FXML private TableColumn<CandidaturaFX, String> colData;

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("estudanteNome"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));

        carregar();
    }

    private void carregar() {
        try {
            String ofertaId = UserSession.getOfertaEditarId(); // oferta selecionada antes

            JSONArray arr = ApiClient.getArray("/api/candidaturas/oferta/" + ofertaId);

            List<CandidaturaFX> lista = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject c = arr.getJSONObject(i);

                lista.add(new CandidaturaFX(
                        c.getString("id"),
                        c.getString("estudanteNome"),
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
    public void aprovar() {
        CandidaturaFX sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        try {
            ApiClient.post("/api/candidaturas/" + sel.getId() + "/aprovar", new JSONObject());
            carregar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void rejeitar() {
        CandidaturaFX sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        try {
            ApiClient.post("/api/candidaturas/" + sel.getId() + "/rejeitar", new JSONObject());
            carregar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_coordenador.fxml");
    }
}
