package br.com.kombuskini.entity;

public class ItemEstoque {
    private Kombuskini kombuskini;
    private int quantidade;

    public ItemEstoque(Kombuskini kombuskini, int quantidade) {
        this.kombuskini = kombuskini;
        this.quantidade = quantidade;
    }

    public Kombuskini getKombuskini() {
        return kombuskini;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
