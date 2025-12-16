package com.upt.pt.fx.controller;

import com.upt.pt.fx.service.ApiClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

public class CoordenadorEstatisticaController {

    @FXML private PieChart pieChartCandidaturas; // Gráfico Pizza para status
    @FXML private Label lblOfertasSemCandidatos; // Texto simples
    @FXML private ListView<String> listaTopCursos; // Lista para o ranking

    @FXML
    public void initialize() {
        carregarEstatisticas();
    }

    private void carregarEstatisticas() {
        try {
            // Chama o endpoint que acabámos de criar
            JSONObject stats = ApiClient.getObject("/api/estatisticas");

            // 1. Gráfico de Pizza (Status Candidaturas)
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Aprovadas", stats.optDouble("percentAprovadas", 0)),
                new PieChart.Data("Pendentes", stats.optDouble("percentPendentes", 0)),
                new PieChart.Data("Rejeitadas", stats.optDouble("percentRejeitadas", 0))
            );
            pieChartCandidaturas.setData(pieData);

            // 2. Número solto
            int semCand = stats.optInt("ofertasSemCandidaturas", 0);
            lblOfertasSemCandidatos.setText("Ofertas sem candidatos: " + semCand);

            // 3. Lista de Top Cursos
            JSONArray arrCursos = stats.optJSONArray("cursosMaisProcurados");
            if (arrCursos != null) {
                ObservableList<String> cursos = FXCollections.observableArrayList();
                for(int i=0; i<arrCursos.length(); i++) {
                    cursos.add(arrCursos.getString(i));
                }
                listaTopCursos.setItems(cursos);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar dashboard.");
        }
    }
}