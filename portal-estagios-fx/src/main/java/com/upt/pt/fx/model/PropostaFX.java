package com.upt.pt.fx.model;

public class PropostaFX {

    private String titulo;
    private String estado;
    private int vagas;
    private int duracao;

    public PropostaFX(String titulo, String estado, int vagas, int duracao) {
        this.titulo = titulo;
        this.estado = estado;
        this.vagas = vagas;
        this.duracao = duracao;
    }

    public String getTitulo() { return titulo; }
    public String getEstado() { return estado; }
    public int getVagas() { return vagas; }
    public int getDuracao() { return duracao; }
}
