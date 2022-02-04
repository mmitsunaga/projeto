package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoArquivamentoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ProcessoArquivamentoTipoPsGen extends Persistencia {

	private static final long serialVersionUID = -5923763645354046120L;

	public ProcessoArquivamentoTipoPsGen() {
	}

	public void inserir(ProcessoArquivamentoTipoDt dados) throws Exception {

		String stSqlCampos = "";
		String stSqlValores = "";
		String stSql = "";
		String stVirgula = "";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos = "INSERT INTO proc_arquivamento_tipo  (";

		stSqlValores += " Values (";

		if ((dados.getProcessoArquivamentoTipo().length() > 0)) {
			stSqlCampos += stVirgula + "PROC_ARQUIVAMENTO_TIPO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarString(dados.getProcessoArquivamentoTipo());

			stVirgula = ",";
		}
		stSqlCampos += ")";
		stSqlValores += ")";
		stSql += stSqlCampos + stSqlValores;

		try {
			dados.setId(executarInsert(stSql, "ID_PROC_ARQUIVAMENTO_TIPO", ps));

		} catch (Exception e) {
			System.out.println("....psinserir: " + e.getMessage());
			throw new Exception(" <{Erro:.....}> ProcessoArquivamentoTipoPsGen.inserir() " + e.getMessage());
		}
	}

	public void alterar(ProcessoArquivamentoTipoDt dados) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql = "";

		stSql = "UPDATE proc_arquivamento_tipo  SET  ";
		stSql += "PROC_ARQUIVAMENTO_TIPO = ?";
		ps.adicionarString(dados.getProcessoArquivamentoTipo());
		if (dados.getCodigoTemp() != null && !dados.getCodigoTemp().equals("")) {
			stSql += ", CODIGO_TEMP = ? ";
			ps.adicionarLong(dados.getCodigoTemp());
		}

		stSql += " WHERE ID_PROC_ARQUIVAMENTO_TIPO  = ? ";
		ps.adicionarLong(dados.getId());

		executarUpdateDelete(stSql, ps);
	}

	public void excluir(String chave) throws Exception {

		String stSql = "";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "DELETE FROM proc_arquivamento_tipo ";
		stSql += " WHERE ID_PROC_ARQUIVAMENTO_TIPO = ?";
		ps.adicionarLong(chave);

		executarUpdateDelete(stSql, ps);
	}

	public ProcessoArquivamentoTipoDt consultarId(String id_prococessoarquivamentotipo) throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoArquivamentoTipoDt Dados = null;

		stSql = "SELECT * FROM view_proc_arquivamento_tipo  WHERE ID_PROC_ARQUIVAMENTO_TIPO = ?";
		ps.adicionarLong(id_prococessoarquivamentotipo);

		try {
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados = new ProcessoArquivamentoTipoDt();
				associarDt(Dados, rs1);
			}
		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			}
		}
		return Dados;
	}

	protected void associarDt(ProcessoArquivamentoTipoDt Dados, ResultSetTJGO rs) throws Exception {

		Dados.setId(rs.getString("ID_PROC_ARQUIVAMENTO_TIPO"));
		Dados.setProcessoArquivamentoTipo(rs.getString("PROC_ARQUIVAMENTO_TIPO"));
		Dados.setCodigoTemp(rs.getString("CODIGO_TEMP"));
	}

	public List consultarDescricao(String descricao, String posicao) throws Exception {

		String stSql, stSqlOrder, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT ID_PROC_ARQUIVAMENTO_TIPO, PROC_ARQUIVAMENTO_TIPO ";
		stSqlFrom = " FROM view_proc_arquivamento_tipo  WHERE PROC_ARQUIVAMENTO_TIPO LIKE ?";
		stSqlOrder = " ORDER BY PROC_ARQUIVAMENTO_TIPO ";
		ps.adicionarString(descricao + "%");

		try {

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);

			while (rs1.next()) {
				ProcessoArquivamentoTipoDt obTemp = new ProcessoArquivamentoTipoDt();
				obTemp.setId(rs1.getString("ID_PROC_ARQUIVAMENTO_TIPO"));
				obTemp.setProcessoArquivamentoTipo(rs1.getString("PROC_ARQUIVAMENTO_TIPO"));
				liTemp.add(obTemp);
			}
			stSql = "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom, ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}

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
		return liTemp;
	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {

		String stSql, stSqlFrom, stSqlOrder;
		String stTemp = "";
		int qtdeColunas = 1;
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT ID_PROC_ARQUIVAMENTO_TIPO as id, PROC_ARQUIVAMENTO_TIPO as descricao1 ";
		stSqlFrom = " FROM view_proc_arquivamento_tipo  WHERE PROC_ARQUIVAMENTO_TIPO LIKE ?";
		ps.adicionarString(descricao + "%");

		stSqlOrder = " ORDER BY PROC_ARQUIVAMENTO_TIPO ";
		try {

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql = "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

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
