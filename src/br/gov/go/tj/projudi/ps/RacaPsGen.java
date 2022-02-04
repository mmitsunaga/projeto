package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import java.util.List;
import java.util.ArrayList;
import br.gov.go.tj.projudi.dt.RacaDt;


public class RacaPsGen extends Persistencia {


//---------------------------------------------------------
	public RacaPsGen() {


	}



//---------------------------------------------------------
	public void inserir(RacaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.RACA ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getRaca().length()>0)) {
			 stSqlCampos+=   stVirgula + "RACA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getRaca());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_RACA",ps));
	} 

//---------------------------------------------------------
	public void alterar(RacaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.RACA SET  ";
		stSql+= "RACA = ?";		 ps.adicionarString(dados.getRaca());  

		stSql += " WHERE ID_RACA  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.RACA";
		stSql += " WHERE ID_RACA = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public RacaDt consultarId(String id_raca )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		RacaDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_RACA WHERE ID_RACA = ?";		ps.adicionarLong(id_raca); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new RacaDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( RacaDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_RACA"));
		Dados.setRaca(rs.getString("RACA"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_RACA, RACA FROM PROJUDI.VIEW_RACA WHERE RACA LIKE ?";
		stSql+= " ORDER BY RACA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				RacaDt obTemp = new RacaDt();
				obTemp.setId(rs1.getString("ID_RACA"));
				obTemp.setRaca(rs1.getString("RACA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_RACA WHERE RACA LIKE ?";
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


		stSql= "SELECT ID_RACA as id, RACA as descricao1 FROM PROJUDI.VIEW_RACA WHERE RACA LIKE ?";
		stSql+= " ORDER BY RACA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_RACA WHERE RACA LIKE ?";
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
