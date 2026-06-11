package br.com.kombuskini.boundary.controller;

import br.com.kombuskini.entity.Cliente;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ClienteBoundary extends BorderPane {

    private final TableView<Cliente> tblClientes = new TableView<>();
    private final TextField txtId = new TextField();
    private final TextField txtNome = new TextField();
    private final TextField txtTelefone = new TextField();
    private final TextField txtInstagram = new TextField();
    private final TextField txtEmail = new TextField();

    private final TextField txtBuscaNome = new TextField();
    private final Button btnBuscar = new Button("Buscar");

    private final Button btnCadastrar = new Button("Cadastrar");
    private final Button btnAtualizar = new Button("Atualizar");
    private final Button btnApagar = new Button("Excluir");
    private final Button btnLimpar = new Button("Limpar");

    public ClienteBoundary() {
        setPadding(new Insets(15));

        // 1. Tabela (Centro)
        tblClientes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        colId.setSortable(true);

        TableColumn<Cliente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        colNome.setSortable(true);

        TableColumn<Cliente, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefone()));
        colTelefone.setSortable(true);

        TableColumn<Cliente, String> colInstagram = new TableColumn<>("Instagram");
        colInstagram.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getInstagram()));
        colInstagram.setSortable(true);

        TableColumn<Cliente, String> colEmail = new TableColumn<>("E-mail");
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        colEmail.setSortable(true);

        tblClientes.getColumns().addAll(colId, colNome, colTelefone, colInstagram, colEmail);

        // Barra de busca
        HBox barBusca = new HBox(10);
        barBusca.setAlignment(Pos.CENTER_LEFT);
        barBusca.setPadding(new Insets(0, 0, 10, 0));
        txtBuscaNome.setPromptText("Buscar por nome...");
        barBusca.getChildren().addAll(new Label("Filtro:"), txtBuscaNome, btnBuscar);

        VBox centerPane = new VBox(tblClientes);
        VBox.setVgrow(tblClientes, Priority.ALWAYS);
        
        BorderPane innerCenter = new BorderPane();
        innerCenter.setTop(barBusca);
        innerCenter.setCenter(centerPane);

        setCenter(innerCenter);

        // 2. Formulário (Direita)
        VBox form = new VBox(10);
        form.setPadding(new Insets(0, 0, 0, 15));
        form.setPrefWidth(280);

        Label lblTitle = new Label("Cadastro de Cliente");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        txtId.setDisable(true);
        txtId.setPromptText("Gerado automaticamente");

        form.getChildren().addAll(
                lblTitle,
                new Label("ID:"), txtId,
                new Label("Nome:"), txtNome,
                new Label("Telefone:"), txtTelefone,
                new Label("Instagram:"), txtInstagram,
                new Label("E-mail:"), txtEmail
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

        // Ajustar largura dos botões
        btnCadastrar.setMaxWidth(Double.MAX_VALUE);
        btnAtualizar.setMaxWidth(Double.MAX_VALUE);
        btnApagar.setMaxWidth(Double.MAX_VALUE);
        btnLimpar.setMaxWidth(Double.MAX_VALUE);

        form.getChildren().add(gridButtons);
        setRight(form);
    }

    public TableView<Cliente> getTblClientes() {
        return tblClientes;
    }

    public TextField getTxtId() {
        return txtId;
    }

    public TextField getTxtNome() {
        return txtNome;
    }

    public TextField getTxtTelefone() {
        return txtTelefone;
    }

    public TextField getTxtInstagram() {
        return txtInstagram;
    }

    public TextField getTxtEmail() {
        return txtEmail;
    }

    public TextField getTxtBuscaNome() {
        return txtBuscaNome;
    }

    public Button getBtnBuscar() {
        return btnBuscar;
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
        txtNome.clear();
        txtTelefone.clear();
        txtInstagram.clear();
        txtEmail.clear();
        tblClientes.getSelectionModel().clearSelection();
    }

    public void preencherCampos(Cliente c) {
        if (c != null) {
            txtId.setText(String.valueOf(c.getId()));
            txtNome.setText(c.getNome());
            txtTelefone.setText(c.getTelefone() != null ? c.getTelefone() : "");
            txtInstagram.setText(c.getInstagram() != null ? c.getInstagram() : "");
            txtEmail.setText(c.getEmail() != null ? c.getEmail() : "");
        }
    }
}
