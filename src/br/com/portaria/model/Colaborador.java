package br.com.portaria.model;

public class Colaborador {
    private String id;
    private String nome;

    public Colaborador(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Colaborador: " + id + " (" + nome + ")";
    }
}