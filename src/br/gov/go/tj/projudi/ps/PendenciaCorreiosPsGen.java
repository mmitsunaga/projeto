package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaCorreiosDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class PendenciaCorreiosPsGen extends Persistencia {

	private static final long serialVersionUID = 481133476592479691L;

	public PendenciaCorreiosPsGen() {}

	public void inserir(PendenciaCorreiosDt dados) throws Exception {

		String stSqlCampos = "";
		String stSqlValores = "";
		String stSql = "";
		String stVirgula = "";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos = "INSERT INTO PEND_CORREIOS (";

		stSqlValores += " Values (";

		if ((dados.getCodigoRastreamento().length() > 0)) {
			stSqlCampos += stVirgula + "COD_RASTREAMENTO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarString(dados.getCodigoRastreamento());

			stVirgula = ",";
		}
		if ((dados.getCodigoModelo().length() > 0)) {
			stSqlCampos += stVirgula + "COD_MODELO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarString(dados.getCodigoModelo());

			stVirgula = ",";
		}
		if ((dados.getMaoPropria().length() > 0)) {
			stSqlCampos += stVirgula + "MAO_PROPRIA ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarBoolean(Funcoes.BancoLogico(dados.getMaoPropria()));

			stVirgula = ",";
		}
		if ((dados.getOrdemServico().length() > 0)) {
			stSqlCampos += stVirgula + "ORDEM_SERVICO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarBoolean(Funcoes.BancoLogico(dados.getOrdemServico()));

			stVirgula = ",";
		}
		if ((dados.getMatriz().length() > 0)) {
			stSqlCampos += stVirgula + "MATRIZ ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getMatriz());

			stVirgula = ",";
		}
		if ((dados.getLote().length() > 0)) {
			stSqlCampos += stVirgula + "LOTE ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getLote());

			stVirgula = ",";
		}
		if ((dados.getCodigoInconsistencia().length() > 0)) {
			stSqlCampos += stVirgula + "COD_INCONSISTENCIA ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getCodigoInconsistencia());

			stVirgula = ",";
		}
		if ((dados.getCodigoBaixa().length() > 0)) {
			stSqlCampos += stVirgula + "COD_BAIXA ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getCodigoBaixa());

			stVirgula = ",";
		}
		if ((dados.getDataEntrega().length() > 0)) {
			stSqlCampos += stVirgula + "DATA_ENTREGA ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarDateTime(dados.getDataEntrega());

			stVirgula = ",";
		}
		if ((dados.getDataExpedicao().length() > 0)) {
			stSqlCampos += stVirgula + "DATA_EXPEDICAO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarDateTime(dados.getDataExpedicao());

			stVirgula = ",";
		}
		if ((dados.getId_Pend().length() > 0)) {
			stSqlCampos += stVirgula + "ID_PEND ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getId_Pend());

			stVirgula = ",";
		}
		if ((dados.getId_ProcessoCustaTipo().length() > 0)) {
			stSqlCampos += stVirgula + "ID_PROC_CUSTA_TIPO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getId_ProcessoCustaTipo());

			stVirgula = ",";
		}
		if ((dados.getMetaDados().length() > 0)) {
			stSqlCampos += stVirgula + "META_DADOS ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarClob(dados.getMetaDados());

			stVirgula = ",";
		}
		stSqlCampos += ")";
		stSqlValores += ")";
		stSql += stSqlCampos + stSqlValores;

		try {
			dados.setId(executarInsert(stSql, "ID_PEND_CORREIOS", ps));

		} catch (Exception e) {
			System.out.println("....psinserir: " + e.getMessage());
			throw new Exception(" <{Erro:.....}> PendenciaCorreioPsGen.inserir() " + e.getMessage());
		}
	}

	public void alterar(PendenciaCorreiosDt dados) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql = "";

		stSql = "UPDATE PEND_CORREIOS SET  ";
		stSql += "COD_RASTREAMENTO = ?";
		ps.adicionarString(dados.getCodigoRastreamento());

		stSql += ",MATRIZ = ?";
		ps.adicionarLong(dados.getMatriz());

		stSql += ",LOTE = ?";
		ps.adicionarLong(dados.getLote());

		stSql += ",COD_INCONSISTENCIA = ?";
		ps.adicionarLong(dados.getCodigoInconsistencia());

		stSql += ",COD_BAIXA = ?";
		ps.adicionarLong(dados.getCodigoBaixa());

		stSql += ",DATA_ENTREGA = ?";
		ps.adicionarDateTime(dados.getDataEntrega());
		
		stSql += ",DATA_EXPEDICAO = ?";
		ps.adicionarDateTime(dados.getDataExpedicao());

		stSql += ",ID_PEND = ?";
		ps.adicionarLong(dados.getId_Pend());
		
		stSql += ",COD_MODELO = ?";
		ps.adicionarLong(dados.getCodigoModelo());
		
		stSql += ",MAO_PROPRIA = ?";
		ps.adicionarBoolean(dados.getMaoPropria());
		
		stSql += ",ORDEM_SERVICO = ?";
		ps.adicionarBoolean(dados.getOrdemServico());
		
		stSql += ",ID_PROC_CUSTA_TIPO = ?";
		ps.adicionarLong(dados.getId_ProcessoCustaTipo());
		
		stSql += ",META_DADOS = ?";
		ps.adicionarClob(dados.getMetaDados());

		stSql += " WHERE ID_PEND_CORREIOS  = ? ";
		ps.adicionarLong(dados.getId());

		executarUpdateDelete(stSql, ps);

	}

	public void excluir(String chave) throws Exception {

		String stSql = "";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "DELETE FROM PEND_CORREIOS";
		stSql += " WHERE ID_PEND_CORREIOS = ?";
		ps.adicionarLong(chave);

		executarUpdateDelete(stSql, ps);

	}

	public PendenciaCorreiosDt consultarId(String id_pendcorreios) throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaCorreiosDt Dados = null;

		stSql = "SELECT * FROM PEND_CORREIOS WHERE ID_PEND_CORREIOS = ?";
		ps.adicionarLong(id_pendcorreios);

		try {
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados = new PendenciaCorreiosDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try {
				if (rs1 != null) rs1.close();
			} catch (Exception e) {}
		}
		return Dados;
	}

	protected void associarDt(PendenciaCorreiosDt Dados, ResultSetTJGO rs) throws Exception {

		Dados.setId(rs.getString("ID_PEND_CORREIOS"));
		Dados.setCodigoRastreamento(rs.getString("COD_RASTREAMENTO"));
		Dados.setMatriz(rs.getString("MATRIZ"));
		Dados.setLote(rs.getString("LOTE"));
		Dados.setCodigoInconsistencia(rs.getString("COD_INCONSISTENCIA"));
		Dados.setCodigoBaixa(rs.getString("COD_BAIXA"));
		Dados.setDataEntrega(Funcoes.FormatarData(rs.getDate("DATA_ENTREGA")));
		Dados.setDataExpedicao(Funcoes.FormatarData(rs.getDate("DATA_EXPEDICAO")));
		Dados.setId_Pend(rs.getString("ID_PEND"));
		Dados.setCodigoModelo(rs.getString("COD_MODELO"));
		Dados.setId_ProcessoCustaTipo(rs.getString("ID_PROC_CUSTA_TIPO"));
		Dados.setMaoPropria(Funcoes.FormatarLogico(rs.getString("MAO_PROPRIA")));
		Dados.setOrdemServico(Funcoes.FormatarLogico(rs.getString("ORDEM_SERVICO")));
		Dados.setMetaDados(rs.getString("META_DADOS"));
	}

	public List consultarDescricao(String descricao, String posicao) throws Exception {

		String stSql, stSqlOrder, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT ID_PEND_CORREIOS, COD_RASTREAMENTO ";
		stSqlFrom = " FROM VIEW_PEND_CORREIOS WHERE COD_RASTREAMENTO LIKE ?";
		stSqlOrder = " ORDER BY COD_RASTREAMENTO ";
		ps.adicionarString(descricao + "%");

		try {

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);

			while (rs1.next()) {
				PendenciaCorreiosDt obTemp = new PendenciaCorreiosDt();
				obTemp.setId(rs1.getString("ID_PEND_CORREIOS"));
				obTemp.setCodigoRastreamento(rs1.getString("COD_RASTREAMENTO"));
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

		stSql = "SELECT ID_PEND_CORREIOS as id, COD_RASTREAMENTO as descricao1 ";
		stSqlFrom = " FROM VIEW_PEND_CORREIOS WHERE COD_RASTREAMENTO LIKE ?";
		ps.adicionarString(descricao + "%");

		stSqlOrder = " ORDER BY COD_RASTREAMENTO ";
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