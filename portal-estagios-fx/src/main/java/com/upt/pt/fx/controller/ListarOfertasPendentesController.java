package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.model.OfertaFX;
import com.upt.pt.fx.service.ApiClient;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListarOfertasPendentesController {

    @FXML private TableView<OfertaFX> tabela;
    @FXML private TableColumn<OfertaFX, String> colTitulo;
    @FXML private TableColumn<OfertaFX, String> colEmpresa;
    @FXML private TableColumn<OfertaFX, Integer> colVagas;
    @FXML private TableColumn<OfertaFX, Integer> colDuracao;

    @FXML
    public void initialize() {
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colEmpresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        colVagas.setCellValueFactory(new PropertyValueFactory<>("vagas"));
        colDuracao.setCellValueFactory(new PropertyValueFactory<>("duracao"));

        carregar();
    }

    private void carregar() {
        try {
            JSONArray arr = ApiClient.getArray("/api/ofertas/status/PENDENTE");

            List<OfertaFX> lista = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                lista.add(new OfertaFX(
                        o.getString("id"),
                        o.getString("titulo"),
                        o.getString("empresaNome"),
                        o.getInt("numeroVagas"),
                        o.getInt("duracaoMeses")
                ));
            }

            tabela.setItems(FXCollections.observableArrayList(lista));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void aprovar() {
        OfertaFX sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        try {
            ApiClient.post("/api/ofertas/" + sel.getId() + "/aprovar", new JSONObject());
            carregar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void rejeitar() {
        OfertaFX sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        try {
            ApiClient.post("/api/ofertas/" + sel.getId() + "/rejeitar", new JSONObject());
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
