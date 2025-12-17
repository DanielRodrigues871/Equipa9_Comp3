package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.model.CursoOption;
import com.upt.pt.fx.service.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class RegisterEstudanteController {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<CursoOption> cursoCombo;
    
    @FXML
    private TextField numeroEstudanteField;
    @FXML
    private TextField anoMatriculaField;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        carregarCursos();
    }

    private void carregarCursos() {
        try {
            JSONArray arr = ApiClient.getArray("/api/cursos");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject c = arr.getJSONObject(i);
                // Assume que criou a classe CursoOption corretamente
                cursoCombo.getItems().add(new CursoOption(c.getString("id"), c.getString("nome")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erro ao carregar cursos.");
        }
    }

    @FXML
    public void registar() {
        errorLabel.setText(""); // Limpar erros anteriores

        try {
            // Validações básicas antes de enviar
            if (nomeField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                errorLabel.setText("Preencha os dados pessoais.");
                return;
            }

            CursoOption curso = cursoCombo.getValue();
            if (curso == null) {
                errorLabel.setText("Selecione um curso.");
                return;
            }

            // Validar se o ano é número
            int ano;
            try {
                ano = Integer.parseInt(anoMatriculaField.getText());
            } catch (NumberFormatException e) {
                errorLabel.setText("O ano de matrícula deve ser um número.");
                return;
            }

            JSONObject json = new JSONObject();
            json.put("nome", nomeField.getText());
            json.put("email", emailField.getText());
            json.put("password", passwordField.getText());
            json.put("tipo", "ESTUDANTE");

            json.put("cursoId", curso.getId()); 
            
            json.put("numeroEstudante", numeroEstudanteField.getText());
            json.put("anoMatricula", ano);

            // Enviar para o Backend
            ApiClient.post("/api/auth/register", json);

            // Sucesso
            SceneManager.changeScene("login.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erro no registo: " + e.getMessage());
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("register.fxml");
    }
}