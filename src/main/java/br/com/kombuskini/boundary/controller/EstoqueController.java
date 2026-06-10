package br.com.kombuskini.boundary.controller;

import br.com.kombuskini.boundary.repository.KombuskiniDAO;
import br.com.kombuskini.control.EstoqueControl;
import br.com.kombuskini.entity.ItemEstoque;
import br.com.kombuskini.entity.Kombuskini;
import br.com.kombuskini.util.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;

import java.util.List;

public class EstoqueController {

    private final EstoqueControl estoqueControl;
    private final KombuskiniDAO kombuskiniDAO;
    private final EstoqueBoundary boundary;

    private final ObservableList<ItemEstoque> observableListTable = FXCollections.observableArrayList();
    private final ObservableList<Kombuskini> observableListCombo = FXCollections.observableArrayList();

    public EstoqueController(EstoqueControl estoqueControl, KombuskiniDAO kombuskiniDAO) {
        this.estoqueControl = estoqueControl;
        this.kombuskiniDAO = kombuskiniDAO;
        this.boundary = new EstoqueBoundary();

        // Inicializar tabelas e combos
        boundary.getTblItens().setItems(observableListTable);
        boundary.getCmbKombuskini().setItems(observableListCombo);

        recarregarDados();
        configurarEventos();
    }

    public Parent getView() {
        return boundary;
    }

    private void configurarEventos() {
        // Seleção na tabela
        boundary.getTblItens().getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                boundary.preencherCampos(newSel);
            }
        });

        // Evento de seleção no ComboBox para atualizar o estoque atual em tempo real
        boundary.getCmbKombuskini().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                int qtd = estoqueControl.obterQuantidade(newVal.getId());
                boundary.getTxtQtdAtual().setText(String.valueOf(qtd));
            } else {
                boundary.getTxtQtdAtual().clear();
            }
        });

        // Botão Movimentar
        boundary.getBtnMovimentar().setOnAction(e -> {
            Kombuskini prodSelecionado = boundary.getCmbKombuskini().getSelectionModel().getSelectedItem();
            if (prodSelecionado == null) {
                AlertHelper.showError("Erro", null, "Por favor, selecione um produto para a movimentação.");
                return;
            }

            int quantidade;
            try {
                quantidade = Integer.parseInt(boundary.getTxtQtdMovimento().getText());
                if (quantidade <= 0) {
                    throw new IllegalArgumentException("A quantidade movimentada deve ser maior que zero.");
                }
            } catch (NumberFormatException ex) {
                AlertHelper.showError("Erro", null, "A quantidade informada é inválida.");
                return;
            } catch (IllegalArgumentException ex) {
                AlertHelper.showError("Erro", null, ex.getMessage());
                return;
            }

            String tipoMov = boundary.getCmbTipoMov().getSelectionModel().getSelectedItem();

            try {
                if ("Entrada".equalsIgnoreCase(tipoMov)) {
                    estoqueControl.registrarEntrada(prodSelecionado.getId(), quantidade);
                    AlertHelper.showInfo("Sucesso", null, "Entrada de estoque realizada com sucesso!");
                } else if ("Saída".equalsIgnoreCase(tipoMov)) {
                    estoqueControl.registrarSaida(prodSelecionado.getId(), quantidade);
                    AlertHelper.showInfo("Sucesso", null, "Saída de estoque realizada com sucesso!");
                }
                boundary.limparCampos();
                recarregarDados();
            } catch (Exception ex) {
                AlertHelper.showError("Erro", "Erro ao processar movimentação", ex.getMessage());
            }
        });

        // Botão Limpar
        boundary.getBtnLimpar().setOnAction(e -> boundary.limparCampos());
    }

    public void recarregarDados() {
        try {
            // Carregar produtos no ComboBox
            List<Kombuskini> produtos = kombuskiniDAO.findAll();
            observableListCombo.setAll(produtos);

            // Carregar itens do estoque na tabela
            List<ItemEstoque> itens = estoqueControl.obterItensEstoque(produtos);
            observableListTable.setAll(itens);
        } catch (Exception e) {
            AlertHelper.showError("Erro", "Erro ao carregar dados do estoque", e.getMessage());
        }
    }
}
