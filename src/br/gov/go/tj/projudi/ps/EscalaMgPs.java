package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.BancoDt;
import br.gov.go.tj.projudi.dt.ContaUsuarioDt;
import br.gov.go.tj.projudi.dt.EscalaMgDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatoriosMandadoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.ValidacaoUtil;

//---------------------------------------------------------

public class EscalaMgPs extends EscalaMgPsGen {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7281671179072458549L;

	public EscalaMgPs(Connection conexao) {
		Conexao = conexao;
	}

	public List listaTodosEscala(String idServ) throws Exception {

		List listaUsuarios = new ArrayList();

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EscalaMgDt escalaMgDt;
		sql.append("SELECT e.id_escala_mg, e.id_usuario, et.id_escala_tipo_mg, e.data_inicio,"
				+ " e.data_fim, e.codigo_temp, u.nome as nomeUsuario, et.escala_tipo_mg as escalaTipoMg FROM  projudi.escala_mg e"
				+ " INNER JOIN projudi.escala_tipo_mg et ON et.id_escala_tipo_mg = e.id_escala_tipo_mg"
				+ " INNER JOIN projudi.usu u ON u.id_usu = e.id_usuario"
				+ " INNER JOIN projudi.usu_serv us ON us.id_usu = u.id_usu AND us.ativo = ? AND us.id_serv = ?"
				+ " ORDER BY nome, et.escala_tipo_mg");
		ps.adicionarLong(UsuarioServentiaDt.ATIVO);	
		ps.adicionarLong(idServ);	
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				escalaMgDt = new EscalaMgDt();
				this.associarDt(escalaMgDt, rs);
				escalaMgDt.setEscalaTipoMg(rs.getString("escalaTipoMg"));
				escalaMgDt.setUsuario(rs.getString("nomeUsuario"));
				listaUsuarios.add(escalaMgDt);
			}

		} 
		
		finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaUsuarios;
	}

	public EscalaMgDt consultaPorIdUsuarioAtivo(String idUsuario) throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EscalaMgDt Dados = null;

		stSql = "SELECT et.escala_tipo_mg as escalaTipoMg FROM projudi.escala_mg e"
				+ " INNER JOIN projudi.escala_tipo_mg et ON et.id_escala_tipo_mg = e.id_escala_tipo_mg"
				+ " WHERE e.id_usuario = ? AND e.data_fim is null";
		ps.adicionarLong(idUsuario);

		try {

			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados = new EscalaMgDt();
				Dados.setEscalaTipoMg(rs1.getString("escalaTipoMg"));
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

	public EscalaMgDt consultaPorIdEscala(String idEscala) throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EscalaMgDt Dados = null;

		stSql = "SELECT e.id_escala_mg, e.id_usuario, e.id_escala_tipo_mg, e.data_inicio, e.data_fim, e.codigo_temp, u.nome, et.escala_tipo_mg  FROM projudi.escala_mg e"
				+ " INNER JOIN projudi.usu u ON u.id_usu = e.id_usuario"
				+ " INNER JOIN projudi.escala_tipo_mg et ON et.id_escala_tipo_mg = e.id_escala_tipo_mg"
				+ " WHERE e.id_escala_mg = ?";
		ps.adicionarLong(idEscala);

		try {

			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados = new EscalaMgDt();
				associarDt(Dados, rs1);
				Dados.setUsuario(rs1.getString("nome"));
				Dados.setEscalaTipoMg(rs1.getString("escala_tipo_mg"));

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

	public List listaCenopsParaMandadoGratuito() throws Exception {

		List listaUsuarios = new ArrayList();

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EscalaMgDt obTemp;
		String array[] = new String[5];

		sql.append("SELECT tab.*,"

				+ " (SELECT c.comarca FROM projudi.usu_serv us INNER JOIN projudi.serv s ON s.id_serv = us.id_serv"
				+ "	INNER JOIN projudi.serv_tipo st ON"
				+ "	st.id_serv_tipo = s.id_serv_tipo AND st.serv_tipo_codigo = ?"
				+ "	INNER JOIN projudi.comarca c ON c.id_comarca = s.id_comarca"
				+ "	WHERE ROWNUM = 1 AND us.ativo = ? AND us.id_usu = tab.idUsuario) AS nomeComarca,"

				+ "	(SELECT b.banco_codigo||'#'||a.agencia_codigo||'#'||cu.conta_usu_operacao||'#'"
				+ "	||cu.conta_usu||'#'||cu.conta_usu_dv FROM projudi.conta_usu cu"
				+ "	INNER JOIN projudi.agencia a ON a.id_agencia = cu.id_agencia"
				+ "	INNER JOIN projudi.banco b ON b.id_banco = a.id_banco WHERE rownum = 1  AND cu.ativa = ?"
				+ "	AND cu.id_usu = tab.idUsuario) as infoConta FROM"

				+ " (SELECT u.id_usu AS idUsuario, u.nome AS nomeUsuario, u.cpf AS cpfUsuario"
				+ "	FROM projudi.escala_mg emg INNER JOIN projudi.usu u ON u.id_usu = emg.id_usuario"
				+ "	WHERE emg.id_escala_tipo_mg = ? AND emg.data_fim is null) tab");

		ps.adicionarLong(ServentiaTipoDt.CENTRAL_MANDADOS);
		ps.adicionarLong(UsuarioServentiaDt.ATIVO);
		ps.adicionarLong(ContaUsuarioDt.ATIVO);
		ps.adicionarLong(EscalaMgDt.ID_ESCALA_TIPO_CENOPS);

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				obTemp = new EscalaMgDt();

				obTemp.setIdUsuario(rs.getString("idUsuario"));
				obTemp.setUsuario(rs.getString("nomeUsuario"));
				obTemp.setCpfUsuario(rs.getString("cpfUsuario"));
				obTemp.setComarca(rs.getString("nomeComarca"));
				obTemp.setInfoConta(rs.getString("infoConta"));
				if (obTemp.getInfoConta() != null) {
					array = obTemp.getInfoConta().split("#");
					obTemp.setBanco(Integer.parseInt(array[0]));
					if (obTemp.getBanco() == BancoDt.CODIGO_CAIXA_ECONOMICA_FEDERAL)
						obTemp.setNomeBanco("CAIXA");
					else
						obTemp.setNomeBanco("OUTROS BANCOS");
					obTemp.setAgencia(Integer.parseInt(array[1]));
					obTemp.setContaOperacao(Integer.parseInt(array[2]));
					obTemp.setConta(Integer.parseInt(array[3]));
					obTemp.setContaDv(array[04]);
				}
				listaUsuarios.add(obTemp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally

		{
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaUsuarios;
	}
}
