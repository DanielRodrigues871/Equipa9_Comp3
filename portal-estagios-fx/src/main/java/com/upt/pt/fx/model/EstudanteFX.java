package com.upt.pt.fx.model;

public class EstudanteFX {

    private String id;
    private String nome;
    private String email;
    private String numeroAluno;

    public EstudanteFX(String id, String nome, String email, String numeroAluno) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.numeroAluno = numeroAluno;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getNumeroAluno() { return numeroAluno; }
}
