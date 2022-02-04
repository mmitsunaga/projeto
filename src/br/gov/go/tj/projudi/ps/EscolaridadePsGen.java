package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EscolaridadeDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class EscolaridadePsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 7400854212653034154L;

	//---------------------------------------------------------
	public EscolaridadePsGen() {


	}



//---------------------------------------------------------
	public void inserir(EscolaridadeDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.ESCOLARIDADE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getEscolaridade().length()>0)) {
			 stSqlCampos+=   stVirgula + "ESCOLARIDADE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getEscolaridade());  

			stVirgula=",";
		}
		if ((dados.getEscolaridadeCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ESCOLARIDADE_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getEscolaridadeCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_ESCOLARIDADE",ps));
	} 

//---------------------------------------------------------
	public void alterar(EscolaridadeDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.ESCOLARIDADE SET  ";
		stSql+= "ESCOLARIDADE = ?";		 ps.adicionarString(dados.getEscolaridade());  

		stSql+= ",ESCOLARIDADE_CODIGO = ?";		 ps.adicionarLong(dados.getEscolaridadeCodigo());  

		stSql += " WHERE ID_ESCOLARIDADE  = ? "; 		ps.adicionarLong(dados.getId()); 


		executarUpdateDelete(stSql,ps);	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.ESCOLARIDADE";
		stSql += " WHERE ID_ESCOLARIDADE = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public EscolaridadeDt consultarId(String id_escolaridade )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EscolaridadeDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_ESCOLARIDADE WHERE ID_ESCOLARIDADE = ?";		ps.adicionarLong(id_escolaridade); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new EscolaridadeDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( EscolaridadeDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_ESCOLARIDADE"));
		Dados.setEscolaridade(rs.getString("ESCOLARIDADE"));
		Dados.setEscolaridadeCodigo( rs.getString("ESCOLARIDADE_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_ESCOLARIDADE, ESCOLARIDADE FROM PROJUDI.VIEW_ESCOLARIDADE WHERE ESCOLARIDADE LIKE ?";
		stSql+= " ORDER BY ESCOLARIDADE ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				EscolaridadeDt obTemp = new EscolaridadeDt();
				obTemp.setId(rs1.getString("ID_ESCOLARIDADE"));
				obTemp.setEscolaridade(rs1.getString("ESCOLARIDADE"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_ESCOLARIDADE WHERE ESCOLARIDADE LIKE ?";
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
//	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
//
//		String stSql="";
//		String stTemp="";
//		int qtdeColunas = 1;
//		ResultSetTJGO rs1=null;
//		ResultSetTJGO rs2=null;
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//
//
//		stSql= "SELECT ID_ESCOLARIDADE as id, ESCOLARIDADE as descricao1 FROM PROJUDI.VIEW_ESCOLARIDADE WHERE ESCOLARIDADE LIKE ?";
//		stSql+= " ORDER BY "+ordenacao;
//		ps.adicionarString("%"+descricao+"%"); 
//
//		try{
//
//
//			rs1 = consultarPaginacao(stSql, ps, posicao, quantidadeRegistros);
//			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_ESCOLARIDADE WHERE ESCOLARIDADE LIKE ?";
//			rs2 = consultar(stSql,ps);
//			rs2.next();
//			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
//
//		} finally {
//			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
//			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
//		}
//		return stTemp; 
//	}

} 
