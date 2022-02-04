package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ServentiaGrupoServentiaCargoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ServentiaGrupoServentiaCargoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -1586662032851995111L;

	//---------------------------------------------------------
	public ServentiaGrupoServentiaCargoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(ServentiaGrupoServentiaCargoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.SERV_CARGO_SERV_GRUPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getServentiaCargoServentiaGrupo().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV_CARGO_SERV_GRUPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getServentiaCargoServentiaGrupo());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaCargo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_CARGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaCargo());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaGrupo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_GRUPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaGrupo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_SERV_CARGO_SERV_GRUPO",ps));
	} 

//---------------------------------------------------------
	public void alterar(ServentiaGrupoServentiaCargoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.SERV_CARGO_SERV_GRUPO SET  ";
		stSql+= "SERV_CARGO_SERV_GRUPO = ?";		 ps.adicionarString(dados.getServentiaCargoServentiaGrupo());  

		stSql+= ",ID_SERV_CARGO = ?";		 ps.adicionarLong(dados.getId_ServentiaCargo());  

		stSql+= ",ID_SERV_GRUPO = ?";		 ps.adicionarLong(dados.getId_ServentiaGrupo());  

		stSql += " WHERE ID_SERV_CARGO_SERV_GRUPO  = ? "; 		ps.adicionarLong(dados.getId()); 


		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.SERV_CARGO_SERV_GRUPO";
		stSql += " WHERE ID_SERV_CARGO_SERV_GRUPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ServentiaGrupoServentiaCargoDt consultarId(String id_serventiacargoserventiagrupo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ServentiaGrupoServentiaCargoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_SERV_CARGO_SERV_GRUPO WHERE ID_SERV_CARGO_SERV_GRUPO = ?";		ps.adicionarLong(id_serventiacargoserventiagrupo); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ServentiaGrupoServentiaCargoDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( ServentiaGrupoServentiaCargoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_SERV_CARGO_SERV_GRUPO"));
		Dados.setServentiaCargoServentiaGrupo(rs.getString("SERV_CARGO_SERV_GRUPO"));
		Dados.setId_ServentiaCargo( rs.getString("ID_SERV_CARGO"));
		Dados.setServentiaCargo( rs.getString("SERV_CARGO"));
		Dados.setId_ServentiaGrupo( rs.getString("ID_SERV_GRUPO"));
		Dados.setServentiaGrupo( rs.getString("SERV_GRUPO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_SERV_CARGO_SERV_GRUPO, SERV_CARGO_SERV_GRUPO FROM PROJUDI.VIEW_SERV_CARGO_SERV_GRUPO WHERE SERV_CARGO_SERV_GRUPO LIKE ?";
		stSql+= " ORDER BY SERV_CARGO_SERV_GRUPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				ServentiaGrupoServentiaCargoDt obTemp = new ServentiaGrupoServentiaCargoDt();
				obTemp.setId(rs1.getString("ID_SERV_CARGO_SERV_GRUPO"));
				obTemp.setServentiaCargoServentiaGrupo(rs1.getString("SERV_CARGO_SERV_GRUPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_SERV_CARGO_SERV_GRUPO WHERE SERV_CARGO_SERV_GRUPO LIKE ?";
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


		stSql= "SELECT ID_SERV_CARGO_SERV_GRUPO as id, SERV_CARGO_SERV_GRUPO as descricao1 FROM PROJUDI.VIEW_SERV_CARGO_SERV_GRUPO WHERE SERV_CARGO_SERV_GRUPO LIKE ?";
		stSql+= " ORDER BY SERV_CARGO_SERV_GRUPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_SERV_CARGO_SERV_GRUPO WHERE SERV_CARGO_SERV_GRUPO LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}

//---------------------------------------------------------
	public List consultarServentiaCargoServentiaGrupoGeral(String id_serventiagrupo, String id_Serventia ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT t2.ID_SERV_CARGO_SERV_GRUPO, t1.ID_SERV_CARGO, t1.SERV_CARGO, t3.ID_SERV_GRUPO, t3.SERV_GRUPO";
		stSql+= " FROM PROJUDI.Serv_Cargo  t1 ";
		stSql+= " LEFT JOIN PROJUDI.VIEW_SERV_CARGO_SERV_GRUPO  t2 " +
					"ON t1.ID_SERV_CARGO = t2.ID_SERV_CARGO AND t2.ID_SERV_GRUPO = ? ";ps.adicionarLong( id_serventiagrupo);	
		stSql+= " LEFT JOIN PROJUDI.Serv_Grupo  t3 ON t3.ID_SERV_GRUPO = t2.ID_SERV_GRUPO";
		stSql+= " where t1.id_serv = ? "; ps.adicionarLong( id_Serventia);	
		
		try{
			rs = consultar(stSql,ps);
			while (rs.next()) {
				ServentiaGrupoServentiaCargoDt obTemp = new ServentiaGrupoServentiaCargoDt();
				obTemp.setId(rs.getString("ID_SERV_CARGO_SERV_GRUPO"));
				obTemp.setId_ServentiaCargo (rs.getString("ID_SERV_CARGO"));
				obTemp.setServentiaCargo(rs.getString("SERV_CARGO"));
				obTemp.setId_ServentiaGrupo (rs.getString("ID_SERV_GRUPO"));
				obTemp.setServentiaGrupo(rs.getString("SERV_GRUPO"));
				liTemp.add(obTemp);
			}

		} finally {
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
