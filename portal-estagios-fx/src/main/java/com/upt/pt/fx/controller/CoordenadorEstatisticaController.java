package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

public class CoordenadorEstatisticaController {

    @FXML private PieChart pieChartCandidaturas;
    @FXML private Label lblOfertasSemCandidatos;
    @FXML private ListView<String> listaTopCursos;

    @FXML
    public void initialize() {
        carregarEstatisticas();
    }

    private void carregarEstatisticas() {
        try {
            // 
            JSONObject stats = ApiClient.getJson("/api/estatisticas");

            // 1. Gráfico de Pizza
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Aprovadas", stats.optDouble("percentAprovadas", 0)),
                new PieChart.Data("Pendentes", stats.optDouble("percentPendentes", 0)),
                new PieChart.Data("Rejeitadas", stats.optDouble("percentRejeitadas", 0))
            );
            pieChartCandidaturas.setData(pieData);
            
            // Opcional: Mostrar etiquetas no gráfico
            pieChartCandidaturas.setLabelsVisible(true);

            // 2. Número solto
            int semCand = stats.optInt("ofertasSemCandidaturas", 0);
            lblOfertasSemCandidatos.setText("Ofertas sem candidatos: " + semCand);

            // 3. Lista de Top Cursos
            JSONArray arrCursos = stats.optJSONArray("cursosMaisProcurados");
            
            // 
            listaTopCursos.getItems().clear();
            
            if (arrCursos != null) {
                ObservableList<String> cursos = FXCollections.observableArrayList();
                for(int i=0; i<arrCursos.length(); i++) {
                    cursos.add(arrCursos.getString(i));
                }
                listaTopCursos.setItems(cursos);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar dashboard: " + e.getMessage());
        }
    }

    // 
    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_coordenador.fxml");
    }
}