package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import java.util.List;
import java.util.ArrayList;
import br.gov.go.tj.projudi.dt.TemaAssuntoDt;


public class TemaAssuntoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -2739177918825745890L;

	//---------------------------------------------------------
	public TemaAssuntoPsGen() {


	}



//---------------------------------------------------------
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
			 stSqlCampos+=   stVirgula + "ASSUNTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getAssunto());  

			stVirgula=",";
		}
		if ((dados.getTemaCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "TEMA_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getTemaCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


		dados.setId(executarInsert(stSql,"ID_TEMA_ASSUNTO",ps)); 
	} 

//---------------------------------------------------------
	public void alterar(TemaAssuntoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.TEMA_ASSUNTO SET  ";
		stSql+= "ID_TEMA = ?";		 ps.adicionarLong(dados.getId_Tema());  

		stSql+= ",ASSUNTO = ?";		 ps.adicionarString(dados.getAssunto());  

		stSql+= ",TEMA_CODIGO = ?";		 ps.adicionarLong(dados.getTemaCodigo());  

		stSql += " WHERE ID_TEMA_ASSUNTO  = ? "; 		ps.adicionarLong(dados.getId()); 


		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.TEMA_ASSUNTO";
		stSql += " WHERE ID_TEMA_ASSUNTO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public TemaAssuntoDt consultarId(String id_temaassunto )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		TemaAssuntoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_TEMA_ASSUNTO WHERE ID_TEMA_ASSUNTO = ?";		ps.adicionarLong(id_temaassunto); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new TemaAssuntoDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( TemaAssuntoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_TEMA_ASSUNTO"));
		Dados.setAssunto(rs.getString("ASSUNTO"));
		Dados.setId_Tema( rs.getString("ID_TEMA"));
		Dados.setId_Assunto( rs.getString("ID_ASSUNTO"));
		Dados.setTemaCodigo( rs.getString("TEMA_CODIGO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_TEMA_ASSUNTO, ASSUNTO FROM PROJUDI.VIEW_TEMA_ASSUNTO WHERE ASSUNTO LIKE ?";
		stSql+= " ORDER BY ASSUNTO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				TemaAssuntoDt obTemp = new TemaAssuntoDt();
				obTemp.setId(rs1.getString("ID_TEMA_ASSUNTO"));
				obTemp.setAssunto(rs1.getString("ASSUNTO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_TEMA_ASSUNTO WHERE ASSUNTO LIKE ?";
			rs2 = consultar(stSql,ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}

		} finally { 
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

//---------------------------------------------------------
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_TEMA_ASSUNTO as id, ASSUNTO as descricao1 FROM PROJUDI.VIEW_TEMA_ASSUNTO WHERE ASSUNTO LIKE ?";
		stSql+= " ORDER BY ASSUNTO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_TEMA_ASSUNTO WHERE ASSUNTO LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}

} 
