package br.com.kombuskini.boundary.repository.impl;

import br.com.kombuskini.boundary.repository.ClienteDAO;
import br.com.kombuskini.entity.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLClienteDAO implements ClienteDAO {

    @Override
    public Cliente cadastrar(Cliente cliente){
        String sql = "insert into clientes (nome, telefone, instagram, email) values (?, ?, ?, ?)";

        try{
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setString(1, cliente.getNome());
            stm.setString(2, cliente.getTelefone());
            stm.setString(3, cliente.getInstagram());
            stm.setString(4, cliente.getEmail());
            stm.executeUpdate();
            ResultSet id = stm.getGeneratedKeys();
            if(id.next()){
                cliente.setId(id.getInt(1));
            }
            return cliente;
        }catch(Exception e){
            throw new RuntimeException("Erro ao salvar no banco de dados");
        }
    }

    @Override
    public List<Cliente> pesquisarPorNome(String nome) {
        String sql = "select * from clientes where nome like ?";
        List<Cliente> clientes = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, "%" + nome + "%");
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("telefone"),
                            rs.getString("instagram"),
                            rs.getString("email")
                    );
                    clientes.add(cliente);
                }
            }
            return clientes;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean atualizar(Cliente cliente){
        String sql = "update clientes set nome = ?, telefone = ?, instagram = ?, email = ? where id = ?";
        try{
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, cliente.getNome());
            stm.setString(2, cliente.getTelefone());
            stm.setString(3, cliente.getInstagram());
            stm.setString(4, cliente.getEmail());
            stm.setInt(5, cliente.getId());

            int linhasAfetadas = stm.executeUpdate();
            if(linhasAfetadas == 0){
                throw new RuntimeException("Nenhum cliente encontrado com o ID informado.");
            }
            return true;
        }catch(SQLException e){
            throw new RuntimeException("Erro ao atualizar cliente no banco de dados");
        }
    }

    @Override
    public boolean apagar(int id){
        String sql = "delete from clientes where id = ?";
        try{
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, id);
            int linhasAfetadas = stm.executeUpdate();
            if(linhasAfetadas == 0){
                throw new RuntimeException("Nenhum cliente encontrado com o ID informado");
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Cliente> pesquisarPorInstagram(String instagram){
        String sql = "select * from clientes where instagram like ?";
        List<Cliente> clientes = new ArrayList<>();
        try{
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, "%" + instagram + "%");
            try(ResultSet rs = stm.executeQuery()){
                while(rs.next()){
                    Cliente cliente = new Cliente(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("telefone"),
                            rs.getString("instagram"),
                            rs.getString("email")
                    );
                    clientes.add(cliente);
                }
            }
            return clientes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    void cadastrar(Cliente cliente);                // C
    List<Cliente> pesquisarPorNome(String nome);     // R
    void atualizar(Cliente cliente);                // U
    void apagar(Cliente cliente);                   // D
    void pesquisarPorEmail(String email);
     */
}
