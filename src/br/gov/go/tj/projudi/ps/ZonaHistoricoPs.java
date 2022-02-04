package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.ZonaDt;
import br.gov.go.tj.projudi.dt.ZonaHistoricoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ZonaHistoricoPs extends Persistencia{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3562374905390844483L;

	public ZonaHistoricoPs(Connection conexao){
		Conexao = conexao;		
	}
    
	public void inserir(ZonaHistoricoDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSqlCampos= "INSERT INTO PROJUDI.ZONA_HIST ("; 

		stSqlValores +=  " Values (";
		
		if (dados.getDataFim() != null && dados.getDataFim().length()>0) {
			stSqlCampos+=   stVirgula + "DATA_FIM " ;
			stSqlValores+=   stVirgula + "? " ;
			 
			ps.adicionarDateTime(dados.getDataFim());
			
			stVirgula=",";
		} else {
			stSqlCampos+=   stVirgula + "DATA_FIM " ;
			stSqlValores+=   stVirgula + "? " ;
			
			ps.adicionarDateTime(new Date());
			
			stVirgula=",";
		}
		
		if (dados.getZonaDt() != null) {
			if (dados.getZonaDt().getId() != null && dados.getZonaDt().getId().length()>0) {
				 stSqlCampos+=   stVirgula + "ID_ZONA " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarString(dados.getZonaDt().getId());  

				stVirgula=",";
			}
			
			if (dados.getZonaDt().getZona() != null && dados.getZonaDt().getZona().length()>0) {
				 stSqlCampos+=   stVirgula + "ZONA " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarString(dados.getZonaDt().getZona());  

				stVirgula=",";
			}
			
			if (dados.getZonaDt().getZonaCodigo() != null && dados.getZonaDt().getZonaCodigo().length()>0) {
				 stSqlCampos+=   stVirgula + "ZONA_CODIGO " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarLong(dados.getZonaDt().getZonaCodigo());  

				stVirgula=",";
			}
			
			if (dados.getZonaDt().getId_Comarca() != null && dados.getZonaDt().getId_Comarca().length()>0) {
				 stSqlCampos+=   stVirgula + "ID_COMARCA " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarLong(dados.getZonaDt().getId_Comarca());  

				 stVirgula=",";
			}		
			
			if (dados.getZonaDt().getDataInicio() != null && dados.getZonaDt().getDataInicio().length()>0) {
				stSqlCampos+=   stVirgula + "DATA_INICIO " ;
				stSqlValores+=   stVirgula + "? " ;
				 
				ps.adicionarDateTime(dados.getZonaDt().getDataInicio());
				
				stVirgula=",";
			} else {
				stSqlCampos+=   stVirgula + "DATA_INICIO " ;
				stSqlValores+=   stVirgula + "? " ;
				
				ps.adicionarDateTime(new Date());
				
				stVirgula=",";
			}
			
			if (dados.getZonaDt().getId_UsuarioServentia() != null && dados.getZonaDt().getId_UsuarioServentia().length()>0) {
				 stSqlCampos+=   stVirgula + "ID_USU_SERV " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarLong(dados.getZonaDt().getId_UsuarioServentia());  

				 stVirgula=",";
			}
			
			if (dados.getZonaDt().getValorCivel() != null && dados.getZonaDt().getValorCivel().length()>0) {
				 stSqlCampos+=   stVirgula + "VALOR_CIVEL" ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarBigDecimal(dados.getZonaDt().getValorCivel());  

				stVirgula=",";
			}
			
			if (dados.getZonaDt().getValorCriminal() != null && dados.getZonaDt().getValorCriminal().length()>0) {
				 stSqlCampos+=   stVirgula + "VALOR_CRIMINAL " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarBigDecimal(dados.getZonaDt().getValorCriminal());  

				stVirgula=",";
			}
			
			if (dados.getZonaDt().getValorContaVinculada() != null && dados.getZonaDt().getValorContaVinculada().length()>0) {
				 stSqlCampos+=   stVirgula + "VALOR_CONTA_VINCULADA " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarBigDecimal(dados.getZonaDt().getValorContaVinculada());  

				stVirgula=",";
			}
			
			if (dados.getZonaDt().getValorSegundoGrau() != null && dados.getZonaDt().getValorSegundoGrau().length()>0) {
				 stSqlCampos+=   stVirgula + "VALOR_SEGUNDO_GRAU " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarBigDecimal(dados.getZonaDt().getValorSegundoGrau());  

				stVirgula=",";
			}
			
			if (dados.getZonaDt().getValorSegundoGrauContadoria() != null && dados.getZonaDt().getValorSegundoGrauContadoria().length()>0) {
				 stSqlCampos+=   stVirgula + "VALOR_SEGUNDO_GRAU_CONT " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarBigDecimal(dados.getZonaDt().getValorSegundoGrauContadoria());  

				stVirgula=",";
			}
			
			if (dados.getZonaDt().getCodigoTemp() != null && dados.getZonaDt().getCodigoTemp().length()>0) {
				 stSqlCampos+=   stVirgula + "CODIGO_TEMP " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarBigDecimal(dados.getZonaDt().getCodigoTemp());  

				stVirgula=",";
			}
		}
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_ZONA_HIST",ps)); 
	} 
	
	public List<ZonaHistoricoDt> consultar(String idZona) throws Exception {

		String Sql;
		List<ZonaHistoricoDt> listaZonaDeHistoricos = new ArrayList<ZonaHistoricoDt>();
		ResultSetTJGO rs=null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		

		Sql= "SELECT * FROM PROJUDI.VIEW_ZONA_HIST WHERE ID_ZONA = ? ORDER BY ID_ZONA_HIST DESC ";
		ps.adicionarLong(idZona);
				
		try{

			rs = consultar(Sql, ps);

			while (rs.next()) {
				ZonaHistoricoDt zonaHistorico = new ZonaHistoricoDt();
				
				zonaHistorico.setId(rs.getString("ID_ZONA_HIST"));
				zonaHistorico.setDataFim(rs.getString("DATA_FIM"));
				zonaHistorico.setNomeUsuario(rs.getString("NOME"));
				
				ZonaDt zona = new ZonaDt();
				
				zonaHistorico.setZonaDt(zona);
				
				listaZonaDeHistoricos.add(zonaHistorico);
			}			
		} finally {
			try {if (rs != null) rs.close();} catch (Exception e) {}
		}
		
		return listaZonaDeHistoricos; 
	}
}
