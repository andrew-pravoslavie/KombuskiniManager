package br.com.kombuskini.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {
    public enum StatusPedido {
        RASCUNHO,      // Pedido sendo montado
        REGISTRADO,    // Pedido confirmado e fechado para edição
        ENVIADO,       // Pedido enviado ao cliente
        FINALIZADO     // Pedido entregue e concluído (dado baixa)
    }

    private int id;
    private Cliente cliente;
    private final List<ItemPedido> itens = new ArrayList<>();
    private StatusPedido status = StatusPedido.RASCUNHO;

    public Pedido(int id, Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("O cliente é obrigatório para abrir um pedido.");
        }
        this.id = id;
        this.cliente = cliente;
    }

    //Adicionar novo item ao pedido
    public void adicionarProduto(Kombuskini produto, int quantidade) {
        if (status != StatusPedido.RASCUNHO) {
            throw new IllegalStateException("Não é possível alterar itens de um pedido que não está em Rascunho.");
        }
        for (ItemPedido item : itens) {
            if (item.getKombuskini().getId().equals(produto.getId())) {
                item.adicionar(quantidade);
                return;
            }
        }
        itens.add(new ItemPedido(produto, quantidade));
    }

    //registra o pedido, usado pelo botão de Registrar Pedido
    public void registrar() {
        if (status != StatusPedido.RASCUNHO) {
            throw new IllegalStateException("Apenas pedidos em Rascunho podem ser registrados.");
        }
        if (itens.isEmpty()) {
            throw new IllegalStateException("Não é possível registrar um pedido sem itens.");
        }
        this.status = StatusPedido.REGISTRADO;
    }

    /**
     * Libera o pedido deduzindo os produtos do estoque.
     * Implementa: '2.5 Liberar Pedido com todos os produtos dele'
     * Só é permitido em pedidos com status REGISTRADO.
     */
    public void liberarEstoque() {
        if (status != StatusPedido.REGISTRADO) {
            throw new IllegalStateException("Apenas pedidos no estado REGISTRADO podem ter o estoque liberado.");
        }
        for (ItemPedido item : itens) {
            item.remover(item.getQuantidade());
        }
    }

    /**
     * Marca o pedido como enviado ao cliente.
     * Transição: REGISTRADO -> ENVIADO
     */
    public void enviar() {
        if (status != StatusPedido.REGISTRADO) {
            throw new IllegalStateException("Apenas pedidos REGISTRADOS podem ser enviados.");
        }
        this.status = StatusPedido.ENVIADO;
    }

    /**
     * Finaliza o pedido como entregue e concluído (dar baixa).
     * Transição: ENVIADO -> FINALIZADO
     */
    public void darBaixa() {
        if (status != StatusPedido.ENVIADO) {
            throw new IllegalStateException("Apenas pedidos ENVIADOS podem ser finalizados (dar baixa).");
        }
        this.status = StatusPedido.FINALIZADO;
    }

    /**
     * Calcula o valor total do pedido somando todos os itens.
     */
    public double getValorTotal() {
        return itens.stream().mapToDouble(ItemPedido::getPrecoTotal).sum();
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public StatusPedido getStatus() {
        return status;
    }
}
