package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CrimeExecucaoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class CrimeExecucaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -2265148650780471885L;

	//---------------------------------------------------------
	public CrimeExecucaoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(CrimeExecucaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.CRIME_EXE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getCrimeExecucaoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "CRIME_EXE_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCrimeExecucaoCodigo());  

			stVirgula=",";
		}
		if ((dados.getCrimeExecucao().length()>0)) {
			 stSqlCampos+=   stVirgula + "CRIME_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCrimeExecucao());  

			stVirgula=",";
		}
		if ((dados.getLei().length()>0)) {
			 stSqlCampos+=   stVirgula + "LEI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getLei());  

			stVirgula=",";
		}
		if ((dados.getArtigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ARTIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getArtigo());  

			stVirgula=",";
		}
		if ((dados.getParagrafo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PARAGRAFO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getParagrafo());  

			stVirgula=",";
		}
		if ((dados.getInciso().length()>0)) {
			 stSqlCampos+=   stVirgula + "INCISO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getInciso());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_CRIME_EXE",ps));
	} 

//---------------------------------------------------------
	public void alterar(CrimeExecucaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.CRIME_EXE SET  ";
		stSql+= "CRIME_EXE_CODIGO = ?";		 ps.adicionarLong(dados.getCrimeExecucaoCodigo());  

		stSql+= ",CRIME_EXE = ?";		 ps.adicionarString(dados.getCrimeExecucao());  

		stSql+= ",LEI = ?";		 ps.adicionarString(dados.getLei());  

		stSql+= ",ARTIGO = ?";		 ps.adicionarString(dados.getArtigo());  

		stSql+= ",PARAGRAFO = ?";		 ps.adicionarString(dados.getParagrafo());  

		stSql+= ",INCISO = ?";		 ps.adicionarString(dados.getInciso());  

		stSql += " WHERE ID_CRIME_EXE  = ? "; 		ps.adicionarLong(dados.getId()); 


		executarUpdateDelete(stSql,ps);	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.CRIME_EXE";
		stSql += " WHERE ID_CRIME_EXE = ?";		ps.adicionarLong(chave); 


		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public CrimeExecucaoDt consultarId(String id_crimeexecucao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		CrimeExecucaoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_CRIME_EXE WHERE ID_CRIME_EXE = ?";		ps.adicionarLong(id_crimeexecucao); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new CrimeExecucaoDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( CrimeExecucaoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_CRIME_EXE"));
		Dados.setCrimeExecucao(rs.getString("CRIME_EXE"));
		Dados.setCrimeExecucaoCodigo( rs.getString("CRIME_EXE_CODIGO"));
		Dados.setLei( rs.getString("LEI"));
		Dados.setArtigo( rs.getString("ARTIGO"));
		Dados.setParagrafo( rs.getString("PARAGRAFO"));
		Dados.setInciso( rs.getString("INCISO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_CRIME_EXE, CRIME_EXE FROM PROJUDI.VIEW_CRIME_EXE WHERE CRIME_EXE LIKE ?";
		stSql+= " ORDER BY CRIME_EXE ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				CrimeExecucaoDt obTemp = new CrimeExecucaoDt();
				obTemp.setId(rs1.getString("ID_CRIME_EXE"));
				obTemp.setCrimeExecucao(rs1.getString("CRIME_EXE"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_CRIME_EXE WHERE CRIME_EXE LIKE ?";
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


		stSql= "SELECT ID_CRIME_EXE as id, CRIME_EXE as descricao1 FROM PROJUDI.VIEW_CRIME_EXE WHERE CRIME_EXE LIKE ?";
		stSql+= " ORDER BY CRIME_EXE ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_CRIME_EXE WHERE CRIME_EXE LIKE ?";
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
