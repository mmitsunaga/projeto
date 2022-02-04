package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.ServentiaAreaDistribuicaoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;

//---------------------------------------------------------
public class ServentiaAreaDistribuicaoPs extends ServentiaAreaDistribuicaoPsGen{

	public ServentiaAreaDistribuicaoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
     * 
     */
    private static final long serialVersionUID = 7753637204951126198L;

  //---------------------------------------------------------
  	public void inserir(ServentiaAreaDistribuicaoDt dados ) throws Exception {

  		String stSqlCampos="";
  		String stSqlValores="";
  		String stSql="";
  		String stVirgula="";
  		PreparedStatementTJGO ps = new PreparedStatementTJGO();
  		////System.out.println("....psServAreaDistinserir()");
  		stSqlCampos= "INSERT INTO PROJUDI.SERV_AREA_DIST ("; 

  		stSqlValores +=  " Values (";
   
  		if ((dados.getServAreaDist().length()>0)) {
  			 stSqlCampos+=   stVirgula + "SERV_AREA_DIST " ;
  			 stSqlValores+=   stVirgula + "? " ;
  			 ps.adicionarString(dados.getServAreaDist());  

  			stVirgula=",";
  		}
  		if ((dados.getId_Serv().length()>0)) {
  			 stSqlCampos+=   stVirgula + "ID_SERV " ;
  			 stSqlValores+=   stVirgula + "? " ;
  			 ps.adicionarLong(dados.getId_Serv());  

  			stVirgula=",";
  		}
  		if ((dados.getId_AreaDist().length()>0)) {
  			 stSqlCampos+=   stVirgula + "ID_AREA_DIST " ;
  			 stSqlValores+=   stVirgula + "? " ;
  			 ps.adicionarLong(dados.getId_AreaDist());  

  			stVirgula=",";
  		}
  		
  		if ((dados.getProbabilidade().length()>0)) {
  			 stSqlCampos+=   stVirgula + "PROBABILIDADE " ;
  			 stSqlValores+=   stVirgula + "? " ;
  			 ps.adicionarDecimal(dados.getProbabilidade());  

  			stVirgula=",";
  		}
  		
  		stSqlCampos+= ")";
  		stSqlValores+= ")";
  		stSql+= stSqlCampos + stSqlValores; 
  		////System.out.println("....Sql " + stSql);

  		
  			dados.setId(executarInsert(stSql,"ID_SERV_AREA_DIST",ps));
  			////System.out.println("....Execução OK"  );

  		 
  	} 

}
