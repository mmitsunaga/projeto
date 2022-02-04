package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import br.gov.go.tj.projudi.dt.AfastamentoDt;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class AfastamentoPs extends AfastamentoPsGen{

    private static final long serialVersionUID = 3534711678510210903L;
    
    @SuppressWarnings("unused")
	private AfastamentoPs(){}
    
    public AfastamentoPs(Connection conexao){
    	Conexao = conexao;
	}	
	
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		
		if (ORDENACAO_PADRAO.equals(ordenacao)) // ordenação do PROJUDI
			ordenacao = " AFASTAMENTO ";

		String stSql="";
		String stSqlFrom="";
		String stSqlOrder="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		stSql = "SELECT ID_AFASTAMENTO AS ID, AFASTAMENTO AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_AFASTAMENTO";
		stSqlFrom += " WHERE AFASTAMENTO LIKE ?";
		stSqlOrder = " ORDER BY " + ordenacao;
		ps.adicionarString("%"+descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao, quantidadeRegistros);
			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
					
		}
			finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}	
	public List consultarTodos() throws Exception {

		List listaAfastamento = new ArrayList();

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		try {
			sql.append("SELECT id_afastamento AS idAfastamento, afastamento AS afastamento FROM projudi.afastamento"
					+ " WHERE id_afastamento NOT IN (?,?) ORDER BY afastamento");
			
			ps.adicionarLong(AfastamentoDt.CODIGO_SUSPENSAO_POR_ATRASO);
			ps.adicionarLong(AfastamentoDt.CODIGO_ATIVO);

			rs = consultar(sql.toString(), ps);

			while (rs.next()) {

				AfastamentoDt objDt = new AfastamentoDt();
				objDt.setId(rs.getString("idAfastamento"));
				objDt.setAfastamento(rs.getString("afastamento"));
				listaAfastamento.add(objDt);
			}
		}

		

		finally

		{
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		return listaAfastamento;
	}

	
}