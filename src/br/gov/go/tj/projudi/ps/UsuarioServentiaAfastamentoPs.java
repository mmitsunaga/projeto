package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AfastamentoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaAfastamentoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;

public class UsuarioServentiaAfastamentoPs extends UsuarioServentiaAfastamentoPsGen {

	public UsuarioServentiaAfastamentoPs(Connection conexao){
		Conexao = conexao;
	}

	private static final long serialVersionUID = -7996625065440878914L;

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql = "";
		String stSqlFrom = "";
		String stSqlOrder = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;

		stSql= "SELECT ID_USU_SERV_AFASTAMENTO AS ID, USU AS DESCRICAO1, TO_CHAR(DATA_INICIO, 'DD/MM/YYYY') AS DESCRICAO2, AFASTAMENTO AS DESCRICAO3";
		stSqlFrom = " FROM PROJUDI.VIEW_USU_SERV_AFASTAMENTO";
		stSqlFrom += " WHERE USU LIKE ?";
		stSqlOrder = " ORDER BY USU ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}
	
	public String consultarDescricaoAfastamentosAbertosJSON(String descricao, String posicao, String id_serv ) throws Exception {

		String stSql = "";
		String stSqlFrom = "";
		String stSqlOrder = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 4;

		stSql= "SELECT ID_USU_SERV_AFASTAMENTO AS ID, USU AS DESCRICAO1, USU_NOME AS DESCRICAO2, TO_CHAR(DATA_INICIO, 'DD/MM/YYYY') AS DESCRICAO3, AFASTAMENTO AS DESCRICAO4";
		stSqlFrom = " FROM PROJUDI.VIEW_USU_SERV_AFASTAMENTO A";		
		stSqlFrom += " JOIN USU_SERV US ON US.ID_USU_SERV = A.ID_USU_SERV ";
		stSqlFrom += " WHERE USU LIKE ?";
		stSqlFrom += " AND ID_USU_SERV_FINALIZADOR IS NULL AND ID_AFASTAMENTO = ? AND US.ID_SERV = ?";   
		stSqlOrder = " ORDER BY USU, AFASTAMENTO";
		ps.adicionarString("%"+descricao+"%"); 
		ps.adicionarString(AfastamentoDt.CODIGO_SUSPENSAO_POR_ATRASO); 
		ps.adicionarString(id_serv);

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}
	
	public void registrarUsuarioAfastamento( String idUsuServAfastado, String idUsuServCadastrador, String mandJudExpiradosComplemento ) throws Exception {
		String sql = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = 	" INSERT INTO PROJUDI.USU_SERV_AFASTAMENTO ";
		sql +=	" (ID_USU_SERV, ID_AFASTAMENTO, DATA_INICIO, ID_USU_SERV_CADASTRADOR, MOTIVO_INICIO) VALUES (";
		sql += 	" 		?,"; 		ps.adicionarLong(idUsuServAfastado);
		sql +=	"	    9,";
		sql +=	"	    SYSDATE,";
		sql +=	"	    ?,"; 		ps.adicionarLong(idUsuServCadastrador);
		sql +=	"   	?)";		ps.adicionarString(mandJudExpiradosComplemento);
		
		this.executarUpdateDelete(sql, ps);
	}
	
