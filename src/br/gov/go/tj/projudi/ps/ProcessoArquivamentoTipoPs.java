package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.ProcessoArquivamentoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ProcessoArquivamentoTipoPs extends ProcessoArquivamentoTipoPsGen {

	private static final long serialVersionUID = -8457960506485676559L;

	private ProcessoArquivamentoTipoPs() {
	}

	public ProcessoArquivamentoTipoPs(Connection conexao) {
		Conexao = conexao;
	}

	public String consultarDescricaoJSON(String stNomeBusca1, boolean boTodos, String posicaoPaginaAtual) throws Exception {

		String stSql, stSqlFrom, stSqlOrder;
		String stTemp = "";
		int qtdeColunas = 1;
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT ID_PROC_ARQUIVAMENTO_TIPO as id, PROC_ARQUIVAMENTO_TIPO as descricao1 ";
		stSqlFrom = " FROM view_proc_arquivamento_tipo  WHERE PROC_ARQUIVAMENTO_TIPO LIKE ?";
		ps.adicionarString("%" + stNomeBusca1 + "%");
		if (!boTodos) {
			stSqlFrom += " AND CODIGO_TEMP = ? ";
			ps.adicionarLong(ProcessoArquivamentoTipoDt.ATIVO);
		}

		stSqlOrder = " ORDER BY PROC_ARQUIVAMENTO_TIPO ";
		try {

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicaoPaginaAtual);
			stSql = "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);

		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			}
			try {
				if (rs2 != null)
					rs2.close();
			} catch (Exception e) {
			}
		}
		return stTemp;
		
	}
}
