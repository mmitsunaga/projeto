package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ParametroCrimeExecucaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ParametroCrimeExecucaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 784115936476877157L;

	//---------------------------------------------------------
	public ParametroCrimeExecucaoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(ParametroCrimeExecucaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.PARAMETRO_CRIME_EXE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getData().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getData());  

			stVirgula=",";
		}
		if ((dados.getHediondoProgressao().length()>0)) {
			 stSqlCampos+=   stVirgula + "HEDIONDO_PROGRESSAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getHediondoProgressao());  

			stVirgula=",";
		}
		if ((dados.getHediondoLivramCond().length()>0)) {
			 stSqlCampos+=   stVirgula + "HEDIONDO_LIVRAM_COND " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getHediondoLivramCond());  

			stVirgula=",";
		}
		if ((dados.getEquiparaHediondoLivramCond().length()>0)) {
			 stSqlCampos+=   stVirgula + "EQUIPARAHEDIONDO_LIVRAM_COND " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getEquiparaHediondoLivramCond());  

			stVirgula=",";
		}

		if ((dados.getId_CrimeExecucao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_CRIME_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_CrimeExecucao());  

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
		if ((dados.getLei().length()>0)) {
			 stSqlCampos+=   stVirgula + "LEI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getLei());  

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

		dados.setId(executarInsert(stSql,"ID_PARAMETRO_CRIME_EXE",ps)); 
	} 

//---------------------------------------------------------
	public void alterar(ParametroCrimeExecucaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.PARAMETRO_CRIME_EXE SET  ";
		stSql+= "DATA = ?";		 ps.adicionarDate(dados.getData());  

		stSql+= ",HEDIONDO_PROGRESSAO = ?";		 ps.adicionarBoolean(dados.getHediondoProgressao());  

		stSql+= ",HEDIONDO_LIVRAM_COND = ?";		 ps.adicionarBoolean(dados.getHediondoLivramCond());  
		
		stSql+= ",EQUIPARA_HEDIONDO_LIVRAM_COND = ?";		 ps.adicionarBoolean(dados.getEquiparaHediondoLivramCond());

		stSql+= ",ID_CRIME_EXE = ?";		 ps.adicionarLong(dados.getId_CrimeExecucao());  

		stSql+= ",ARTIGO = ?";		 ps.adicionarString(dados.getArtigo());  

		stSql+= ",PARAGRAFO = ?";		 ps.adicionarString(dados.getParagrafo());  

		stSql+= ",LEI = ?";		 ps.adicionarString(dados.getLei());  

		stSql+= ",INCISO = ?";		 ps.adicionarString(dados.getInciso());  

		stSql += " WHERE ID_PARAMETRO_CRIME_EXE  = ? "; 		ps.adicionarLong(dados.getId()); 


		executarUpdateDelete(stSql,ps);	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.PARAMETRO_CRIME_EXE";
		stSql += " WHERE ID_PARAMETRO_CRIME_EXE = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ParametroCrimeExecucaoDt consultarId(String id_parametrocrimeexecucao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ParametroCrimeExecucaoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_PARAMETRO_CRIME_EXE WHERE ID_PARAMETRO_CRIME_EXE = ?";		ps.adicionarLong(id_parametrocrimeexecucao); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ParametroCrimeExecucaoDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( ParametroCrimeExecucaoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PARAMETRO_CRIME_EXE"));
		Dados.setData(rs.getString("DATA"));
		Dados.setHediondoProgressao( Funcoes.FormatarLogico(rs.getString("HEDIONDO_PROGRESSAO")));
		Dados.setHediondoLivramCond( Funcoes.FormatarLogico(rs.getString("HEDIONDO_LIVRAM_COND")));
		Dados.setEquiparaHediondoLivramCond( Funcoes.FormatarLogico(rs.getString("EQUIPARA_HEDIONDO_LIVRAM_COND")));
		Dados.setId_CrimeExecucao( rs.getString("ID_CRIME_EXE"));
		Dados.setCrimeExecucao( rs.getString("CRIME_EXE"));
		Dados.setArtigo( rs.getString("ARTIGO"));
		Dados.setParagrafo( rs.getString("PARAGRAFO"));
		Dados.setLei( rs.getString("LEI"));
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


		stSql= "SELECT ID_PARAMETRO_CRIME_EXE, DATA FROM PROJUDI.VIEW_PARAMETRO_CRIME_EXE WHERE DATA LIKE ?";
		stSql+= " ORDER BY DATA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				ParametroCrimeExecucaoDt obTemp = new ParametroCrimeExecucaoDt();
				obTemp.setId(rs1.getString("ID_PARAMETRO_CRIME_EXE"));
				obTemp.setData(rs1.getString("DATA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_PARAMETRO_CRIME_EXE WHERE DATA LIKE ?";
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


		stSql= "SELECT ID_PARAMETRO_CRIME_EXE as id, DATA as descricao1 FROM PROJUDI.VIEW_PARAMETRO_CRIME_EXE WHERE DATA LIKE ?";
		stSql+= " ORDER BY DATA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_PARAMETRO_CRIME_EXE WHERE DATA LIKE ?";
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
