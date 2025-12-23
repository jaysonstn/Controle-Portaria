package br.com.portaria.model;

public class Bloqueado {
    private String idEntidade;

    public Bloqueado(String idEntidade) {
        this.idEntidade = idEntidade;
    }

    public String getIdEntidade() {
        return idEntidade;
    }

    @Override
    public String toString() {
        return idEntidade;
    }
}