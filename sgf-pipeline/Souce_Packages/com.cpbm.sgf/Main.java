package com.cpbm.sgf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Ponto de entrada da aplicação. Conecta ao PostgreSQL e executa, em
 * sequência, cada uma das consultas equivalentes ao script
 * BD_03_postgresql_sgf_real.sql, imprimindo os resultados no console.
 */

public class Main {

    public static void main(String[] args) {
        try (Connection conexao = DatabaseConnection.getConnection()) {
            System.out.println("Conectado ao banco com sucesso.\n");
            SgfDAO dao = new SgfDAO(conexao);

            System.out.println("== CONSULTA 1: Planilha operacional completa (10 primeiras linhas) ==");
            List<SgfDAO.LinhaPlanilha> planilha = dao.listarPlanilhaCompleta();
            planilha.stream().limit(10).forEach(l -> System.out.printf(
                    "%-8s %-10s %-25s %-10s %8s x R$ %10s = R$ %12s%n",
                    l.obraCodigo(), l.subgrupoCodigo(), truncar(l.descricao(), 25),
                    l.unidade(), l.quantidade(), l.valorUnitarioAjustado(), l.valorTotal()
            ));
            System.out.println("Total de linhas: " + planilha.size() + "\n");

            System.out.println("== CONSULTA 2: Total por obra ==");
            for (SgfDAO.TotalObra t : dao.totalPorObra()) {
                System.out.printf("%s - %s: R$ %s%n", t.obraCodigo(), t.obraNome(), t.totalObra());
            }
            System.out.println();

            System.out.println("== CONSULTA 3: Subtotal por grupo de serviço ==");
            for (SgfDAO.Subtotal s : dao.subtotalPorGrupo()) {
                System.out.printf("%s | %s: R$ %s%n", s.contexto(), s.nome(), s.subtotal());
            }
            System.out.println();

            System.out.println("== CONSULTA 4: Subtotal por subgrupo de serviço ==");
            for (SgfDAO.Subtotal s : dao.subtotalPorSubgrupo()) {
                System.out.printf("%s | %s: R$ %s%n", s.contexto(), s.nome(), s.subtotal());
            }
            System.out.println();

            System.out.println("== CONSULTA 5: Top 10 itens de maior impacto financeiro ==");
            for (SgfDAO.ItemImpacto item : dao.top10Itens()) {
                System.out.printf("%s - %s: R$ %s%n", item.itemCodigo(), truncar(item.descricao(), 50), item.totalAcumulado());
            }
            System.out.println();

            System.out.println("== CONSULTA 6: Auditoria de inconsistências ==");
            List<Integer> inconsistentes = dao.auditarInconsistencias();
            if (inconsistentes.isEmpty()) {
                System.out.println("Nenhuma inconsistência encontrada.");
            } else {
                System.out.println("IDs com possível inconsistência: " + inconsistentes);
            }
            System.out.println();

            System.out.println("== CONSULTA 7: Curva ABC (20 primeiros itens, classe A) ==");
            List<SgfDAO.LinhaCurvaAbc> curva = dao.curvaAbc();
            curva.stream().limit(20).forEach(l -> System.out.printf(
                    "[%s] %-8s %-45s R$ %12s  acum: %6s%%%n",
                    l.classeAbc(), l.itemCodigo(), truncar(l.descricao(), 45),
                    l.totalItem(), l.percentualAcumulado()
            ));
            System.out.println();

            System.out.println("== CONSULTA 8: Resumo da Curva ABC ==");
            for (SgfDAO.ResumoAbc r : dao.resumoCurvaAbc()) {
                System.out.printf(
                        "Classe %s: %d itens | R$ %s (%s%% do total)%n",
                        r.classeAbc(), r.quantidadeItens(), r.valorTotalClasse(), r.percentualDoTotal()
                );
            }

        } catch (SQLException e) {
            System.err.println("Erro ao conectar ou consultar o banco: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String truncar(String texto, int tamanho) {
        if (texto == null) return "";
        return texto.length() <= tamanho ? texto : texto.substring(0, tamanho - 3) + "...";
    }
}
