package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import br.gov.go.tj.projudi.dt.EscalaTipoDt;


public class EscalaTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -2672955227940515387L;

	//---------------------------------------------------------
	public EscalaTipoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(EscalaTipoDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.ESCALA_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getEscalaTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ESCALA_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getEscalaTipoCodigo());  

			stVirgula=",";
		}
		if ((dados.getEscalaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ESCALA_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getEscalaTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


		try {
			dados.setId(executarInsert(stSql,"ID_ESCALA_TIPO",ps));


		}catch(Exception e){
			System.out.println("....psinserir: " + e.getMessage());
			throw new Exception(" <{Erro:.....}> EscalaTipoPsGen.inserir() " + e.getMessage() );
			} 
	} 

//---------------------------------------------------------
	public void alterar(EscalaTipoDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.ESCALA_TIPO SET  ";
		stSql+= "ESCALA_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getEscalaTipoCodigo());  

		stSql+= ",ESCALA_TIPO = ?";		 ps.adicionarString(dados.getEscalaTipo());  

		stSql += " WHERE ID_ESCALA_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.ESCALA_TIPO";
		stSql += " WHERE ID_ESCALA_TIPO = ?";		ps.adicionarLong(chave); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public EscalaTipoDt consultarId(String id_escalatipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EscalaTipoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_ESCALA_TIPO WHERE ID_ESCALA_TIPO = ?";		ps.adicionarLong(id_escalatipo); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new EscalaTipoDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( EscalaTipoDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_ESCALA_TIPO"));
			Dados.setEscalaTipo(rs.getString("ESCALA_TIPO"));
			Dados.setEscalaTipoCodigo( rs.getString("ESCALA_TIPO_CODIGO"));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql,stSqlOrder, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_ESCALA_TIPO, ESCALA_TIPO ";
		stSqlFrom= " FROM PROJUDI.VIEW_ESCALA_TIPO WHERE ESCALA_TIPO LIKE ?";
		stSqlOrder = " ORDER BY ESCALA_TIPO ";
		ps.adicionarString(descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);


			while (rs1.next()) {
				EscalaTipoDt obTemp = new EscalaTipoDt();
				obTemp.setId(rs1.getString("ID_ESCALA_TIPO"));
				obTemp.setEscalaTipo(rs1.getString("ESCALA_TIPO"));
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


		stSql= "SELECT ID_ESCALA_TIPO as id, ESCALA_TIPO as descricao1 ";
		stSqlFrom= " FROM PROJUDI.VIEW_ESCALA_TIPO WHERE ESCALA_TIPO LIKE ?";
		ps.adicionarString(descricao+"%"); 

		stSqlOrder= " ORDER BY ESCALA_TIPO ";
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
