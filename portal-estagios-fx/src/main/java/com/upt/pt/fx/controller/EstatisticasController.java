package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class EstatisticasController {

    @FXML private Label lblPercentAprovadas;
    @FXML private Label lblPercentPendentes;
    @FXML private Label lblPercentRejeitadas;
    @FXML private Label lblTotalSemCandidaturas;

    @FXML private ListView<String> listaCursos;
    @FXML private ListView<String> listaEmpresasMais;
    @FXML private ListView<String> listaEmpresasMenos;

    private final DecimalFormat df = new DecimalFormat("0.##");

    @FXML
    public void initialize() {
        carregarReal();
    }

    private void carregarReal() {
        try {
            JSONObject obj = ApiClient.getObject("/api/estatisticas");

            lblPercentAprovadas.setText(
                    "Aprovadas: " + df.format(obj.getDouble("percentAprovadas")) + "%"
            );
            lblPercentPendentes.setText(
                    "Pendentes: " + df.format(obj.getDouble("percentPendentes")) + "%"
            );
            lblPercentRejeitadas.setText(
                    "Rejeitadas: " + df.format(obj.getDouble("percentRejeitadas")) + "%"
            );

            lblTotalSemCandidaturas.setText(
                    "Ofertas sem candidaturas: " + obj.getInt("ofertasSemCandidaturas")
            );

            preencherLista(listaCursos, obj.optJSONArray("cursosMaisProcurados"));
            preencherLista(listaEmpresasMais, obj.optJSONArray("empresasMaisProcuradas"));
            preencherLista(listaEmpresasMenos, obj.optJSONArray("empresasMenosEscolhidas"));

        } catch (Exception e) {
            lblPercentAprovadas.setText("Erro ao carregar estatísticas.");
            e.printStackTrace();
        }
    }

    private void preencherLista(ListView<String> listView, JSONArray arr) {
        listView.getItems().clear();

        if (arr == null || arr.isEmpty()) {
            listView.getItems().add("Sem dados disponíveis");
            return;
        }

        for (int i = 0; i < arr.length(); i++) {
            listView.getItems().add(arr.getString(i));
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_coordenador.fxml");
    }
}
