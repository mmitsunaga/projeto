package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class EstadoCivilPs extends EstadoCivilPsGen {

	private static final long serialVersionUID = 8547591474658172068L;

	public EstadoCivilPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * consulta o id do estado civil pela descrição
	 * 
	 * @param descricao
	 * @return
	 * @throws Exception
	 */
	public String consultarIdEstadoCivil(String descricao) throws Exception {

		String sql;
		String id = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT ID_ESTADO_CIVIL FROM PROJUDI.VIEW_ESTADO_CIVIL WHERE ESTADO_CIVIL like ?";
		ps.adicionarString("%"+ descricao +"%");

		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				id = rs1.getString("ID_ESTADO_CIVIL");
			}
		
		} finally{
			try{if (rs1 != null)rs1.close();} catch(Exception e1) {}
		}
		return id;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		
		if (ORDENACAO_PADRAO.equals(ordenacao))
			ordenacao =  " ESTADO_CIVIL ";
		
		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_ESTADO_CIVIL AS ID, ESTADO_CIVIL AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_ESTADO_CIVIL";
		stSqlFrom += " WHERE ESTADO_CIVIL LIKE ?";
		stSqlOrder = " ORDER BY " + ordenacao;
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao, quantidadeRegistros);
			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}

}
