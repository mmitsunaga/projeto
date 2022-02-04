package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.TemaSituacaoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class TemaSituacaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 2400886997784208316L;

	//---------------------------------------------------------
	public TemaSituacaoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(TemaSituacaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.TEMA_SITUACAO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getTemaSituacaoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "TEMA_SITUACAO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getTemaSituacaoCodigo());  

			stVirgula=",";
		}
		if ((dados.getTemaSituacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "TEMA_SITUACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getTemaSituacao());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_TEMA_SITUACAO",ps)); 
	} 

//---------------------------------------------------------
	public void alterar(TemaSituacaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.TEMA_SITUACAO SET  ";
		stSql+= "TEMA_SITUACAO_CODIGO = ?";		 ps.adicionarLong(dados.getTemaSituacaoCodigo());  

		stSql+= ",TEMA_SITUACAO = ?";		 ps.adicionarString(dados.getTemaSituacao());  

		stSql += " WHERE ID_TEMA_SITUACAO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.TEMA_SITUACAO";
		stSql += " WHERE ID_TEMA_SITUACAO = ?";		ps.adicionarLong(chave); 


		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public TemaSituacaoDt consultarId(String id_temasituacao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		TemaSituacaoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_TEMA_SITUACAO WHERE ID_TEMA_SITUACAO = ?";		ps.adicionarLong(id_temasituacao); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new TemaSituacaoDt();
				associarDt(Dados, rs1);
			}

		} finally { 
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( TemaSituacaoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_TEMA_SITUACAO"));
		Dados.setTemaSituacao(rs.getString("TEMA_SITUACAO"));
		Dados.setTemaSituacaoCodigo( rs.getString("TEMA_SITUACAO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_TEMA_SITUACAO, TEMA_SITUACAO FROM PROJUDI.VIEW_TEMA_SITUACAO WHERE TEMA_SITUACAO LIKE ?";
		stSql+= " ORDER BY TEMA_SITUACAO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				TemaSituacaoDt obTemp = new TemaSituacaoDt();
				obTemp.setId(rs1.getString("ID_TEMA_SITUACAO"));
				obTemp.setTemaSituacao(rs1.getString("TEMA_SITUACAO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_TEMA_SITUACAO WHERE TEMA_SITUACAO LIKE ?";
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


		stSql= "SELECT ID_TEMA_SITUACAO as id, TEMA_SITUACAO as descricao1 FROM PROJUDI.VIEW_TEMA_SITUACAO WHERE TEMA_SITUACAO LIKE ?";
		stSql+= " ORDER BY TEMA_SITUACAO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_TEMA_SITUACAO WHERE TEMA_SITUACAO LIKE ?";
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
