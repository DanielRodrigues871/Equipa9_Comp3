package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class EstatisticasController {

    @FXML private Label lblPercentAprovadas;
    @FXML private Label lblPercentPendentes;
    @FXML private Label lblPercentRejeitadas;
    @FXML private Label lblTotalSemCandidaturas;

    @FXML private ListView<String> listaCursos;
    @FXML private ListView<String> listaEmpresasMais;
    @FXML private ListView<String> listaEmpresasMenos;

    @FXML
    public void initialize() {
        carregarFake(); // será substituído pelo carregarReal();
    }

    /** FUTURO: carregar estatísticas reais via API */
    private void carregarReal() {
        // JSONObject obj = ApiClient.getObject("/api/estatisticas");
        // preencher labels e listas com obj.get(...)
    }

    /** TEMPORÁRIO: valores fictícios só para layout */
    private void carregarFake() {

        lblPercentAprovadas.setText("Aprovadas: 55%");
        lblPercentPendentes.setText("Pendentes: 30%");
        lblPercentRejeitadas.setText("Rejeitadas: 15%");
        lblTotalSemCandidaturas.setText("Ofertas sem candidaturas: 12");

        listaCursos.getItems().setAll(
                "Informática — 42 candidaturas",
                "Gestão — 27 candidaturas",
                "Marketing — 12 candidaturas"
        );

        listaEmpresasMais.getItems().setAll(
                "IBM — 30 candidaturas",
                "SONAE — 18 candidaturas",
                "Accenture — 15 candidaturas"
        );

        listaEmpresasMenos.getItems().setAll(
                "Startup A — 1 candidatura",
                "Empresa X — 2 candidaturas"
        );
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_coordenador.fxml");
    }
}
