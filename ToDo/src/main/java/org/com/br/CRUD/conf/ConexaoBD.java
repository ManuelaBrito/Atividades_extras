package org.com.br.CRUD.conf;

import java.sql.*;
import java.io.*;
import java.util.Properties;

public class ConexaoBD {

    public static Connection getConexao() {
        try {
            // Carrega configurações
            InputStream input = ConexaoBD.class.getClassLoader()
                    .getResourceAsStream("database.properties");
            Properties props = new Properties();
            props.load(input);

            // Conecta
            return DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.pass")
            );

        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return null;
        }
    }
}
