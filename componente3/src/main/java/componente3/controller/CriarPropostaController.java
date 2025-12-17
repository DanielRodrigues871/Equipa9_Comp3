package componente3.controller;

import componente3.SceneManager;
import componente3.service.ApiClient;
import componente3.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONObject;

public class CriarPropostaController {

    @FXML private TextField tituloField;
    @FXML private TextArea descricaoArea;
    @FXML private TextArea requisitosArea;
    @FXML private TextField localizacaoField;
    @FXML private TextField duracaoField;
    @FXML private TextField vagasField;
    @FXML private ComboBox<String> tipoCombo;

    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        tipoCombo.getItems().addAll("CURRICULAR", "EXTRA_CURRICULAR");
    }

    @FXML
    public void criar() {
        try {
            JSONObject json = new JSONObject();
            json.put("titulo", tituloField.getText());
            json.put("descricao", descricaoArea.getText());
            json.put("requisitos", requisitosArea.getText());
            json.put("localizacao", localizacaoField.getText());
            json.put("duracaoMeses", Integer.parseInt(duracaoField.getText()));
            json.put("vagasDisponiveis", Integer.parseInt(vagasField.getText()));
            json.put("tipo", tipoCombo.getValue());

            String empresaId = UserSession.getEmpresaId();
            String representanteId = UserSession.getId();

            ApiClient.post("/api/propostas?empresaId=" + empresaId +
                           "&representanteId=" + representanteId, json);

            SceneManager.changeScene("dashboard_representante.fxml");
        }
        catch (Exception e) {
            errorLabel.setText("Erro ao criar proposta!");
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_representante.fxml");
    }
}
