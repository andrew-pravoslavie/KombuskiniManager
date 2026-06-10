package br.com.kombuskini.boundary.controller;

import br.com.kombuskini.boundary.repository.ClienteDAO;
import br.com.kombuskini.boundary.repository.KombuskiniDAO;
import br.com.kombuskini.boundary.repository.PedidoDAO;
import br.com.kombuskini.control.EstoqueControl;
import br.com.kombuskini.control.VendaControl;
import br.com.kombuskini.entity.Cliente;
import br.com.kombuskini.entity.Kombuskini;
import br.com.kombuskini.entity.Pedido;
import br.com.kombuskini.util.AlertHelper;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;

import java.util.List;

public class VendaController {

    private final VendaControl vendaControl;
    private final ClienteDAO clienteDAO;
    private final KombuskiniDAO kombuskiniDAO;
    private final PedidoDAO pedidoDAO;
    private final EstoqueControl estoqueControl;

    private final VendaBoundary boundary;
    private final ObservableList<Pedido> observablePedidos = FXCollections.observableArrayList();
    private final ObservableList<Cliente> observableClientes = FXCollections.observableArrayList();
    private final ObservableList<Kombuskini> observableKombuskinis = FXCollections.observableArrayList();

    public VendaController(VendaControl vendaControl, ClienteDAO clienteDAO, KombuskiniDAO kombuskiniDAO, PedidoDAO pedidoDAO, EstoqueControl estoqueControl) {
        this.vendaControl = vendaControl;
        this.clienteDAO = clienteDAO;
        this.kombuskiniDAO = kombuskiniDAO;
        this.pedidoDAO = pedidoDAO;
        this.estoqueControl = estoqueControl;
        this.boundary = new VendaBoundary();

        // Vincular coleções
        boundary.getTblPedidos().setItems(observablePedidos);
        boundary.getCmbClienteNovo().setItems(observableClientes);
        boundary.getCmbProduto().setItems(observableKombuskinis);

        recarregarDados();
        configurarEventos();
    }

    public Parent getView() {
        return boundary;
    }

