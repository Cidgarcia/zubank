# ZuBank (Java, POO)

Console app que replica o projeto C anexado (cliente, conta, transferência, investimento, empréstimo, financiamento e extrato)
usando **Programação Orientada a Objetos** em Java.

## Como compilar e executar (sem Maven)

```bash
# 1) Entre na pasta
cd ZuBankJava

# 2) Compile todos os arquivos
javac -encoding UTF-8 $(find src -name "*.java") -d out

# 3) Rode o programa
java -cp out com.zubank.Main
```

## Arquivos gerados
- `transferencias.txt`
- `investimentos.txt`
- `emprestimos.txt`
- `financiamentos.txt`
- `Relatorio_de_financiamento_Price.txt`
- `Relatorio_de_financiamento_SAC.txt`
- `extrato.txt`

## Observações
- Taxas e fórmulas espelham as usadas no projeto em C:
  - Empréstimo: taxa mensal padrão **1.46%** (0.0146).
  - Investimento: rendimento composto mensal de **0,9542%** (`pow(1.009542, meses)`), com IR por faixa de meses (≤6: 22,5%; ≤12: 20%; ≤24: 17,5%; >24: 15%).
  - Financiamento: implementa **Price** e **SAC**, com taxa configurável (padrão 0.917%/mês = 0.00917 quando não especificado pelo usuário).
- O design segue os conceitos pedidos no PDF: `Banco`, `Cliente`, `Conta` (abstrata), `ContaCorrente`, `ContaPoupanca`, `Transacao` e serviços por domínio.
