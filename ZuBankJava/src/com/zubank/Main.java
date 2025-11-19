package com.zubank;

import com.zubank.model.*;
import com.zubank.service.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        System.out.println("=== ZUBANK (Java/POO) ===");

        // Cadastro básico inicial (espelha C: um cliente e uma conta)
        System.out.print("Nome do cliente: ");
        String nome = in.nextLine().trim();
        System.out.print("Número da conta: ");
        int numConta = Integer.parseInt(in.nextLine().trim());
        System.out.print("Saldo inicial: ");
        double saldoInicial = Double.parseDouble(in.nextLine().trim());

        Cliente cliente = new Cliente(nome, numConta);
        Banco banco = new Banco();
        ContaCorrente conta = banco.abrirContaCorrente(cliente, saldoInicial);

        TransferService transferService = new TransferService();
        InvestmentService investmentService = new InvestmentService();
        LoanService loanService = new LoanService();
        FinancingService financingService = new FinancingService();
        ExtratoService extratoService = new ExtratoService();

        int opcao;
        do {
            System.out.println("\n--- MENU ---");
            System.out.println("1 - Transferências");
            System.out.println("2 - Investimentos");
            System.out.println("3 - Empréstimos");
            System.out.println("4 - Financiamentos");
            System.out.println("5 - Extrato");
            System.out.println("6 - Sair");
            System.out.print("Escolha: ");
            opcao = Integer.parseInt(in.nextLine().trim());

            switch (opcao) {
                case 1 -> menuTransferencias(banco, conta, transferService);
                case 2 -> menuInvestimentos(investmentService);
                case 3 -> menuEmprestimos(loanService);
                case 4 -> menuFinanciamentos(financingService);
                case 5 -> {
                    // Gera os arquivos e o extrato como no C
                    var transfEfetuadas = transferService.realizarTodas(conta, n -> banco.buscarConta(n).orElse(null),
                            Path.of("transferencias.txt"));
                    investmentService.salvar(Path.of("investimentos.txt"));
                    loanService.salvar(Path.of("emprestimos.txt"));
                    // relatórios de financiamento são gerados por item no menu de financiamento
                    extratoService.gerarExtrato(
                            cliente,
                            conta,
                            transfEfetuadas,
                            investmentService.listar(),
                            loanService.listar(),
                            financingService.listar(),
                            Path.of("extrato.txt")
                    );
                    System.out.println("Extrato gerado em extrato.txt");
                }
                case 6 -> System.out.println("Encerrando. Obrigado por usar o ZuBank!");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 6);
    }

    private static void menuTransferencias(Banco banco, Conta contaOrigem, TransferService service) throws Exception {
        int op;
        do {
            System.out.println("\n| MENU Transferência |");
            System.out.println("| 1 | Criar Transferência");
            System.out.println("| 2 | Listar Transferências");
            System.out.println("| 3 | Deletar Transferência");
            System.out.println("| 4 | Realizar Todas as Transferências");
            System.out.println("| 5 | Voltar");
            System.out.print("Escolha: ");
            op = Integer.parseInt(in.nextLine().trim());

            switch (op) {
                case 1 -> {
                    System.out.print("Beneficiário: ");
                    String ben = in.nextLine();
                    System.out.print("Conta destino (número): ");
                    int c = Integer.parseInt(in.nextLine().trim());
                    System.out.print("Valor (R$): ");
                    double v = Double.parseDouble(in.nextLine().trim());
                    service.criar(ben, c, v);
                }
                case 2 -> service.listar().forEach(t ->
                        System.out.printf("ID: %d | %s | conta %d | R$%.2f%n", t.id, t.beneficiario, t.contaDestino, t.valor));
                case 3 -> {
                    System.out.print("ID para deletar: ");
                    int id = Integer.parseInt(in.nextLine().trim());
                    System.out.println(service.deletar(id) ? "Removido." : "Não encontrado.");
                }
                case 4 -> {
                    service.realizarTodas(contaOrigem, n -> banco.buscarConta(n).orElse(null),
                            Path.of("transferencias.txt"));
                    System.out.println("Transferências processadas. Veja transferencias.txt.");
                }
                case 5 -> {}
                default -> System.out.println("Opção inválida");
            }
        } while (op != 5);
    }

    private static void menuInvestimentos(InvestmentService service) throws Exception {
        int op;
        do {
            System.out.println("\n| MENU Investimentos |");
            System.out.println("| 1 | Adicionar Investimento");
            System.out.println("| 2 | Listar Investimentos");
            System.out.println("| 3 | Deletar Investimento (por índice)");
            System.out.println("| 4 | Salvar e Voltar");
            System.out.print("Escolha: ");
            op = Integer.parseInt(in.nextLine().trim());

            switch (op) {
                case 1 -> {
                    System.out.print("Valor investido (R$): ");
                    double v = Double.parseDouble(in.nextLine().trim());
                    System.out.print("Meses: ");
                    int m = Integer.parseInt(in.nextLine().trim());
                    service.adicionar(v, m);
                }
                case 2 -> {
                    var list = service.listar();
                    for (int i = 0; i < list.size(); i++) {
                        var it = list.get(i);
                        System.out.printf("[%d] R$%.2f por %d meses | R$ liquido: %.2f%n",
                                i, it.getInvestimento(), it.getMeses(), it.getRendimentoLiquido());
                    }
                }
                case 3 -> {
                    System.out.print("Índice: ");
                    int idx = Integer.parseInt(in.nextLine().trim());
                    System.out.println(service.deletar(idx) ? "Removido." : "Índice inválido.");
                }
                case 4 -> {
                    service.salvar(Path.of("investimentos.txt"));
                    System.out.println("Salvo em investimentos.txt");
                }
                default -> System.out.println("Opção inválida");
            }
        } while (op != 4);
    }

    private static void menuEmprestimos(LoanService service) throws Exception {
        int op;
        do {
            System.out.println("\n| MENU Empréstimos |");
            System.out.println("| 1 | Criar Empréstimo");
            System.out.println("| 2 | Listar Empréstimos");
            System.out.println("| 3 | Atualizar Empréstimo");
            System.out.println("| 4 | Excluir Empréstimo");
            System.out.println("| 5 | Salvar e Voltar");
            System.out.print("Escolha: ");
            op = Integer.parseInt(in.nextLine().trim());

            switch (op) {
                case 1 -> {
                    System.out.print("Valor (R$): ");
                    double v = Double.parseDouble(in.nextLine().trim());
                    System.out.print("Meses: ");
                    int m = Integer.parseInt(in.nextLine().trim());
                    var e = service.criar(v, m);
                    System.out.printf("Criado ID=%d | Parcela: R$%.2f | Juros totais: R$%.2f%n",
                            e.getId(), e.getValorParcela(), e.getJurosTotais());
                }
                case 2 -> service.listar().forEach(e ->
                        System.out.printf("ID=%d | Valor R$%.2f | Meses %d | Parcela R$%.2f | Juros R$%.2f%n",
                                e.getId(), e.getValor(), e.getMeses(), e.getValorParcela(), e.getJurosTotais()));
                case 3 -> {
                    System.out.print("ID: ");
                    int id = Integer.parseInt(in.nextLine().trim());
                    System.out.print("Novo valor (R$): ");
                    double v = Double.parseDouble(in.nextLine().trim());
                    System.out.print("Novos meses: ");
                    int m = Integer.parseInt(in.nextLine().trim());
                    System.out.println(service.atualizar(id, v, m) ? "Atualizado." : "Não encontrado.");
                }
                case 4 -> {
                    System.out.print("ID: ");
                    int id = Integer.parseInt(in.nextLine().trim());
                    System.out.println(service.excluir(id) ? "Excluído." : "Não encontrado.");
                }
                case 5 -> {
                    service.salvar(Path.of("emprestimos.txt"));
                    System.out.println("Salvo em emprestimos.txt");
                }
                default -> System.out.println("Opção inválida");
            }
        } while (op != 5);
    }

    private static void menuFinanciamentos(FinancingService service) throws Exception {
        int op;
        do {
            System.out.println("\n| MENU Financiamentos |");
            System.out.println("| 1 | Criar Financiamento");
            System.out.println("| 2 | Listar Financiamentos");
            System.out.println("| 3 | Atualizar Financiamento");
            System.out.println("| 4 | Excluir Financiamento");
            System.out.println("| 5 | Gerar Relatório Price");
            System.out.println("| 6 | Gerar Relatório SAC");
            System.out.println("| 7 | Voltar");
            System.out.print("Escolha: ");
            op = Integer.parseInt(in.nextLine().trim());

            switch (op) {
                case 1 -> {
                    System.out.print("Valor financiado (R$): ");
                    double v = Double.parseDouble(in.nextLine().trim());
                    System.out.print("Meses: ");
                    int m = Integer.parseInt(in.nextLine().trim());
                    System.out.print("Taxa de juros mensal (ex. 0.00917): ");
                    double t = Double.parseDouble(in.nextLine().trim());
                    var f = service.criar(v, m, t);
                    System.out.printf("Criado ID=%d%n", f.getId());
                }
                case 2 -> service.listar().forEach(f ->
                        System.out.printf("ID=%d | Valor R$%.2f | Meses %d | Taxa %.4f%n",
                                f.getId(), f.getValorFinanciado(), f.getMeses(), f.getTaxaJuros()));
                case 3 -> {
                    System.out.print("ID: ");
                    int id = Integer.parseInt(in.nextLine().trim());
                    System.out.print("Novo valor (R$): ");
                    double v = Double.parseDouble(in.nextLine().trim());
                    System.out.print("Novos meses: ");
                    int m = Integer.parseInt(in.nextLine().trim());
                    System.out.print("Nova taxa mensal: ");
                    double t = Double.parseDouble(in.nextLine().trim());
                    System.out.println(service.atualizar(id, v, m, t) ? "Atualizado." : "Não encontrado.");
                }
                case 4 -> {
                    System.out.print("ID: ");
                    int id = Integer.parseInt(in.nextLine().trim());
                    System.out.println(service.excluir(id) ? "Excluído." : "Não encontrado.");
                }
                case 5 -> {
                    System.out.print("ID: ");
                    int id = Integer.parseInt(in.nextLine().trim());
                    service.gerarRelatorioPrice(id, Path.of("Relatorio_de_financiamento_Price.txt"));
                    System.out.println("Relatório gerado em Relatorio_de_financiamento_Price.txt");
                }
                case 6 -> {
                    System.out.print("ID: ");
                    int id = Integer.parseInt(in.nextLine().trim());
                    service.gerarRelatorioSAC(id, Path.of("Relatorio_de_financiamento_SAC.txt"));
                    System.out.println("Relatório gerado em Relatorio_de_financiamento_SAC.txt");
                }
                case 7 -> {}
                default -> System.out.println("Opção inválida");
            }
        } while (op != 7);
    }
}
