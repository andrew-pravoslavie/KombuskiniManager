package br.com.kombuskini.boundary.controller;

import br.com.kombuskini.control.ClienteControl;
import br.com.kombuskini.entity.Cliente;
import br.com.kombuskini.util.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;

import java.util.List;

public class ClienteCrudController {

    private final ClienteControl clienteControl;
    private final ClienteBoundary boundary;
    private final ObservableList<Cliente> observableList = FXCollections.observableArrayList();

    public ClienteCrudController(ClienteControl clienteControl) {
        this.clienteControl = clienteControl;
        this.boundary = new ClienteBoundary();

        // Inicializar tabela
        boundary.getTblClientes().setItems(observableList);
        recarregarTabela();

        // Configurar Ações
        configurarEventos();
    }

    public Parent getView() {
        return boundary;
    }

    private void configurarEventos() {
        // Seleção na tabela
        boundary.getTblClientes().getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                boundary.preencherCampos(newSel);
            }
        });

        // Botão Cadastrar
        boundary.getBtnCadastrar().setOnAction(e -> {
            try {
                Cliente c = new Cliente(
                        0,
                        boundary.getTxtNome().getText(),
                        boundary.getTxtTelefone().getText(),
                        boundary.getTxtInstagram().getText(),
                        boundary.getTxtEmail().getText()
                );
                clienteControl.cadastrar(c);
                AlertHelper.showInfo("Sucesso", null, "Cliente cadastrado com sucesso!");
                boundary.limparCampos();
                recarregarTabela();
            } catch (Exception ex) {
                AlertHelper.showError("Erro", "Erro ao cadastrar cliente", ex.getMessage());
            }
        });

        // Botão Atualizar
        boundary.getBtnAtualizar().setOnAction(e -> {
            Cliente selecionado = boundary.getTblClientes().getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                AlertHelper.showError("Aviso", null, "Selecione um cliente na tabela para atualizar.");
                return;
            }

            try {
                selecionado.setNome(boundary.getTxtNome().getText());
                selecionado.setTelefone(boundary.getTxtTelefone().getText());
                selecionado.setInstagram(boundary.getTxtInstagram().getText());
                selecionado.setEmail(boundary.getTxtEmail().getText());

                clienteControl.editar(selecionado);
                AlertHelper.showInfo("Sucesso", null, "Cliente atualizado com sucesso!");
                boundary.limparCampos();
                recarregarTabela();
            } catch (Exception ex) {
                AlertHelper.showError("Erro", "Erro ao atualizar cliente", ex.getMessage());
            }
        });

        // Botão Apagar
        boundary.getBtnApagar().setOnAction(e -> {
            Cliente selecionado = boundary.getTblClientes().getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                AlertHelper.showError("Aviso", null, "Selecione um cliente na tabela para excluir.");
                return;
            }

            try {
                clienteControl.excluir(selecionado.getId());
                AlertHelper.showInfo("Sucesso", null, "Cliente excluído com sucesso!");
                boundary.limparCampos();
                recarregarTabela();
            } catch (Exception ex) {
                AlertHelper.showError("Erro", "Erro ao excluir cliente", ex.getMessage());
            }
        });

        // Botão Limpar
        boundary.getBtnLimpar().setOnAction(e -> boundary.limparCampos());

        // Botão Buscar
        boundary.getBtnBuscar().setOnAction(e -> {
            try {
                String filtro = boundary.getTxtBuscaNome().getText();
                List<Cliente> filtrados = clienteControl.pesquisarPorNome(filtro);
                observableList.setAll(filtrados);
            } catch (Exception ex) {
                AlertHelper.showError("Erro", "Erro ao buscar clientes", ex.getMessage());
            }
        });
    }

    public void recarregarTabela() {
        try {
            List<Cliente> todos = clienteControl.pesquisarPorNome("");
            observableList.setAll(todos);
        } catch (Exception e) {
            AlertHelper.showError("Erro", "Erro ao carregar dados dos clientes", e.getMessage());
        }
    }
}
