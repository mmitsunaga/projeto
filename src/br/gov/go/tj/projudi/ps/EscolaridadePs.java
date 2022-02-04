package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.EscolaridadeDt;

import java.sql.Connection;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class EscolaridadePs extends EscolaridadePsGen{

/**
     * 
     */
    private static final long serialVersionUID = 2136920757589706563L;

    public EscolaridadePs(Connection conexao){
    	Conexao = conexao;
    }

	//
	public EscolaridadeDt consultarCodigo(String codigo_escolaridade)  throws Exception {
		String Sql;
		EscolaridadeDt Dados=null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT * FROM PROJUDI.VIEW_ESCOLARIDADE WHERE ESCOLARIDADE_CODIGO = ?";
		ps.adicionarLong(codigo_escolaridade);		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados= new EscolaridadeDt();
				associarDt(Dados, rs1);
			}
		} finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados; 
	}

	public String consultarDescricaoEscolaridadeJSON(String tempNomeBusca, String posicaoPaginaAtual)  throws Exception {
		
		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp="";
		
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		int qtdeColunas = 1;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_ESCOLARIDADE as ID, ESCOLARIDADE as DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_ESCOLARIDADE";
		stSqlFrom += " WHERE ESCOLARIDADE LIKE ?";
		ps.adicionarString("%"+tempNomeBusca+"%"); 
		stSqlOrder = " ORDER BY ESCOLARIDADE ";		

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicaoPaginaAtual);

			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom, ps);			
			rs2.next();
			
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
			
		return stTemp; 
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
			
		if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenacao PROJUDI
			ordenacao = " ESCOLARIDADE ";

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_ESCOLARIDADE as id, ESCOLARIDADE as descricao1 FROM PROJUDI.VIEW_ESCOLARIDADE WHERE ESCOLARIDADE LIKE ?";
		stSql+= " ORDER BY "+ordenacao;
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao, quantidadeRegistros);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_ESCOLARIDADE WHERE ESCOLARIDADE LIKE ?";
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
