package com.zubank.model;

public class Emprestimo {
    private int id;
    private double valor;
    private int meses;
    private double valorParcela;
    private double jurosTotais;

    // taxa mensal fixa: 1.46% (0.0146)
    public static final double TAXA_MENSAL = 0.0146;

    public Emprestimo(int id, double valor, int meses) {
        this.id = id;
        this.valor = valor;
        this.meses = meses;
        recalcular();
    }

    public void atualizar(double novoValor, int novosMeses) {
        this.valor = novoValor;
        this.meses = novosMeses;
        recalcular();
    }

    public void recalcular() {
        double r = TAXA_MENSAL;
        this.valorParcela = (valor * r) / (1.0 - Math.pow(1.0 + r, -meses));
        this.jurosTotais = valorParcela * meses - valor;
    }

    public int getId() { return id; }
    public double getValor() { return valor; }
    public int getMeses() { return meses; }
    public double getValorParcela() { return valorParcela; }
    public double getJurosTotais() { return jurosTotais; }
}
