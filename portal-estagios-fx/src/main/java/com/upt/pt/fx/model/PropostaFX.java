package com.upt.pt.fx.model;

public class PropostaFX {

	private String id;
    private String titulo;
    private String estado;
    private int vagas;
    private int duracao;

    public PropostaFX(String id,String titulo, String estado, int vagas, int duracao) {
        this.id=id;
    	this.titulo = titulo;
        this.estado = estado;
        this.vagas = vagas;
        this.duracao = duracao;
    }
    
 // Construtor secundário (sem id) — compatível com código antigo
    public PropostaFX(String titulo, String estado, int vagas, int duracao) {
        this(null, titulo, estado, vagas, duracao);
    }

    public String getId() { return id;}
    public String getTitulo() { return titulo; }
    public String getEstado() { return estado; }
    public int getVagas() { return vagas; }
    public int getDuracao() { return duracao; }
    
    // setters (opcionais — adiciona se precisares de editar localmente)
    public void setId(String id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setVagas(int vagas) { this.vagas = vagas; }
    public void setDuracao(int duracao) { this.duracao = duracao; }

    @Override
    public String toString() {
        return "PropostaFX{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", estado='" + estado + '\'' +
                ", vagas=" + vagas +
                ", duracao=" + duracao +
                '}';
    }
}
