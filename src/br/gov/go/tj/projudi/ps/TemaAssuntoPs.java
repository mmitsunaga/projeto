package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.TemaAssuntoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class TemaAssuntoPs extends TemaAssuntoPsGen{

	private static final long serialVersionUID = 2414788564172690164L;
	
	public TemaAssuntoPs(Connection conexao){
		Conexao = conexao;
	}

	public void inserir(TemaAssuntoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.TEMA_ASSUNTO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Tema().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_TEMA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Tema());  

			stVirgula=",";
		}
		if ((dados.getAssunto().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ASSUNTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Assunto());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		
		dados.setId(executarInsert(stSql,"ID_TEMA_ASSUNTO",ps)); 
	} 
	
	public void alterar(TemaAssuntoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.TEMA_ASSUNTO SET ";
		stSql+= "ID_TEMA = ?";		 
		ps.adicionarLong(dados.getId_Tema());  
		stSql+= ",ID_ASSUNTO = ?";		 
		ps.adicionarLong(dados.getId_Assunto());  
		stSql += " WHERE ID_TEMA_ASSUNTO  = ? "; 		
		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	} 
	
	public List consultarAssuntosId(String idTema) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_TEMA_ASSUNTO, ASSUNTO, ID_TEMA, ID_ASSUNTO, ASSUNTO_CODIGO FROM PROJUDI.VIEW_TEMA_ASSUNTO WHERE ID_TEMA = ?";
		stSql+= " ORDER BY ID_TEMA_ASSUNTO ";
		ps.adicionarLong(idTema); 

		try{
			rs1 = consultar(stSql, ps);

			while (rs1.next()) {
				TemaAssuntoDt obTemp = new TemaAssuntoDt();
				obTemp.setId(rs1.getString("ID_TEMA_ASSUNTO"));
				obTemp.setId_Assunto(rs1.getString("ID_ASSUNTO"));
				obTemp.setAssunto(rs1.getString("ASSUNTO"));
				obTemp.setId_Tema(rs1.getString("ID_TEMA"));
				obTemp.setAssuntoCodigo(rs1.getString("ASSUNTO_CODIGO"));
				liTemp.add(obTemp);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}
	
}
