package br.com.kombuskini.boundary.repository;

public interface EstoqueDAO {
    void salvarEntrada(int kombuskiniId, int quantidade);
    void salvarSaida(int kombuskiniId, int quantidade);
    int obterQuantidade(int kombuskiniId);
    void atualizarEstoque(int kombuskiniId, int quantidade);
}
