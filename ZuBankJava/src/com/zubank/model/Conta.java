package com.zubank.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Conta {
    protected final int numero;
    protected final Cliente cliente;
    protected double saldo;
    protected final List<Transacao> historico = new ArrayList<>();

    protected Conta(int numero, Cliente cliente, double saldoInicial) {
        this.numero = numero;
        this.cliente = cliente;
        this.saldo = saldoInicial;
        historico.add(new Transacao(TipoTransacao.ABERTURA, saldoInicial, "Abertura de conta", LocalDateTime.now()));
    }

    public int getNumero() { return numero; }
    public Cliente getCliente() { return cliente; }
    public double getSaldo() { return saldo; }
    public List<Transacao> getHistorico() { return historico; }

    public void depositar(double valor) {
        if (valor <= 0) throw new IllegalArgumentException("Valor inválido");
        saldo += valor;
        historico.add(new Transacao(TipoTransacao.DEPOSITO, valor, "Depósito", LocalDateTime.now()));
    }

    public boolean sacar(double valor) {
        if (valor <= 0) throw new IllegalArgumentException("Valor inválido");
        if (saldo >= valor) {
            saldo -= valor;
            historico.add(new Transacao(TipoTransacao.SAQUE, valor, "Saque", LocalDateTime.now()));
            return true;
        }
        return false;
    }

    public boolean transferirPara(Conta destino, double valor) {
        if (destino == null) throw new IllegalArgumentException("Conta destino nula");
        if (this == destino) throw new IllegalArgumentException("Mesma conta");
        if (sacar(valor)) {
            destino.depositar(valor);
            historico.add(new Transacao(TipoTransacao.TRANSFERENCIA, valor,
                    "Transferência para " + destino.getCliente().getNome() + " (" + destino.getNumero() + ")",
                    LocalDateTime.now()));
            return true;
        }
        return false;
    }
}
