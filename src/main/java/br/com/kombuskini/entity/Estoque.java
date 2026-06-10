package br.com.kombuskini.entity;

import java.util.List;

public class Estoque {
    private int id;
    private List<Kombuskini> kombuskini;
    private int quantidadeAtual;

    public Estoque(int id, List<Kombuskini> kombuskini, int quantidadeAtual) {
        if(quantidadeAtual > 0){
            this.id = id;
            this.kombuskini = kombuskini;
            this.quantidadeAtual = quantidadeAtual;
        } else{
            throw new IllegalArgumentException ("Deve ser informada alguma quantidade no novo estoque!");
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Kombuskini> getKombuskini() {
        return kombuskini;
    }

    public void setKombuskini(List<Kombuskini> kombuskini) {
        this.kombuskini = kombuskini;
    }

    public int getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(int quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public void registrarEntrada(int qtd){
        quantidadeAtual += qtd;
    }

    public void registrarSaida(int qtd){
        if(qtd > quantidadeAtual){
            throw new IllegalStateException("Não há peças o suficiente no estoque");
        } else {
            quantidadeAtual -= qtd;
        }
    }
}
