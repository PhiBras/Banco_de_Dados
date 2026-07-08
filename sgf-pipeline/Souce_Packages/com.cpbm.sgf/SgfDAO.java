package com.cpbm.sgf;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Camada de acesso a dados (DAO) para as tabelas obra, grupo_servico,
 * subgrupo_servico, item e item_orcado.
 *
 * Cada método corresponde diretamente a uma das consultas do arquivo
 * BD_03_postgresql_sgf_real.sql, para que o comportamento em Java seja
 * idêntico ao validado em SQL puro.
 */

public class SgfDAO {

    private final Connection conexao;

    public SgfDAO(Connection conexao) {
        this.conexao = conexao;
    }

    // ---------------------------------------------------------------
    // CONSULTA 1: reconstrução da planilha operacional (visão completa)
    // ---------------------------------------------------------------
    public List<LinhaPlanilha> listarPlanilhaCompleta() throws SQLException {
        String sql = """
                SELECT
                    o.obra_codigo, o.obra_nome,
                    g.grupo_codigo, g.grupo_nome,
                    sg.subgrupo_codigo, sg.subgrupo_nome,
                    i.item_codigo, i.descricao, i.unidade,
                    io.quantidade, io.valor_unitario_ajustado, io.valor_total
                FROM item_orcado io
                JOIN obra o ON o.obra_codigo = io.obra_codigo
                JOIN subgrupo_servico sg ON sg.subgrupo_codigo = io.subgrupo_codigo
                JOIN grupo_servico g ON g.grupo_codigo = sg.grupo_codigo
                JOIN item i ON i.item_codigo = io.item_codigo
                ORDER BY o.obra_codigo, g.grupo_codigo, sg.subgrupo_codigo, i.item_codigo
                """;

        List<LinhaPlanilha> linhas = new ArrayList<>();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                linhas.add(new LinhaPlanilha(
                        rs.getString("obra_codigo"),
                        rs.getString("obra_nome"),
                        rs.getString("grupo_codigo"),
                        rs.getString("grupo_nome"),
                        rs.getString("subgrupo_codigo"),
                        rs.getString("subgrupo_nome"),
                        rs.getString("item_codigo"),
                        rs.getString("descricao"),
                        rs.getString("unidade"),
                        rs.getBigDecimal("quantidade"),
                        rs.getBigDecimal("valor_unitario_ajustado"),
                        rs.getBigDecimal("valor_total")
                ));
            }
        }
        return linhas;
    }

    // ---------------------------------------------------------------
    // CONSULTA 2: total geral por obra
    // ---------------------------------------------------------------
    public List<TotalObra> totalPorObra() throws SQLException {
        String sql = """
                SELECT o.obra_codigo, o.obra_nome, ROUND(SUM(io.valor_total), 2) AS total_obra
                FROM item_orcado io
                JOIN obra o ON o.obra_codigo = io.obra_codigo
                GROUP BY o.obra_codigo, o.obra_nome
                ORDER BY total_obra DESC
                """;

        List<TotalObra> resultado = new ArrayList<>();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultado.add(new TotalObra(
                        rs.getString("obra_codigo"),
                        rs.getString("obra_nome"),
                        rs.getBigDecimal("total_obra")
                ));
            }
        }
        return resultado;
    }

    // ---------------------------------------------------------------
    // CONSULTA 3: subtotal por grupo de serviço
    // ---------------------------------------------------------------
    public List<Subtotal> subtotalPorGrupo() throws SQLException {
        String sql = """
                SELECT o.obra_nome, g.grupo_nome, ROUND(SUM(io.valor_total), 2) AS subtotal
                FROM item_orcado io
                JOIN obra o ON o.obra_codigo = io.obra_codigo
                JOIN subgrupo_servico sg ON sg.subgrupo_codigo = io.subgrupo_codigo
                JOIN grupo_servico g ON g.grupo_codigo = sg.grupo_codigo
                GROUP BY o.obra_nome, g.grupo_nome
                ORDER BY o.obra_nome, subtotal DESC
                """;
        return executarSubtotal(sql, "grupo_nome");
    }

    // ---------------------------------------------------------------
    // CONSULTA 4: subtotal por subgrupo de serviço
    // ---------------------------------------------------------------
    public List<Subtotal> subtotalPorSubgrupo() throws SQLException {
        String sql = """
                SELECT sg.subgrupo_codigo AS chave, sg.subgrupo_nome AS nome,
                       ROUND(SUM(io.valor_total), 2) AS subtotal
                FROM item_orcado io
                JOIN subgrupo_servico sg ON sg.subgrupo_codigo = io.subgrupo_codigo
                GROUP BY sg.subgrupo_codigo, sg.subgrupo_nome
                ORDER BY subtotal DESC
                """;

        List<Subtotal> resultado = new ArrayList<>();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultado.add(new Subtotal(
                        rs.getString("chave"),
                        rs.getString("nome"),
                        rs.getBigDecimal("subtotal")
                ));
            }
        }
        return resultado;
    }

    private List<Subtotal> executarSubtotal(String sql, String colunaNome) throws SQLException {
        List<Subtotal> resultado = new ArrayList<>();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultado.add(new Subtotal(
                        rs.getString("obra_nome"),
                        rs.getString(colunaNome),
                        rs.getBigDecimal("subtotal")
                ));
            }
        }
        return resultado;
    }

    // ---------------------------------------------------------------
    // CONSULTA 5: os 10 itens de maior impacto financeiro
    // ---------------------------------------------------------------
    public List<ItemImpacto> top10Itens() throws SQLException {
        String sql = """
                SELECT i.item_codigo, i.descricao, ROUND(SUM(io.valor_total), 2) AS total_acumulado
                FROM item_orcado io
                JOIN item i ON i.item_codigo = io.item_codigo
                GROUP BY i.item_codigo, i.descricao
                ORDER BY total_acumulado DESC
                LIMIT 10
                """;

        List<ItemImpacto> resultado = new ArrayList<>();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultado.add(new ItemImpacto(
                        rs.getString("item_codigo"),
                        rs.getString("descricao"),
                        rs.getBigDecimal("total_acumulado")
                ));
            }
        }
        return resultado;
    }

    // ---------------------------------------------------------------
    // CONSULTA 6: auditoria de possíveis inconsistências
    // ---------------------------------------------------------------
    public List<Integer> auditarInconsistencias() throws SQLException {
        String sql = """
                SELECT id_item_orcado
                FROM item_orcado
                WHERE quantidade <= 0 OR valor_total <= 0 OR valor_unitario_ajustado <= 0
                """;

        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("id_item_orcado"));
            }
        }
        return ids;
    }

    // ---------------------------------------------------------------
    // CONSULTA 7: Curva ABC dos itens (por valor acumulado)
    // ---------------------------------------------------------------
    public List<LinhaCurvaAbc> curvaAbc() throws SQLException {
        String sql = """
                WITH totais_item AS (
                    SELECT i.item_codigo, i.descricao,
                           ROUND(SUM(io.valor_total), 2) AS total_item
                    FROM item_orcado io
                    JOIN item i ON i.item_codigo = io.item_codigo
                    GROUP BY i.item_codigo, i.descricao
                ),
                total_geral AS (
                    SELECT SUM(total_item) AS soma_geral FROM totais_item
                ),
                acumulado AS (
                    SELECT
                        t.item_codigo,
                        t.descricao,
                        t.total_item,
                        SUM(t.total_item) OVER (ORDER BY t.total_item DESC
                            ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS valor_acumulado,
                        ROUND(
                            100 * SUM(t.total_item) OVER (ORDER BY t.total_item DESC
                                ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW)
                            / (SELECT soma_geral FROM total_geral), 2
                        ) AS percentual_acumulado
                    FROM totais_item t
                )
                SELECT
                    item_codigo, descricao, total_item, valor_acumulado, percentual_acumulado,
                    CASE
                        WHEN percentual_acumulado <= 80 THEN 'A'
                        WHEN percentual_acumulado <= 95 THEN 'B'
                        ELSE 'C'
                    END AS classe_abc
                FROM acumulado
                ORDER BY total_item DESC
                """;

        List<LinhaCurvaAbc> resultado = new ArrayList<>();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultado.add(new LinhaCurvaAbc(
                        rs.getString("item_codigo"),
                        rs.getString("descricao"),
                        rs.getBigDecimal("total_item"),
                        rs.getBigDecimal("valor_acumulado"),
                        rs.getBigDecimal("percentual_acumulado"),
                        rs.getString("classe_abc")
                ));
            }
        }
        return resultado;
    }

    // ---------------------------------------------------------------
    // CONSULTA 8: resumo da Curva ABC (totais por classe A/B/C)
    // ---------------------------------------------------------------
    public List<ResumoAbc> resumoCurvaAbc() throws SQLException {
        String sql = """
                WITH totais_item AS (
                    SELECT i.item_codigo,
                           ROUND(SUM(io.valor_total), 2) AS total_item
                    FROM item_orcado io
                    JOIN item i ON i.item_codigo = io.item_codigo
                    GROUP BY i.item_codigo
                ),
                total_geral AS (
                    SELECT SUM(total_item) AS soma_geral FROM totais_item
                ),
                classificado AS (
                    SELECT
                        t.item_codigo,
                        t.total_item,
                        CASE
                            WHEN ROUND(100 * SUM(t.total_item) OVER (ORDER BY t.total_item DESC
                                    ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW)
                                 / (SELECT soma_geral FROM total_geral), 2) <= 80 THEN 'A'
                            WHEN ROUND(100 * SUM(t.total_item) OVER (ORDER BY t.total_item DESC
                                    ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW)
                                 / (SELECT soma_geral FROM total_geral), 2) <= 95 THEN 'B'
                            ELSE 'C'
                        END AS classe_abc
                    FROM totais_item t
                )
                SELECT
                    classe_abc,
                    COUNT(*) AS quantidade_itens,
                    ROUND(SUM(total_item), 2) AS valor_total_classe,
                    ROUND(100.0 * SUM(total_item) / (SELECT soma_geral FROM total_geral), 2) AS percentual_do_total
                FROM classificado
                GROUP BY classe_abc
                ORDER BY classe_abc
                """;

        List<ResumoAbc> resultado = new ArrayList<>();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultado.add(new ResumoAbc(
                        rs.getString("classe_abc"),
                        rs.getInt("quantidade_itens"),
                        rs.getBigDecimal("valor_total_classe"),
                        rs.getBigDecimal("percentual_do_total")
                ));
            }
        }
        return resultado;
    }

    // ---------------------------------------------------------------
    // Registros (DTOs) usados pelos métodos acima
    // ---------------------------------------------------------------
    public record LinhaPlanilha(
            String obraCodigo, String obraNome,
            String grupoCodigo, String grupoNome,
            String subgrupoCodigo, String subgrupoNome,
            String itemCodigo, String descricao, String unidade,
            BigDecimal quantidade, BigDecimal valorUnitarioAjustado, BigDecimal valorTotal
    ) {}

    public record TotalObra(String obraCodigo, String obraNome, BigDecimal totalObra) {}

    public record Subtotal(String contexto, String nome, BigDecimal subtotal) {}

    public record ItemImpacto(String itemCodigo, String descricao, BigDecimal totalAcumulado) {}

    public record LinhaCurvaAbc(
            String itemCodigo, String descricao, BigDecimal totalItem,
            BigDecimal valorAcumulado, BigDecimal percentualAcumulado, String classeAbc
    ) {}

    public record ResumoAbc(String classeAbc, int quantidadeItens, BigDecimal valorTotalClasse, BigDecimal percentualDoTotal) {}
}
