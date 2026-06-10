package br.com.kombuskini.boundary.controller;

import br.com.kombuskini.entity.ItemEstoque;
import br.com.kombuskini.entity.Kombuskini;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;

public class EstoqueBoundary extends BorderPane {

    private final TableView<ItemEstoque> tblItens = new TableView<>();
    private final ComboBox<Kombuskini> cmbKombuskini = new ComboBox<>();
    private final TextField txtQtdAtual = new TextField();
    private final TextField txtQtdMovimento = new TextField();
    private final ComboBox<String> cmbTipoMov = new ComboBox<>();
    private final TextField txtObservacao = new TextField();

    private final Button btnMovimentar = new Button("Salvar Movimentação");
    private final Button btnLimpar = new Button("Limpar");

    public EstoqueBoundary() {
        setPadding(new Insets(15));

        // 1. Tabela (Centro)
        tblItens.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<ItemEstoque, Long> colProdId = new TableColumn<>("Produto ID");
        colProdId.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getKombuskini().getId()).asObject());
        colProdId.setSortable(true);

        TableColumn<ItemEstoque, String> colCor = new TableColumn<>("Cor");
        colCor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKombuskini().getCor()));
        colCor.setSortable(true);

        TableColumn<ItemEstoque, Double> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getKombuskini().getPreco()).asObject());
        colPreco.setSortable(true);

        TableColumn<ItemEstoque, Integer> colQuantidade = new TableColumn<>("Quantidade");
        colQuantidade.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantidade()).asObject());
        colQuantidade.setSortable(true);

        tblItens.getColumns().addAll(colProdId, colCor, colPreco, colQuantidade);

        VBox centerPane = new VBox(tblItens);
        VBox.setVgrow(tblItens, Priority.ALWAYS);
        setCenter(centerPane);

        // 2. Formulário (Direita)
        VBox form = new VBox(8);
        form.setPadding(new Insets(0, 0, 0, 15));
        form.setPrefWidth(280);

        Label lblTitle = new Label("Movimentação de Estoque");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Configurar ComboBox do Kombuskini
        cmbKombuskini.setPromptText("Selecione o Produto...");
        cmbKombuskini.setConverter(new StringConverter<Kombuskini>() {
            @Override
            public String toString(Kombuskini object) {
                return object == null ? "" : "ID: " + object.getId() + " - " + object.getCor();
            }

            @Override
            public Kombuskini fromString(String string) {
                return null;
            }
        });

        txtQtdAtual.setDisable(true);
        txtQtdAtual.setPromptText("0");

        cmbTipoMov.setItems(FXCollections.observableArrayList("Entrada", "Saída"));
        cmbTipoMov.getSelectionModel().selectFirst();

        form.getChildren().addAll(
                lblTitle,
                new Label("Produto:"), cmbKombuskini,
                new Label("Estoque Atual:"), txtQtdAtual,
                new Label("Tipo Movimentação:"), cmbTipoMov,
                new Label("Quantidade:"), txtQtdMovimento,
                new Label("Observação/Motivo:"), txtObservacao
        );

        // Botões do Formulário
        GridPane gridButtons = new GridPane();
        gridButtons.setHgap(10);
        gridButtons.setVgap(10);
        gridButtons.setPadding(new Insets(10, 0, 0, 0));

        gridButtons.add(btnMovimentar, 0, 0);
        gridButtons.add(btnLimpar, 1, 0);

        btnMovimentar.setMaxWidth(Double.MAX_VALUE);
        btnLimpar.setMaxWidth(Double.MAX_VALUE);

        // Definir coluna para expandir
        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setHgrow(Priority.ALWAYS);
        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setHgrow(Priority.ALWAYS);
        gridButtons.getColumnConstraints().addAll(cc1, cc2);

        form.getChildren().add(gridButtons);
        setRight(form);
    }

    public TableView<ItemEstoque> getTblItens() {
        return tblItens;
    }

    public ComboBox<Kombuskini> getCmbKombuskini() {
        return cmbKombuskini;
    }

    public TextField getTxtQtdAtual() {
        return txtQtdAtual;
    }

    public TextField getTxtQtdMovimento() {
        return txtQtdMovimento;
    }

    public ComboBox<String> getCmbTipoMov() {
        return cmbTipoMov;
    }

    public TextField getTxtObservacao() {
        return txtObservacao;
    }

    public Button getBtnMovimentar() {
        return btnMovimentar;
    }

    public Button getBtnLimpar() {
        return btnLimpar;
    }

    public void limparCampos() {
        cmbKombuskini.getSelectionModel().clearSelection();
        txtQtdAtual.clear();
        txtQtdMovimento.clear();
        cmbTipoMov.getSelectionModel().selectFirst();
        txtObservacao.clear();
        tblItens.getSelectionModel().clearSelection();
    }

    public void preencherCampos(ItemEstoque item) {
        if (item != null) {
            cmbKombuskini.getSelectionModel().select(item.getKombuskini());
            txtQtdAtual.setText(String.valueOf(item.getQuantidade()));
        }
    }
}
