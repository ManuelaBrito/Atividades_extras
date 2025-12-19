package org.com.br.CRUD.conf;

import java.sql.*;


public class DataBaseInitializer {

    public static void initDatabase() {
        String[] sqlCommands = {
                "CREATE DATABASE IF NOT EXISTS bancotodolist",

                "USE bancotodolist",
                " CREATE TABLE usuarios (" +
                       "  id INT NOT NULL AUTO_INCREMENT," +
                       "  nome VARCHAR(100) NOT NULL," +
                       " email VARCHAR(100) NOT NULL," +
                       " senha_hash VARCHAR(255) NOT NULL," +
                       " data_cadastro TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP," +
                       " PRIMARY KEY (id)," +
                       " UNIQUE KEY email_unique (email)" +
                ");"
        };
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {

            // Executa cada comando
            for (String sql : sqlCommands) {
                stmt.executeUpdate(sql);
            }

            System.out.println("✅ Banco inicializado com sucesso!");
            System.out.println("📋 Tabelas criadas conforme especificado");

        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar banco: " + e.getMessage());
            e.printStackTrace();
        }
    }
}