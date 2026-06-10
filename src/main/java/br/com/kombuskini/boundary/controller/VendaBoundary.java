package br.com.kombuskini.boundary.controller;

import br.com.kombuskini.entity.Cliente;
import br.com.kombuskini.entity.ItemPedido;
import br.com.kombuskini.entity.Kombuskini;
import br.com.kombuskini.entity.Pedido;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;

public class VendaBoundary extends BorderPane {

    // Lista de Pedidos (Esquerda)
    private final TableView<Pedido> tblPedidos = new TableView<>();
    private final ComboBox<Cliente> cmbClienteNovo = new ComboBox<>();
    private final Button btnNovoPedido = new Button("Abrir Novo Pedido");

    // Lado Direito (Detalhes e Itens)
    private final TextField txtPedidoId = new TextField();
    private final TextField txtClienteNome = new TextField();
    private final TextField txtStatus = new TextField();
    private final ComboBox<Kombuskini> cmbProduto = new ComboBox<>();
    private final TextField txtQuantidade = new TextField();
    private final TableView<ItemPedido> tblItens = new TableView<>();
    private final Label lblValorTotal = new Label("Total: R$ 0.00");

    private final Button btnAdicionarItem = new Button("Adicionar Item");
    private final Button btnRegistrar = new Button("Confirmar Pedido (Registrar)");
    private final Button btnEnviar = new Button("Liberar Estoque (Enviar)");
    private final Button btnFinalizar = new Button("Concluir Pedido (Finalizar)");
    private final Button btnExcluirPedido = new Button("Excluir Pedido");

    public VendaBoundary() {
        setPadding(new Insets(15));

        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.45);

        // ------------- TABELA ESQUERDA - Lista de Pedidos ---------------
        VBox esquerda = new VBox(10);
        esquerda.setPadding(new Insets(0, 10, 0, 0));

        Label lblPedidos = new Label("Pedidos no Sistema");
        lblPedidos.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Tabela de Pedidos
        tblPedidos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        TableColumn<Pedido, Integer> colPedId = new TableColumn<>("ID");
        colPedId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        colPedId.setSortable(true);

