package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.json.JSONArray;
import org.json.JSONObject;

public class RepresentantePropostasController {

	@FXML
	private TableView<PropostaTabela> tabela;
	@FXML
	private TableColumn<PropostaTabela, String> colId;
	@FXML
	private TableColumn<PropostaTabela, String> colTitulo;
	@FXML
	private TableColumn<PropostaTabela, String> colEstado;
	@FXML
	private TableColumn<PropostaTabela, String> colVagas;
	@FXML
	private TableColumn<PropostaTabela, String> colDuracao;

	@FXML
	public void initialize() {
		configurarColunas();
		carregar();
	}

	private void configurarColunas() {
		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
		colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
		colVagas.setCellValueFactory(new PropertyValueFactory<>("vagas"));
		colDuracao.setCellValueFactory(new PropertyValueFactory<>("duracao"));
	}

	@FXML
	public void carregar() {
		try {
			String repId = UserSession.getId();
			JSONArray array = ApiClient.getArray("/api/propostas/representante/" + repId);

			ObservableList<PropostaTabela> lista = FXCollections.observableArrayList();

			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);

				PropostaTabela p = new PropostaTabela(String.valueOf(obj.get("id")), obj.getString("titulo"),
						obj.optString("status", "Pendente"), String.valueOf(obj.optInt("vagasDisponiveis", 0)),
						String.valueOf(obj.optInt("duracaoMeses", 0)));
				lista.add(p);
			}

			tabela.setItems(lista);

		} catch (Exception e) {
			e.printStackTrace();
			mostrarAlerta("Erro", "Não foi possível carregar as propostas.");
		}
	}

	// Ver Detalhes da Proposta
	@FXML
    public void verDetalhes() {
        PropostaTabela selecionada = tabela.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarAlerta("Aviso", "Selecione uma proposta na tabela.");
            return;
        }

        try {
            String repId = UserSession.getId();
            
            String respostaJson = ApiClient.get("/api/propostas/" + selecionada.getId());
            
            JSONObject detalhes = new JSONObject(respostaJson);
            // -------------------------------

            StringBuilder info = new StringBuilder();
            info.append("ID: ").append(selecionada.getId()).append("\n");
            info.append("Título: ").append(selecionada.getTitulo()).append("\n");
            info.append("Estado: ").append(selecionada.getEstado()).append("\n");
            info.append("Vagas: ").append(selecionada.getVagas()).append("\n");
            info.append("Duração: ").append(selecionada.getDuracao()).append(" meses\n\n");

            if (detalhes.has("descricao"))
                info.append("Descrição: ").append(detalhes.optString("descricao")).append("\n\n");
            if (detalhes.has("requisitos"))
                info.append("Requisitos: ").append(detalhes.optString("requisitos")).append("\n");
            if (detalhes.has("localizacao"))
                info.append("Localização: ").append(detalhes.optString("localizacao"));

            showDetalhesAlert("Detalhes da Proposta", info.toString());

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível carregar os detalhes.");
        }
    }

	@FXML
	public void editar() {
		PropostaTabela selecionada = tabela.getSelectionModel().getSelectedItem();

		if (selecionada == null) {
			mostrarAlerta("Aviso", "Selecione uma proposta na tabela para editar.");
			return;
		}

		try {
			UserSession.setPropostaEditarId(selecionada.getId());
			System.out.println("A editar proposta ID: " + selecionada.getId());
			SceneManager.changeScene("representante_editar_proposta.fxml");

		} catch (Exception e) {
			e.printStackTrace();
			mostrarAlerta("Erro", "Falha ao abrir edição.");
		}
	}

	@FXML
	public void criarNova() {
		SceneManager.changeScene("representante_criar_proposta.fxml");
	}

	@FXML
	public void voltar() {
		SceneManager.changeScene("dashboard_representante.fxml");
	}

	private void mostrarAlerta(String titulo, String msg) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}

	// Alerta expandível para detalhes
	private void showDetalhesAlert(String titulo, String conteudo) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(titulo);
		alert.setHeaderText(null);

		TextArea textArea = new TextArea(conteudo);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(textArea, 0, 0);

		alert.getDialogPane().setExpandableContent(expContent);
		alert.getDialogPane().setExpanded(true);
		alert.showAndWait();
	}

	public static class PropostaTabela {
		private final String id;
		private final String titulo;
		private final String estado;
		private final String vagas;
		private final String duracao;

		public PropostaTabela(String id, String titulo, String estado, String vagas, String duracao) {
			this.id = id;
			this.titulo = titulo;
			this.estado = estado;
			this.vagas = vagas;
			this.duracao = duracao;
		}

		public String getId() {
			return id;
		}

		public String getTitulo() {
			return titulo;
		}

		public String getEstado() {
			return estado;
		}

		public String getVagas() {
			return vagas;
		}

		public String getDuracao() {
			return duracao;
		}
	}
}