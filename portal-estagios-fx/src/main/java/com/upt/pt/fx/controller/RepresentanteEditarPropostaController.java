package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.json.JSONObject;

public class RepresentanteEditarPropostaController {

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
        errorLabel.setText("");
        try {
            String id = UserSession.getPropostaEditarId();
            if (id == null || id.isBlank()) {
                errorLabel.setText("Nenhuma proposta selecionada.");
                return;
            }

            JSONObject p = ApiClient.getObject("/api/propostas/" + id);

            tituloField.setText(p.optString("titulo", ""));
            descricaoArea.setText(p.optString("descricao", ""));
            requisitosArea.setText(p.optString("requisitos", ""));
            localizacaoField.setText(p.optString("localizacao", ""));
            duracaoField.setText(String.valueOf(p.optInt("duracaoMeses", 0)));
            vagasField.setText(String.valueOf(p.optInt("vagasDisponiveis", 0)));

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erro ao carregar proposta.");
        }
    }

    @FXML
    public void guardar() {
        errorLabel.setText("");

        // Validação simples
        if (tituloField.getText().isBlank()
                || duracaoField.getText().isBlank()
                || vagasField.getText().isBlank()) {

            errorLabel.setText("Preencha pelo menos Título, Duração e Vagas.");
            return;
        }

        int duracao;
        int vagas;
        try {
            duracao = Integer.parseInt(duracaoField.getText());
            vagas = Integer.parseInt(vagasField.getText());
        } catch (NumberFormatException nfe) {
            errorLabel.setText("Duração e Vagas têm de ser números inteiros.");
            return;
        }

        try {
            String id = UserSession.getPropostaEditarId();
            if (id == null || id.isBlank()) {
                errorLabel.setText("Nenhuma proposta selecionada.");
                return;
            }

            JSONObject json = new JSONObject();
            json.put("titulo", tituloField.getText());
            json.put("descricao", descricaoArea.getText());
            json.put("requisitos", requisitosArea.getText());
            json.put("localizacao", localizacaoField.getText());
            json.put("duracaoMeses", duracao);
            json.put("vagasDisponiveis", vagas);

            ApiClient.put("/api/propostas/" + id, json);

            // Depois de guardar, volta à lista de propostas
            SceneManager.changeScene("representante_propostas.fxml"); 

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erro ao guardar alterações.");
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("representante_propostas.fxml"); 
    }
}
