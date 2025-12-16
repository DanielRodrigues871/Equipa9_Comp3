package com.upt.pt.fx.controller;

import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.json.JSONObject;

import java.io.IOException;

public class EditarEmpresaController {

    @FXML private AnchorPane contentPane;
    @FXML private TextField nomeField;
    @FXML private TextField areaField;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        carregarDados();
    }

    private void carregarDados() {
        try {
            JSONObject emp = ApiClient.getObject("/api/empresas/" + UserSession.getEmpresaId());
            nomeField.setText(emp.getString("nome"));
            areaField.setText(emp.getString("area"));
            errorLabel.setText("");
        } catch (Exception e) {
            errorLabel.setText("Erro ao carregar dados.");
            e.printStackTrace();
        }
    }

    @FXML
    public void guardar() {
        try {
            JSONObject json = new JSONObject();
            json.put("nome", nomeField.getText());
            json.put("area", areaField.getText());

            ApiClient.put("/api/empresas/" + UserSession.getEmpresaId(), json);
            errorLabel.setText("Dados guardados com sucesso!");
            
            // Opcional: voltar automaticamente após 1s ou deixar user clicar voltar
            // loadView("coordenador_menu_empresas.fxml");
            
        } catch (Exception e) {
            errorLabel.setText("Erro ao guardar.");
            e.printStackTrace();
        }
    }

    @FXML
    public void voltar() {
        // Volta ao menu correto dependendo do tipo de user
        String menu = UserSession.getTipo().equals("COORDENADOR") 
            ? "coordenador_menu_empresas.fxml" 
            : "representante_menu_empresas.fxml";
        loadView(menu);
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + fxmlFile));
            Node view = loader.load();
            contentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) {
            System.err.println("ERRO ao carregar: " + fxmlFile);
            e.printStackTrace();
        }
    }
}
