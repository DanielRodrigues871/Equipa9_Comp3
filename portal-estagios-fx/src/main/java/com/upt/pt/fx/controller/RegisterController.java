package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class RegisterController {

    @FXML private ComboBox<String> tipoCombo;
    @FXML private Label errorLabel;   

    @FXML
    public void initialize() {
        tipoCombo.getItems().addAll(
                "ESTUDANTE",
                "COORDENADOR",
                "REPRESENTANTE"
        );
    }

    @FXML
    public void continuar() {
        String tipo = tipoCombo.getValue();

        if (tipo == null) {
            errorLabel.setText("Selecione um tipo!");
            return;
        }

        switch (tipo) {
            case "ESTUDANTE":
                SceneManager.changeScene("register_estudante.fxml");
                break;

            case "COORDENADOR":
                SceneManager.changeScene("register_coordenador.fxml");
                break;

            case "REPRESENTANTE":
                SceneManager.changeScene("register_representante.fxml");
                break;

            default:
                errorLabel.setText("Tipo inválido!");
        }
    }

    @FXML
    public void voltarLogin() {
        SceneManager.changeScene("welcome.fxml");
    }
}
