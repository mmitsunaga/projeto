package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ParametroCrimeExecucaoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ParametroCrimeExecucaoPs extends ParametroCrimeExecucaoPsGen{

    private static final long serialVersionUID = 998406673132401285L;
    
    public ParametroCrimeExecucaoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * sobrescrevento método do gen
	 */
	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT *";
		SqlFrom = " FROM PROJUDI.VIEW_PARAMETRO_CRIME_EXE";
		SqlFrom += " WHERE CRIME_EXE LIKE ? ";
		ps.adicionarString("%"+ descricao +"%");
		SqlOrder = " ORDER BY CRIME_EXE ";		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				ParametroCrimeExecucaoDt obTemp = new ParametroCrimeExecucaoDt();
				obTemp.setId(rs1.getString("ID_PARAMETRO_CRIME_EXE"));
				String descricaoCrime = "";
				if (!rs1.getString("LEI").equals("")) descricaoCrime += "LEI: " + rs1.getString("LEI") + " - ";
				if (!rs1.getString("ARTIGO").equals("")) descricaoCrime += "ARTIGO: " + rs1.getString("ARTIGO") + " - ";
				if (!rs1.getString("PARAGRAFO").equals("")) descricaoCrime +=  "PARÁGRAFO: " + rs1.getString("PARAGRAFO") + " - ";
				if (!rs1.getString("INCISO").equals("")) descricaoCrime += "INCISO: " + rs1.getString("INCISO") + " - ";
				descricaoCrime += rs1.getString("CRIME_EXE");
				obTemp.setCrimeExecucao(descricaoCrime);
				obTemp.setData( Funcoes.FormatarData(rs1.getDateTime("DATA")));
				obTemp.setHediondoProgressao( Funcoes.FormatarLogico(rs1.getString("HEDIONDO_PROGRESSAO")));
				obTemp.setHediondoLivramCond( Funcoes.FormatarLogico(rs1.getString("HEDIONDO_LIVRAM_COND")));
				obTemp.setEquiparaHediondoLivramCond( Funcoes.FormatarLogico(rs1.getString("EQUIPARA_HEDIONDO_LIVRAM_COND")));
				obTemp.setId_CrimeExecucao( rs1.getString("ID_CRIME_EXE"));
				obTemp.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
				liTemp.add(obTemp);
			}
			Sql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
		} finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}
	
	/**
	 * Lista os ParâmetrosCrimeExecução
	 * @param descricao: filtro de pesquisa (descrição do crime)
	 * @param lei: filtro de pesquisa 
	 * @param artigo: filtro de pesquisa
	 * @param posicao
	 * @return List
	 * @throws Exception
	 */
	public List listarParametroCrimeExecucao(String descricao, String lei, String artigo, String posicao ) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT *";
		SqlFrom = " FROM PROJUDI.VIEW_PARAMETRO_CRIME_EXE";
		SqlFrom += " WHERE CRIME_EXE LIKE ? ";
		ps.adicionarString("%"+ descricao +"%");
		SqlFrom += " AND LEI LIKE ? ";
		ps.adicionarString( lei +"%");
		SqlFrom += " AND ARTIGO LIKE ? ";
		ps.adicionarString( artigo +"%");
		SqlOrder = " ORDER BY CRIME_EXE ";		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);			

			while (rs1.next()) {
				ParametroCrimeExecucaoDt obTemp = new ParametroCrimeExecucaoDt();
				obTemp.setId(rs1.getString("ID_PARAMETRO_CRIME_EXE"));
				String descricaoCrime = "";
				if (!rs1.getString("LEI").equals("")) descricaoCrime += "LEI: " + rs1.getString("LEI") + " - ";
				if (!rs1.getString("PARAGRAFO").equals("")) descricaoCrime +=  "PARÁGRAFO: " + rs1.getString("PARAGRAFO") + " - ";
				if (!rs1.getString("ARTIGO").equals("")) descricaoCrime += "ARTIGO: " + rs1.getString("ARTIGO") + " - ";
				if (!rs1.getString("INCISO").equals("")) descricaoCrime += "INCISO: " + rs1.getString("INCISO") + " - ";
				descricaoCrime += rs1.getString("CRIME_EXE");
				obTemp.setCrimeExecucao(descricaoCrime);
				descricaoCrime = "";
				obTemp.setData( Funcoes.FormatarData(rs1.getDateTime("DATA")));
				obTemp.setHediondoProgressao( Funcoes.FormatarLogico(rs1.getString("HEDIONDO_PROGRESSAO")));
				obTemp.setHediondoLivramCond( Funcoes.FormatarLogico(rs1.getString("HEDIONDO_LIVRAM_COND")));
				obTemp.setEquiparaHediondoLivramCond( Funcoes.FormatarLogico(rs1.getString("EQUIPARA_HEDIONDO_LIVRAM_COND")));
				obTemp.setId_CrimeExecucao( rs1.getString("ID_CRIME_EXE"));
				obTemp.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
				liTemp.add(obTemp);
			}
			
			Sql= "SELECT COUNT(*)";			
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
		}  finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}
	
	public ParametroCrimeExecucaoDt consultarId(String id_parametrocrimeexecucao )  throws Exception {

		String Sql;
		ParametroCrimeExecucaoDt Dados=null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT * FROM PROJUDI.VIEW_PARAMETRO_CRIME_EXE WHERE ID_PARAMETRO_CRIME_EXE = ? ";
		ps.adicionarLong(id_parametrocrimeexecucao);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados= new ParametroCrimeExecucaoDt();
				associarDt(Dados, rs1);
			}
		} finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados; 
	}
	
	protected void associarDt( ParametroCrimeExecucaoDt Dados, ResultSetTJGO rs1 )  throws Exception {
		
		Dados.setId(rs1.getString("ID_PARAMETRO_CRIME_EXE"));
		String descricaoCrime = "";
		if (!rs1.getString("LEI").equals("")) descricaoCrime += "LEI: " + rs1.getString("LEI") + " - ";
		if (!rs1.getString("PARAGRAFO").equals("")) descricaoCrime +=  "PARÁGRAFO: " + rs1.getString("PARAGRAFO") + " - ";
		if (!rs1.getString("ARTIGO").equals("")) descricaoCrime += "ARTIGO: " + rs1.getString("ARTIGO") + " - ";
		if (!rs1.getString("INCISO").equals("")) descricaoCrime += "INCISO: " + rs1.getString("INCISO") + " - ";
		descricaoCrime += rs1.getString("CRIME_EXE");
		Dados.setCrimeExecucao(descricaoCrime);
		Dados.setData( Funcoes.FormatarData(rs1.getDateTime("DATA")));
		Dados.setHediondoProgressao( Funcoes.FormatarLogico(rs1.getString("HEDIONDO_PROGRESSAO")));
		Dados.setHediondoLivramCond( Funcoes.FormatarLogico(rs1.getString("HEDIONDO_LIVRAM_COND")));
		Dados.setEquiparaHediondoLivramCond( Funcoes.FormatarLogico(rs1.getString("EQUIPARA_HEDIONDO_LIVRAM_COND")));
		Dados.setId_CrimeExecucao( rs1.getString("ID_CRIME_EXE"));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		
	}
	
	public String consultarDescricaoJSON(String crime, String lei, String artigo, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp="";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;

		stSql= "SELECT ID_PARAMETRO_CRIME_EXE as ID, LEI, PARAGRAFO, ARTIGO, INCISO, CRIME_EXE, DATA," +
				" HEDIONDO_PROGRESSAO, HEDIONDO_LIVRAM_COND, EQUIPARA_HEDIONDO_LIVRAM_COND";
		stSqlFrom = " FROM PROJUDI.VIEW_PARAMETRO_CRIME_EXE ";
		stSqlFrom += " WHERE CRIME_EXE LIKE ? ";
		stSqlFrom += " AND LEI LIKE ? ";
		stSqlFrom += " AND ARTIGO LIKE ? ";
		stSqlOrder = " ORDER BY CRIME_EXE";		
		
		ps.adicionarString( crime +"%");
		ps.adicionarString( lei +"%");
		ps.adicionarString( artigo +"%");
		
		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSONParametroCrime(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}

	public String gerarJSONParametroCrime(long qtdPaginas, String posicaoAtual, ResultSetTJGO rs, int qtdeColunas) throws Exception{
		StringBuilder stTemp = new StringBuilder();
		stTemp.append("[");
		stTemp.append("{\"id\":\"-50000\",\"desc1\":\"").append(qtdPaginas);
		for (int i = 2; i <= qtdeColunas; i++) {
			stTemp.append("\",\"desc" + i + "\":\"");
		}
		stTemp.append("\"}");
		stTemp.append(",{\"id\":\"-60000\",\"desc1\":\"").append(posicaoAtual);
		for (int i = 2; i <= qtdeColunas; i++) {
			stTemp.append("\",\"desc" + i + "\":\"");
		}
		stTemp.append("\"}");
		while (rs.next()){			
			String descricaoCrime = "";
			if (rs.getString("LEI") != null && !rs.getString("LEI").equals("")) descricaoCrime += "LEI: " + rs.getString("LEI") + " - ";
			if (rs.getString("ARTIGO") != null && !rs.getString("ARTIGO").equals("")) descricaoCrime += "ARTIGO: " + rs.getString("ARTIGO") + " - ";
			if (rs.getString("PARAGRAFO") != null && !rs.getString("PARAGRAFO").equals("")) descricaoCrime +=  "PARÁGRAFO: " + rs.getString("PARAGRAFO") + " - ";
			if (rs.getString("INCISO") != null && !rs.getString("INCISO").equals("")) descricaoCrime += "INCISO: " + rs.getString("INCISO") + " - ";
			descricaoCrime += rs.getString("CRIME_EXE");
			
			stTemp.append(",{\"id\":\"").append(rs.getString("ID")).append("\",\"desc1\":\"").append(descricaoCrime);
			stTemp.append("\",\"desc2\":\"").append(Funcoes.FormatarData(rs.getDateTime("DATA")));
			
			String descricaoHediondo = "";
			if (Funcoes.FormatarLogico(rs.getString("HEDIONDO_PROGRESSAO")) == "true") descricaoHediondo += "Hediondo para Progressão de Regime";
			if (Funcoes.FormatarLogico(rs.getString("HEDIONDO_LIVRAM_COND")) == "true") descricaoHediondo += "\\nHediondo para Livramento Condicional";
			if (Funcoes.FormatarLogico(rs.getString("EQUIPARA_HEDIONDO_LIVRAM_COND")) == "true") descricaoHediondo += "\\nCrime comum - Equipara com Hediondo para Livramento Condicional";
			
			stTemp.append("\",\"desc3\":\"").append(descricaoHediondo);
			stTemp.append("\"}");
		}
		stTemp.append("]");
		return stTemp.toString();
	}

	/**
	 * sobrescrevendo método do gerador
	 */
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
			 stSqlCampos+=   stVirgula + "EQUIPARA_HEDIONDO_LIVRAM_COND " ;
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
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_PARAMETRO_CRIME_EXE",ps)); 
	} 

	/**
	 * sobrescrevendo método do gerador
	 */
	public void alterar(ParametroCrimeExecucaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.PARAMETRO_CRIME_EXE SET  ";
		stSql+= "DATA = ?";		 ps.adicionarDate(dados.getData());  
		stSql+= ",HEDIONDO_PROGRESSAO = ?";		 ps.adicionarBoolean(dados.getHediondoProgressao());  
		stSql+= ",HEDIONDO_LIVRAM_COND = ?";		 ps.adicionarBoolean(dados.getHediondoLivramCond());  
		stSql+= ",EQUIPARA_HEDIONDO_LIVRAM_COND = ?";		 ps.adicionarBoolean(dados.getEquiparaHediondoLivramCond());
		stSql+= ",ID_CRIME_EXE = ?";		 ps.adicionarLong(dados.getId_CrimeExecucao());  
		stSql += " WHERE ID_PARAMETRO_CRIME_EXE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	} 
	
}
