package br.com.kombuskini.boundary.repository;

import br.com.kombuskini.entity.ItemPedido;
import br.com.kombuskini.entity.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository {
    Pedido save(Pedido pedido);
    void update (Pedido pedido);
    void delete (int id);
    Optional<Pedido> findBy(int id);
    List<Pedido> findAll();
    void saveItem (int PedidoId, ItemPedido item);
}
