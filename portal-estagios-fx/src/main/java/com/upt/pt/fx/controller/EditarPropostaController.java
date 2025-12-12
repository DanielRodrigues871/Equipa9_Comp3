package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONObject;

public class EditarPropostaController {

    @FXML private TextField tituloField;
    @FXML private TextArea descricaoArea;
    @FXML private TextArea requisitosArea;
    @FXML private TextField localizacaoField;
    @FXML private TextField duracaoField;
    @FXML private TextField vagasField;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        carregarProposta();
    }

    private void carregarProposta() {
        try {
            String id = UserSession.getPropostaEditarId();

            JSONObject p = ApiClient.getObject("/api/propostas/" + id);

            tituloField.setText(p.getString("titulo"));
            descricaoArea.setText(p.getString("descricao"));
            requisitosArea.setText(p.getString("requisitos"));
            localizacaoField.setText(p.getString("localizacao"));
            duracaoField.setText(String.valueOf(p.getInt("duracaoMeses")));
            vagasField.setText(String.valueOf(p.getInt("vagasDisponiveis")));

        } catch (Exception e) {
            errorLabel.setText("Erro ao carregar proposta!");
        }
    }

    @FXML
    public void guardar() {
        try {
            String id = UserSession.getPropostaEditarId();

            JSONObject json = new JSONObject();
            json.put("titulo", tituloField.getText());
            json.put("descricao", descricaoArea.getText());
            json.put("requisitos", requisitosArea.getText());
            json.put("localizacao", localizacaoField.getText());
            json.put("duracaoMeses", Integer.parseInt(duracaoField.getText()));
            json.put("vagasDisponiveis", Integer.parseInt(vagasField.getText()));

            ApiClient.put("/api/propostas/" + id, json);

            SceneManager.changeScene("listar_propostas.fxml");

        } catch (Exception e) {
            errorLabel.setText("Erro ao guardar alterações!");
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("listar_propostas.fxml");
    }
}
