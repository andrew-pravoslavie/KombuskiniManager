package br.com.kombuskini.entity;

public class Kombuskini {
    private Long id;
    private String cor;
    private int quantidadeNos;
    private String tipoDivisoes;
    private int quantidadeDivisoes;
    private String tipoCruz;
    private boolean hasTassel;
    private double preco;

    public Kombuskini(Long id, String cor, int quantidadeNos, String tipoDivisoes, int quantidadeDivisoes, String tipoCruz, boolean hasTassel, double preco) {
        this.id = id;
        this.cor = cor;
        this.quantidadeNos = quantidadeNos;
        this.tipoDivisoes = tipoDivisoes;
        this.quantidadeDivisoes = quantidadeDivisoes;
        this.tipoCruz = tipoCruz;
        this.hasTassel = hasTassel;
        this.preco = preco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public int getQuantidadeNos() {
        return quantidadeNos;
    }

    public void setQuantidadeNos(int quantidadeNos) {
        this.quantidadeNos = quantidadeNos;
    }

    public String getTipoDivisoes() {
        return tipoDivisoes;
    }

    public void setTipoDivisoes(String tipoDivisoes) {
        this.tipoDivisoes = tipoDivisoes;
    }

    public int getQuantidadeDivisoes() {
        return quantidadeDivisoes;
    }

    public void setQuantidadeDivisoes(int quantidadeDivisoes) {
        this.quantidadeDivisoes = quantidadeDivisoes;
    }

    public String getTipoCruz() {
        return tipoCruz;
    }

    public void setTipoCruz(String tipoCruz) {
        this.tipoCruz = tipoCruz;
    }

    public boolean isHasTassel() {
        return hasTassel;
    }

    public void setHasTassel(boolean hasTassel) {
        this.hasTassel = hasTassel;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}