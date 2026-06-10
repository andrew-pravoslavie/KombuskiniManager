package br.com.kombuskini.control;

import br.com.kombuskini.boundary.repository.KombuskiniDAO;
import br.com.kombuskini.boundary.repository.PedidoDAO;
import br.com.kombuskini.entity.Cliente;
import br.com.kombuskini.entity.ItemPedido;
import br.com.kombuskini.entity.Kombuskini;
import br.com.kombuskini.entity.Pedido;

public class VendaControl {
    private final PedidoDAO pedidoDAO;
    private final KombuskiniDAO kombuskiniDAO;

    public VendaControl(PedidoDAO pedido, KombuskiniDAO kombuskini) {
        this.pedidoDAO = pedido;
        this.kombuskiniDAO = kombuskini;
    }

    public Pedido abrirPedido(Cliente cliente) {
        Pedido pedido = new Pedido(0, cliente);
        return pedidoDAO.save(pedido);
    }

    public void adicionarItem(Pedido pedido, Kombuskini kombuskini, int quantidade) {
        pedido.adicionarProduto(kombuskini, quantidade);

        // Busca o item atualizado na lista do pedido (com a nova quantidade somada)
        ItemPedido itemAtualizado = pedido.getItens().stream()
                .filter(item -> item.getKombuskini().getId().equals(kombuskini.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Item não encontrado no pedido após adição."));

        pedidoDAO.saveItem(pedido.getId(), itemAtualizado);
    }

    public void registrarPedido(Pedido pedido) {
        pedido.registrar();
        pedidoDAO.update(pedido);
    }

    public void liberarEstoquePedido(Pedido pedido, EstoqueControl estoqueControl) {
        if (pedido.getStatus() != Pedido.StatusPedido.REGISTRADO) {
            throw new IllegalStateException("Apenas pedidos no estado REGISTRADO podem ter o estoque liberado.");
        }

        // Dá baixa de cada item do pedido no estoque do banco de dados
        for (ItemPedido item : pedido.getItens()) {
            estoqueControl.registrarSaida(item.getKombuskini().getId(), item.getQuantidade());
        }

        // Executa a lógica de liberação no objeto Pedido (que zera a quantidade local para indicar a baixa)
        pedido.liberarEstoque();
        pedidoDAO.update(pedido);
    }

    public void enviarPedido(Pedido pedido) {
        pedido.enviar();
        pedidoDAO.update(pedido);
    }

    public void finalizarPedido(Pedido pedido) {
        pedido.darBaixa();
        pedidoDAO.update(pedido);
    }
}
