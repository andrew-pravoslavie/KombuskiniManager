package br.com.kombuskini.boundary.repository.impl;

import br.com.kombuskini.boundary.repository.KombuskiniDAO;
import br.com.kombuskini.entity.Kombuskini;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySQLKombuskiniDAO implements KombuskiniDAO {

    @Override
    public Kombuskini save(Kombuskini kombuskini) {
        String sql = "INSERT INTO kombuskinis (cor, quantidade_nos, tipo_divisoes, quantidade_divisoes, tipo_cruz, has_tassel, preco) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, kombuskini.getCor());
            stmt.setInt(2, kombuskini.getQuantidadeNos());
            stmt.setString(3, kombuskini.getTipoDivisoes());
            stmt.setInt(4, kombuskini.getQuantidadeDivisoes());
            stmt.setString(5, kombuskini.getTipoCruz());
            stmt.setBoolean(6, kombuskini.isHasTassel());
            stmt.setDouble(7, kombuskini.getPreco());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    kombuskini.setId(rs.getLong(1));
                }
            }
            return kombuskini;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar Kombuskini: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Kombuskini kombuskini) {
        String sql = "UPDATE kombuskinis SET cor = ?, quantidade_nos = ?, tipo_divisoes = ?, quantidade_divisoes = ?, tipo_cruz = ?, has_tassel = ?, preco = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, kombuskini.getCor());
            stmt.setInt(2, kombuskini.getQuantidadeNos());
            stmt.setString(3, kombuskini.getTipoDivisoes());
            stmt.setInt(4, kombuskini.getQuantidadeDivisoes());
            stmt.setString(5, kombuskini.getTipoCruz());
            stmt.setBoolean(6, kombuskini.isHasTassel());
            stmt.setDouble(7, kombuskini.getPreco());
            stmt.setLong(8, kombuskini.getId());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new RuntimeException("Nenhum Kombuskini encontrado com o ID informado: " + kombuskini.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar Kombuskini: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM kombuskinis WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new RuntimeException("Nenhum Kombuskini encontrado com o ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir Kombuskini: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Kombuskini> findById(int id) {
        String sql = "SELECT * FROM kombuskinis WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Kombuskini kombuskini = new Kombuskini(
                            rs.getLong("id"),
                            rs.getString("cor"),
                            rs.getInt("quantidade_nos"),
                            rs.getString("tipo_divisoes"),
                            rs.getInt("quantidade_divisoes"),
                            rs.getString("tipo_cruz"),
                            rs.getBoolean("has_tassel"),
                            rs.getDouble("preco")
                    );
                    return Optional.of(kombuskini);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar Kombuskini por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Kombuskini> findAll() {
        List<Kombuskini> list = new ArrayList<>();
        String sql = "SELECT * FROM kombuskinis";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Kombuskini kombuskini = new Kombuskini(
                        rs.getLong("id"),
                        rs.getString("cor"),
                        rs.getInt("quantidade_nos"),
                        rs.getString("tipo_divisoes"),
                        rs.getInt("quantidade_divisoes"),
                        rs.getString("tipo_cruz"),
                        rs.getBoolean("has_tassel"),
                        rs.getDouble("preco")
                );
                list.add(kombuskini);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar Kombuskinis: " + e.getMessage(), e);
        }
        return list;
    }
}
