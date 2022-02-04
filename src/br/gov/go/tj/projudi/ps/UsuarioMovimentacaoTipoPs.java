package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class UsuarioMovimentacaoTipoPs extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4159707300382507656L;
	
	
	public UsuarioMovimentacaoTipoPs(Connection conexao){
		Conexao = conexao;
	}

	public void excluir(String id_UsuarioMovimentacaoTipo) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSql= "DELETE FROM PROJUDI.USU_MOVI_TIPO";
		stSql += " WHERE ID_USU_MOVI_TIPO = ?";		ps.adicionarLong(id_UsuarioMovimentacaoTipo);	
			executarUpdateDelete(stSql,ps);
		

	}	
	
	public void inserir(String id_Usuario, String grupoCodigo, UsuarioMovimentacaoTipoDt usuarioMovimentacaoTipoDt) throws Exception
	{
		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSql= "INSERT INTO PROJUDI.USU_MOVI_TIPO (ID_USU, ID_MOVI_TIPO, GRUPO_CODIGO) VALUES (?, ?, ?)";	
		ps.adicionarLong(id_Usuario);
		ps.adicionarLong(usuarioMovimentacaoTipoDt.getId_MovimentacaoTipo());
		ps.adicionarLong(grupoCodigo);
		
		usuarioMovimentacaoTipoDt.setId(executarInsert(stSql, "ID_USU_MOVI_TIPO", ps)); 
	}
	
	/**
	 * Consultar tipos de movimentação definidos para um determinado usuário serventia e grupo independentes se o usuário havia selecionado
	 */	
	public List consultaTodosGeral(String id_Usuario, String grupoCodigo) throws Exception
	{		
		return consultaTodos(id_Usuario, grupoCodigo, false);  
	}
	
	/**
	 * Consultar tipos de movimentação definidos para um determinado grupo independentes se o usuário havia selecionado
	 */
	public List consultaTodosMarcados(String id_Usuario, String grupoCodigo) throws Exception
	{
		return consultaTodos(id_Usuario, grupoCodigo, true); 
	}
	
	private List consultaTodos(String id_Usuario, String grupoCodigo, boolean somenteMarcados) throws Exception
	{
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql= "SELECT UMT.ID_USU_MOVI_TIPO, GCC.ID_MOVI_TIPO, MT.MOVI_TIPO, MT.ID_CNJ_MOVI ";
		Sql+= " FROM (((PROJUDI.GRUPO_MOVI_TIPO GCC";
		Sql+= " JOIN PROJUDI.GRUPO G ON((GCC.ID_GRUPO = G.ID_GRUPO)))";
		Sql+= " JOIN PROJUDI.MOVI_TIPO MT ON((GCC.ID_MOVI_TIPO = MT.ID_MOVI_TIPO)))";
		Sql+= (somenteMarcados ? "" : " LEFT");
		Sql+= " JOIN PROJUDI.USU_MOVI_TIPO UMT ON ((MT.ID_MOVI_TIPO = UMT.ID_MOVI_TIPO AND G.GRUPO_CODIGO = UMT.GRUPO_CODIGO AND UMT.ID_USU = ?)))";
		ps.adicionarLong(id_Usuario);
		Sql+= " WHERE G.GRUPO_CODIGO = ?";
		ps.adicionarLong(grupoCodigo);
		Sql+= " AND MT.ID_CNJ_MOVI is not null";	
		Sql+= " ORDER BY MT.MOVI_TIPO";		
		
		try{
		
			rs1 = consultar(Sql,ps);

			while (rs1.next()) {
				UsuarioMovimentacaoTipoDt obTemp = new UsuarioMovimentacaoTipoDt();
				obTemp.setId(rs1.getString("ID_USU_MOVI_TIPO"));
				obTemp.setId_MovimentacaoTipo(rs1.getString("ID_MOVI_TIPO"));
				obTemp.setMovimentacaoTipo(rs1.getString("MOVI_TIPO") + " (CNJ:" + rs1.getString("ID_CNJ_MOVI") + ")" );
				
				liTemp.add(obTemp);
			}			
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp; 
	}	

	// Se somenteMarcados = true retorna as movimentacoes marcadas, Se false Retorna as nao marcadas; OBS: Nao Retorna Todos!
	public String consultarDescricaoJSON(String id_Usuario, String grupoCodigo, String descricao, String posicao, String ordenacao, String quantidadeRegistros, boolean somenteMarcados, String pesquisa) throws Exception {
		String stTemp = "";
		String stSql = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;
		
		stSql = "SELECT GCC.ID_MOVI_TIPO AS Id, UMT.ID_USU_MOVI_TIPO AS descricao1,"
				+ "MT.MOVI_TIPO || ' (CNJ:' || MT.ID_CNJ_MOVI || ')' AS descricao2 FROM"
				+ " (((PROJUDI.GRUPO_MOVI_TIPO GCC JOIN PROJUDI.GRUPO G ON"
				+ " ( ( GCC.ID_GRUPO = G.ID_GRUPO ) ) ) JOIN PROJUDI.MOVI_TIPO MT ON"
				+ " ( ( GCC.ID_MOVI_TIPO = MT.ID_MOVI_TIPO ) ) ) ";
		
		if (!somenteMarcados) stSql += "LEFT"; 
		
		stSql += " JOIN PROJUDI.USU_MOVI_TIPO UMT ON ( ( MT.ID_MOVI_TIPO ="
				+ " UMT.ID_MOVI_TIPO AND G.GRUPO_CODIGO = UMT.GRUPO_CODIGO AND"
				+ " UMT.ID_USU = ? ) ) ) ";
		ps.adicionarLong(id_Usuario);
		stSql += " WHERE G.GRUPO_CODIGO = ? ";
		ps.adicionarLong(grupoCodigo);
		
		if (!somenteMarcados) stSql += " AND UMT.ID_USU_MOVI_TIPO IS NULL";
		
		if (pesquisa != "") stSql += " AND MT.MOVI_TIPO LIKE '%"+pesquisa+"%'";
		
		stSql += " AND MT.ID_CNJ_MOVI IS NOT NULL";
		stSql += " ORDER BY " + ordenacao;

		try {
			
			rs1 = consultarPaginacao(stSql, ps, posicao, quantidadeRegistros);
			//rs1 = consultar(stSql, ps);
			
			stSql = "SELECT COUNT(GCC.ID_MOVI_TIPO) AS QUANTIDADE FROM"
					+ " (((PROJUDI.GRUPO_MOVI_TIPO GCC JOIN PROJUDI.GRUPO G ON"
					+ " ( ( GCC.ID_GRUPO = G.ID_GRUPO ) ) ) JOIN PROJUDI.MOVI_TIPO MT ON"
					+ " ( ( GCC.ID_MOVI_TIPO = MT.ID_MOVI_TIPO ) ) ) ";
			
			if (!somenteMarcados) stSql += "LEFT";
			
			stSql += " JOIN PROJUDI.USU_MOVI_TIPO UMT ON ( ( MT.ID_MOVI_TIPO ="
					+ " UMT.ID_MOVI_TIPO AND G.GRUPO_CODIGO = UMT.GRUPO_CODIGO AND"
					+ " UMT.ID_USU = ? ) ) ) ";
			stSql += " WHERE G.GRUPO_CODIGO = ? ";
			
			if (!somenteMarcados) stSql += " AND UMT.ID_USU_MOVI_TIPO IS NULL";
			
			if (pesquisa != "") stSql += " AND MT.MOVI_TIPO LIKE '%"+pesquisa+"%'";
			
			stSql += " AND MT.ID_CNJ_MOVI IS NOT NULL";
			
			rs2 = consultar(stSql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
			try {if (rs1 != null)rs1.close();} catch (Exception e) {}
			try {if (rs2 != null)rs2.close();} catch (Exception e) {}
		}
		rs1.close();
		rs2.close();
		return stTemp;
	}
	
}
