package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PrisaoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class PrisaoTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -1740161928026033794L;

	//---------------------------------------------------------
	public PrisaoTipoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(PrisaoTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.PRISAO_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getPrisaoTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PRISAO_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPrisaoTipoCodigo());  

			stVirgula=",";
		}
		if ((dados.getPrisaoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PRISAO_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPrisaoTipo());  

			stVirgula=",";
		}
		if ((dados.getCodigoTepmp().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODIGO_TEPMP " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCodigoTepmp());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_PRISAO_TIPO",ps)); 
	} 

//---------------------------------------------------------
	public void alterar(PrisaoTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.PRISAO_TIPO SET  ";
		stSql+= "PRISAO_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getPrisaoTipoCodigo());  

		stSql+= ",PRISAO_TIPO = ?";		 ps.adicionarString(dados.getPrisaoTipo());  

		stSql+= ",CODIGO_TEPMP = ?";		 ps.adicionarString(dados.getCodigoTepmp());  

		stSql += " WHERE ID_PRISAO_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 


		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.PRISAO_TIPO";
		stSql += " WHERE ID_PRISAO_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public PrisaoTipoDt consultarId(String id_prisaotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PrisaoTipoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_PRISAO_TIPO WHERE ID_PRISAO_TIPO = ?";		ps.adicionarLong(id_prisaotipo); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PrisaoTipoDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( PrisaoTipoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PRISAO_TIPO"));
		Dados.setPrisaoTipo(rs.getString("PRISAO_TIPO"));
		Dados.setPrisaoTipoCodigo( rs.getString("PRISAO_TIPO_CODIGO"));
		Dados.setCodigoTepmp( rs.getString("CODIGO_TEPMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_PRISAO_TIPO, PRISAO_TIPO FROM PROJUDI.VIEW_PRISAO_TIPO WHERE PRISAO_TIPO LIKE ?";
		stSql+= " ORDER BY PRISAO_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				PrisaoTipoDt obTemp = new PrisaoTipoDt();
				obTemp.setId(rs1.getString("ID_PRISAO_TIPO"));
				obTemp.setPrisaoTipo(rs1.getString("PRISAO_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_PRISAO_TIPO WHERE PRISAO_TIPO LIKE ?";
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


		stSql= "SELECT ID_PRISAO_TIPO as id, PRISAO_TIPO as descricao1 FROM PROJUDI.VIEW_PRISAO_TIPO WHERE PRISAO_TIPO LIKE ?";
		stSql+= " ORDER BY PRISAO_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_PRISAO_TIPO WHERE PRISAO_TIPO LIKE ?";
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
