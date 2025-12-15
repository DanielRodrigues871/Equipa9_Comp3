package com.upt.pt.fx.model;

public class NotificationFX {

    private String id;
    private String mensagem;
    private boolean lida;

    public NotificationFX(String id, String mensagem, boolean lida) {
        this.id = id;
        this.mensagem = mensagem;
        this.lida = lida;
    }

    public String getId() { return id; }
    public String getMensagem() { return mensagem; }
    public boolean isLida() { return lida; }
}
