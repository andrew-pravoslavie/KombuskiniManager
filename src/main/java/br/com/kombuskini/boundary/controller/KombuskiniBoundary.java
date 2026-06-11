package br.com.kombuskini.boundary.controller;

import br.com.kombuskini.entity.Kombuskini;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class KombuskiniBoundary extends BorderPane {

    private final TableView<Kombuskini> tblKombuskinis = new TableView<>();
    private final TextField txtId = new TextField();
    private final TextField txtCor = new TextField();
    private final TextField txtQtdNos = new TextField();
    private final TextField txtTipoDivisoes = new TextField();
    private final TextField txtQtdDivisoes = new TextField();
    private final TextField txtTipoCruz = new TextField();
    private final CheckBox chkTassel = new CheckBox("Possui Tassel");
    private final TextField txtPreco = new TextField();

    private final Button btnCadastrar = new Button("Cadastrar");
    private final Button btnAtualizar = new Button("Atualizar");
    private final Button btnApagar = new Button("Excluir");
    private final Button btnLimpar = new Button("Limpar");

    public KombuskiniBoundary() {
        setPadding(new Insets(15));

        // 1. Tabela (Centro)
        tblKombuskinis.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Kombuskini, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getId()).asObject());
        colId.setSortable(true);

        TableColumn<Kombuskini, String> colCor = new TableColumn<>("Cor");
        colCor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCor()));
        colCor.setSortable(true);

        TableColumn<Kombuskini, Integer> colNos = new TableColumn<>("Qtd. Nós");
        colNos.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantidadeNos()).asObject());
        colNos.setSortable(true);

        TableColumn<Kombuskini, Double> colPreco = new TableColumn<>("Preço (R$)");
        colPreco.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPreco()).asObject());
        colPreco.setSortable(true);

        tblKombuskinis.getColumns().addAll(colId, colCor, colNos, colPreco);

        VBox centerPane = new VBox(tblKombuskinis);
        VBox.setVgrow(tblKombuskinis, Priority.ALWAYS);
        setCenter(centerPane);

        // 2. Formulário (Direita)
        VBox form = new VBox(8);
        form.setPadding(new Insets(0, 0, 0, 15));
        form.setPrefWidth(280);

        Label lblTitle = new Label("Cadastro de Kombuskini");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        txtId.setDisable(true);
        txtId.setPromptText("Gerado automaticamente");

        form.getChildren().addAll(
                lblTitle,
                new Label("ID:"), txtId,
                new Label("Cor:"), txtCor,
                new Label("Preço (R$):"), txtPreco,
                new Label("Quantidade Nós:"), txtQtdNos,
                new Label("Tipo Divisões:"), txtTipoDivisoes,
                new Label("Quantidade Divisões:"), txtQtdDivisoes,
                new Label("Tipo Cruz:"), txtTipoCruz,
                chkTassel
        );

        // Botões do Formulário
        GridPane gridButtons = new GridPane();
        gridButtons.setHgap(10);
        gridButtons.setVgap(10);
        gridButtons.setPadding(new Insets(10, 0, 0, 0));

        gridButtons.add(btnCadastrar, 0, 0);
        gridButtons.add(btnAtualizar, 1, 0);
        gridButtons.add(btnApagar, 0, 1);
        gridButtons.add(btnLimpar, 1, 1);

        btnCadastrar.setMaxWidth(Double.MAX_VALUE);
        btnAtualizar.setMaxWidth(Double.MAX_VALUE);
        btnApagar.setMaxWidth(Double.MAX_VALUE);
        btnLimpar.setMaxWidth(Double.MAX_VALUE);

        form.getChildren().add(gridButtons);
        setRight(form);
    }

    public TableView<Kombuskini> getTblKombuskinis() {
        return tblKombuskinis;
    }

    public TextField getTxtId() {
        return txtId;
    }

    public TextField getTxtCor() {
        return txtCor;
    }

    public TextField getTxtQtdNos() {
        return txtQtdNos;
    }

    public TextField getTxtTipoDivisoes() {
        return txtTipoDivisoes;
    }

    public TextField getTxtQtdDivisoes() {
        return txtQtdDivisoes;
    }

    public TextField getTxtTipoCruz() {
        return txtTipoCruz;
    }

    public CheckBox getChkTassel() {
        return chkTassel;
    }

    public TextField getTxtPreco() {
        return txtPreco;
    }

    public Button getBtnCadastrar() {
        return btnCadastrar;
    }

    public Button getBtnAtualizar() {
        return btnAtualizar;
    }

    public Button getBtnApagar() {
        return btnApagar;
    }

    public Button getBtnLimpar() {
        return btnLimpar;
    }

    public void limparCampos() {
        txtId.clear();
        txtCor.clear();
        txtQtdNos.clear();
        txtTipoDivisoes.clear();
        txtQtdDivisoes.clear();
        txtTipoCruz.clear();
        chkTassel.setSelected(false);
        txtPreco.clear();
        tblKombuskinis.getSelectionModel().clearSelection();
    }

    public void preencherCampos(Kombuskini k) {
        if (k != null) {
            txtId.setText(String.valueOf(k.getId()));
            txtCor.setText(k.getCor());
            txtQtdNos.setText(String.valueOf(k.getQuantidadeNos()));
            txtTipoDivisoes.setText(k.getTipoDivisoes() != null ? k.getTipoDivisoes() : "");
            txtQtdDivisoes.setText(String.valueOf(k.getQuantidadeDivisoes()));
            txtTipoCruz.setText(k.getTipoCruz() != null ? k.getTipoCruz() : "");
            chkTassel.setSelected(k.isHasTassel());
            txtPreco.setText(String.valueOf(k.getPreco()));
        }
    }
}
