package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.model.EstudanteFX;
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

public class ListarEstudantesController {

    @FXML private TableView<EstudanteFX> tabela;
    @FXML private TableColumn<EstudanteFX, String> colNome;
    @FXML private TableColumn<EstudanteFX, String> colEmail;
    @FXML private TableColumn<EstudanteFX, String> colNumero;

    @FXML private Label tituloLabel;

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroAluno"));

        carregar();
    }

    private void carregar() {
        try {
            String cursoId = UserSession.getCursoSelecionado();

            JSONArray arr = ApiClient.getArray("/api/estudantes/curso/" + cursoId);

            List<EstudanteFX> lista = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject e = arr.getJSONObject(i);

                lista.add(new EstudanteFX(
                        e.getString("id"),
                        e.getString("nome"),
                        e.getString("email"),
                        e.getString("numeroAluno")
                ));
            }

            tabela.setItems(FXCollections.observableArrayList(lista));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("listar_cursos.fxml");
    }
}
