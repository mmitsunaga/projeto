package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.ZonaBairroRegiaoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ZonaBairroRegiaoPs extends Persistencia {
	
	
	/**
     * 
     */
    private static final long serialVersionUID = -2934356159277822652L;
    
    public ZonaBairroRegiaoPs(Connection conexao){
    	Conexao = conexao;
	}

    public void inserir(ZonaBairroRegiaoDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSqlCampos= "INSERT INTO PROJUDI.ZONA_BAIRRO_REGIAO ("; 

		stSqlValores +=  " Values (";
		
		if (dados.getId_Zona() != null && dados.getId_Zona().length()>0) {
			 stSqlCampos+=   stVirgula + "ID_ZONA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Zona());  

			 stVirgula=",";
		}		
		
		if (dados.getId_Bairro() != null && dados.getId_Bairro().length()>0) {
			 stSqlCampos+=   stVirgula + "ID_BAIRRO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Bairro());  

			 stVirgula=",";
		}
		
		if (dados.getId_Regiao() != null && dados.getId_Regiao().length()>0) {
			 stSqlCampos+=   stVirgula + "ID_REGIAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Regiao());  

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

		dados.setId(executarInsert(stSql,"ID_ZONA_BAIRRO_REGIAO",ps)); 
	} 
	
	public void alterar(ZonaBairroRegiaoDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";
		String stVirgula="";

		stSql= "UPDATE PROJUDI.ZONA_BAIRRO_REGIAO SET  ";
		
		if ((dados.getId_Zona() != null && dados.getId_Zona().length()>0)) {
			stSql+= stVirgula + " ID_ZONA = ?";		 
			ps.adicionarLong(dados.getId_Zona());
			
			stVirgula=",";
		}	
		
		if ((dados.getId_Bairro() != null && dados.getId_Bairro().length()>0)) {
			stSql+= stVirgula + " ID_BAIRRO = ?";		 
			ps.adicionarLong(dados.getId_Bairro());
			
			stVirgula=",";
		}
		
		if ((dados.getId_Regiao() != null && dados.getId_Regiao().length()>0)) {
			stSql+= stVirgula + " ID_REGIAO = ?";		 
			ps.adicionarLong(dados.getId_Regiao());
			
			stVirgula=",";
		}
		
		if ((dados.getCodigoTemp() != null && dados.getCodigoTemp().length()>0)) {
			stSql+= stVirgula + " CODIGO_TEMP = ?";		 
			ps.adicionarBigDecimal(dados.getCodigoTemp());
			
			stVirgula=",";
		}
		
		stSql += " WHERE ID_ZONA_BAIRRO_REGIAO  = ? "; 		
		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	} 
	
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.ZONA_BAIRRO_REGIAO";
		stSql += " WHERE ID_ZONA_BAIRRO_REGIAO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	}
	
	public ZonaBairroRegiaoDt consultarId(String id_zonaBairroRegiao)  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ZonaBairroRegiaoDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_ZONA_BAIRRO_REGIAO WHERE ID_ZONA_BAIRRO_REGIAO = ?";		ps.adicionarLong(id_zonaBairroRegiao); 

		try {
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ZonaBairroRegiaoDt();
				associarDt(Dados, rs1);
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return Dados; 
	}
	
	public ZonaBairroRegiaoDt consultarIdBairro(String id_bairro)  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ZonaBairroRegiaoDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_ZONA_BAIRRO_REGIAO WHERE ID_BAIRRO = ?";		ps.adicionarLong(id_bairro); 

		try {
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ZonaBairroRegiaoDt();
				associarDt(Dados, rs1);
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return Dados; 
	}
	
	protected void associarDt(ZonaBairroRegiaoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_ZONA_BAIRRO_REGIAO"));
		Dados.setId_Zona(rs.getString("ID_ZONA"));
		Dados.setZona(rs.getString("ZONA"));
		Dados.setId_Cidade( rs.getString("ID_CIDADE"));
		Dados.setCidade( rs.getString("CIDADE"));
		Dados.setId_Bairro( rs.getString("ID_BAIRRO"));
		Dados.setBairro( rs.getString("BAIRRO"));
		Dados.setId_Regiao( rs.getString("ID_REGIAO"));
		Dados.setRegiao( rs.getString("REGIAO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}
	
	public String consultarDescricaoJSON(String descricao, String cidade, String bairro, String regiao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 3;

		Sql= "SELECT ID_ZONA_BAIRRO_REGIAO AS ID, ZONA AS DESCRICAO1, CIDADE AS DESCRICAO2, BAIRRO AS DESCRICAO3, REGIAO AS DESCRICAO4";
		SqlFrom = " FROM PROJUDI.VIEW_ZONA_BAIRRO_REGIAO";
		SqlFrom += " WHERE ZONA LIKE ? ";
		ps.adicionarString("%"+ descricao +"%");
		
		if( cidade != null && cidade.length() > 0 ){
			SqlFrom += " AND CIDADE LIKE ? ";
			ps.adicionarString(cidade + "%");
		}
		if( bairro != null && bairro.length() > 0 ){
			SqlFrom += " AND BAIRRO LIKE ? ";
			ps.adicionarString( bairro +"%");
		}
		if( regiao != null && regiao.length() > 0 ){
			SqlFrom += " AND REGIAO LIKE ? ";
			ps.adicionarString( regiao +"%");
		}
		SqlOrder = " ORDER BY CIDADE, BAIRRO, ZONA, REGIAO ";		

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

	public String consultarId_Regiao(String id_bairro) throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String id_regiao = null;

		stSql= "SELECT ID_REGIAO FROM PROJUDI.ZONA_BAIRRO_REGIAO WHERE ID_BAIRRO = ?";	ps.adicionarLong(id_bairro); 

		try {
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {				
				id_regiao = rs1.getString("ID_REGIAO");
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return id_regiao; 
	}
}
