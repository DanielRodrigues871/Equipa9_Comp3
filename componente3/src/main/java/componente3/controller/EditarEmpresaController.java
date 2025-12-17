package componente3.controller;

import componente3.SceneManager;
import componente3.service.ApiClient;
import componente3.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONObject;

public class EditarEmpresaController {

    @FXML private TextField nomeField;
    @FXML private TextField areaField;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        carregarDados();
    }

    private void carregarDados() {
        try {
            String empId = UserSession.getEmpresaId();
            
            // 
            JSONObject emp = ApiClient.getJson("/api/empresas/" + empId);
            
            // 
            nomeField.setText(emp.optString("nome", ""));
            
            // 
            areaField.setText(emp.optString("areaAtuacao", "")); 
            
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erro ao carregar dados: " + e.getMessage());
        }
    }

    @FXML
    public void guardar() {
        try {
            JSONObject json = new JSONObject();
            json.put("nome", nomeField.getText());
            // 
            json.put("areaAtuacao", areaField.getText()); 

            // 
            ApiClient.put("/api/empresas/" + UserSession.getEmpresaId(), json);

            SceneManager.changeScene("representante_empresas.fxml"); 
            
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erro ao guardar: " + e.getMessage());
        }
    }

    @FXML
    public void voltar() {
        // Redireciona para o ecrã anterior
        SceneManager.changeScene("representante_empresas.fxml");
    }
}
