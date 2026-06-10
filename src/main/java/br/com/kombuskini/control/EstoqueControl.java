package br.com.kombuskini.control;

import br.com.kombuskini.boundary.repository.EstoqueDAO;
import br.com.kombuskini.entity.Estoque;
import br.com.kombuskini.entity.ItemEstoque;
import br.com.kombuskini.entity.Kombuskini;

import java.util.ArrayList;
import java.util.List;

public class EstoqueControl {
    private final EstoqueDAO estoqueDAO;

    public EstoqueControl(EstoqueDAO estoqueDAO) {

        this.estoqueDAO = estoqueDAO;
    }

    public void registrarEntrada(Long kombuskiniId, int quantidade) {
        if (kombuskiniId == null) {
            throw new IllegalArgumentException("O id do kombuskini não pode ser nulo.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de entrada deve ser positiva.");
        }
        estoqueDAO.salvarEntrada(kombuskiniId.intValue(), quantidade);
    }

    public void registrarSaida(Long kombuskiniId, int quantidade) {
        if (kombuskiniId == null) {
            throw new IllegalArgumentException("O id do kombuskini não pode ser nulo.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de saída deve ser positiva.");
        }
        int atual = obterQuantidade(kombuskiniId);
        if (atual < quantidade) {
            throw new IllegalStateException(
                "Quantidade insuficiente no estoque para o kombuskini com ID " + kombuskiniId +
                ". Estoque atual: " + atual + ", solicitado: " + quantidade
            );
        }
        estoqueDAO.salvarSaida(kombuskiniId.intValue(), quantidade);
    }

    public int obterQuantidade(Long kombuskiniId) {
        if (kombuskiniId == null) {
            throw new IllegalArgumentException("O id do kombuskini não pode ser nulo.");
        }
        return estoqueDAO.obterQuantidade(kombuskiniId.intValue());
    }

    //retorna o total de kombuskini no estoque por kombuskini
    public Estoque obterEstoqueGeral(List<Kombuskini> kombuskinis) {
        int total = 0;
        for (Kombuskini k : kombuskinis) {
            total += obterQuantidade(k.getId());
        }
        return new Estoque(1, kombuskinis, total);
    }

    //mostra a lista de itens do estoque
    public List<ItemEstoque> obterItensEstoque(List<Kombuskini> kombuskinis) {
        List<ItemEstoque> itens = new ArrayList<>();
        for (Kombuskini k : kombuskinis) {
            int qtd = obterQuantidade(k.getId());
            if (qtd > 0) {
                // Instancia ItemEstoque. O construtor de ItemEstoque exige (Kombuskini, int, LocalDate)
                itens.add(new ItemEstoque(k, qtd, null));
            }
        }
        return itens;
    }
}