    private void configurarEventos() {
        // Seleção de Pedido na tabela da esquerda
        boundary.getTblPedidos().getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                // Recarrega o pedido completo do banco para garantir consistência
                try {
                    Pedido completo = pedidoDAO.findBy(newSel.getId()).orElse(newSel);
                    boundary.preencherDetalhes(completo);
                    Platform.runLater(() -> boundary.getTxtQuantidade().requestFocus());
                } catch (Exception e) {
                    boundary.preencherDetalhes(newSel);
                }
            } else {
                boundary.limparDetalhes();
            }
        });


        // Botão Novo Pedido
        boundary.getBtnNovoPedido().setOnAction(e -> {
            Cliente cliente = boundary.getCmbClienteNovo().getSelectionModel().getSelectedItem();
            if (cliente == null) {
                AlertHelper.showError("Erro", null, "Por favor, selecione um cliente para abrir o pedido.");
                return;
            }

            try {
                Pedido novo = vendaControl.abrirPedido(cliente);
                AlertHelper.showInfo("Sucesso", null, "Pedido #" + novo.getId() + " aberto em Rascunho!");
                recarregarDados();
                boundary.getTblPedidos().getSelectionModel().select(novo);
                Platform.runLater(() -> boundary.getCmbProduto().requestFocus());
            } catch (Exception ex) {
                AlertHelper.showError("Erro", "Erro ao abrir pedido", ex.getMessage());
            }
        });

        // Botão Adicionar Item
        boundary.getBtnAdicionarItem().setOnAction(e -> {
            Pedido selecionado = boundary.getTblPedidos().getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                AlertHelper.showError("Erro", null, "Nenhum pedido selecionado.");
                return;
            }

            Kombuskini produto = boundary.getCmbProduto().getSelectionModel().getSelectedItem();
            if (produto == null) {
                AlertHelper.showError("Erro", null, "Por favor, selecione o produto.");
                return;
            }

            int quantidade;
            try {
                quantidade = Integer.parseInt(boundary.getTxtQuantidade().getText());
                if (quantidade <= 0) {
                    throw new IllegalArgumentException("A quantidade deve ser positiva.");
                }
            } catch (NumberFormatException ex) {
                AlertHelper.showError("Erro", null, "Quantidade inválida.");
                return;
            } catch (IllegalArgumentException ex) {
                AlertHelper.showError("Erro", null, ex.getMessage());
                return;
            }

            try {
                // Faz o recarregamento do pedido antes de alterar para evitar sobrescrever dados antigos
                Pedido completo = pedidoDAO.findBy(selecionado.getId()).orElse(selecionado);
                vendaControl.adicionarItem(completo, produto, quantidade);
                AlertHelper.showInfo("Sucesso", null, "Produto adicionado ao pedido!");
                boundary.getTxtQuantidade().clear();
                boundary.getCmbProduto().getSelectionModel().clearSelection();
                
                // Recarrega visual
                recarregarDados();
                boundary.getTblPedidos().getSelectionModel().select(completo);
                boundary.preencherDetalhes(completo);
            } catch (Exception ex) {
                AlertHelper.showError("Erro", "Erro ao adicionar item", ex.getMessage());
            }
        });

        // Botão Confirmar Pedido (Registrar)
        boundary.getBtnRegistrar().setOnAction(e -> {
            Pedido selecionado = boundary.getTblPedidos().getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                AlertHelper.showError("Erro", null, "Nenhum pedido selecionado.");
                return;
            }

            try {
                Pedido completo = pedidoDAO.findBy(selecionado.getId()).orElse(selecionado);
                vendaControl.registrarPedido(completo);
                AlertHelper.showInfo("Sucesso", null, "Pedido registrado com sucesso!");
                recarregarDados();
                boundary.getTblPedidos().getSelectionModel().select(completo);
            } catch (Exception ex) {
                AlertHelper.showError("Erro", "Erro ao registrar pedido", ex.getMessage());
            }
        });

        // Botão Liberar Estoque (Enviar)
        boundary.getBtnEnviar().setOnAction(e -> {
            Pedido selecionado = boundary.getTblPedidos().getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                AlertHelper.showError("Erro", null, "Nenhum pedido selecionado.");
                return;
            }

            try {
                Pedido completo = pedidoDAO.findBy(selecionado.getId()).orElse(selecionado);
                
                // Realiza a baixa do estoque (saída) e envia o pedido
                vendaControl.liberarEstoquePedido(completo, estoqueControl);
                vendaControl.enviarPedido(completo);
                
                AlertHelper.showInfo("Sucesso", null, "Estoque liberado e pedido enviado!");
                recarregarDados();
                boundary.getTblPedidos().getSelectionModel().select(completo);
            } catch (Exception ex) {
                AlertHelper.showError("Erro", "Erro ao processar envio/baixa de estoque", ex.getMessage());
            }
        });

        // Botão Concluir Pedido (Finalizar)
        boundary.getBtnFinalizar().setOnAction(e -> {
            Pedido selecionado = boundary.getTblPedidos().getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                AlertHelper.showError("Erro", null, "Nenhum pedido selecionado.");
                return;
            }

            try {
                Pedido completo = pedidoDAO.findBy(selecionado.getId()).orElse(selecionado);
                vendaControl.finalizarPedido(completo);
                AlertHelper.showInfo("Sucesso", null, "Pedido finalizado com sucesso!");
                recarregarDados();
                boundary.getTblPedidos().getSelectionModel().select(completo);
            } catch (Exception ex) {
                AlertHelper.showError("Erro", "Erro ao finalizar pedido", ex.getMessage());
            }
        });

        // Botão Excluir Pedido
        boundary.getBtnExcluirPedido().setOnAction(e -> {
            Pedido selecionado = boundary.getTblPedidos().getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                AlertHelper.showError("Erro", null, "Nenhum pedido selecionado.");
                return;
            }

            try {
                pedidoDAO.delete(selecionado.getId());
                AlertHelper.showInfo("Sucesso", null, "Pedido excluído!");
                boundary.limparDetalhes();
                recarregarDados();
            } catch (Exception ex) {
                AlertHelper.showError("Erro", "Erro ao excluir pedido", ex.getMessage());
            }
        });
    }

    public void recarregarDados() {
        try {
            // Carregar listas nos ComboBoxes
            List<Cliente> clientes = clienteDAO.pesquisarPorNome("");
            observableClientes.setAll(clientes);

            List<Kombuskini> kombuskinis = kombuskiniDAO.findAll();
            observableKombuskinis.setAll(kombuskinis);

            // Carregar pedidos
            List<Pedido> pedidos = pedidoDAO.findAll();
            observablePedidos.setAll(pedidos);
        } catch (Exception e) {
            AlertHelper.showError("Erro", "Erro ao carregar dados de vendas", e.getMessage());
        }
    }
}
