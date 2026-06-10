package br.com.kombuskini;

import br.com.kombuskini.boundary.controller.*;
import br.com.kombuskini.boundary.repository.*;
import br.com.kombuskini.boundary.repository.impl.*;
import br.com.kombuskini.control.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Instanciar DAOs
        ClienteDAO clienteDAO = new MySQLClienteDAO();
        KombuskiniDAO kombuskiniDAO = new MySQLKombuskiniDAO();
        PedidoDAO pedidoDAO = new MySQLPedidoDAO();
        EstoqueDAO estoqueDAO = new MySQLEstoqueDAO();

        // 2. Instanciar Controls
        ClienteControl clienteControl = new ClienteControl(clienteDAO);
        EstoqueControl estoqueControl = new EstoqueControl(estoqueDAO);
        VendaControl vendaControl = new VendaControl(pedidoDAO, kombuskiniDAO);

        // 3. Instanciar Controllers das Telas
        ClienteCrudController clienteCrudController = new ClienteCrudController(clienteControl);
        ProdutoCrudController produtoCrudController = new ProdutoCrudController(kombuskiniDAO);
        EstoqueController estoqueController = new EstoqueController(estoqueControl, kombuskiniDAO);
        VendaController vendaController = new VendaController(vendaControl, clienteDAO, kombuskiniDAO, pedidoDAO, estoqueControl);

        // 4. Instanciar MainController (gerencia a MenuBar e trocas de telas)
        MainController mainController = new MainController(
                clienteCrudController,
                produtoCrudController,
                estoqueController,
                vendaController
        );

        // 5. Configurar Cena
        Scene scene = new Scene(mainController.getView(), 1024, 768);
        primaryStage.setTitle("Kombuskini Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}