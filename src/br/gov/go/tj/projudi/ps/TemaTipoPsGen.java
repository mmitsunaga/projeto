package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import java.util.List;
import java.util.ArrayList;
import br.gov.go.tj.projudi.dt.TemaTipoDt;


public class TemaTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -4574471599246739876L;

	//---------------------------------------------------------
	public TemaTipoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(TemaTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.TEMA_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getTemaTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "TEMA_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getTemaTipoCodigo());  

			stVirgula=",";
		}
		if ((dados.getTemaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "TEMA_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getTemaTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_TEMA_TIPO",ps)); 
	} 

//---------------------------------------------------------
	public void alterar(TemaTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.TEMA_TIPO SET  ";
		stSql+= "TEMA_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getTemaTipoCodigo());  

		stSql+= ",TEMA_TIPO = ?";		 ps.adicionarString(dados.getTemaTipo());  

		stSql += " WHERE ID_TEMA_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.TEMA_TIPO";
		stSql += " WHERE ID_TEMA_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public TemaTipoDt consultarId(String id_tematipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		TemaTipoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_TEMA_TIPO WHERE ID_TEMA_TIPO = ?";		ps.adicionarLong(id_tematipo); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new TemaTipoDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( TemaTipoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_TEMA_TIPO"));
		Dados.setTemaTipo(rs.getString("TEMA_TIPO"));
		Dados.setTemaTipoCodigo( rs.getString("TEMA_TIPO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_TEMA_TIPO, TEMA_TIPO FROM PROJUDI.VIEW_TEMA_TIPO WHERE TEMA_TIPO LIKE ?";
		stSql+= " ORDER BY TEMA_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				TemaTipoDt obTemp = new TemaTipoDt();
				obTemp.setId(rs1.getString("ID_TEMA_TIPO"));
				obTemp.setTemaTipo(rs1.getString("TEMA_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_TEMA_TIPO WHERE TEMA_TIPO LIKE ?";
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


		stSql= "SELECT ID_TEMA_TIPO as id, TEMA_TIPO as descricao1 FROM PROJUDI.VIEW_TEMA_TIPO WHERE TEMA_TIPO LIKE ?";
		stSql+= " ORDER BY TEMA_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_TEMA_TIPO WHERE TEMA_TIPO LIKE ?";
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
