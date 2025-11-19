package com.zubank.model;

public class Investimento {
    private double investimento;
    private int meses;
    private double aliquotaIR; // 0.225, 0.20, 0.175, 0.15
    private double rendimentoBruto;
    private double rendimentoLiquido;

    public static final double REND_MENSAL_FACTOR = 1.009542; // 0,9542% a.m.

    public Investimento(double investimento, int meses) {
        this.investimento = investimento;
        this.meses = meses;
        this.aliquotaIR = calculaIR(meses);
        calcular();
    }

    private static double calculaIR(int meses) {
        if (meses <= 6) return 0.225;
        if (meses <= 12) return 0.20;
        if (meses <= 24) return 0.175;
        return 0.15;
    }

    private void calcular() {
        double brutoFinal = investimento * Math.pow(RENDE_MENSAL(), meses);
        double ganhoBruto = brutoFinal - investimento;
        double ganhoLiquido = ganhoBruto * (1.0 - aliquotaIR);
        this.rendimentoBruto = ganhoBruto;
        this.rendimentoLiquido = ganhoLiquido;
    }

    private static double RENDE_MENSAL() { return REND_MENSAL_FACTOR; }

    public double getInvestimento() { return investimento; }
    public int getMeses() { return meses; }
    public double getAliquotaIR() { return aliquotaIR; }
    public double getRendimentoBruto() { return rendimentoBruto; }
    public double getRendimentoLiquido() { return rendimentoLiquido; }
}
