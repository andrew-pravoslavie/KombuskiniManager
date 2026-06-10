package br.com.kombuskini.boundary.repository;

import br.com.kombuskini.entity.Cliente;

import java.util.List;

public interface ClienteDAO {
    Cliente cadastrar(Cliente cliente);                // C
    List<Cliente>pesquisarPorNome(String nome);     // R
    boolean atualizar(Cliente cliente);                // U
    boolean apagar(int id);                   // D
    List<Cliente> pesquisarPorInstagram(String instagram);
}
