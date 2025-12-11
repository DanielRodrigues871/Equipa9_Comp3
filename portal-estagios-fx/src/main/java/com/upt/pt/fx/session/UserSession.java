package com.upt.pt.fx.session;

public class UserSession {

    private static String id;
    private static String nome;
    private static String email;
    private static String tipo;

    private static String empresaId;
    private static String ofertaEditarId;
    private static String ofertaSelecionada;
    private static String candidaturaSelecionada;
    private static String cursoSelecionado;// ← ADICIONADO

    public static void setUser(String idU, String n, String e, String t) {
        id = idU;
        nome = n;
        email = e;
        tipo = t;
    }

    public static String getId() { return id; }
    public static String getNome() { return nome; }
    public static String getEmail() { return email; }
    public static String getTipo() { return tipo; }

    public static void logout() {
        id = nome = email = tipo = null;
        empresaId = ofertaEditarId = ofertaSelecionada = null;
    }

    // ---------------- EMPRESA ----------------
    public static void setEmpresaId(String empId) { empresaId = empId; }
    public static String getEmpresaId() { return empresaId; }

    // ---------------- OFERTA EDITAR ----------------
    public static void setOfertaEditarId(String ofertaId) { ofertaEditarId = ofertaId; }
    public static String getOfertaEditarId() { return ofertaEditarId; }

    // ---------------- OFERTA SELECIONADA (para candidatura) ----------------
    public static void setOfertaSelecionada(String ofertaId) { ofertaSelecionada = ofertaId; }
    public static String getOfertaSelecionada() { return ofertaSelecionada; }
    
    public static void setCandidaturaSelecionada(String id) { candidaturaSelecionada = id; }
    public static String getCandidaturaSelecionada() { return candidaturaSelecionada; }
    

    public static void setCursoSelecionado(String id) { cursoSelecionado = id; }
    public static String getCursoSelecionado() { return cursoSelecionado; }

}
