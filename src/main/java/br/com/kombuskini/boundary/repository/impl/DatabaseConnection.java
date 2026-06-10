package br.com.kombuskini.boundary.repository.impl;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection = null;
    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Dotenv dotenv = Dotenv.load();
                
                String dbName = dotenv.get("MYSQL_DATABASE");
                String user = dotenv.get("MYSQL_USER");
                String password = dotenv.get("MYSQL_PASSWORD");
                String url = "jdbc:mysql://100.119.145.58:3306/" + dbName + "?useTimezone=true&serverTimezone=UTC";
//                String url = "jdbc:mysql://localhost:3306/" + dbName + "?useTimezone=true&serverTimezone=UTC";

                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Conexão com o banco de dados '" + dbName + "' estabelecida com sucesso!");
                
            } catch (Exception e) {
                throw new SQLException("Erro ao tentar conectar no banco ou ler o arquivo .env: " + e.getMessage(), e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexão fechada.");
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}
