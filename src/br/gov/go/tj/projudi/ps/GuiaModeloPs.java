package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class GuiaModeloPs extends GuiaModeloPsGen{

	private static final long serialVersionUID = -882334655604855291L;

	@SuppressWarnings("unused")
	private GuiaModeloPs( ) {}
	
	public GuiaModeloPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * Método para consultar GuiaModelo pelo idGuiaTipo e idProcessoTipo.
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @return GuiaModeloDt
	 * @throws Exception
	 */
	public GuiaModeloDt consultarGuiaModelo(String idGuiaTipo, String idProcessoTipo)  throws Exception {
		
		String Sql;
		ResultSetTJGO rs1 = null;
		GuiaModeloDt Dados=null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String ClausulaWhere = " WHERE ";
		
		Sql= "SELECT * FROM projudi.VIEW_GUIA_MODELO ";
		if( idGuiaTipo != null ) {
			Sql += ClausulaWhere + " ID_GUIA_TIPO = ?";
			ps.adicionarLong(idGuiaTipo);
			ClausulaWhere = " AND ";
		}
		if( idProcessoTipo != null ) {
			Sql += ClausulaWhere + " ID_PROC_TIPO = ?";
			ps.adicionarLong(idProcessoTipo);
			ClausulaWhere = " AND ";
		}

		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				Dados= new GuiaModeloDt();
				associarDt(Dados, rs1);
			}
		
		}
		finally{
			 rs1.close();
		}
		return Dados; 
	}
	
	/**
	 * Método para consultar GuiaModelo pelo idGuiaTipo e idProcessoTipo para novo Regimento de custa. (PROAD: 201703000030747).
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @return GuiaModeloDt
	 * @throws Exception
	 */
	public GuiaModeloDt consultarGuiaModeloNovoRegimento(String idGuiaTipo, String idProcessoTipo)  throws Exception {
		
		String Sql;
		ResultSetTJGO rs1 = null;
		GuiaModeloDt Dados=null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String ClausulaWhere = " AND ";
		
		Sql= "SELECT * FROM projudi.VIEW_GUIA_MODELO WHERE CODIGO_TEMP = 1 ";
		if( idGuiaTipo != null ) {
			Sql += ClausulaWhere + " ID_GUIA_TIPO = ?";
			ps.adicionarLong(idGuiaTipo);
			ClausulaWhere = " AND ";
		}
		if( idProcessoTipo != null ) {
			Sql += ClausulaWhere + " ID_PROC_TIPO = ?";
			ps.adicionarLong(idProcessoTipo);
			ClausulaWhere = " AND ";
		}
		
		

		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				Dados= new GuiaModeloDt();
				associarDt(Dados, rs1);
			}
		
		}
		finally{
			 rs1.close();
		}
		return Dados; 
	}
	
	/**
	 * Método para consultar GuiaModelo pelo idGuiaTipo e idNaturezaSPG.
	 * @param String idGuiaTipo
	 * @param String idNaturezaSPG
	 * @return GuiaModeloDt
	 * @throws Exception
	 */
	public GuiaModeloDt consultarGuiaModeloNaturezaSPG(String idGuiaTipo, String idNaturezaSPG)  throws Exception {
		
		String Sql;
		ResultSetTJGO rs1 = null;
		GuiaModeloDt Dados=null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String ClausulaWhere = " WHERE ";
		
		Sql= "SELECT * FROM projudi.VIEW_GUIA_MODELO ";
		if( idGuiaTipo != null ) {
			Sql += ClausulaWhere + " ID_GUIA_TIPO = ?";
			ps.adicionarLong(idGuiaTipo);
			ClausulaWhere = " AND ";
		}
		if( idNaturezaSPG != null ) {
			Sql += ClausulaWhere + " ID_NATUREZA_SPG = ?";
			ps.adicionarLong(idNaturezaSPG);
			ClausulaWhere = " AND ";
		}

		try {
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				Dados= new GuiaModeloDt();
				associarDt(Dados, rs1);
			}
		}
		finally {
			 if (rs1 != null) rs1.close();
		}
		
		return Dados; 
	}
	
	/**
	 * Método para consultar lista de GuiaModelo pelo idGuiaTipo.
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaModelo(String idGuiaTipo)  throws Exception {
		
		String Sql;
		ResultSetTJGO rs1 = null;
		List listaGuiaModeloDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String ClausulaWhere = " WHERE ";
		
		Sql= "SELECT * FROM projudi.VIEW_GUIA_MODELO ";
		if( idGuiaTipo != null ) {
			Sql += ClausulaWhere + " ID_GUIA_TIPO = ?";
			ps.adicionarLong(idGuiaTipo);
			ClausulaWhere = " AND ";
		}

		try{
			rs1 = consultar(Sql, ps);
			if( rs1 != null ) {
				while(rs1.next()) {
					if( listaGuiaModeloDt == null ) {
						listaGuiaModeloDt = new ArrayList();
					}
					
					GuiaModeloDt guiaModeloDt = new GuiaModeloDt();
					
					guiaModeloDt.setId(rs1.getString("ID_GUIA_MODELO"));
					
					listaGuiaModeloDt.add(guiaModeloDt);
				}
			}
		
		}
		finally{
			 rs1.close();
		}
		return listaGuiaModeloDt; 
	}
	
	public String consultarDescricaoJSON(String descricao, String guiaTipo, String processoTipo, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";		
		String stSqlOrder ="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_GUIA_MODELO AS ID, GUIA_MODELO AS DESCRICAO1, GUIA_TIPO AS DESCRICAO2, PROC_TIPO AS DESCRICAO3";
		stSqlFrom = " FROM PROJUDI.VIEW_GUIA_MODELO";
		stSqlFrom += " WHERE GUIA_MODELO LIKE ?";
		stSqlFrom += " AND GUIA_TIPO LIKE ?";
		stSqlFrom += " AND PROC_TIPO LIKE ?";
		stSqlOrder = " ORDER BY GUIA_MODELO ";
		
		ps.adicionarString("%"+descricao+"%");
		ps.adicionarString("%"+guiaTipo+"%");
		ps.adicionarString("%"+processoTipo+"%");

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		}
		finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
		return stTemp; 
	}
}
