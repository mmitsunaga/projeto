package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import br.gov.go.tj.projudi.dt.AssuntoDt;


public class AssuntoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -785869514969353489L;

	//---------------------------------------------------------
	public AssuntoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(AssuntoDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.assunto ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getAssunto().length()>0)) {
			 stSqlCampos+=   stVirgula + "ASSUNTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getAssunto());  

			stVirgula=",";
		}
		if ((dados.getAssuntoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ASSUNTO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getAssuntoCodigo());  

			stVirgula=",";
		}
		if ((dados.getId_AssuntoPai().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ASSUNTO_PAI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_AssuntoPai());  

			stVirgula=",";
		}
		if ((dados.getIsAtivo().length()>0)) {
			 stSqlCampos+=   stVirgula + "IS_ATIVO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getIsAtivo());  

			stVirgula=",";
		}
		if ((dados.getDispositivoLegal().length()>0)) {
			 stSqlCampos+=   stVirgula + "DISPOSITIVO_LEGAL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getDispositivoLegal());  

			stVirgula=",";
		}
		if ((dados.getArtigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ARTIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getArtigo());  

			stVirgula=",";
		}
		if ((dados.getId_CnjAssunto().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_CNJ_ASSUNTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_CnjAssunto());  

			stVirgula=",";
		}
		if ((dados.getSigla().length()>0)) {
			 stSqlCampos+=   stVirgula + "SIGLA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getSigla());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


		try {
			dados.setId(executarInsert(stSql,"ID_ASSUNTO",ps));


		}catch(Exception e){
			System.out.println("....psinserir: " + e.getMessage());
			throw new Exception(" <{Erro:.....}> AssuntoPsGen.inserir() " + e.getMessage() );
			} 
	} 

//---------------------------------------------------------
	public void alterar(AssuntoDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.assunto SET  ";
		stSql+= "ASSUNTO = ?";		 ps.adicionarString(dados.getAssunto());  

		stSql+= ",ASSUNTO_CODIGO = ?";		 ps.adicionarLong(dados.getAssuntoCodigo());  

		stSql+= ",ID_ASSUNTO_PAI = ?";		 ps.adicionarLong(dados.getId_AssuntoPai());  

		stSql+= ",IS_ATIVO = ?";		 ps.adicionarString(dados.getIsAtivo());  

		stSql+= ",DISPOSITIVO_LEGAL = ?";		 ps.adicionarString(dados.getDispositivoLegal());  

		stSql+= ",ARTIGO = ?";		 ps.adicionarString(dados.getArtigo());  

		stSql+= ",ID_CNJ_ASSUNTO = ?";		 ps.adicionarLong(dados.getId_CnjAssunto());  

		stSql+= ",SIGLA = ?";		 ps.adicionarString(dados.getSigla());  

		stSql += " WHERE ID_ASSUNTO  = ? "; 		ps.adicionarLong(dados.getId()); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.assunto";
		stSql += " WHERE ID_ASSUNTO = ?";		ps.adicionarLong(chave); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public AssuntoDt consultarId(String id_assunto )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		AssuntoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_assunto WHERE ID_ASSUNTO = ?";		ps.adicionarLong(id_assunto); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new AssuntoDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( AssuntoDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_ASSUNTO"));
			Dados.setAssunto(rs.getString("ASSUNTO"));
			Dados.setAssuntoCodigo( rs.getString("ASSUNTO_CODIGO"));
			Dados.setId_AssuntoPai( rs.getString("ID_ASSUNTO_PAI"));
			Dados.setAssuntoPai( rs.getString("ASSUNTO_PAI"));
			Dados.setIsAtivo( rs.getString("IS_ATIVO"));
			Dados.setDispositivoLegal( rs.getString("DISPOSITIVO_LEGAL"));
			Dados.setArtigo( rs.getString("ARTIGO"));
			Dados.setId_CnjAssunto( rs.getString("ID_CNJ_ASSUNTO"));
			Dados.setSigla( rs.getString("SIGLA"));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql,stSqlOrder, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_ASSUNTO, ASSUNTO, ASSUNTO_CODIGO, ID_CNJ_ASSUNTO ";
		stSqlFrom= " FROM PROJUDI.VIEW_assunto WHERE ASSUNTO LIKE ?";
		stSqlOrder = " ORDER BY ASSUNTO ";
		ps.adicionarString(descricao+"%"); 

		try{
			
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);

			while (rs1.next()) {
				AssuntoDt obTemp = new AssuntoDt();
				obTemp.setId(rs1.getString("ID_ASSUNTO"));
				obTemp.setAssunto(rs1.getString("ASSUNTO"));
				obTemp.setAssuntoCodigo(rs1.getString("ASSUNTO_CODIGO"));
				obTemp.setId_CnjAssunto(rs1.getString("ID_CNJ_ASSUNTO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom,ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
			return liTemp; 
	}

//---------------------------------------------------------
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql, stSqlFrom, stSqlOrder;
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_ASSUNTO as id, ASSUNTO as descricao1 ";
		stSqlFrom= " FROM PROJUDI.VIEW_assunto WHERE ASSUNTO LIKE ?";
		ps.adicionarString(descricao+"%"); 

		stSqlOrder= " ORDER BY ASSUNTO ";
		try{


			rs1 = consultarPaginacao(stSql+stSqlFrom+stSqlOrder, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql+stSqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
			return stTemp; 
	}

} 
