package br.com.kombuskini.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DbInitializer {

    public static void main(String[] args) {
        System.out.println("Iniciando o inicializador automático de banco de dados...");

        // Carregar senhas do env atual
        String envRootPass = "";
        String envUserPass = "";
        try {
            Dotenv dotenv = Dotenv.load();
            envRootPass = dotenv.get("MYSQL_ROOT_PASSWORD");
            envUserPass = dotenv.get("MYSQL_PASSWORD");
        } catch (Exception e) {
            // Se .env não carregar, continua com os padrões
        }

        // Lista de senhas comuns para testar no root local
        String[] possiblePasswords = {
                "",           // Sem senha
                "root",       // Padrão comum
                "1234",       // Padrão comum
                "123456",     // Padrão comum
                "admin",      // Padrão comum
                envRootPass,  // Do env
                envUserPass   // Do env
        };

        Connection conn = null;
        String workingPassword = null;

        for (String password : possiblePasswords) {
            if (password == null) continue;
            try {
                String url = "jdbc:mysql://localhost:3306/?useTimezone=true&serverTimezone=UTC";
                conn = DriverManager.getConnection(url, "root", password);
                workingPassword = password;
                System.out.println("Conexão efetuada com sucesso como 'root' usando a senha: '" + password + "'");
                break;
            } catch (Exception e) {
                // Tenta a próxima senha
            }
        }

        if (conn == null) {
            System.err.println("NÃO foi possível conectar ao seu MySQL local com nenhuma senha padrão de 'root'.");
            System.err.println("Por favor, verifique qual é a senha do seu MySQL local ('MySQL80').");
            return;
        }

        try (Connection c = conn; Statement stmt = c.createStatement()) {
            // 1. Criar banco de dados
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS kombuskini_db");
            System.out.println("Banco de dados 'kombuskini_db' criado/verificado.");

            stmt.executeUpdate("USE kombuskini_db");

            // 2. Ler e executar o schema.sql
            String schemaPath = "database/schema.sql";
            if (!new File(schemaPath).exists()) {
                schemaPath = "src/main/resources/db/schema.sql";
            }

            if (new File(schemaPath).exists()) {
                String schemaSql = new String(Files.readAllBytes(Paths.get(schemaPath)));
                // Dividir comandos SQL por ponto e vírgula
                String[] commands = schemaSql.split(";");
                int executed = 0;
                for (String command : commands) {
                    String trimmed = command.trim();
                    if (!trimmed.isEmpty()) {
                        stmt.executeUpdate(trimmed);
                        executed++;
                    }
                }
                System.out.println("Executados " + executed + " comandos SQL do arquivo '" + schemaPath + "' com sucesso!");
            } else {
                System.err.println("Arquivo de schema.sql não foi encontrado!");
            }

            // 3. Atualizar o arquivo .env
            atualizarEnv(workingPassword);

        } catch (Exception e) {
            System.err.println("Erro durante a inicialização do banco: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void atualizarEnv(String rootPassword) {
        File envFile = new File(".env");
        try {
            String content = "MYSQL_ROOT_PASSWORD=" + rootPassword + "\n" +
                             "MYSQL_DATABASE=kombuskini_db\n" +
                             "MYSQL_USER=root\n" +
                             "MYSQL_PASSWORD=" + rootPassword + "\n" +
                             "MYSQL_HOST=localhost\n";
            Files.write(Paths.get(".env"), content.getBytes());
            System.out.println("Arquivo '.env' atualizado com sucesso para apontar para o seu MySQL local (localhost)!");
        } catch (IOException e) {
            System.err.println("Erro ao atualizar o arquivo .env: " + e.getMessage());
        }
    }
}