	public String consultarQuantidadeAfastamentosAbertos(String idServ) throws Exception {
		String sql = null;
		String qtd = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();			
		
		sql = " SELECT COUNT(1) AS QTD FROM USU_SERV_AFASTAMENTO USA ";
		sql += " JOIN USU_SERV US ON USA.ID_USU_SERV = US.ID_USU_SERV ";
		sql += " AND US.ID_SERV = ? "; ps.adicionarLong(idServ);
		sql += " AND USA.ID_USU_SERV_FINALIZADOR IS NULL";
		sql += " AND USA.ID_AFASTAMENTO = ? "; ps.adicionarString(AfastamentoDt.CODIGO_SUSPENSAO_POR_ATRASO); 
		
		try{
				rs1 = consultar(sql, ps);
				rs1.next();
				qtd = rs1.getString("QTD");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		
		return qtd;
	}
	/**
	 * Retorna lista de afastamentos por oficial e serventia.
	 * 
	 * @param idUsuario
	 * @param idServentia
	 * @return List
	 * @author Fernando Meireles
	 */

	public List consultaAfastamentoPorOficialServentia(String idUsuario, String idServentia) throws Exception {

		List listaOficiaisAfastamento = new ArrayList();

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		try {
			
			
			sql.append("SELECT u.nome AS nomeUsuario, u1.nome AS nomeCadastrador, u2.nome AS nomeFinalizador,  usa.afastamento AS afastamento,"
					+ " TO_CHAR(usa.data_inicio,'dd/mm/yyyy') AS dataInicio, TO_CHAR(usa.data_fim,'dd/mm/yyyy') AS dataFim"
					+ " FROM projudi.view_usu_serv_afastamento usa"
					+ " INNER JOIN projudi.usu_serv us ON us.id_usu_serv = usa.id_usu_serv"
					+ "	INNER JOIN projudi.usu u ON u.id_usu = us.id_usu"
					+ "	INNER JOIN projudi.usu_serv us1 ON us1.id_usu_serv = usa.id_usu_serv_cadastrador"
					+ "	INNER JOIN projudi.usu u1 ON u1.id_usu = us1.id_usu"
					+ " LEFT JOIN projudi.usu_serv us2 ON us2.id_usu_serv = usa.id_usu_serv_finalizador"
					+ " LEFT JOIN projudi.usu u2 ON u2.id_usu = us2.id_usu"
					+ "	WHERE us.ativo = ? AND us.id_usu = ? AND us.id_serv = ? AND usa.id_afastamento <> ?"
					+ " ORDER BY TO_CHAR(usa.data_inicio,'yyyymmdd')");
			
			ps.adicionarLong(UsuarioServentiaDt.ATIVO);
			ps.adicionarLong(idUsuario);
			ps.adicionarLong(idServentia);
			ps.adicionarLong(AfastamentoDt.CODIGO_SUSPENSAO_POR_ATRASO);

			rs = consultar(sql.toString(), ps);

			while (rs.next()) {

				UsuarioServentiaAfastamentoDt objDt = new UsuarioServentiaAfastamentoDt();
				objDt.setNomeUsuario(rs.getString("nomeUsuario"));
				objDt.setNomeUsuarioCadastrador(rs.getString("nomeCadastrador"));
				objDt.setNomeUsuarioFinalizador(rs.getString("nomeFinalizador"));
				objDt.setAfastamento(rs.getString("afastamento"));
				objDt.setDataInicio(rs.getString("dataInicio"));
				objDt.setDataFim(rs.getString("dataFim"));
				listaOficiaisAfastamento.add(objDt);
			}
		}

		

		finally

		{
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		return listaOficiaisAfastamento;
	}

	/**
	 * 
	 * Retorna oficial de uma serventia com afastamento aberto.
	 * 
	 * @param idUsuario
	 * @param idServentia
	 * @return Objeto
	 * @author Fernando Meireles
	 */

	public UsuarioServentiaAfastamentoDt consultarAfastamentoAbertoPorOficialServentia(String idUsuario,
			String idServentia) throws Exception {

		UsuarioServentiaAfastamentoDt objDt = null;

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		try {
			sql.append("SELECT usa.id_usu_serv_afastamento AS idUsuServAfastamento, u.id_usu AS idUsuario,"
					+ " u.nome AS nomeUsuario, u.cpf AS cpfUsuario, usa.afastamento AS afastamento, usa.id_afastamento AS idAfastamento,"
					+ " usa.motivo_inicio  AS motivoInicio, usa.motivo_fim AS motivoFim,"
					+ " TO_CHAR(usa.data_inicio,'dd/mm/yyyy') AS dataInicio, usa.id_usu_serv AS idUsuServ,"
					+ " TO_CHAR(usa.data_fim,'dd/mm/yyyy') AS dataFim FROM projudi.view_usu_serv_afastamento usa"
					+ "	INNER JOIN projudi.usu_serv us ON us.id_usu_serv = usa.id_usu_serv"
					+ "	INNER JOIN projudi.usu u ON u.id_usu = us.id_usu"
					+ "	WHERE us.ativo = ? AND us.id_usu = ? AND us.id_serv = ? AND usa.data_fim IS NULL");

			ps.adicionarLong(UsuarioServentiaDt.ATIVO);
			ps.adicionarLong(idUsuario);
			ps.adicionarLong(idServentia);
			rs = consultar(sql.toString(), ps);

			if (rs.next()) {
				objDt = new UsuarioServentiaAfastamentoDt();
				objDt.setId_Usuario(rs.getString("idUsuario"));
				objDt.setNomeUsuario(rs.getString("nomeUsuario"));
				objDt.setCpfUsuario(rs.getString("cpfUsuario"));
				objDt.setId_Afastamento(rs.getString("idAfastamento"));
				objDt.setAfastamento(rs.getString("afastamento"));
				objDt.setDataInicio(rs.getString("dataInicio"));
				objDt.setDataFim(rs.getString("dataFim"));
				objDt.setId_UsuarioServentiaAfastamento(rs.getString("idUsuServAfastamento"));
				objDt.setId_UsuarioServentia(rs.getString("idUsuServ"));
				objDt.setMotivoInicio(rs.getString("motivoInicio"));
				objDt.setMotivoFim(rs.getString("motivoFim"));
				objDt.setAcao("Retornar");
			}
		}

		 

		finally

		{
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		return objDt;
	}

	/**
	 * Finaliza afastamento de um oficial de justiça.
	 * 
	 * @param idUsuarioServentiaAfastamento
	 * @param idUsuServFinalizador
	 * @param motivoFim
	 * @author Fernando Meireles
	 * @throws Exception
	 */

	public void retornoUsuario(String idUsuarioServentiaAfastamento, String idUsuServFinalizador, String motivoFim)
			throws Exception {
		 
		StringBuffer sql = new StringBuffer();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append("UPDATE projudi.usu_serv_afastamento SET  data_fim = SYSDATE, id_usu_serv_finalizador = ?,"
				+ " motivo_fim = ? WHERE id_usu_serv_afastamento = ?");

		ps.adicionarLong(idUsuServFinalizador);
		ps.adicionarString(motivoFim);
		ps.adicionarLong(idUsuarioServentiaAfastamento);

		executarUpdateDelete(sql.toString(), ps);
		 
	}

	/**
	 * Cria um registro de afastamento para um oficial de justiça.
	 * 
	 * @param idUsuServCadastrador
	 * @param idAfastamento
	 * @param motivoInicio
	 * @author Fernando Meireles
	 * @throws Exception
	 */

	public String afastamentoOficial(String idAfastamento, String idUsuServ, String motivoInicio, String idUsuServCadastrador)
			throws Exception {
	    
		String sql = "";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = ("INSERT INTO projudi.usu_serv_afastamento (id_usu_serv, id_afastamento, data_inicio, id_usu_serv_cadastrador, motivo_inicio)"
				+ " VALUES (?, ?, SYSDATE, ?, ?)");

		ps.adicionarLong(idUsuServ);
		ps.adicionarLong(idAfastamento);
		ps.adicionarLong(idUsuServCadastrador);
		ps.adicionarString(motivoInicio);
		return executarInsert(sql, "ID_USU_SERV_AFASTAMENTO", ps);	
		 
	}
}
