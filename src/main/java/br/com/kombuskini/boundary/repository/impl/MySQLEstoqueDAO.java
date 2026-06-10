package br.com.kombuskini.boundary.repository.impl;

import br.com.kombuskini.boundary.repository.EstoqueDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLEstoqueDAO implements EstoqueDAO {

    @Override
    public int obterQuantidade(int kombuskiniId) {
        String sql = "SELECT quantidade FROM estoque_kombuskini WHERE kombuskini_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, kombuskiniId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantidade");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter quantidade do estoque: " + e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public void atualizarEstoque(int kombuskiniId, int quantidade) {
        String sql = "INSERT INTO estoque_kombuskini (kombuskini_id, quantidade) VALUES (?, ?) " +
                     "ON DUPLICATE KEY UPDATE quantidade = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, kombuskiniId);
            stmt.setInt(2, quantidade);
            stmt.setInt(3, quantidade);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar estoque: " + e.getMessage(), e);
        }
    }

    @Override
    public void salvarEntrada(int kombuskiniId, int quantidade) {
        int atual = obterQuantidade(kombuskiniId);
        atualizarEstoque(kombuskiniId, atual + quantidade);
    }

    @Override
    public void salvarSaida(int kombuskiniId, int quantidade) {
        int atual = obterQuantidade(kombuskiniId);
        if (atual < quantidade) {
            throw new IllegalStateException("Quantidade insuficiente no estoque para realizar a saída.");
        }
        atualizarEstoque(kombuskiniId, atual - quantidade);
    }
}
