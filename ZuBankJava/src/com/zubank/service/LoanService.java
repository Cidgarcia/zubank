package com.zubank.service;

import com.zubank.model.Emprestimo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LoanService {
    private final List<Emprestimo> emprestimos = new ArrayList<>();

    public Emprestimo criar(double valor, int meses) {
        int id = emprestimos.size() + 1;
        Emprestimo e = new Emprestimo(id, valor, meses);
        emprestimos.add(e);
        return e;
    }

    public List<Emprestimo> listar() { return new ArrayList<>(emprestimos); }

    public boolean atualizar(int id, double novoValor, int novosMeses) {
        for (Emprestimo e : emprestimos) {
            if (e.getId() == id) {
                e.atualizar(novoValor, novosMeses);
                return true;
            }
        }
        return false;
    }

    public boolean excluir(int id) {
        return emprestimos.removeIf(e -> e.getId() == id);
    }

    public void salvar(Path arquivo) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Emprestimo e : emprestimos) {
            sb.append(String.format("ID: %d%nVALOR: R$%.2f%nPARCELA: R$%.2f%nJUROS TOTAIS: R$%.2f%n------------------%n",
                    e.getId(), e.getValor(), e.getValorParcela(), e.getJurosTotais()));
        }
        Files.writeString(arquivo, sb.toString());
    }
}
