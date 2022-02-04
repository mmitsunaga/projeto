package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MovimentacaoTipoMovimentacaoTipoClasseDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class MovimentacaoTipoMovimentacaoTipoClassePsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -4861242341054095073L;

	//---------------------------------------------------------
	public MovimentacaoTipoMovimentacaoTipoClassePsGen() {


	}



//---------------------------------------------------------
	public void inserir(MovimentacaoTipoMovimentacaoTipoClasseDt dados ) throws Exception{

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.MOVI_TIPO_MOVI_TIPO_CLASS ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getMovimentacaoTipoMovimentacaoTipoClasse().length()>0)) {
			 stSqlCampos+=   stVirgula + "MOVI_TIPO_MOVI_TIPO_CLASSE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getMovimentacaoTipoMovimentacaoTipoClasse());  

			stVirgula=",";
		}
		if ((dados.getId_MovimentacaoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_MOVI_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_MovimentacaoTipo());  

			stVirgula=",";
		}
		if ((dados.getId_MovimentacaoTipoClasse().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_MOVI_TIPO_CLASSE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_MovimentacaoTipoClasse());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_MOVI_TIPO_MOVI_TIPO_CLASSE",ps));
	} 

//---------------------------------------------------------
	public void alterar(MovimentacaoTipoMovimentacaoTipoClasseDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.MOVI_TIPO_MOVI_TIPO_CLASS SET  ";
		stSql+= "MOVI_TIPO_MOVI_TIPO_CLASSE = ?";		 ps.adicionarString(dados.getMovimentacaoTipoMovimentacaoTipoClasse());  

		stSql+= ",ID_MOVI_TIPO = ?";		 ps.adicionarLong(dados.getId_MovimentacaoTipo());  

		stSql+= ",ID_MOVI_TIPO_CLASSE = ?";		 ps.adicionarLong(dados.getId_MovimentacaoTipoClasse());  

		stSql += " WHERE ID_MOVI_TIPO_MOVI_TIPO_CLASSE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.MOVI_TIPO_MOVI_TIPO_CLASS";
		stSql += " WHERE ID_MOVI_TIPO_MOVI_TIPO_CLASSE = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public MovimentacaoTipoMovimentacaoTipoClasseDt consultarId(String id_movitipomovitipoclasse )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		MovimentacaoTipoMovimentacaoTipoClasseDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_MOVI_TIPO_MOVI_TIPO_CLASS WHERE ID_MOVI_TIPO_MOVI_TIPO_CLASSE = ?";		ps.adicionarLong(id_movitipomovitipoclasse); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new MovimentacaoTipoMovimentacaoTipoClasseDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( MovimentacaoTipoMovimentacaoTipoClasseDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_MOVI_TIPO_MOVI_TIPO_CLASSE"));
		Dados.setMovimentacaoTipoMovimentacaoTipoClasse(rs.getString("MOVI_TIPO_MOVI_TIPO_CLASSE"));
		Dados.setId_MovimentacaoTipo( rs.getString("ID_MOVI_TIPO"));
		Dados.setMovimentacaoTipo( rs.getString("MOVI_TIPO"));
		Dados.setId_MovimentacaoTipoClasse( rs.getString("ID_MOVI_TIPO_CLASSE"));
		Dados.setMoviTipoClasse( rs.getString("MOVI_TIPO_CLASSE"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_MOVI_TIPO_MOVI_TIPO_CLASSE, MOVI_TIPO_MOVI_TIPO_CLASSE FROM PROJUDI.VIEW_MOVI_TIPO_MOVI_TIPO_CLASS WHERE MOVI_TIPO_MOVI_TIPO_CLASSE LIKE ?";
		stSql+= " ORDER BY MOVI_TIPO_MOVI_TIPO_CLASSE ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				MovimentacaoTipoMovimentacaoTipoClasseDt obTemp = new MovimentacaoTipoMovimentacaoTipoClasseDt();
				obTemp.setId(rs1.getString("ID_MOVI_TIPO_MOVI_TIPO_CLASSE"));
				obTemp.setMovimentacaoTipoMovimentacaoTipoClasse(rs1.getString("MOVI_TIPO_MOVI_TIPO_CLASSE"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_MOVI_TIPO_MOVI_TIPO_CLASS WHERE MOVI_TIPO_MOVI_TIPO_CLASSE LIKE ?";
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
	public List consultarMovimentacaoTipoMovimentacaoTipoClasseGeral(String id_movimentacaotipoclasse ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT t2.ID_MOVI_TIPO_MOVI_TIPO_CLASSE, t1.ID_MOVI_TIPO, t1.MOVI_TIPO, t3.ID_MOVI_TIPO_CLASSE, t3.MOVI_TIPO_CLASSE";
		stSql+= " FROM PROJUDI.MovimentacaoTipo as t1 ";
		stSql+= " LEFT JOIN PROJUDI.VIEW_MOVI_TIPO_MOVI_TIPO_CLASS as t2 ON t1.ID_MOVI_TIPO = t2.ID_MOVI_TIPO AND t2.ID_MOVI_TIPO_CLASSE = ?";
		stSql+= " LEFT JOIN PROJUDI.MovimentacaoTipoClasse as t3 ON t3.ID_MOVI_TIPO_CLASSE = t2.ID_MOVI_TIPO_CLASSE";
		ps.adicionarLong( id_movimentacaotipoclasse);		try{


			rs = consultar(stSql,ps);


			while (rs.next()) {
				MovimentacaoTipoMovimentacaoTipoClasseDt obTemp = new MovimentacaoTipoMovimentacaoTipoClasseDt();
				obTemp.setId(rs.getString("ID_MOVI_TIPO_MOVI_TIPO_CLASSE"));
				obTemp.setMovimentacaoTipoMovimentacaoTipoClasse(rs.getString("MOVI_TIPO_MOVI_TIPO_CLASSE"));
				obTemp.setId_MovimentacaoTipo (rs.getString("ID_MOVI_TIPO"));
				obTemp.setMovimentacaoTipo(rs.getString("MOVI_TIPO"));
				obTemp.setId_MovimentacaoTipoClasse (id_movimentacaotipoclasse);
				obTemp.setMoviTipoClasse(rs.getString("MOVI_TIPO_CLASSE"));
				liTemp.add(obTemp);
			}

		} finally {
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
