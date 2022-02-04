package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.projudi.dt.EscalaMgDt;

public class EscalaMgPsGen extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5878978237987535878L;

	// ---------------------------------------------------------
	public EscalaMgPsGen() {

	}

	// ---------------------------------------------------------
	public void inserir(EscalaMgDt dados) throws Exception {

		String stSqlCampos = "";
		String stSqlValores = "";
		String stSql = "";
		String stVirgula = "";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos = "INSERT INTO PROJUDI.ESCALA_MG (";

		stSqlValores += " Values (";

		if ((dados.getIdUsuario().length() > 0)) {
			stSqlCampos += stVirgula + "ID_USUARIO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getIdUsuario());

			stVirgula = ",";
		}
		if ((dados.getIdEscalaTipoMg().length() > 0)) {
			stSqlCampos += stVirgula + "ID_ESCALA_TIPO_MG ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getIdEscalaTipoMg());

			stVirgula = ",";
		}
		if ((dados.getDataInicio().length() > 0)) {
			stSqlCampos += stVirgula + "DATA_INICIO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarDateTime(dados.getDataInicio());

			stVirgula = ",";
		}
		if ((dados.getDataFim().length() > 0)) {
			stSqlCampos += stVirgula + "DATA_FIM ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarDateTime(dados.getDataFim());

			stVirgula = ",";
		}

		stSqlCampos += ")";
		stSqlValores += ")";
		stSql += stSqlCampos + stSqlValores;

		try {
			dados.setId(executarInsert(stSql, "ID_ESCALA_MG", ps));

		} catch (Exception e) {
			System.out.println("....psinserir: " + e.getMessage());
			throw new Exception(" <{Erro:.....}> escalaMgPsGen.inserir() " + e.getMessage());
		}
	}

	// ---------------------------------------------------------
	public void alterar(EscalaMgDt dados) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql = "";

		stSql = "UPDATE PROJUDI.ESCALA_MG SET  ";
		stSql += "ID_USUARIO = ?";
		ps.adicionarLong(dados.getIdUsuario());

		stSql += ",ID_ESCALA_TIPO_MG = ?";
		ps.adicionarLong(dados.getIdEscalaTipoMg());

		stSql += ",DATA_INICIO = ?";
		ps.adicionarDateTime(dados.getDataInicio());

		stSql += ",DATA_FIM = ?";
		ps.adicionarDateTime(dados.getDataFim());

		stSql += " WHERE ID_ESCALA_MG  = ? ";
		ps.adicionarLong(dados.getId());

		executarUpdateDelete(stSql, ps);

	}

	// ---------------------------------------------------------
	public void excluir(String chave) throws Exception {

		String stSql = "";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "DELETE FROM PROJUDI.ESCALA_MG";
		stSql += " WHERE ID_ESCALA_MG = ?";
		ps.adicionarLong(chave);

		executarUpdateDelete(stSql, ps);

	}

	// ---------------------------------------------------------
	public EscalaMgDt consultarId(String id_escalaMg) throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EscalaMgDt Dados = null;

		stSql = "SELECT * FROM PROJUDI.ESCALA_MG WHERE ID_ESCALA_MG = ?";
		ps.adicionarLong(id_escalaMg);

		try {

			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados = new EscalaMgDt();
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

	protected void associarDt(EscalaMgDt Dados, ResultSetTJGO rs) throws Exception {

		Dados.setId(rs.getString("ID_ESCALA_MG"));
		Dados.setIdUsuario(rs.getString("ID_USUARIO"));
		Dados.setIdEscalaTipoMg(rs.getString("ID_ESCALA_TIPO_MG"));
		Dados.setDataInicio(Funcoes.FormatarData(rs.getDate("DATA_INICIO")));
		Dados.setDataFim(Funcoes.FormatarData(rs.getDate("DATA_FIM")));
		Dados.setCodigoTemp(rs.getString("CODIGO_TEMP"));
	}
}
