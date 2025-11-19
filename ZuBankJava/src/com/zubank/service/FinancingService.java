package com.zubank.service;

import com.zubank.model.Financiamento;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FinancingService {
    private final List<Financiamento> financiamentos = new ArrayList<>();

    public Financiamento criar(double valor, int meses, double taxa) {
        int id = financiamentos.size() + 1;
        Financiamento f = new Financiamento(id, valor, meses, taxa);
        financiamentos.add(f);
        return f;
    }

    public List<Financiamento> listar() { return new ArrayList<>(financiamentos); }

    public boolean atualizar(int id, double novoValor, int novosMeses, double novaTaxa) {
        for (Financiamento f : financiamentos) {
            if (f.getId() == id) {
                // recriar objeto simples (ou teria setters); para simplicidade, criamos novo registro
                financiamentos.set(id - 1, new Financiamento(id, novoValor, novosMeses, novaTaxa));
                return true;
            }
        }
        return false;
    }

    public boolean excluir(int id) {
        return financiamentos.removeIf(f -> f.getId() == id);
    }

    public void gerarRelatorioPrice(int id, Path arquivo) throws IOException {
        for (Financiamento f : financiamentos) {
            if (f.getId() == id) {
                f.gerarRelatorioPrice(arquivo);
                return;
            }
        }
    }

    public void gerarRelatorioSAC(int id, Path arquivo) throws IOException {
        for (Financiamento f : financiamentos) {
            if (f.getId() == id) {
                f.gerarRelatorioSAC(arquivo);
                return;
            }
        }
    }
}
