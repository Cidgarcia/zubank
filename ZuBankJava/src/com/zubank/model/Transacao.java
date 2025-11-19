package com.zubank.model;

import java.time.LocalDateTime;

public class Transacao {
    private final TipoTransacao tipo;
    private final double valor;
    private final String descricao;
    private final LocalDateTime dataHora;

    public Transacao(TipoTransacao tipo, double valor, String descricao, LocalDateTime dataHora) {
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.dataHora = dataHora;
    }

    public TipoTransacao getTipo() { return tipo; }
    public double getValor() { return valor; }
    public String getDescricao() { return descricao; }
    public LocalDateTime getDataHora() { return dataHora; }
}
