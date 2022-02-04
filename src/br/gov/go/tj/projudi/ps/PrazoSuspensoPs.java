package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.PrazoSuspensoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;

import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class PrazoSuspensoPs extends PrazoSuspensoPsGen {

	private static final long serialVersionUID = 8090943876271473899L;
	private static String PRAZO_SUSPENSO_INVALIDO_SESSAO_VIRTUAL = "Resolução 313 do CNJ";

	public PrazoSuspensoPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * Retorna a quantidade de dias não válidos de acordo com os prazos
	 * suspensos cadastrados.
	 * 
	 * @author jrcorrea
	 */
	/*
	 * public int getProximaDataValida(List datas, String id_comarca, String
	 * id_cidade, String id_serventia){int QUANTIDADE_MAXIMA =
	 * 200; int inRetorno = 0; String stSql = ""; 
	 * 
	 * int i = 0; while (i < datas.size()) {
	 * 
	 * stSql = "SELECT COUNT(Id_PrazoSuspenso) AS QUANTIDADE FROM PrazoSuspenso
	 * WHERE "; stSql += " (Id_Comarca is null OR Id_Comarca = " + id_comarca + ")
	 * AND "; stSql += " (Id_Cidade is null OR Id_Cidade = " + id_cidade + ")
	 * AND "; stSql += " (Id_Serventia is null OR Id_Serventia = " +
	 * id_serventia + ") AND ( ";
	 * 
	 * stSql += " Data = " + Funcoes.BancoData((Date) datas.get(0));
	 * 
	 * for (int j = 0; j <= QUANTIDADE_MAXIMA && j < datas.size(); j++, i++)
	 * stSql += " OR Data = " + Funcoes.BancoData((Date) datas.get(i));
	 * 
	 * stSql += " )";
	 * 
	 * ResultSetTJGO rs1 = consultar(stSql);
	 * 
	 * if (rs1.next()) {inRetorno += rs1.getInt("QUANTIDADE"); } //rs1.close(); }
	 * 
	 * return inRetorno; }
	 */

	public boolean isDataValida(Date data, ServentiaDt serventia) throws Exception {

		boolean boRetorno = true;
		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		try{

			stSql = "SELECT ID_PRAZO_SUSPENSO FROM PROJUDI.PRAZO_SUSPENSO WHERE ";
			stSql += " (ID_COMARCA is null OR ID_COMARCA = ?) AND ";			ps.adicionarLong(serventia.getId_Comarca());
			stSql += " (ID_CIDADE is null OR ID_CIDADE = ?) AND ";				ps.adicionarLong(serventia.getId_Cidade());
			stSql += " (ID_SERV is null OR ID_SERV = ?) AND ( ";  				ps.adicionarLong(serventia.getId());
			stSql += " DATA = ?)";												ps.adicionarDate(data);
			if (serventia.isCriminal()){
				stSql += " AND ( ID_PRAZO_SUSPENSO_TIPO <> ?)";					ps.adicionarLong(PrazoSuspensoDt.ID_RECESSO_CPC);
			}			

			rs1 = consultar(stSql, ps);

			if (rs1.next()) {
				boRetorno = false;
			}
			// rs1.close();

		
		} finally{
			try{
				if (rs1 != null)
					rs1.close();
			} catch(Exception e1) {
			}
		}

		return boRetorno;

	}
	
	/**
	 * Verifica se a data deve ser considerada como válida para a expedição de um mandado judicial.
	 * @param data
	 * @param serventia
	 * @return
	 * @throws Exception
	 */
	public boolean isDataValidaMandadoJudicialCentral(Date data, ServentiaDt serventia) throws Exception {

		boolean boRetorno = true;
		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		try{
			stSql = "SELECT ID_PRAZO_SUSPENSO FROM PROJUDI.PRAZO_SUSPENSO WHERE ";
			stSql += " (ID_COMARCA is null OR ID_COMARCA = ?) AND ";			ps.adicionarLong(serventia.getId_Comarca());
			stSql += " (ID_CIDADE is null OR ID_CIDADE = ?) AND ";				ps.adicionarLong(serventia.getId_Cidade());
			stSql += " (ID_SERV is null OR ID_SERV = ?) AND ( ";  				ps.adicionarLong(serventia.getId());
			stSql += " DATA = ?)";												ps.adicionarDate(data);
			stSql += " AND ( ID_PRAZO_SUSPENSO_TIPO <> ?)";					ps.adicionarLong(PrazoSuspensoDt.ID_RECESSO_CPC);

			rs1 = consultar(stSql, ps);

			if (rs1.next()) {
				boRetorno = false;
			}
		
		} finally{
			try{
				if (rs1 != null)
					rs1.close();
			} catch(Exception e1) {
			}
		}

		return boRetorno;

	}
	
	
	//lrcampos 24/03/2020 14:21 - Verifica se uma data é valida desconsiderando a Resolução 313 do CNJ
	public boolean isDataValidaSesaoVirtualPJD(Date data, ServentiaDt serventia) throws Exception {

		boolean boRetorno = true;
		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		

		try{

			stSql = "SELECT ID_PRAZO_SUSPENSO FROM PROJUDI.PRAZO_SUSPENSO WHERE ";
			stSql += " (ID_COMARCA is null OR ID_COMARCA = ?) AND ";			ps.adicionarLong(serventia.getId_Comarca());
			stSql += " (ID_CIDADE is null OR ID_CIDADE = ?) AND ";				ps.adicionarLong(serventia.getId_Cidade());
			stSql += " (ID_SERV is null OR ID_SERV = ?) AND ( ";  				ps.adicionarLong(serventia.getId());
			stSql += " DATA = ?)";												ps.adicionarDate(data);
			stSql += " AND MOTIVO <> ? "; ps.adicionarString(PRAZO_SUSPENSO_INVALIDO_SESSAO_VIRTUAL);
			if (serventia.isCriminal()){
				stSql += " AND ( ID_PRAZO_SUSPENSO_TIPO <> ?)";					ps.adicionarLong(PrazoSuspensoDt.ID_RECESSO_CPC);
			}			

			rs1 = consultar(stSql, ps);

			if (rs1.next()) {
				boRetorno = false;
			}
			// rs1.close();

		
		} finally{
			try{
				if (rs1 != null)
					rs1.close();
			} catch(Exception e1) {
			}
		}

		return boRetorno;

	}
	
	public boolean isDataValidaBancaria(Date data, String id_comarca) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{
			String stSql = "SELECT ID_PRAZO_SUSPENSO FROM PROJUDI.PRAZO_SUSPENSO WHERE ";
			stSql += " (ID_COMARCA is null OR ID_COMARCA = ?) AND ";
			ps.adicionarLong(id_comarca);
			stSql += " DATA = ?";
			ps.adicionarDate(data);
			stSql += " AND ID_PRAZO_SUSPENSO_TIPO in (?,?,?)";
			ps.adicionarLong(PrazoSuspensoDt.ID_FERIADO_MUNICIPAL);
			ps.adicionarLong(PrazoSuspensoDt.ID_FERIADO_ESTADUAL);
			ps.adicionarLong(PrazoSuspensoDt.ID_FERIADO_FEDERAL);

			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				return false;
			}
		} finally{
			try{
				if (rs1 != null)
					rs1.close();
			} catch(Exception e1) {
			}
		}
		return true;

	}
	
	public boolean isDataValidaProtocolo(Date data, ServentiaDt serventia) throws Exception {

		boolean boRetorno = true;
		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		try{

			stSql = "SELECT ID_PRAZO_SUSPENSO FROM PROJUDI.PRAZO_SUSPENSO WHERE ";
			stSql += " (ID_COMARCA is null OR ID_COMARCA = ?) AND ";			ps.adicionarLong(serventia.getId_Comarca());
			stSql += " (ID_CIDADE is null OR ID_CIDADE = ?) AND ";				ps.adicionarLong(serventia.getId_Cidade());
			stSql += " (ID_SERV is null OR ID_SERV = ?) AND ( ";  				ps.adicionarLong(serventia.getId());
			stSql += " DATA = ?)";												ps.adicionarDate(data);
			stSql += " AND ( ID_PRAZO_SUSPENSO_TIPO <> ?)";						ps.adicionarLong(PrazoSuspensoDt.ID_RECESSO_CPC);
			stSql += " AND ( PLANTAO_LIBERADO = ?)";							ps.adicionarLong(1);

			rs1 = consultar(stSql, ps);

			if (rs1.next()) {
				boRetorno = false;
			}
		
		} finally{
			try{
				if (rs1 != null)
					rs1.close();
			} catch(Exception e1) {
			}
		}

		return boRetorno;

	}

	/**
	 * Método que consulta Prazos Suspenso pelo motivo
	 */
	public List consultarDescricao(String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_PRAZO_SUSPENSO, PRAZO_SUSPENSO_TIPO, MOTIVO, DATA, PLANTAO_LIBERADO";
		SqlFrom = " FROM PROJUDI.VIEW_PRAZO_SUSPENSO";
		SqlFrom += " WHERE MOTIVO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlOrder = " ORDER BY MOTIVO ";

		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				PrazoSuspensoDt obTemp = new PrazoSuspensoDt();
				obTemp.setId(rs1.getString("ID_PRAZO_SUSPENSO"));
				obTemp.setPrazoSuspensoTipo(rs1.getString("PRAZO_SUSPENSO_TIPO"));
				obTemp.setMotivo(rs1.getString("MOTIVO"));
				obTemp.setData(Funcoes.FormatarData(rs1.getDateTime("DATA")));
				obTemp.setPlantaoLiberado(rs1.getString("PLANTAO_LIBERADO"));
				liTemp.add(obTemp);
			}
			// rs1.close();
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next())
				liTemp.add(rs2.getLong("QUANTIDADE"));
			// rs1.close();
		
		} finally{
			try{
				if (rs1 != null)
					rs1.close();
			} catch(Exception e1) {
			}
			try{
				if (rs2 != null)
					rs2.close();
			} catch(Exception e1) {
			}
		}
		return liTemp;
	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;

		Sql = "SELECT ID_PRAZO_SUSPENSO AS ID, PRAZO_SUSPENSO_TIPO AS DESCRICAO1, MOTIVO AS DESCRICAO2, TO_CHAR(DATA,'dd/mm/yyyy') AS DESCRICAO3";
		SqlFrom = " FROM PROJUDI.VIEW_PRAZO_SUSPENSO";
		SqlFrom += " WHERE MOTIVO LIKE ?";
		ps.adicionarString("%" + descricao +"%");
		SqlOrder = " ORDER BY MOTIVO ";

		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
		}
		return stTemp;
	}
}
