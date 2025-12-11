package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.model.OfertaFX;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

public class ListarOfertasCoordenadorController {

    @FXML private TableView<OfertaFX> tabela;
    @FXML private TableColumn<OfertaFX, String> colTitulo;
    @FXML private TableColumn<OfertaFX, String> colEstado;
    @FXML private TableColumn<OfertaFX, Integer> colVagas;
    @FXML private TableColumn<OfertaFX, Integer> colDuracao;

    @FXML
    public void initialize() {
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colVagas.setCellValueFactory(new PropertyValueFactory<>("vagasDisponiveis"));
        colDuracao.setCellValueFactory(new PropertyValueFactory<>("duracaoMeses"));

        carregar();
    }

    private void carregar() {
        try {
            String coordId = UserSession.getId();

            JSONArray arr = ApiClient.getArray("/api/ofertas/coordenador/" + coordId);

            ObservableList<OfertaFX> lista = FXCollections.observableArrayList();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                lista.add(new OfertaFX(
                        o.getString("id"),
                        o.getString("titulo"),
                        o.getString("estado"),
                        o.getInt("duracaoMeses"),
                        o.getInt("vagasDisponiveis")
                ));
            }

            tabela.setItems(lista);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void editarSelecionada() {
        OfertaFX sel = tabela.getSelectionModel().getSelectedItem();

        if (sel == null) return;

        UserSession.setOfertaEditarId(sel.getId());


        SceneManager.changeScene("editar_oferta.fxml");
    }
    
    @FXML
    public void eliminar() {
        OfertaFX sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Tem a certeza?");
        alert.setContentText("A oferta será arquivada, mas o histórico será mantido.");
        
        if (alert.showAndWait().get() != ButtonType.OK) {
            return;
        }

        try {
            ApiClient.post("/api/ofertas/" + sel.getId() + "/arquivar", new JSONObject());
            carregar(); // atualizar lista
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_coordenador.fxml");
    }
}
