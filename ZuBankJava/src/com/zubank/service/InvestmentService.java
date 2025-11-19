package com.zubank.service;

import com.zubank.model.Investimento;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class InvestmentService {
    private final List<Investimento> investimentos = new ArrayList<>();

    public void adicionar(double valor, int meses) {
        investimentos.add(new Investimento(valor, meses));
    }

    public List<Investimento> listar() { return new ArrayList<>(investimentos); }

    public boolean deletar(int index) {
        if (index < 0 || index >= investimentos.size()) return false;
        investimentos.remove(index);
        return true;
    }

    public void salvar(Path arquivo) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Investimento i : investimentos) {
            sb.append(String.format("INVESTIMENTO: R$%.2f | MESES: %d | RENDIMENTO LÍQUIDO: R$%.2f%n",
                    i.getInvestimento(), i.getMeses(), i.getRendimentoLiquido()));
        }
        Files.writeString(arquivo, sb.toString());
    }
}
