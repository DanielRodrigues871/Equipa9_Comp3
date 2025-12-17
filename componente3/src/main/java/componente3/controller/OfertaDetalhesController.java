package componente3.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONObject;

public class OfertaDetalhesController {

    @FXML private Label lblTituloHeader;
    @FXML private Label lblEmpresaHeader;
    @FXML private Label lblEstadoBadge;
    @FXML private Label lblTipoBadge;
    
    @FXML private Label lblDuracao;
    @FXML private Label lblVagas;
    @FXML private Label lblDataInicio;
    @FXML private Label lblDataLimite;
    @FXML private Label lblLocalizacao;
    @FXML private Label lblDatasInfo;

    @FXML private Text txtDescricao;
    @FXML private Text txtRequisitos;

    public void setDadosOferta(JSONObject oferta) {
        if (oferta == null) return;

        // Cabeçalho
        lblTituloHeader.setText(oferta.optString("titulo", "Sem Título"));
        
        // Empresa
        String empresa = oferta.optString("empresaNome", "Empresa N/A");
        if(oferta.has("empresa") && oferta.get("empresa") instanceof JSONObject) {
            empresa = oferta.getJSONObject("empresa").optString("nome", "Empresa N/A");
        }
        
        // Localização (concatena com empresa no header ou mostra "Remoto" se vazio)
        String local = oferta.optString("localizacao", "Não especificado");
        lblEmpresaHeader.setText(empresa + " • " + local);
        lblLocalizacao.setText(local);

        // Badges
        String status = oferta.optString("status", "N/A");
        lblEstadoBadge.setText(status);
        configurarCorEstado(status);
        
        lblTipoBadge.setText(oferta.optString("tipo", "ESTÁGIO"));

        // Dados Grelha
        lblDuracao.setText(oferta.optInt("duracaoMeses", 0) + " Meses");
        lblVagas.setText(String.valueOf(oferta.optInt("numeroVagas", 0)));
        
        // Datas (Formatar se necessário, aqui assume YYYY-MM-DD direto do JSON)
        lblDataInicio.setText(oferta.optString("dataInicio", "A definir"));
        lblDataLimite.setText(oferta.optString("dataLimiteInscricao", "Sem data limite"));
        
        // Textos Longos
        txtDescricao.setText(oferta.optString("descricao", "Sem descrição disponível."));
        txtRequisitos.setText(oferta.optString("requisitos", "Nenhum requisito especificado."));

        // Rodapé
        String publicacao = oferta.optString("dataPublicacao", "");
        if(publicacao.length() > 10) publicacao = publicacao.substring(0, 10); // Cortar hora
        lblDatasInfo.setText("Publicado em: " + publicacao);
    }

    private void configurarCorEstado(String status) {
        switch (status) {
            case "APROVADA":
                lblEstadoBadge.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-padding: 5 10; -fx-background-radius: 15; -fx-font-weight: bold;");
                break;
            case "REJEITADA":
                lblEstadoBadge.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 5 10; -fx-background-radius: 15; -fx-font-weight: bold;");
                break;
            case "PENDENTE":
                lblEstadoBadge.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-padding: 5 10; -fx-background-radius: 15; -fx-font-weight: bold;");
                break;
            default:
                lblEstadoBadge.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-padding: 5 10; -fx-background-radius: 15; -fx-font-weight: bold;");
        }
    }

    @FXML
    public void fechar() {
        Stage stage = (Stage) lblTituloHeader.getScene().getWindow();
        stage.close();
    }
}
