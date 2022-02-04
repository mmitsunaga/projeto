package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.ObjetoPedidoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ObjetoPedidoPs extends ObjetoPedidoPsGen {

	public ObjetoPedidoPs(Connection conexao){
		Conexao = conexao;
	}

	private static final long serialVersionUID = 1726602158686246407L;

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {

		String stSql = "";
		String stSqlFrom = "";
		String stSqlOrder = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql = "SELECT ID_OBJETO_PEDIDO AS ID, OBJETO_PEDIDO AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_OBJETO_PEDIDO";
		stSqlFrom += " WHERE OBJETO_PEDIDO LIKE ?";
		stSqlOrder = " ORDER BY OBJETO_PEDIDO ";
		ps.adicionarString( descricao +"%");

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public ObjetoPedidoDt consultarObjetoPedidoCodigo(String objetoPedidoCodigo) throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ObjetoPedidoDt Dados = null;

		stSql = "SELECT * ";
		stSql += "FROM PROJUDI.VIEW_OBJETO_PEDIDO ";
		stSql += "WHERE OBJETO_PEDIDO_CODIGO = ? ";
		ps.adicionarLong(objetoPedidoCodigo); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados = new ObjetoPedidoDt();
				associarDt(Dados, rs1);
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}
}