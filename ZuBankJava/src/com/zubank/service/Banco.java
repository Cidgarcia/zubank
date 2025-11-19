package com.zubank.service;

import com.zubank.model.*;

import java.util.*;

public class Banco {
    private final Map<Integer, Conta> contas = new HashMap<>();

    public ContaCorrente abrirContaCorrente(Cliente cliente, double saldoInicial) {
        ContaCorrente c = new ContaCorrente(cliente.getNumeroConta(), cliente, saldoInicial);
        contas.put(c.getNumero(), c);
        return c;
    }

    public ContaPoupanca abrirContaPoupanca(Cliente cliente, double saldoInicial) {
        ContaPoupanca c = new ContaPoupanca(cliente.getNumeroConta(), cliente, saldoInicial);
        contas.put(c.getNumero(), c);
        return c;
    }

    public Optional<Conta> buscarConta(int numero) {
        return Optional.ofNullable(contas.get(numero));
    }

    public Collection<Conta> listarContas() { return contas.values(); }

    public boolean transferir(int contaOrigem, int contaDestino, double valor) {
        Conta origem = contas.get(contaOrigem);
        Conta destino = contas.get(contaDestino);
        if (origem == null || destino == null) return false;
        return origem.transferirPara(destino, valor);
    }
}
