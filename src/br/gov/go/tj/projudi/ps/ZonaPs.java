package br.gov.go.tj.projudi.ps;

import java.util.Date;

import br.gov.go.tj.projudi.dt.ZonaDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ZonaPs extends ZonaPsGen{

	/**
     * 
     */
    private static final long serialVersionUID = 910591197686138840L;

    public ZonaPs(Connection conexao){
    	Conexao = conexao;
	}
    
	public void inserir(ZonaDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSqlCampos= "INSERT INTO PROJUDI.ZONA ("; 

		stSqlValores +=  " Values (";
		
		if (dados.getZona() != null && dados.getZona().length()>0) {
			 stSqlCampos+=   stVirgula + "ZONA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getZona());  

			stVirgula=",";
		}
		
		if (dados.getZonaCodigo() != null && dados.getZonaCodigo().length()>0) {
			 stSqlCampos+=   stVirgula + "ZONA_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getZonaCodigo());  

			stVirgula=",";
		}
		
		if (dados.getId_Comarca() != null && dados.getId_Comarca().length()>0) {
			 stSqlCampos+=   stVirgula + "ID_COMARCA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Comarca());  

			 stVirgula=",";
		}		
		
		if (dados.getDataInicio() != null && dados.getDataInicio().length()>0) {
			stSqlCampos+=   stVirgula + "DATA_INICIO " ;
			stSqlValores+=   stVirgula + "? " ;
			 
			ps.adicionarDateTime(dados.getDataInicio());
			
			stVirgula=",";
		} else {
			stSqlCampos+=   stVirgula + "DATA_INICIO " ;
			stSqlValores+=   stVirgula + "? " ;
			
			ps.adicionarDateTime(new Date());
			
			stVirgula=",";
		}
		
		if (dados.getId_UsuarioServentia() != null && dados.getId_UsuarioServentia().length()>0) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentia());  

			 stVirgula=",";
		}
		
		if (dados.getValorCivel() != null && dados.getValorCivel().length()>0) {
			 stSqlCampos+=   stVirgula + "VALOR_CIVEL" ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBigDecimal(dados.getValorCivel());  

			stVirgula=",";
		}
		
		if (dados.getValorCriminal() != null && dados.getValorCriminal().length()>0) {
			 stSqlCampos+=   stVirgula + "VALOR_CRIMINAL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBigDecimal(dados.getValorCriminal());  

			stVirgula=",";
		}
		
		if (dados.getValorContaVinculada() != null && dados.getValorContaVinculada().length()>0) {
			 stSqlCampos+=   stVirgula + "VALOR_CONTA_VINCULADA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBigDecimal(dados.getValorContaVinculada());  

			stVirgula=",";
		}
		
		if (dados.getValorSegundoGrau() != null && dados.getValorSegundoGrau().length()>0) {
			 stSqlCampos+=   stVirgula + "VALOR_SEGUNDO_GRAU " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBigDecimal(dados.getValorSegundoGrau());  

			stVirgula=",";
		}
		
		if (dados.getValorSegundoGrauContadoria() != null && dados.getValorSegundoGrauContadoria().length()>0) {
			 stSqlCampos+=   stVirgula + "VALOR_SEGUNDO_GRAU_CONT " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBigDecimal(dados.getValorSegundoGrauContadoria());  

			stVirgula=",";
		}
		
		if (dados.getCodigoTemp() != null && dados.getCodigoTemp().length()>0) {
			 stSqlCampos+=   stVirgula + "CODIGO_TEMP " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBigDecimal(dados.getCodigoTemp());  

			stVirgula=",";
		}
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_ZONA",ps)); 
	} 
	
	public void alterar(ZonaDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";
		String stVirgula="";

		stSql= "UPDATE PROJUDI.ZONA SET  ";
		
		if ((dados.getZona() != null && dados.getZona().length()>0)) {
			stSql+= " ZONA = ?";		 
			ps.adicionarString(dados.getZona());
			
			stVirgula=",";
		}
		
		if ((dados.getZonaCodigo() != null && dados.getZonaCodigo().length()>0)) {
			stSql+= stVirgula + " ZONA_CODIGO = ?";		 
			ps.adicionarLong(dados.getZonaCodigo());
			
			stVirgula=",";
		}
		
		if ((dados.getId_Comarca() != null && dados.getId_Comarca().length()>0)) {
			stSql+= stVirgula + " ID_COMARCA = ?";		 
			ps.adicionarLong(dados.getId_Comarca());
			
			stVirgula=",";
		}
		
		if ((dados.getDataInicio() != null && dados.getDataInicio().length()>0)) {
			stSql+= stVirgula + " DATA_INICIO = ?";
			ps.adicionarDateTime(dados.getDataInicio());
			
			stVirgula=",";
		} else {
			stSql+= stVirgula + " DATA_INICIO = ?";
			ps.adicionarDateTime(new Date());
			
			stVirgula=",";
		}	
		
		if ((dados.getId_UsuarioServentia() != null && dados.getId_UsuarioServentia().length()>0)) {
			stSql+= stVirgula + " ID_USU_SERV = ?";		 
			ps.adicionarLong(dados.getId_UsuarioServentia());
			
			stVirgula=",";
		}
		
		if ((dados.getValorCivel() != null && dados.getValorCivel().length()>0)) {
			stSql+= stVirgula + " VALOR_CIVEL = ?";		 
			ps.adicionarBigDecimal(dados.getValorCivel());
			
			stVirgula=",";
		}
		
		if ((dados.getValorCriminal() != null && dados.getValorCriminal().length()>0)) {
			stSql+= stVirgula + " VALOR_CRIMINAL = ?";		 
			ps.adicionarBigDecimal(dados.getValorCriminal());
			
			stVirgula=",";
		}
		
		if ((dados.getValorContaVinculada() != null && dados.getValorContaVinculada().length()>0)) {
			stSql+= stVirgula +  " VALOR_CONTA_VINCULADA = ?";		 
			ps.adicionarBigDecimal(dados.getValorContaVinculada());
			
			stVirgula=",";
		}
		
		if ((dados.getValorSegundoGrau() != null && dados.getValorSegundoGrau().length()>0)) {
			stSql+= stVirgula + " VALOR_SEGUNDO_GRAU = ?";		 
			ps.adicionarBigDecimal(dados.getValorSegundoGrau());  
			
			stVirgula=",";
		}
		
		if ((dados.getValorSegundoGrauContadoria() != null && dados.getValorSegundoGrauContadoria().length()>0)) {
			stSql+= stVirgula + " VALOR_SEGUNDO_GRAU_CONT = ?";		 
			ps.adicionarBigDecimal(dados.getValorSegundoGrauContadoria()); 
			
			stVirgula=",";
		}
		
		if ((dados.getCodigoTemp() != null && dados.getCodigoTemp().length()>0)) {
			stSql+= stVirgula + " CODIGO_TEMP = ?";		 
			ps.adicionarBigDecimal(dados.getCodigoTemp());
			
			stVirgula=",";
		}
		
		stSql += " WHERE ID_ZONA  = ? "; 		
		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	} 
	
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.ZONA";
		stSql += " WHERE ID_ZONA = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	}
	
	public ZonaDt consultarId(String id_zona )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ZonaDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_ZONA WHERE ID_ZONA = ?";		ps.adicionarLong(id_zona); 

		try {
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ZonaDt();
				associarDt(Dados, rs1);
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return Dados; 
	}
	
	protected void associarDt( ZonaDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_ZONA"));
		Dados.setZona(rs.getString("ZONA"));
		Dados.setZonaCodigo( rs.getString("ZONA_CODIGO"));
		Dados.setId_Comarca( rs.getString("ID_COMARCA"));
		Dados.setComarca( rs.getString("COMARCA"));
		Dados.setDataInicio(Funcoes.FormatarDataHora(rs.getDateTime("DATA_INICIO")));
		Dados.setId_UsuarioServentia(rs.getString("ID_USU_SERV"));
		Dados.setValorCivel(Funcoes.FormatarDecimal(rs.getString("VALOR_CIVEL")));
		Dados.setValorCriminal(Funcoes.FormatarDecimal(rs.getString("VALOR_CRIMINAL")));
		Dados.setValorContaVinculada(Funcoes.FormatarDecimal(rs.getString("VALOR_CONTA_VINCULADA")));
		Dados.setValorSegundoGrau(Funcoes.FormatarDecimal(rs.getString("VALOR_SEGUNDO_GRAU")));
		Dados.setValorSegundoGrauContadoria(Funcoes.FormatarDecimal(rs.getString("VALOR_SEGUNDO_GRAU_CONT")));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}
	
	public String consultarDescricaoJSON(String descricao, String comarca, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql= "SELECT ID_ZONA AS ID, ZONA AS DESCRICAO1, COMARCA AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_ZONA";
		SqlFrom += " WHERE ZONA LIKE ? ";
		ps.adicionarString( descricao +"%");
		
		if( comarca != null && comarca.length() > 0 ){
			SqlFrom += " AND COMARCA LIKE ? ";
			ps.adicionarString( comarca +"%");
		}	
		SqlOrder = " ORDER BY ZONA ";		

		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);			
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
		return stTemp; 
	}

