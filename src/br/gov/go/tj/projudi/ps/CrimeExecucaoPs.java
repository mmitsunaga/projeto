package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CrimeExecucaoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class CrimeExecucaoPs extends CrimeExecucaoPsGen{

    private static final long serialVersionUID = -1260739730311673952L;

    public CrimeExecucaoPs(Connection conexao){
		Conexao = conexao;
	}

	public CrimeExecucaoDt consultarCodigo(String codigo_crime)  throws Exception {
		String Sql;
		CrimeExecucaoDt Dados=null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT * FROM PROJUDI.VIEW_CRIME_EXE WHERE CRIME_EXE_CODIGO = ? ";
		ps.adicionarLong(codigo_crime);
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados= new CrimeExecucaoDt();
				associarDt(Dados, rs1);
			}
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados; 
	}
	
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT ID_CRIME_EXE, CRIME_EXE, LEI, PARAGRAFO, ARTIGO, INCISO"; 
		SqlFrom = " FROM PROJUDI.VIEW_CRIME_EXE";
		SqlFrom += " WHERE CRIME_EXE LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlOrder = " ORDER BY CRIME_EXE";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				CrimeExecucaoDt obTemp = new CrimeExecucaoDt();
				obTemp.setId(rs1.getString("ID_CRIME_EXE"));
				String descricaoCrime = "";
				if (!rs1.getString("LEI").equals("")) descricaoCrime += "LEI: " + rs1.getString("LEI") + " - ";
				if (!rs1.getString("ARTIGO").equals("")) descricaoCrime += "ARTIGO: " + rs1.getString("ARTIGO") + " - ";
				if (!rs1.getString("PARAGRAFO").equals("")) descricaoCrime +=  "PARÁGRAFO: " + rs1.getString("PARAGRAFO") + " - ";
				if (!rs1.getString("INCISO").equals("")) descricaoCrime += "INCISO: " + rs1.getString("INCISO") + " - ";
				descricaoCrime += rs1.getString("CRIME_EXE");
				obTemp.setCrimeExecucao(descricaoCrime);
				descricaoCrime = "";
				liTemp.add(obTemp);
			}
			Sql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}
	
	public List listarCrime(String descricao, String lei, String artigo, String posicao ) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT ID_CRIME_EXE, CRIME_EXE, LEI, PARAGRAFO, ARTIGO, INCISO";
		SqlFrom = " FROM PROJUDI.VIEW_CRIME_EXE";
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
				CrimeExecucaoDt obTemp = new CrimeExecucaoDt();
				obTemp.setId(rs1.getString("ID_CRIME_EXE"));
				String descricaoCrime = "";
				if (rs1.getString("LEI") != null && !rs1.getString("LEI").equals("")) descricaoCrime += "LEI: " + rs1.getString("LEI") + " - ";
				if (rs1.getString("ARTIGO") != null & !rs1.getString("ARTIGO").equals("")) descricaoCrime += "ARTIGO: " + rs1.getString("ARTIGO") + " - ";
				if (rs1.getString("PARAGRAFO") != null && !rs1.getString("PARAGRAFO").equals("")) descricaoCrime +=  "PARÁGRAFO: " + rs1.getString("PARAGRAFO") + " - ";
				if (rs1.getString("INCISO") != null && !rs1.getString("INCISO").equals("")) descricaoCrime += "INCISO: " + rs1.getString("INCISO") + " - ";
				descricaoCrime += rs1.getString("CRIME_EXE");
				obTemp.setCrimeExecucao(descricaoCrime);
				descricaoCrime = "";
				liTemp.add(obTemp);
			}
			Sql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}
	
	public String consultarDescricaoJSON(String crime, String lei, String artigo, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp="";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_CRIME_EXE as ID,  CRIME_EXE, LEI, ARTIGO, PARAGRAFO, INCISO";
		stSqlFrom = " FROM PROJUDI.VIEW_CRIME_EXE ";
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

	public String consultarDescricaoJSONPJD(String crime, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {

		String stSql="";
		String stTemp="";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 5;

		stSql= "SELECT ID_CRIME_EXE as ID,  CRIME_EXE AS DESCRICAO1, LEI AS DESCRICAO2, ARTIGO AS DESCRICAO3, PARAGRAFO AS DESCRICAO4, INCISO AS DESCRICAO5" +
				" FROM PROJUDI.VIEW_CRIME_EXE ";
		stSql+= " WHERE (CRIME_EXE LIKE ? ";
		stSql+= " OR LEI LIKE ? ";
		stSql+= " OR ARTIGO LIKE ? )";
		stSql+= " ORDER BY "+ordenacao;		
		
		ps.adicionarString("%" + crime + "%");
		ps.adicionarString("%" + crime + "%");
		ps.adicionarString("%" + crime + "%");
		
		try{
			rs1 = consultarPaginacao(stSql, ps, posicao, quantidadeRegistros);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_CRIME_EXE";
			stSql+= " WHERE CRIME_EXE LIKE ? ";
			stSql+= " AND LEI LIKE ? ";
			stSql+= " AND ARTIGO LIKE ? ";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
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
			stTemp.append("\"}");
		}
		stTemp.append("]");
		return stTemp.toString();
	}
}
