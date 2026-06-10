package br.com.kombuskini.entity;

import java.time.LocalDate;
import java.util.Locale;

public class ItemEstoque {
    private Kombuskini kombuskini;
    private int quantidade;
    private LocalDate dataVenda;

    public ItemEstoque(Kombuskini kombuskini, int quantidade, LocalDate dataVenda) {
        if(kombuskini == null){
            throw  new IllegalArgumentException("Deve-se informar algum kombuskini.");
        }

        if(quantidade <= 0){
            throw new IllegalArgumentException("Deve-se informar alguma quantidade positiva");
        }

        this.kombuskini = kombuskini;
        this.quantidade = quantidade;
        this.dataVenda = dataVenda;
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
