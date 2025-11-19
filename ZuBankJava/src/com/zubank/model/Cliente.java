package com.zubank.model;

import java.util.Objects;

public class Cliente {
    private final String nome;
    private final int numeroConta;

    public Cliente(String nome, int numeroConta) {
        this.nome = Objects.requireNonNull(nome, "nome");
        this.numeroConta = numeroConta;
    }

    public String getNome() { return nome; }
    public int getNumeroConta() { return numeroConta; }

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                ", numeroConta=" + numeroConta +
                '}';
    }
}
