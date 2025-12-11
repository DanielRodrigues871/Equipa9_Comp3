package com.upt.pt.fx.model;

public class NotificationFX {

    private String id;
    private String mensagem;
    private boolean lida;
    private String data;

    public NotificationFX(String id, String mensagem, boolean lida, String data) {
        this.id = id;
        this.mensagem = mensagem;
        this.lida = lida;
        this.data = data;
    }

    public String getId() { return id; }
    public String getMensagem() { return mensagem; }
    public boolean isLida() { return lida; }
    public String getData() { return data; }
}
