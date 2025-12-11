package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONObject;

public class EditarOfertaController {

    @FXML private TextField tituloField;
    @FXML private TextArea descricaoArea;
    @FXML private TextArea requisitosArea;
    @FXML private TextField localizacaoField;
    @FXML private TextField duracaoField;
    @FXML private TextField vagasField;

    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        carregarOferta();
    }

    private void carregarOferta() {
        try {
            String id = UserSession.getOfertaEditarId();

            JSONObject o = ApiClient.getObject("/api/ofertas/" + id);

            tituloField.setText(o.getString("titulo"));
            descricaoArea.setText(o.getString("descricao"));
            requisitosArea.setText(o.getString("requisitos"));
            localizacaoField.setText(o.getString("localizacao"));
            duracaoField.setText(String.valueOf(o.getInt("duracaoMeses")));
            vagasField.setText(String.valueOf(o.getInt("vagasDisponiveis")));

        } catch (Exception e) {
            errorLabel.setText("Erro ao carregar oferta!");
        }
    }

    @FXML
    public void guardar() {
        try {
            String id = UserSession.getOfertaEditarId();

            JSONObject json = new JSONObject();
            json.put("titulo", tituloField.getText());
            json.put("descricao", descricaoArea.getText());
            json.put("requisitos", requisitosArea.getText());
            json.put("localizacao", localizacaoField.getText());
            json.put("duracaoMeses", Integer.parseInt(duracaoField.getText()));
            json.put("vagasDisponiveis", Integer.parseInt(vagasField.getText()));

            ApiClient.put("/api/ofertas/" + id, json);

            SceneManager.changeScene("listar_ofertas_coordenador.fxml");

        } catch (Exception e) {
            errorLabel.setText("Erro ao guardar alterações!");
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("listar_ofertas_coordenador.fxml");
    }
}