        TableColumn<Pedido, String> colPedCliente = new TableColumn<>("Cliente");
        colPedCliente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCliente().getNome()));
        colPedCliente.setSortable(true);

        TableColumn<Pedido, String> colPedStatus = new TableColumn<>("Status");
        colPedStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus().name()));
        colPedStatus.setSortable(true);

        tblPedidos.getColumns().addAll(colPedId, colPedCliente, colPedStatus);

        // Formulário para abrir pedido
        VBox formNovoPedido = new VBox(5);
        cmbClienteNovo.setPromptText("Selecione o Cliente...");
        cmbClienteNovo.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente object) {
                return object == null ? "" : "ID: " + object.getId() + " - " + object.getNome();
            }

            @Override
            public Cliente fromString(String string) {
                return null;
            }
        });
        cmbClienteNovo.setMaxWidth(Double.MAX_VALUE);
        btnNovoPedido.setMaxWidth(Double.MAX_VALUE);
        formNovoPedido.getChildren().addAll(new Label("Novo Pedido para:"), cmbClienteNovo, btnNovoPedido);

        esquerda.getChildren().addAll(lblPedidos, tblPedidos, formNovoPedido);
        VBox.setVgrow(tblPedidos, Priority.ALWAYS);

        // ================= DIREITA (Detalhes do Pedido Selecionado) =================
        VBox direita = new VBox(10);
        direita.setPadding(new Insets(0, 0, 0, 10));

        Label lblDetalhes = new Label("Detalhes do Pedido Selecionado");
        lblDetalhes.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        txtPedidoId.setDisable(true);
        txtClienteNome.setDisable(true);
        txtStatus.setDisable(true);

        // ComboBox de produto
        cmbProduto.setPromptText("Selecione o Kombuskini...");
        cmbProduto.setConverter(new StringConverter<Kombuskini>() {
            @Override
            public String toString(Kombuskini object) {
                return object == null ? "" : "ID: " + object.getId() + " - " + object.getCor() + " (R$ " + object.getPreco() + ")";
            }

            @Override
            public Kombuskini fromString(String string) {
                return null;
            }
        });
        cmbProduto.setMaxWidth(Double.MAX_VALUE);

        // Layout de campos superiores do pedido
        GridPane gridCampos = new GridPane();
        gridCampos.setHgap(10);
        gridCampos.setVgap(5);
        gridCampos.add(new Label("Pedido ID:"), 0, 0);
        gridCampos.add(txtPedidoId, 1, 0);
        gridCampos.add(new Label("Cliente:"), 0, 1);
        gridCampos.add(txtClienteNome, 1, 1);
        gridCampos.add(new Label("Status:"), 0, 2);
        gridCampos.add(txtStatus, 1, 2);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPrefWidth(80);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setHgrow(Priority.ALWAYS);
        gridCampos.getColumnConstraints().addAll(c1, c2);

        // Tabela de itens do pedido selecionado
        tblItens.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        TableColumn<ItemPedido, Long> colItemProdId = new TableColumn<>("Prod. ID");
        colItemProdId.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getKombuskini().getId()).asObject());
        colItemProdId.setSortable(true);

        TableColumn<ItemPedido, String> colItemCor = new TableColumn<>("Cor");
        colItemCor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKombuskini().getCor()));
        colItemCor.setSortable(true);

        TableColumn<ItemPedido, Integer> colItemQtd = new TableColumn<>("Qtd");
        colItemQtd.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantidade()).asObject());
        colItemQtd.setSortable(true);

        TableColumn<ItemPedido, Double> colItemTotal = new TableColumn<>("Preço Total");
        colItemTotal.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrecoTotal()).asObject());
        colItemTotal.setSortable(true);

        tblItens.getColumns().addAll(colItemProdId, colItemCor, colItemQtd, colItemTotal);

        // Form para adicionar itens
        HBox formItens = new HBox(10);
        txtQuantidade.setPromptText("Qtd");
        txtQuantidade.setPrefWidth(60);
        btnAdicionarItem.setMinWidth(120);
        formItens.getChildren().addAll(cmbProduto, txtQuantidade, btnAdicionarItem);
        HBox.setHgrow(cmbProduto, Priority.ALWAYS);

        // Total e Ações do Pedido
        lblValorTotal.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        VBox botoesAcao = new VBox(5);
        btnRegistrar.setMaxWidth(Double.MAX_VALUE);
        btnEnviar.setMaxWidth(Double.MAX_VALUE);
        btnFinalizar.setMaxWidth(Double.MAX_VALUE);
        btnExcluirPedido.setMaxWidth(Double.MAX_VALUE);
        botoesAcao.getChildren().addAll(btnRegistrar, btnEnviar, btnFinalizar, btnExcluirPedido);

        direita.getChildren().addAll(lblDetalhes, gridCampos, new Label("Itens do Pedido:"), tblItens, formItens, lblValorTotal, botoesAcao);
        VBox.setVgrow(tblItens, Priority.ALWAYS);

        splitPane.getItems().addAll(esquerda, direita);
        setCenter(splitPane);
    }

    public TableView<Pedido> getTblPedidos() {
        return tblPedidos;
    }

    public ComboBox<Cliente> getCmbClienteNovo() {
        return cmbClienteNovo;
    }

    public Button getBtnNovoPedido() {
        return btnNovoPedido;
    }

    public TextField getTxtPedidoId() {
        return txtPedidoId;
    }

    public TextField getTxtClienteNome() {
        return txtClienteNome;
    }

    public TextField getTxtStatus() {
        return txtStatus;
    }

    public ComboBox<Kombuskini> getCmbProduto() {
        return cmbProduto;
    }

    public TextField getTxtQuantidade() {
        return txtQuantidade;
    }

    public TableView<ItemPedido> getTblItens() {
        return tblItens;
    }

    public Label getLblValorTotal() {
        return lblValorTotal;
    }

    public Button getBtnAdicionarItem() {
        return btnAdicionarItem;
    }

    public Button getBtnRegistrar() {
        return btnRegistrar;
    }

    public Button getBtnEnviar() {
        return btnEnviar;
    }

    public Button getBtnFinalizar() {
        return btnFinalizar;
    }

    public Button getBtnExcluirPedido() {
        return btnExcluirPedido;
    }

    public void limparDetalhes() {
        txtPedidoId.clear();
        txtClienteNome.clear();
        txtStatus.clear();
        cmbProduto.getSelectionModel().clearSelection();
        txtQuantidade.clear();
        tblItens.getItems().clear();
        lblValorTotal.setText("Total: R$ 0.00");
    }

    public void preencherDetalhes(Pedido pedido) {
        if (pedido != null) {
            txtPedidoId.setText(String.valueOf(pedido.getId()));
            txtClienteNome.setText(pedido.getCliente().getNome());
            txtStatus.setText(pedido.getStatus().name());
            tblItens.getItems().setAll(pedido.getItens());
            lblValorTotal.setText(String.format("Total: R$ %.2f", pedido.getValorTotal()));
        }
    }
}
