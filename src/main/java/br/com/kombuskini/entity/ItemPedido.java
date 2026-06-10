package br.com.kombuskini.entity;

public class ItemPedido {
    private final Kombuskini kombuskini;
    private int quantidade;

    public ItemPedido(Kombuskini kombuskini, int quantidade) {
        if (kombuskini == null) {
            throw new IllegalArgumentException("O produto não pode ser nulo.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        this.kombuskini = kombuskini;
        this.quantidade = quantidade;
    }

    public void adicionar(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("A quantidade a ser adicionada deve ser maior que zero.");
        }
        this.quantidade += n;
    }

    public void remover(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("A quantidade a ser removida deve ser maior que zero.");
        }
        if (quantidade < n) {
            throw new IllegalArgumentException(
                "Quantidade insuficiente para remover " + n + " unidades. Quantidade atual: " + quantidade
            );
        }
        this.quantidade -= n;
    }

    public double getPrecoTotal() {
        return kombuskini.getPreco() * quantidade;
    }

    public Kombuskini getKombuskini() {
        return kombuskini;
    }

    public int getQuantidade() {
        return quantidade;
    }
}
