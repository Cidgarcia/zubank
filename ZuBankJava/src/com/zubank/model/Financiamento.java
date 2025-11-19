package com.zubank.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Financiamento {
    private int id;
    private double valorFinanciado;
    private int meses;
    private double taxaJuros; // mensal (ex.: 0.00917)
    private double valorParcela;
    private double jurosTotais;
    private double amortizacao; // usado em SAC

    public Financiamento(int id, double valorFinanciado, int meses, double taxaJuros) {
        this.id = id;
        this.valorFinanciado = valorFinanciado;
        this.meses = meses;
        this.taxaJuros = taxaJuros;
    }

    public int getId() { return id; }
    public double getValorFinanciado() { return valorFinanciado; }
    public int getMeses() { return meses; }
    public double getTaxaJuros() { return taxaJuros; }
    public double getValorParcela() { return valorParcela; }
    public double getJurosTotais() { return jurosTotais; }
    public double getAmortizacao() { return amortizacao; }

    // Sistema PRICE
    public void gerarRelatorioPrice(Path arquivo) throws IOException {
        double r = taxaJuros;
        double parcela = valorFinanciado * r * Math.pow(1 + r, meses) / (Math.pow(1 + r, meses) - 1);
        this.valorParcela = parcela;
        this.jurosTotais = parcela * meses - valorFinanciado;
        StringBuilder sb = new StringBuilder();
        sb.append("MODO DE FINANCIAMENTO: SISTEMA PRICE\n");
        sb.append(String.format("Valor das Parcelas: R$%.2f%n", parcela));
        sb.append(String.format("JUROS TOTAIS: R$%.2f%n", jurosTotais));
        sb.append(String.format("VALOR TOTAL A SER PAGO: R$%.2f%n", parcela * meses));
        Files.writeString(arquivo, sb.toString());
    }

    // Sistema SAC
    public void gerarRelatorioSAC(Path arquivo) throws IOException {
        this.amortizacao = valorFinanciado / meses;
        double saldo = valorFinanciado;
        double totalPago = 0;
        double totalJuros = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("MODO DE FINANCIAMENTO: SISTEMA SAC\n\n");
        for (int i = 1; i <= meses; i++) {
            double juros = saldo * taxaJuros;
            double parcela = amortizacao + juros;
            sb.append(String.format("Parcela %d: Amortização R$%.2f | Juros R$%.2f | Parcela R$%.2f | Saldo após: R$%.2f%n",
                    i, amortizacao, juros, parcela, saldo - amortizacao));
            saldo -= amortizacao;
            totalPago += parcela;
            totalJuros += juros;
        }
        this.jurosTotais = totalJuros;
        Files.writeString(arquivo, sb.toString() +
                String.format("%nJUROS TOTAIS: R$%.2f%nVALOR TOTAL A SER PAGO: R$%.2f%n", totalJuros, totalPago));
    }
}
