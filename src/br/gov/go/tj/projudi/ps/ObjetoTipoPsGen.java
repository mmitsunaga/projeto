package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ObjetoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ObjetoTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 2460362404639650869L;

	//---------------------------------------------------------
	public ObjetoTipoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(ObjetoTipoDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO objeto_tipo ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getObjetoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "OBJETO_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getObjetoTipo());  

			stVirgula=",";
		}
		if ((dados.getCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


		dados.setId(executarInsert(stSql,"ID_OBJETO_TIPO",ps));


	} 

//---------------------------------------------------------
	public void alterar(ObjetoTipoDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE objeto_tipo SET  ";
		stSql+= "OBJETO_TIPO = ?";		 ps.adicionarString(dados.getObjetoTipo());  

		stSql+= ",CODIGO = ?";		 ps.adicionarLong(dados.getCodigo());  

		stSql += " WHERE ID_OBJETO_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM objeto_tipo";
		stSql += " WHERE ID_OBJETO_TIPO = ?";		ps.adicionarLong(chave); 


		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ObjetoTipoDt consultarId(String id_objetotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ObjetoTipoDt Dados=null;


		stSql= "SELECT * FROM view_objeto_tipo WHERE ID_OBJETO_TIPO = ?";		ps.adicionarLong(id_objetotipo); 


		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ObjetoTipoDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( ObjetoTipoDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_OBJETO_TIPO"));
			Dados.setObjetoTipo(rs.getString("OBJETO_TIPO"));
			Dados.setCodigo( rs.getString("CODIGO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql,stSqlOrder, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_OBJETO_TIPO, OBJETO_TIPO ";
		stSqlFrom= " FROM view_objeto_tipo WHERE OBJETO_TIPO LIKE ?";
		stSqlOrder = " ORDER BY OBJETO_TIPO ";
		ps.adicionarString(descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);


			while (rs1.next()) {
				ObjetoTipoDt obTemp = new ObjetoTipoDt();
				obTemp.setId(rs1.getString("ID_OBJETO_TIPO"));
				obTemp.setObjetoTipo(rs1.getString("OBJETO_TIPO"));
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


		stSql= "SELECT ID_OBJETO_TIPO as id, OBJETO_TIPO as descricao1 ";
		stSqlFrom= " FROM view_objeto_tipo WHERE OBJETO_TIPO LIKE ?";
		ps.adicionarString(descricao+"%"); 

		stSqlOrder= " ORDER BY OBJETO_TIPO ";
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
