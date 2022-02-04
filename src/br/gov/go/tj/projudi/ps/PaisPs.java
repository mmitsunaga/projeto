package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.projudi.dt.PaisDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ProjudiConstantes;
import br.gov.go.tj.utils.ResultSetTJGO;

public class PaisPs extends PaisPsGen {

	private static final long serialVersionUID = -3180629631717470991L;

	public PaisPs(Connection conexao){
		Conexao = conexao;
	}

	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		
		if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenacao PROJUDI
			ordenacao = " PAIS ";
		
		String stSql = "";
		String stSqlFrom = "";
		String stSqlWhere = "";
		String stSqlOrder = "";
		String stTemp = "";		
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql = "SELECT ID_PAIS as id, PAIS as descricao1";
		stSqlFrom = " FROM PROJUDI.VIEW_PAIS";
		stSqlWhere = " WHERE PAIS LIKE ?";
		stSqlOrder += " ORDER BY " + ordenacao;
		ps.adicionarString("%"+ descricao +"%");

		try{
			rs1 = consultarPaginacao(stSql+stSqlFrom+stSqlWhere+stSqlOrder, ps, posicao, quantidadeRegistros);
			stSql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom +stSqlWhere, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null)rs1.close();} catch(Exception e) {}
			try{if (rs2 != null)rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}

	public List consultarDescricao(String descricao) throws Exception {

		String stSql = "";
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT ID_PAIS, PAIS FROM PROJUDI.VIEW_PAIS WHERE PAIS LIKE ?";
		stSql += " ORDER BY PAIS ";
		ps.adicionarString("%"+ descricao +"%");

		try{
			rs = consultar(stSql, ps);

			while (rs.next()) {
				PaisDt obTemp = new PaisDt();
				obTemp.setId(rs.getString("ID_PAIS"));
				obTemp.setPais(rs.getString("PAIS"));
				liTemp.add(obTemp);
			}
		
		} finally{
			try{if (rs != null)rs.close();} catch(Exception e) {}
		}
		return liTemp;
	}
	
	public List<DescritorComboDt> consultarDescricaoCombo(String descricao) throws Exception {

		String Sql;
		List<DescritorComboDt> listaTemp = new ArrayList<DescritorComboDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		String valorFiltro = obtenhaValorFiltroConsultarDescricaoCombo(descricao);
		
		Sql = "SELECT * FROM PROJUDI.PAIS";
		if (valorFiltro != null) {
			Sql += " WHERE PAIS LIKE ?"; 
			ps.adicionarString(valorFiltro);	
		}		 
		Sql += " ORDER BY PAIS";		
		
		try {
			rs1 = consultar(Sql, ps);

			while (rs1.next() && listaTemp.size() <= ProjudiConstantes.QUANTIDADE_OBJETOS_COMBO) {
				DescritorComboDt obTemp = new DescritorComboDt();
				obTemp.setId(rs1.getString("ID_PAIS"));
				obTemp.setDescricao(rs1.getString("PAIS"));				
				listaTemp.add(obTemp);
			}			
		} finally{
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
        }
		return listaTemp;
	}

}
