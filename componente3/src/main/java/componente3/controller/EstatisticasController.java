package componente3.controller;

import componente3.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EstatisticasController {

    @FXML private PieChart pieChart;
    @FXML private BarChart<String, Number> barChart;

    @FXML
    public void initialize() {
        carregarEstatisticasFake();
    }

    /** 
     * TODO: No futuro trocar por: 
     * JSONArray arr = ApiClient.getArray("/api/estatisticas");
     */
    private void carregarEstatisticasFake() {

        // ---- PIE CHART: Distribuição de Propostas ----
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Propostas Aprovadas", 18),
                new PieChart.Data("Propostas Pendentes", 7),
                new PieChart.Data("Propostas Rejeitadas", 4)
        );
        pieChart.setData(pieData);

        // ---- BAR CHART: Candidaturas por Curso ----
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Candidaturas");

        series.getData().add(new XYChart.Data<>("Informática", 42));
        series.getData().add(new XYChart.Data<>("Gestão", 27));
        series.getData().add(new XYChart.Data<>("Marketing", 12));

        barChart.getData().add(series);
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_coordenador.fxml");
    }
}