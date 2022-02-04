package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ProcessoParteAusenciaPs extends ProcessoParteAusenciaPsGen {

	private static final long serialVersionUID = 6452888850087116910L;

	public ProcessoParteAusenciaPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * Altera dados da parte marcando a ausência da mesma (Revelia ou
	 * Contumácia), ou retirando uma ausência marcada
	 * 
	 * @param id_ProcessoParte,
	 *            identificação da parte do processo
	 * @param processoParteAusenciaCodigo,
	 *            tipo de ausência a ser marcada
	 * @author msapaula
	 */
	public void alterarAusenciaProcessoParte(String id_ProcessoParte, String processoParteAusenciaCodigo) throws Exception {
		String sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "UPDATE PROJUDI.PROC_PARTE SET ID_PROC_PARTE_AUS = ";
		if (processoParteAusenciaCodigo != null) {
			sql += " (SELECT ID_PROC_PARTE_AUS FROM PROJUDI.PROC_PARTE_AUS WHERE PROC_PARTE_AUS_CODIGO = ?)";
			ps.adicionarLong(processoParteAusenciaCodigo);
		} else {
			sql += "?";
			ps.adicionarLongNull();
		}
		sql += " WHERE ID_PROC_PARTE = ?";
		ps.adicionarLong(id_ProcessoParte);

			executarUpdateDelete(sql, ps);
	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {

		String stSql = "";
		String stSqlFrom = "";
		String stSqlOrder = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql = "SELECT ID_PROC_PARTE_AUS AS ID, PROC_PARTE_AUS AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_PROC_PARTE_AUS";
		stSqlFrom += " WHERE PROC_PARTE_AUS LIKE ?";
		stSqlOrder = " ORDER BY PROC_PARTE_AUS ";
		ps.adicionarString( descricao +"%");

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{
				if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null)rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
}
