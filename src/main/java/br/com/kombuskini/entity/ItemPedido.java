package br.com.kombuskini.entity;

public class ItemPedido {
    private Kombuskini kombuskini;
    private int quantidade;

    public ItemPedido(Kombuskini kombuskini, int quantidade) {
        this.kombuskini = kombuskini;
        this.quantidade = quantidade;
    }

    public void adicionar(int n){
        if(n <= 0){
            throw new IllegalArgumentException("A quantidade a ser adicionada deve ser maior que zero");
        }
        this.quantidade = quantidade + n;
    }

    public void remover(int n){
        if(n <= 0){
            throw new IllegalArgumentException("A quantidade a ser removida deve ser maior que zero");
        }
        if(quantidade < n){
            throw new IllegalArgumentException("Estoque insuficiente");
        }
        this.quantidade = quantidade - n;
    }

    public Kombuskini getKombuskini() {
        return kombuskini;
    }

    public int getQuantidade() {
        return quantidade;
    }
}
