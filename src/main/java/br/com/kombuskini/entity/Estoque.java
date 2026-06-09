package br.com.kombuskini.entity;

public class Estoque {
    private int id;
    private Kombuskini kombuskini;
    private int quantidadeAtual;

    public Estoque(int id, Kombuskini kombuskini, int quantidadeAtual) {
        this.id = id;
        this.kombuskini = kombuskini;
        this.quantidadeAtual = quantidadeAtual;
    }
}
