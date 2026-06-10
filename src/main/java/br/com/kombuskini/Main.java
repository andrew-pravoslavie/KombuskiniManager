package br.com.kombuskini;

import br.com.kombuskini.boundary.repository.impl.DatabaseConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        try {
            Connection sql = DatabaseConnection.getConnection();
        }catch(Exception e){}

    }
}