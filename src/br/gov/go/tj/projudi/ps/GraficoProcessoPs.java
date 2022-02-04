package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EstatisticaProdutividadeItemDt;
import br.gov.go.tj.projudi.dt.relatorios.GraficoProcessoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class GraficoProcessoPs extends Persistencia{
	
	/**
     * 
     */
    private static final long serialVersionUID = 113248626686384149L;

    public GraficoProcessoPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * 
	 * param anoInicial
	 * param mesInicial
	 * param anoFinal
	 * param mesFinal
	 * param idComarca
	 * return
	 * throws Exception
	 */
   //TODO: Migração Banco - Ajuste no Group By
	public List graficoProcessoComarca (String anoInicial, String mesInicial, String anoFinal, String mesFinal, String idComarca) throws Exception {

		List lista = new ArrayList();
		String stSql = new String();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		//------------------------------------------------------------------------------------------
		stSql = " SELECT ANO, MES, COMARCA, ID_EST_PROD_ITEM, EST_PROD_ITEM, SUM(QUANTIDADE) AS QUANTIDADE FROM projudi.VIEW_EST_PROD WHERE ";
		//------------------------------------------------------------------------------------------
		/* Há diferença na cláusula WHERE da consulta se os anos INICIAL e FINAL forem diferentes */
		if (anoInicial.equals(anoFinal)) {
			stSql += "(ANO = ? AND MES >= ? AND MES <= ?)";
			ps.adicionarLong(anoInicial);
			ps.adicionarLong(mesInicial);
			ps.adicionarLong(mesFinal);
		} else {
			stSql += "((ANO = ? AND MES >= ? ) OR (ANO = ? AND MES <= ?)";
			ps.adicionarLong(anoInicial);
			ps.adicionarLong(mesInicial);
			ps.adicionarLong(anoFinal);
			ps.adicionarLong(mesFinal);
			for(int i = Funcoes.StringToInt(anoInicial) + 1; i < Funcoes.StringToInt(anoFinal); i++){
				stSql += " OR ANO = ?";
				ps.adicionarLong(i);
			}		
			stSql += ")";
		}
		stSql += " AND ID_COMARCA = ? AND ID_EST_PROD_ITEM IN (?,?,?)";
		ps.adicionarLong(idComarca);
		ps.adicionarString(GraficoProcessoDt.ARQUIVADO);
		ps.adicionarString(GraficoProcessoDt.ATIVO);
		ps.adicionarString(GraficoProcessoDt.RECEBIDO);
		//------------------------------------------------------------------------------------------
		stSql += " GROUP BY ANO, MES, COMARCA, ID_EST_PROD_ITEM, EST_PROD_ITEM";
		//------------------------------------------------------------------------------------------
		stSql += " ORDER BY ANO, MES, COMARCA, ID_EST_PROD_ITEM";

		//------------------------------------------------------------------------------------------
		try{
			rs = consultar(stSql.toString(), ps);
			while (rs.next()) {
				GraficoProcessoDt obTemp = new GraficoProcessoDt();
				obTemp.setAno(rs.getInt("ANO"));
				if (rs.getInt("MES") < 10) {
					obTemp.setMes("0" + rs.getString("MES"));
				} else {
					obTemp.setMes(rs.getString("MES"));
				}
				obTemp.setComarca(rs.getString("COMARCA"));
				obTemp.setEstatisticaProdutividadeItem(rs.getString("EST_PROD_ITEM"));
				obTemp.setQuantidade(rs.getLong("QUANTIDADE"));
				lista.add(obTemp);
			}

		
		} finally{
		   try{if (rs != null) rs.close();} catch(Exception e1) {}
		}
		return lista;
	}
	
	/**
	 * 
	 * param anoInicial
	 * param mesInicial
	 * param anoFinal
	 * param mesFinal
	 * param idComarca
	 * return
	 * throws Exception
	 */
	//TODO: Migração Banco - Ajuste no Group By
	public List graficoProcessoServentia (String anoInicial, String mesInicial, String anoFinal, String mesFinal, String idServentia) throws Exception {

		List lista = new ArrayList();
		String stSql = new String();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		//------------------------------------------------------------------------------------------
		stSql += " SELECT ANO, MES, SERV, ID_EST_PROD_ITEM, EST_PROD_ITEM, SUM(QUANTIDADE) AS QUANTIDADE FROM projudi.VIEW_EST_PROD WHERE ";
		//------------------------------------------------------------------------------------------
		/* Há diferença na cláusula WHERE da consulta se os anos INICIAL e FINAL forem diferentes */
		if (anoInicial.equals(anoFinal)) {
			stSql += "(ANO = ? AND MES >= ? AND MES <= ?)";
			ps.adicionarLong(anoInicial);
			ps.adicionarLong(mesInicial);
			ps.adicionarLong(mesFinal);
		} else {
			stSql += "((ANO = ? AND MES >= ? ) OR (ANO = ? AND MES <= ?)";
			ps.adicionarLong(anoInicial);
			ps.adicionarLong(mesInicial);
			ps.adicionarLong(anoFinal);
			ps.adicionarLong(mesFinal);
			for(int i = Funcoes.StringToInt(anoInicial) + 1; i < Funcoes.StringToInt(anoFinal); i++){
				stSql += " OR ANO = ?";
				ps.adicionarLong(i);
			}			
			stSql += ")";
		}
		stSql += " AND ID_SERV = ? AND ID_EST_PROD_ITEM IN (?,?,?)";
		ps.adicionarLong(idServentia);
		ps.adicionarString(GraficoProcessoDt.ARQUIVADO);
		ps.adicionarString(GraficoProcessoDt.ATIVO);
		ps.adicionarString(GraficoProcessoDt.RECEBIDO);
		//------------------------------------------------------------------------------------------
		stSql += " GROUP BY ANO, MES, SERV, ID_EST_PROD_ITEM, EST_PROD_ITEM";
		//------------------------------------------------------------------------------------------
		stSql += " ORDER BY ANO, MES, SERV, ID_EST_PROD_ITEM";

		//------------------------------------------------------------------------------------------
		try{
			rs = consultar(stSql, ps);
			while (rs.next()) {
				GraficoProcessoDt obTemp = new GraficoProcessoDt();
				obTemp.setAno(rs.getInt("ANO"));
				if (rs.getInt("MES") < 10) {
					obTemp.setMes("0" + rs.getString("MES"));
				} else {
					obTemp.setMes(rs.getString("MES"));
				}
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setEstatisticaProdutividadeItem(rs.getString("EST_PROD_ITEM"));
				obTemp.setQuantidade(rs.getLong("QUANTIDADE"));
				lista.add(obTemp);
			}

		
		} finally{
			try{if (rs != null) rs.close();} catch(Exception e1) {}					
		}
		return lista;
	}
	
	//TODO: Migração Banco - Ajuste no Group By
	public List graficoProcessoItemProdutividade (String anoInicial, String mesInicial, String anoFinal, String mesFinal, String idComarca, String idServentia, GraficoProcessoDt graficoProcessoDt) throws Exception {

		List lista = new ArrayList();
		String stSql = new String();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		//------------------------------------------------------------------------------------------
		stSql += " SELECT ANO, MES, COMARCA, SERV, ID_EST_PROD_ITEM, EST_PROD_ITEM, SUM(QUANTIDADE) AS QUANTIDADE FROM projudi.VIEW_EST_PROD WHERE ";
		//------------------------------------------------------------------------------------------
		/* Há diferença na cláusula WHERE da consulta se os anos INICIAL e FINAL forem diferentes */
		if (anoInicial.equals(anoFinal)) {
			stSql += "(ANO = ? AND MES >= ? AND MES <= ?)";
			ps.adicionarLong(anoInicial);
			ps.adicionarLong(mesInicial);
			ps.adicionarLong(mesFinal);
		} else {
			stSql += "((ANO = ? AND MES >= ? ) OR (ANO = ? AND MES <= ?)";
			ps.adicionarLong(anoInicial);
			ps.adicionarLong(mesInicial);
			ps.adicionarLong(anoFinal);
			ps.adicionarLong(mesFinal);
			for(int i = Funcoes.StringToInt(anoInicial) + 1; i < Funcoes.StringToInt(anoFinal); i++){
				stSql += " OR ANO = ?";
				ps.adicionarLong(i);
			}
			stSql += ")";
		}
		if (!idComarca.equalsIgnoreCase("")){
			stSql += " AND ID_COMARCA = ?";
			ps.adicionarLong(idComarca);
		}
		if (!idServentia.equalsIgnoreCase("")){
			stSql += " AND ID_SERV = ?";
			ps.adicionarLong(idServentia);
		}
		stSql += " AND ID_EST_PROD_ITEM IN (";
		List listaEstatisticaProdutividadeItem = graficoProcessoDt.getListaEstatisticaProdutividadeItem();
		for (int i=0;i < listaEstatisticaProdutividadeItem.size();i++){
   			EstatisticaProdutividadeItemDt estatisticaProdutividadeItemDt = (EstatisticaProdutividadeItemDt)listaEstatisticaProdutividadeItem.get(i);
   			if(i>0) stSql += ",?";
   			else stSql += "?";
   			ps.adicionarString(estatisticaProdutividadeItemDt.getId());
		}
		stSql += ")";			
		//------------------------------------------------------------------------------------------
		stSql += " GROUP BY ANO, MES, COMARCA, ID_EST_PROD_ITEM, SERV, EST_PROD_ITEM";
		//------------------------------------------------------------------------------------------
		stSql += " ORDER BY ANO, MES, COMARCA, ID_EST_PROD_ITEM";

		//------------------------------------------------------------------------------------------
		try{
			rs = consultar(stSql.toString(), ps);
			while (rs.next()) {
				GraficoProcessoDt obTemp = new GraficoProcessoDt();
				obTemp.setAno(rs.getInt("ANO"));
				if (rs.getInt("MES") < 10) {
					obTemp.setMes("0" + rs.getString("MES"));
				} else {
					obTemp.setMes(rs.getString("MES"));
				}
				obTemp.setServentia(rs.getString("COMARCA"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setEstatisticaProdutividadeItem(rs.getString("EST_PROD_ITEM"));
				obTemp.setQuantidade(rs.getLong("QUANTIDADE"));
				lista.add(obTemp);
			}

		
		} finally{
			try{if (rs != null) rs.close();} catch(Exception e1) {}
		}
		return lista;
	}
}
