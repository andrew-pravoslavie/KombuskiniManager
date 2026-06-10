package br.com.kombuskini.boundary.controller;

import br.com.kombuskini.boundary.repository.KombuskiniDAO;
import br.com.kombuskini.entity.Kombuskini;
import br.com.kombuskini.util.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;

import java.util.List;

public class ProdutoCrudController {

    private final KombuskiniDAO kombuskiniDAO;
    private final KombuskiniBoundary boundary;
    private final ObservableList<Kombuskini> observableList = FXCollections.observableArrayList();

    public ProdutoCrudController(KombuskiniDAO kombuskiniDAO) {
        this.kombuskiniDAO = kombuskiniDAO;
        this.boundary = new KombuskiniBoundary();

        // Inicializar tabela
        boundary.getTblKombuskinis().setItems(observableList);
        recarregarTabela();

        // Configurar Ações
        configurarEventos();
    }

    public Parent getView() {
        return boundary;
    }

    private void configurarEventos() {
        // Seleção na tabela
        boundary.getTblKombuskinis().getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                boundary.preencherCampos(newSel);
            }
        });

        // Botão Cadastrar
        boundary.getBtnCadastrar().setOnAction(e -> {
            try {
                Kombuskini k = extrairCampos(0L);
                kombuskiniDAO.save(k);
                AlertHelper.showInfo("Sucesso", null, "Kombuskini cadastrado com sucesso!");
                boundary.limparCampos();
                recarregarTabela();
            } catch (Exception ex) {
                AlertHelper.showError("Erro", "Erro ao cadastrar produto", ex.getMessage());
            }
        });

        // Botão Atualizar
        boundary.getBtnAtualizar().setOnAction(e -> {
            Kombuskini selecionado = boundary.getTblKombuskinis().getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                AlertHelper.showError("Aviso", null, "Selecione um Kombuskini na tabela para atualizar.");
                return;
            }

            try {
                Kombuskini k = extrairCampos(selecionado.getId());
                kombuskiniDAO.update(k);
                AlertHelper.showInfo("Sucesso", null, "Kombuskini atualizado com sucesso!");
                boundary.limparCampos();
                recarregarTabela();
            } catch (Exception ex) {
                AlertHelper.showError("Erro", "Erro ao atualizar produto", ex.getMessage());
            }
        });

        // Botão Apagar
        boundary.getBtnApagar().setOnAction(e -> {
            Kombuskini selecionado = boundary.getTblKombuskinis().getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                AlertHelper.showError("Aviso", null, "Selecione um Kombuskini na tabela para excluir.");
                return;
            }

            try {
                kombuskiniDAO.delete(selecionado.getId().intValue());
                AlertHelper.showInfo("Sucesso", null, "Kombuskini excluído com sucesso!");
                boundary.limparCampos();
                recarregarTabela();
            } catch (Exception ex) {
                AlertHelper.showError("Erro", "Erro ao excluir produto", ex.getMessage());
            }
        });

        // Botão Limpar
        boundary.getBtnLimpar().setOnAction(e -> boundary.limparCampos());
    }

    private Kombuskini extrairCampos(Long id) {
        String cor = boundary.getTxtCor().getText();
        if (cor == null || cor.trim().isEmpty()) {
            throw new IllegalArgumentException("A cor é obrigatória.");
        }

        double preco;
        try {
            preco = Double.parseDouble(boundary.getTxtPreco().getText());
            if (preco <= 0) {
                throw new IllegalArgumentException("O preço deve ser positivo.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Preço inválido.");
        }

        int qtdNos;
        try {
            qtdNos = Integer.parseInt(boundary.getTxtQtdNos().getText());
            if (qtdNos < 0) {
                throw new IllegalArgumentException("A quantidade de nós não pode ser negativa.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Quantidade de nós inválida.");
        }

        int qtdDivisoes;
        try {
            qtdDivisoes = Integer.parseInt(boundary.getTxtQtdDivisoes().getText());
            if (qtdDivisoes < 0) {
                throw new IllegalArgumentException("A quantidade de divisões não pode ser negativa.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Quantidade de divisões inválida.");
        }

        String tipoDivisoes = boundary.getTxtTipoDivisoes().getText();
        String tipoCruz = boundary.getTxtTipoCruz().getText();
        boolean hasTassel = boundary.getChkTassel().isSelected();

        return new Kombuskini(id, cor, qtdNos, tipoDivisoes, qtdDivisoes, tipoCruz, hasTassel, preco);
    }

    public void recarregarTabela() {
        try {
            List<Kombuskini> todos = kombuskiniDAO.findAll();
            observableList.setAll(todos);
        } catch (Exception e) {
            AlertHelper.showError("Erro", "Erro ao carregar dados dos produtos", e.getMessage());
        }
    }
}
