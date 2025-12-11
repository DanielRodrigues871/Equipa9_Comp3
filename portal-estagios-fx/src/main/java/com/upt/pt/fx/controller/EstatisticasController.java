package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

public class EstatisticasController {

    @FXML private Label propostasAprovadas;
    @FXML private Label propostasPendentes;
    @FXML private Label propostasRejeitadas;
    @FXML private Label estagiosSemCandidaturas;

    @FXML private ListView<String> listCursos;
    @FXML private ListView<String> listEmpresasMais;
    @FXML private ListView<String> listEmpresasMenos;

    @FXML
    public void initialize() {
        carregar();
    }

    private void carregar() {
        try {
            JSONObject obj = ApiClient.getObject("/api/estatisticas");

            JSONObject propostas = obj.getJSONObject("propostas");
            propostasAprovadas.setText("Aprovadas: " + propostas.getLong("aprovadas"));
            propostasPendentes.setText("Pendentes: " + propostas.getLong("pendentes"));
            propostasRejeitadas.setText("Rejeitadas: " + propostas.getLong("rejeitadas"));

            JSONObject sem = obj.getJSONObject("estagiosSemCandidaturas");
            long total = sem.getLong("total");
            double pct = sem.getDouble("percentagem");
            estagiosSemCandidaturas.setText("Total: " + total + " (" + String.format("%.1f", pct) + "%)");

            // Cursos
            listCursos.getItems().clear();
            JSONArray cursos = obj.getJSONArray("cursosMaisProcurados");
            for (int i = 0; i < cursos.length(); i++) {
                JSONObject it = cursos.getJSONObject(i);
                listCursos.getItems().add((i+1) + ". " + it.getString("nome") + " — " + it.getLong("count") + " candidaturas");
            }

            // Empresas mais
            listEmpresasMais.getItems().clear();
            JSONArray empMais = obj.getJSONArray("empresasMaisProcuradas");
            for (int i = 0; i < empMais.length(); i++) {
                JSONObject it = empMais.getJSONObject(i);
                listEmpresasMais.getItems().add((i+1) + ". " + it.getString("nome") + " — " + it.getLong("count"));
            }

            // Empresas menos (mostramos os primeiros N)
            listEmpresasMenos.getItems().clear();
            JSONArray empMenos = obj.getJSONArray("empresasMenosProcuradas");
            for (int i = 0; i < empMenos.length(); i++) {
                JSONObject it = empMenos.getJSONObject(i);
                listEmpresasMenos.getItems().add((i+1) + ". " + it.getString("nome") + " — " + it.getLong("count"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            // fallback: mostrar mensagem simples
            propostasAprovadas.setText("Erro a carregar estatísticas.");
        }
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_coordenador.fxml");
    }
}
