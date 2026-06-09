package br.com.kombuskini.entity;

import java.util.List;

public class Estoque {
    private int id;
    private List<Kombuskini> kombuskini;
    private int quantidadeAtual;

    public Estoque(int id, List<Kombuskini> kombuskini, int quantidadeAtual) {
        this.id = id;
        this.kombuskini = kombuskini;
        this.quantidadeAtual = quantidadeAtual;
    }
}
