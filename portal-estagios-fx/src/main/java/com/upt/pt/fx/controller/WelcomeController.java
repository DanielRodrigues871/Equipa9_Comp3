package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import javafx.fxml.FXML;

public class WelcomeController {

    @FXML
    public void goToLogin() {
        SceneManager.changeScene("login.fxml");
    }

    @FXML
    public void goToRegister() {
        SceneManager.changeScene("register.fxml");
    }
}
