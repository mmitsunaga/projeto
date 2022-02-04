package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ObjetoSubtipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ObjetoSubtipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -3101247577615225771L;

//---------------------------------------------------------
	public ObjetoSubtipoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(ObjetoSubtipoDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO objeto_subtipo ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getObjetoSubtipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "OBJETO_SUBTIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getObjetoSubtipo());  

			stVirgula=",";
		}
		if ((dados.getId_ObjetoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_OBJETO_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ObjetoTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


			dados.setId(executarInsert(stSql,"ID_OBJETO_SUBTIPO",ps));


	} 

//---------------------------------------------------------
	public void alterar(ObjetoSubtipoDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE objeto_subtipo SET  ";
		stSql+= "OBJETO_SUBTIPO = ?";		 ps.adicionarString(dados.getObjetoSubtipo());  

		stSql+= ",ID_OBJETO_TIPO = ?";		 ps.adicionarLong(dados.getId_ObjetoTipo());  

		stSql += " WHERE ID_OBJETO_SUBTIPO  = ? "; 		ps.adicionarLong(dados.getId()); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM objeto_subtipo";
		stSql += " WHERE ID_OBJETO_SUBTIPO = ?";		ps.adicionarLong(chave); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public ObjetoSubtipoDt consultarId(String id_objetosubtipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ObjetoSubtipoDt Dados=null;


		stSql= "SELECT * FROM view_objeto_subtipo WHERE ID_OBJETO_SUBTIPO = ?";		ps.adicionarLong(id_objetosubtipo); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ObjetoSubtipoDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( ObjetoSubtipoDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_OBJETO_SUBTIPO"));
			Dados.setObjetoSubtipo(rs.getString("OBJETO_SUBTIPO"));
			Dados.setId_ObjetoTipo( rs.getString("ID_OBJETO_TIPO"));
			Dados.setObjetoTipo( rs.getString("OBJETO_TIPO"));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql,stSqlOrder, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_OBJETO_SUBTIPO, OBJETO_SUBTIPO ";
		stSqlFrom= " FROM view_objeto_subtipo WHERE OBJETO_SUBTIPO LIKE ?";
		stSqlOrder = " ORDER BY OBJETO_SUBTIPO ";
		ps.adicionarString(descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);


			while (rs1.next()) {
				ObjetoSubtipoDt obTemp = new ObjetoSubtipoDt();
				obTemp.setId(rs1.getString("ID_OBJETO_SUBTIPO"));
				obTemp.setObjetoSubtipo(rs1.getString("OBJETO_SUBTIPO"));
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
		int qtdeColunas = 2;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_OBJETO_SUBTIPO as id, OBJETO_SUBTIPO as descricao1, OBJETO_TIPO as descricao2  ";
		stSqlFrom= " FROM view_objeto_subtipo WHERE OBJETO_SUBTIPO LIKE ?";
		ps.adicionarString(descricao+"%"); 

		stSqlOrder= " ORDER BY OBJETO_SUBTIPO ";
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
