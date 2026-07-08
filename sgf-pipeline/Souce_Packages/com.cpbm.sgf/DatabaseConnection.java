package com.cpbm.sgf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centraliza a conexão com o banco PostgreSQL via JDBC.
 */

public class DatabaseConnection {

    private static final String HOST = env("SGF_DB_HOST", "localhost");
    private static final String PORTA = env("SGF_DB_PORT", "5432");
    private static final String BANCO = env("SGF_DB_NAME", "sgf_cpbm");
    private static final String USUARIO = env("SGF_DB_USER", "postgres");
    private static final String SENHA = env("SGF_DB_PASSWORD", "12lipe34");

    private static String env(String chave, String padrao) {
        String valor = System.getenv(chave);
        return (valor == null || valor.isBlank()) ? padrao : valor;
    }

    private static String urlConexao() {
        return "jdbc:postgresql://" + HOST + ":" + PORTA + "/" + BANCO
                + "?useUnicode=true&characterEncoding=UTF-8";
    }

    /**
     * Abre e retorna uma nova conexão. Quem chamar este método é responsável
     * por fechá-la (idealmente com try-with-resources).
     */
  
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC do PostgreSQL não encontrado no classpath.", e);
        }
        return DriverManager.getConnection(urlConexao(), USUARIO, SENHA);
    }
}
