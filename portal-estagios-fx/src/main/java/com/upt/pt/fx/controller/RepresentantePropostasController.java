package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.model.PropostaFX;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RepresentantePropostasController {

    @FXML private TableView<PropostaFX> tabela;
    @FXML private TableColumn<PropostaFX, String>  colId;
    @FXML private TableColumn<PropostaFX, String>  colTitulo;
    @FXML private TableColumn<PropostaFX, String>  colEstado;
    @FXML private TableColumn<PropostaFX, Integer> colVagas;
    @FXML private TableColumn<PropostaFX, Integer> colDuracao;

    @FXML
    public void initialize() {
        configurarColunas();
        carregar();
    }

    private void configurarColunas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colVagas.setCellValueFactory(new PropertyValueFactory<>("vagas"));
        colDuracao.setCellValueFactory(new PropertyValueFactory<>("duracao"));
    }

    @FXML
    public void carregar() {
        try {
            String idRep = UserSession.getId();

            JSONArray arr = ApiClient.getArray("/api/propostas/representante/" + idRep);

            List<PropostaFX> lista = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject p = arr.getJSONObject(i);

                lista.add(new PropostaFX(
                        p.getString("id"),
                        p.getString("titulo"),
                        p.optString("estado", "-"),
                        p.optInt("vagasDisponiveis", 0),
                        p.optInt("duracaoMeses", 0)
                ));
            }

            tabela.setItems(FXCollections.observableArrayList(lista));
        } catch (Exception e) {
            e.printStackTrace();
            // Se quiseres, podes depois adicionar um Label para mostrar erro na UI
        }
    }

    @FXML
    public void editar() {
        PropostaFX sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) {
            return;
        }

        UserSession.setPropostaEditarId(sel.getId());
        SceneManager.changeScene("representante_editar_proposta.fxml"); // ou representante_editar_proposta.fxml se renomeares
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_representante.fxml");
    }
}
