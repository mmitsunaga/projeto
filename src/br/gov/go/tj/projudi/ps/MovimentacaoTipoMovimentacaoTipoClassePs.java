package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MovimentacaoTipoMovimentacaoTipoClasseDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class MovimentacaoTipoMovimentacaoTipoClassePs extends MovimentacaoTipoMovimentacaoTipoClassePsGen {

	private static final long serialVersionUID = 482484815828960935L;

	public MovimentacaoTipoMovimentacaoTipoClassePs(Connection conexao){
		Conexao = conexao;
	}

	public void inserir(MovimentacaoTipoMovimentacaoTipoClasseDt dados) throws Exception {

		String stSqlCampos = "";
		String stSqlValores = "";
		String stSql = "";
		String stVirgula = "";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos = "INSERT INTO PROJUDI.MOVI_TIPO_MOVI_TIPO_CLASSE (";

		stSqlValores += " Values (";

		if ((dados.getMovimentacaoTipoMovimentacaoTipoClasse().length() > 0)) {
			stSqlCampos += stVirgula + "MOVI_TIPO_MOVI_TIPO_CLASSE ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarString(dados.getMovimentacaoTipoMovimentacaoTipoClasse());

			stVirgula = ",";
		}
		if ((dados.getId_MovimentacaoTipo().length() > 0)) {
			stSqlCampos += stVirgula + "ID_MOVI_TIPO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getId_MovimentacaoTipo());

			stVirgula = ",";
		}
		if ((dados.getId_MovimentacaoTipoClasse().length() > 0)) {
			stSqlCampos += stVirgula + "ID_MOVI_TIPO_CLASSE ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getId_MovimentacaoTipoClasse());

			stVirgula = ",";
		}
		stSqlCampos += ")";
		stSqlValores += ")";
		stSql += stSqlCampos + stSqlValores;

			dados.setId(executarInsert(stSql, "ID_MOVI_TIPO_MOVI_TIPO_CLASSE", ps));
	}

	public void alterar(MovimentacaoTipoMovimentacaoTipoClasseDt dados) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql = "";

		stSql = "UPDATE PROJUDI.MOVI_TIPO_MOVI_TIPO_CLASSE SET  ";
		stSql += "MOVI_TIPO_MOVI_TIPO_CLASSE = ?";
		ps.adicionarString(dados.getMovimentacaoTipoMovimentacaoTipoClasse());

		stSql += ",ID_MOVI_TIPO = ?";
		ps.adicionarLong(dados.getId_MovimentacaoTipo());

		stSql += ",ID_MOVI_TIPO_CLASSE = ?";
		ps.adicionarLong(dados.getId_MovimentacaoTipoClasse());

		stSql += " WHERE ID_MOVI_TIPO_MOVI_TIPO_CLASSE  = ? ";
		ps.adicionarLong(dados.getId());

			executarUpdateDelete(stSql, ps);

	}

	public void excluir(String chave) throws Exception {
		String stSql = "";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "DELETE FROM PROJUDI.MOVI_TIPO_MOVI_TIPO_CLASSE";
		stSql += " WHERE ID_MOVI_TIPO_MOVI_TIPO_CLASSE = ?";
		ps.adicionarLong(chave);

			executarUpdateDelete(stSql, ps);

	}

	public List consultarMovimentacaoTipoMovimentacaoTipoClasseGeral(String id_movimentacaotipoclasse) throws Exception {

		String stSql = "";
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT t2.ID_MOVI_TIPO_MOVI_TIPO_CLASSE, t2.MOVI_TIPO_MOVI_TIPO_CLASSE, t1.ID_MOVI_TIPO, t1.MOVI_TIPO, t3.ID_MOVI_TIPO_CLASSE, t3.MOVI_TIPO_CLASSE, t1.ID_CNJ_MOVI ";
		stSql += " FROM PROJUDI.MOVI_TIPO t1 ";
		stSql += " LEFT JOIN PROJUDI.VIEW_MOVI_TIPO_MOVI_TIPO_CLASS t2 ON t1.ID_MOVI_TIPO = t2.ID_MOVI_TIPO AND t2.ID_MOVI_TIPO_CLASSE = ? ";
		stSql += " LEFT JOIN PROJUDI.MOVI_TIPO_CLASSE t3 ON t3.ID_MOVI_TIPO_CLASSE = t2.ID_MOVI_TIPO_CLASSE ";
		ps.adicionarLong(id_movimentacaotipoclasse);
		stSql += " ORDER BY t1.MOVI_TIPO ";
		try{

			rs = consultar(stSql, ps);

			while (rs.next()) {
				MovimentacaoTipoMovimentacaoTipoClasseDt obTemp = new MovimentacaoTipoMovimentacaoTipoClasseDt();
				obTemp.setId(rs.getString("ID_MOVI_TIPO_MOVI_TIPO_CLASSE"));
				obTemp.setMovimentacaoTipoMovimentacaoTipoClasse(rs.getString("MOVI_TIPO_MOVI_TIPO_CLASSE"));
				obTemp.setId_MovimentacaoTipo(rs.getString("ID_MOVI_TIPO"));
				obTemp.setMovimentacaoTipo(rs.getString("MOVI_TIPO") + " (CNJ:" + rs.getString("ID_CNJ_MOVI") + ")");
				obTemp.setId_MovimentacaoTipoClasse(id_movimentacaotipoclasse);
				obTemp.setMoviTipoClasse(rs.getString("MOVI_TIPO_CLASSE"));
				liTemp.add(obTemp);
			}

		
		} finally{
			try{
				if (rs != null)
					rs.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
}
