package br.com.kombuskini.boundary.repository.impl;

import br.com.kombuskini.boundary.repository.PedidoDAO;
import br.com.kombuskini.entity.Cliente;
import br.com.kombuskini.entity.ItemPedido;
import br.com.kombuskini.entity.Kombuskini;
import br.com.kombuskini.entity.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySQLPedidoDAO implements PedidoDAO {

    @Override
    public Pedido save(Pedido pedido) {
        String sql = "INSERT INTO pedido (cliente_id, status) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, pedido.getCliente().getId());
            stmt.setString(2, pedido.getStatus().name());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pedido.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar pedido: " + e.getMessage(), e);
        }

        // Persiste todos os itens do pedido
        for (ItemPedido item : pedido.getItens()) {
            saveItem(pedido.getId(), item);
        }

        return pedido;
    }

    @Override
    public void update(Pedido pedido) {
        String sql = "UPDATE pedido SET cliente_id = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pedido.getCliente().getId());
            stmt.setString(2, pedido.getStatus().name());
            stmt.setInt(3, pedido.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM pedido WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Pedido> findBy(int id) {
        String sql = "SELECT * FROM pedido WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int clienteId = rs.getInt("cliente_id");
                    String statusStr = rs.getString("status");

                    // Busca o cliente real no banco de dados
                    Cliente cliente = buscarCliente(conn, clienteId);
                    Pedido pedido = new Pedido(id, cliente);

                    // Carregar itens do pedido enquanto o status temporário é RASCUNHO
                    carregarItens(conn, pedido);

                    // Restaurar status real do banco de dados
                    pedido.setStatus(Pedido.StatusPedido.valueOf(statusStr));

                    return Optional.of(pedido);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedido por id: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Pedido> findAll() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int clienteId = rs.getInt("cliente_id");
                String statusStr = rs.getString("status");

                Cliente cliente = buscarCliente(conn, clienteId);
                Pedido pedido = new Pedido(id, cliente);

                carregarItens(conn, pedido);

                pedido.setStatus(Pedido.StatusPedido.valueOf(statusStr));
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todos os pedidos: " + e.getMessage(), e);
        }
        return pedidos;
    }

    @Override
    public void saveItem(int PedidoId, ItemPedido item) {
        String sql = "INSERT INTO item_pedido (pedido_id, kombuskini_id, quantidade) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE quantidade = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, PedidoId);
            stmt.setLong(2, item.getKombuskini().getId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setInt(4, item.getQuantidade());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar item do pedido: " + e.getMessage(), e);
        }
    }

    private void carregarItens(Connection conn, Pedido pedido) throws SQLException {
        String sql = "SELECT * FROM item_pedido WHERE pedido_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedido.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int kombuskiniId = rs.getInt("kombuskini_id");
                    int quantidade = rs.getInt("quantidade");

                    // Busca o kombuskini real no banco de dados
                    Kombuskini kombuskini = buscarKombuskini(conn, kombuskiniId);
                    pedido.adicionarProduto(kombuskini, quantidade);
                }
            }
        }
    }

    private Cliente buscarCliente(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("telefone"),
                            rs.getString("instagram"),
                            rs.getString("email")
                    );
                }
            }
        }
        // Fallback caso não seja encontrado
        return new Cliente(id, "Cliente Desconhecido (" + id + ")", "99999-9999", null, null);
    }

    private Kombuskini buscarKombuskini(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM kombuskinis WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Kombuskini(
                            rs.getLong("id"),
                            rs.getString("cor"),
                            rs.getInt("quantidade_nos"),
                            rs.getString("tipo_divisoes"),
                            rs.getInt("quantidade_divisoes"),
                            rs.getString("tipo_cruz"),
                            rs.getBoolean("has_tassel"),
                            rs.getDouble("preco")
                    );
                }
            }
        }
        // Fallback caso não seja encontrado
        return new Kombuskini((long) id, "Cor Desconhecida", 10, "Tipo", 2, "Cruz", false, 15.0);
    }
}
