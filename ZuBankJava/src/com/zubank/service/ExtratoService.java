package com.zubank.service;

import com.zubank.model.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ExtratoService {

    public void gerarExtrato(Cliente cliente,
                             Conta conta,
                             List<TransferService.Transferencia> transferenciasEfetuadas,
                             List<Investimento> investimentos,
                             List<Emprestimo> emprestimos,
                             List<com.zubank.model.Financiamento> financiamentos,
                             Path arquivo) throws IOException {

        StringBuilder sb = new StringBuilder();
        sb.append("==================== EXTRATO BANCÁRIO ====================\n");
        sb.append(String.format("Nome do Cliente: %s%n", cliente.getNome()));
        sb.append(String.format("Número da Conta: %d%n", cliente.getNumeroConta()));
        sb.append(String.format("Saldo Atual: R$%.2f%n", conta.getSaldo()));
        sb.append("\n==================== TRANSFERÊNCIAS ====================\n");
        if (transferenciasEfetuadas != null && !transferenciasEfetuadas.isEmpty()) {
            for (TransferService.Transferencia t : transferenciasEfetuadas) {
                sb.append(String.format("Beneficiário: %s | Conta: %d | Valor: R$%.2f%n",
                        t.beneficiario, t.contaDestino, t.valor));
            }
        } else {
            sb.append("Nenhuma transferência registrada.\n");
        }

        sb.append("\n==================== INVESTIMENTOS ====================\n");
        if (investimentos != null && !investimentos.isEmpty()) {
            for (Investimento i : investimentos) {
                sb.append(String.format("Valor Investido: R$%.2f | Meses: %d | Rendimento Líquido: R$%.2f%n",
                        i.getInvestimento(), i.getMeses(), i.getRendimentoLiquido()));
            }
        } else {
            sb.append("Nenhum investimento registrado.\n");
        }

        sb.append("\n==================== EMPRÉSTIMOS ====================\n");
        if (emprestimos != null && !emprestimos.isEmpty()) {
            for (Emprestimo e : emprestimos) {
                sb.append(String.format("ID: %d | Valor: R$%.2f | Parcela: R$%.2f | Juros Totais: R$%.2f%n",
                        e.getId(), e.getValor(), e.getValorParcela(), e.getJurosTotais()));
            }
        } else {
            sb.append("Nenhum empréstimo registrado.\n");
        }

        sb.append("\n==================== FINANCIAMENTOS ====================\n");
        if (financiamentos != null && !financiamentos.isEmpty()) {
            for (Financiamento f : financiamentos) {
                sb.append(String.format("ID: %d | Valor: R$%.2f | Meses: %d | Taxa: %.4f%n",
                        f.getId(), f.getValorFinanciado(), f.getMeses(), f.getTaxaJuros()));
            }
        } else {
            sb.append("Nenhum financiamento registrado.\n");
        }

        Files.writeString(arquivo, sb.toString());
    }
}
