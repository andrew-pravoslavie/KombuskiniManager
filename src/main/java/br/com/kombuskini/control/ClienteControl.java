package br.com.kombuskini.control;

import br.com.kombuskini.boundary.repository.ClienteDAO;
import br.com.kombuskini.entity.Cliente;

import java.util.List;

public class ClienteControl {
    private final ClienteDAO clienteDAO;

    public ClienteControl(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public Cliente cadastrar(Cliente cliente) {
        cliente.validar();
        return clienteDAO.cadastrar(cliente);
    }

    public void editar(Cliente cliente) {
        cliente.validar();
        clienteDAO.atualizar(cliente);
    }

    public void excluir(int id) {
        clienteDAO.apagar(id);
    }

    public List<Cliente> pesquisarPorNome(String nome) {
        if (nome == null) {
            nome = "";
        }
        return clienteDAO.pesquisarPorNome(nome);
    }

    public List<Cliente> pesquisarPorInstagram(String instagram) {
        if (instagram == null) {
            instagram = "";
        }
        return clienteDAO.pesquisarPorInstagram(instagram);
    }
}
