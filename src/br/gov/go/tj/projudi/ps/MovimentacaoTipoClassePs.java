package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MovimentacaoTipoClasseDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class MovimentacaoTipoClassePs extends MovimentacaoTipoClassePsGen {

	private static final long serialVersionUID = -5337180806160659982L;

	public MovimentacaoTipoClassePs(Connection conexao){
		Conexao = conexao;
	}

	public void inserir(MovimentacaoTipoClasseDt dados) throws Exception {

		String stSqlCampos = "";
		String stSqlValores = "";
		String stSql = "";
		String stVirgula = "";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSqlCampos = "INSERT INTO projudi.MOVI_TIPO_CLASSE (";

		stSqlValores += " Values (";

		if ((dados.getMovimentacaoTipoClasse().length() > 0)) {
			stSqlCampos += stVirgula + "MOVI_TIPO_CLASSE ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarString(dados.getMovimentacaoTipoClasse());

			stVirgula = ",";
		}
		if ((dados.getMovimentacaoTipoClasseCodigo().length() > 0)) {
			stSqlCampos += stVirgula + "MOVI_TIPO_CLASSE_CODIGO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getMovimentacaoTipoClasseCodigo());

			stVirgula = ",";
		}
		stSqlCampos += ")";
		stSqlValores += ")";
		stSql += stSqlCampos + stSqlValores;

			dados.setId(executarInsert(stSql, "ID_MOVI_TIPO_CLASSE", ps));
	}

	public void alterar(MovimentacaoTipoClasseDt dados) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql = "";

		stSql = "UPDATE projudi.MOVI_TIPO_CLASSE SET  ";
		stSql += "MOVI_TIPO_CLASSE = ?";
		ps.adicionarString(dados.getMovimentacaoTipoClasse());

		stSql += ",MOVI_TIPO_CLASSE_CODIGO = ?";
		ps.adicionarLong(dados.getMovimentacaoTipoClasseCodigo());

		stSql += " WHERE ID_MOVI_TIPO_CLASSE  = ? ";
		ps.adicionarLong(dados.getId());

			executarUpdateDelete(stSql, ps);

	}

	public void excluir(String chave) throws Exception {

		String stSql = "";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "DELETE FROM projudi.MOVI_TIPO_CLASSE";
		stSql += " WHERE ID_MOVI_TIPO_CLASSE = ?";
		ps.adicionarLong(chave);

			executarUpdateDelete(stSql, ps);

	}

	public MovimentacaoTipoClasseDt consultarId(String id_movimentacaotipoclasse) throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		MovimentacaoTipoClasseDt Dados = null;

		stSql = "SELECT * FROM projudi.VIEW_MOVI_TIPO_CLASSE WHERE ID_MOVI_TIPO_CLASSE = ?";
		ps.adicionarLong(id_movimentacaotipoclasse);

		try{
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados = new MovimentacaoTipoClasseDt();
				associarDt(Dados, rs1);
			}
		
		} finally{
			try{
				if (rs1 != null)
					rs1.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}

	protected void associarDt(MovimentacaoTipoClasseDt Dados, ResultSetTJGO rs) throws Exception{
		
		Dados.setId(rs.getString("ID_MOVI_TIPO_CLASSE"));
		Dados.setMovimentacaoTipoClasse(rs.getString("MOVI_TIPO_CLASSE"));
		Dados.setMovimentacaoTipoClasseCodigo(rs.getString("MOVI_TIPO_CLASSE_CODIGO"));
		Dados.setCodigoTemp(rs.getString("CODIGO_TEMP"));
				
	}

	public List consultarDescricao(String descricao, String posicao) throws Exception {

		String stSql = "";
		String stSqlFrom = "";
		String stSqlOrder = "";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT ID_MOVI_TIPO_CLASSE, MOVI_TIPO_CLASSE";
		stSqlFrom = " FROM projudi.VIEW_MOVI_TIPO_CLASSE";
		stSqlFrom += " WHERE MOVI_TIPO_CLASSE LIKE ?";
		stSqlOrder = " ORDER BY MOVI_TIPO_CLASSE ";
		ps.adicionarString( descricao +"%");

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			while (rs1.next()) {
				MovimentacaoTipoClasseDt obTemp = new MovimentacaoTipoClasseDt();
				obTemp.setId(rs1.getString("ID_MOVI_TIPO_CLASSE"));
				obTemp.setMovimentacaoTipoClasse(rs1.getString("MOVI_TIPO_CLASSE"));
				liTemp.add(obTemp);
			}
			stSql = "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom, ps);
			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}
		
		} finally{
			try{
				if (rs1 != null)
					rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null)
					rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	public List listarMovimentacoesTipoClasse() throws Exception {
		String stSql = "";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;

		stSql = "SELECT ID_MOVI_TIPO_CLASSE, MOVI_TIPO_CLASSE, MOVI_TIPO_CLASSE_CODIGO FROM projudi.VIEW_MOVI_TIPO_CLASSE ORDER BY MOVI_TIPO_CLASSE ";
		try{
			rs1 = consultarSemParametros(stSql);
			while (rs1.next()) {
				MovimentacaoTipoClasseDt obTemp = new MovimentacaoTipoClasseDt();
				obTemp.setId(rs1.getString("ID_MOVI_TIPO_CLASSE"));
				obTemp.setMovimentacaoTipoClasse(rs1.getString("MOVI_TIPO_CLASSE"));
				obTemp.setMovimentacaoTipoClasseCodigo(rs1.getString("MOVI_TIPO_CLASSE_CODIGO"));
				liTemp.add(obTemp);
			}
		
		} finally{
			try{
				if (rs1 != null)
					rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;

	}

}