//	public List consultarDescricao(String descricao, String posicao ) throws Exception {
//
//		String Sql;
//		List liTemp = new ArrayList();
//		ResultSetTJGO rs=null;
//		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//
//		Sql= "SELECT ID_ZONA, ZONA, ID_CIDADE, CIDADE, ID_AREA FROM PROJUDI.VIEW_ZONA";		
//		if( descricao != null && descricao.length() > 0 ){
//			Sql += " WHERE ZONA LIKE ? ";
//			ps.adicionarString( descricao +"%");
//		}		
//		Sql+= " ORDER BY ZONA ";		
//
//		try{
//
//			rs = consultarPaginacao(Sql, ps, posicao);
//
//			while (rs.next()) {
//				ZonaDt obTemp = new ZonaDt();
//				obTemp.setId(rs.getString("ID_ZONA"));
//				if( rs.getString("ID_AREA").equals(String.valueOf(AreaDt.CIVEL)) ) {
//					obTemp.setZona(rs.getString("ZONA") + " - CÍVEL");
//				}
//				else {
//					obTemp.setZona(rs.getString("ZONA") + " - CRIMINAL");
//				}
//				obTemp.setCidade(rs.getString("CIDADE"));
//				obTemp.setId_Cidade(rs.getString("ID_CIDADE"));
//				liTemp.add(obTemp);
//			}
//			Sql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_ZONA";
//			if( descricao != null && descricao.length() > 0 ) Sql += " WHERE ZONA LIKE ? ";		
//			rs = consultar(Sql, ps);
//			if (rs.next()) liTemp.add(rs.getLong("QUANTIDADE"));			
//		} finally{
//				try {if (rs != null) rs.close();} catch (Exception e) {}
//			}
//			return liTemp; 
//	}
//	
//	public List consultarDescricao(String descricaoZona, String descricaoCidade, String posicao ) throws Exception {
//
//		String Sql;
//		List liTemp = new ArrayList();
//		ResultSetTJGO rs=null;
//		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//
//		Sql= "SELECT ID_ZONA, ZONA, ID_CIDADE, CIDADE, ID_AREA FROM PROJUDI.VIEW_ZONA WHERE 1=1 ";
//		
//		if( descricaoZona != null && descricaoZona.length() > 0 ){
//			Sql += " AND ZONA LIKE ?";
//			ps.adicionarString( descricaoZona +"%");
//		}
//		if( descricaoCidade != null && descricaoCidade.length() > 0 ){
//			Sql += " AND CIDADE LIKE ?";
//			ps.adicionarString( descricaoCidade +"%");
//		}		
//		Sql+= " ORDER BY ZONA ";		
//		try{
//
//			rs = consultarPaginacao(Sql, ps, posicao);
//
//			while (rs.next()) {
//				ZonaDt obTemp = new ZonaDt();
//				obTemp.setId(rs.getString("ID_ZONA"));
//				if( rs.getString("ID_AREA").equals(String.valueOf(AreaDt.CIVEL)) ) {
//					obTemp.setZona(rs.getString("ZONA") + " - CÍVEL");
//				}
//				else {
//					obTemp.setZona(rs.getString("ZONA") + " - CRIMINAL");
//				}
//				obTemp.setId_Cidade(rs.getString("ID_CIDADE"));
//				obTemp.setCidade(rs.getString("CIDADE"));
//				liTemp.add(obTemp);
//			}
//			Sql= "SELECT COUNT(*) as QUANTIDADE FROM PROJUDI.VIEW_ZONA WHERE 1=1 ";
//			
//			if( descricaoZona != null && descricaoZona.length() > 0 )
//				Sql += " AND ZONA LIKE ?";
//			if( descricaoCidade != null && descricaoCidade.length() > 0 )
//				Sql += " AND CIDADE LIKE ?";
//			
//			rs = consultar(Sql, ps);
//
//			if (rs.next()) liTemp.add(rs.getLong("QUANTIDADE"));			
//		} finally{
//				try {if (rs != null) rs.close();} catch (Exception e) {}
//			}
//			return liTemp; 
//	}
	
}
