package br.com.kombuskini.boundary.controller;

import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

public class MainController {

    private final BorderPane rootLayout;

    private final ClienteCrudController clienteCrudController;
    private final ProdutoCrudController produtoCrudController;
    private final EstoqueController estoqueController;
    private final VendaController vendaController;

    public MainController(ClienteCrudController clienteCrud,
                          ProdutoCrudController produtoCrud,
                          EstoqueController estoque,
                          VendaController venda) {
        this.clienteCrudController = clienteCrud;
        this.produtoCrudController = produtoCrud;
        this.estoqueController = estoque;
        this.vendaController = venda;

        this.rootLayout = new BorderPane();

        // Criar MenuBar
        MenuBar menuBar = new MenuBar();

        Menu menuCadastros = new Menu("Cadastros");
        MenuItem itemClientes = new MenuItem("Clientes");
        MenuItem itemProdutos = new MenuItem("Produtos (Kombuskinis)");
        menuCadastros.getItems().addAll(itemClientes, itemProdutos);

        Menu menuMovimentacoes = new Menu("Movimentações");
        MenuItem itemVendas = new MenuItem("Vendas / Pedidos");
        MenuItem itemEstoque = new MenuItem("Estoque");
        menuMovimentacoes.getItems().addAll(itemVendas, itemEstoque);

        menuBar.getMenus().addAll(menuCadastros, menuMovimentacoes);

        // Configurar eventos de clique do menu
        itemClientes.setOnAction(e -> mostrarClientes());
        itemProdutos.setOnAction(e -> mostrarProdutos());
        itemVendas.setOnAction(e -> mostrarVendas());
        itemEstoque.setOnAction(e -> mostrarEstoque());

        rootLayout.setTop(menuBar);

        // Tela padrão inicial
        mostrarClientes();
    }

    public Parent getView() {
        return rootLayout;
    }

    private void mostrarClientes() {
        clienteCrudController.recarregarTabela();
        rootLayout.setCenter(clienteCrudController.getView());
    }

    private void mostrarProdutos() {
        produtoCrudController.recarregarTabela();
        rootLayout.setCenter(produtoCrudController.getView());
    }

    private void mostrarVendas() {
        vendaController.recarregarDados();
        rootLayout.setCenter(vendaController.getView());
    }

    private void mostrarEstoque() {
        estoqueController.recarregarDados();
        rootLayout.setCenter(estoqueController.getView());
    }
}
