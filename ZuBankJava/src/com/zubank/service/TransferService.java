package com.zubank.service;

import com.zubank.model.Conta;
import com.zubank.model.TipoTransacao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TransferService {

    public static class Transferencia {
        public final int id;
        public final String beneficiario;
        public final int contaDestino;
        public final double valor;

        public Transferencia(int id, String beneficiario, int contaDestino, double valor) {
            this.id = id;
            this.beneficiario = beneficiario;
            this.contaDestino = contaDestino;
            this.valor = valor;
        }
    }

    private final List<Transferencia> pendentes = new ArrayList<>();

    public void criar(String beneficiario, int contaDestino, double valor) {
        int id = pendentes.size() + 1;
        pendentes.add(new Transferencia(id, beneficiario, contaDestino, valor));
    }

    public List<Transferencia> listar() {
        return new ArrayList<>(pendentes);
    }

    public boolean deletar(int id) {
        return pendentes.removeIf(t -> t.id == id);
    }

    /** Realiza todas as transferências pendentes, grava no arquivo e retorna a lista processada. */
    public List<Transferencia> realizarTodas(Conta origem, Function<Integer, Conta> resolveContaPorNumero, Path arquivo) throws IOException {
        StringBuilder sb = new StringBuilder();
        List<Transferencia> processadas = new ArrayList<>();
        for (Transferencia t : new ArrayList<>(pendentes)) {
            Conta destino = resolveContaPorNumero.apply(t.contaDestino);
            if (destino != null && origem.transferirPara(destino, t.valor)) {
                sb.append(String.format("Beneficiário: %s | Conta: %d | Valor: R$%.2f%n",
                        t.beneficiario, t.contaDestino, t.valor));
                processadas.add(t);
            } else {
                sb.append(String.format("FALHA: Saldo insuficiente ou conta inválida para %s (conta %d) valor R$%.2f%n",
                        t.beneficiario, t.contaDestino, t.valor));
            }
        }
        Files.writeString(arquivo, sb.toString());
        pendentes.clear();
        return processadas;
    }
}
