package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import java.util.List;
import java.util.ArrayList;
import br.gov.go.tj.projudi.dt.PonteiroLogTipoDt;


public class PonteiroLogTipoPsGen extends Persistencia {


//---------------------------------------------------------
	public PonteiroLogTipoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(PonteiroLogTipoDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.PONTEIRO_LOG_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getPonteiroLogTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PONTEIRO_LOG_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPonteiroLogTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


		try {
			dados.setId(executarInsert(stSql,"ID_PONTEIRO_LOG_TIPO",ps));


		}catch(Exception e){
			System.out.println("....psinserir: " + e.getMessage());
			throw new Exception(" <{Erro:.....}> PonteiroLogTipoPsGen.inserir() " + e.getMessage() );
			} 
	} 

//---------------------------------------------------------
	public void alterar(PonteiroLogTipoDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.PONTEIRO_LOG_TIPO SET  ";
		stSql+= "PONTEIRO_LOG_TIPO = ?";		 ps.adicionarString(dados.getPonteiroLogTipo());  

		stSql += " WHERE ID_PONTEIRO_LOG_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.PONTEIRO_LOG_TIPO";
		stSql += " WHERE ID_PONTEIRO_LOG_TIPO = ?";		ps.adicionarLong(chave); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public PonteiroLogTipoDt consultarId(String id_ponteirologtipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PonteiroLogTipoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_PONTEIRO_LOG_TIPO WHERE ID_PONTEIRO_LOG_TIPO = ?";		ps.adicionarLong(id_ponteirologtipo); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PonteiroLogTipoDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( PonteiroLogTipoDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_PONTEIRO_LOG_TIPO"));
			Dados.setPonteiroLogTipo(rs.getString("PONTEIRO_LOG_TIPO"));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_PONTEIRO_LOG_TIPO, PONTEIRO_LOG_TIPO FROM PROJUDI.VIEW_PONTEIRO_LOG_TIPO WHERE PONTEIRO_LOG_TIPO LIKE ?";
		stSql+= " ORDER BY PONTEIRO_LOG_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				PonteiroLogTipoDt obTemp = new PonteiroLogTipoDt();
				obTemp.setId(rs1.getString("ID_PONTEIRO_LOG_TIPO"));
				obTemp.setPonteiroLogTipo(rs1.getString("PONTEIRO_LOG_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_PONTEIRO_LOG_TIPO WHERE PONTEIRO_LOG_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);

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

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_PONTEIRO_LOG_TIPO as id, PONTEIRO_LOG_TIPO as descricao1 FROM PONTEIRO_LOG_TIPO WHERE PONTEIRO_LOG_TIPO LIKE ?";
		stSql+= " ORDER BY PONTEIRO_LOG_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade FROM PONTEIRO_LOG_TIPO WHERE PONTEIRO_LOG_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
			return stTemp; 
	}

} 
