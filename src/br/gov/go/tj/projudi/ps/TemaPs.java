package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.TemaDt;
import br.gov.go.tj.projudi.dt.TemaSituacaoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.ValidacaoUtil;

public class TemaPs extends TemaPsGen{

	private static final long serialVersionUID = 2117468699492363420L;

	public TemaPs(Connection conexao){
		Conexao = conexao;
	}
	
	public String consultarDescricaoJSON(String descricao, String origem, String situacao, String codigo, String posicao) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp="";
		int qtdeColunas = 4;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_TEMA as id, TEMA_CODIGO || ' - [' || TITULO || '] - ' || QUES_DIREITO as descricao1, TEMA_TIPO_CNJ as descricao2, TEMA_ORIGEM as descricao3, TEMA_SITUACAO as descricao4";
		stSqlFrom = " FROM PROJUDI.VIEW_TEMA";
		stSqlFrom += " WHERE QUES_DIREITO LIKE ?";
		ps.adicionarString("%"+descricao+"%");
		
		if(origem.length() > 0) {
			stSqlFrom+= " AND TEMA_ORIGEM LIKE ? ";
			ps.adicionarString("%"+origem+"%");	
		}
		if(situacao.length() > 0) {
			stSqlFrom+= " AND TEMA_SITUACAO LIKE ? ";
			ps.adicionarString("%"+situacao+"%");	
		}			
		if(codigo.length() > 0) {
			stSqlFrom+= " AND TEMA_CODIGO = ? ";
			ps.adicionarLong(codigo);	
		}
		
		stSqlOrder = " ORDER BY TEMA_CODIGO DESC";

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	
	public String consultarTemasValidosPorDescricaoJSON(String descricao, String origem, String situacao, String codigo, String posicao) throws Exception {
		
		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp="";
		int qtdeColunas = 4;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_TEMA as id, TEMA_CODIGO || ' - [' || TITULO || '] - ' || QUES_DIREITO as descricao1, TEMA_TIPO_CNJ as descricao2, TEMA_ORIGEM as descricao3, TEMA_SITUACAO as descricao4";
		stSqlFrom = " FROM PROJUDI.VIEW_TEMA";
		stSqlFrom += " WHERE QUES_DIREITO LIKE ?";
		ps.adicionarString("%"+descricao+"%");
		
		if(origem.length() > 0) {
			stSqlFrom+= " AND TEMA_ORIGEM LIKE ? ";
			ps.adicionarString("%"+origem+"%");	
		}
		if(situacao.length() > 0) {
			stSqlFrom+= " AND TEMA_SITUACAO LIKE ? ";
			ps.adicionarString("%"+situacao+"%");	
		}			
		if(codigo.length() > 0) {
			stSqlFrom+= " AND TEMA_CODIGO = ? ";
			ps.adicionarLong(codigo);	
		}
		
		// Somente os temas com situacao diferente de cancelado, transito julgado ou transitado julgado
		stSqlFrom+= " AND TEMA_SITUACAO_CODIGO NOT IN (" + TemaSituacaoDt.CANCELADO_CODIGO + "," + TemaSituacaoDt.TRANSITADO_JULGADO_CODIGO + "," + TemaSituacaoDt.TRANSITO_JULGADO_CODIGO +")";
		
		// Data de transito é vazio
		stSqlFrom+= " AND (DATA_TRANSITO IS NULL)";
		
		stSqlOrder = " ORDER BY TEMA_CODIGO DESC";

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
			
	}
	
	//Exclui Todos
	public void excluirTodosAssuntos(TemaDt temaAssunto) throws Exception {
			
		String stSql="DELETE FROM TEMA_ASSUNTO WHERE ID_TEMA = ?";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ps.adicionarString(temaAssunto.getId());
		
		executarUpdateDelete(stSql,ps);
	}
	
	/**
	 * Consulta um tema através do código CNJ
	 * @param codigo
	 * @param id_origem
	 * @throws Exception 
	 */
	public List<TemaDt> consultarTemaPorCodigoEOrigem(String codigo, String origem) throws Exception{
		List<TemaDt> dados = null;
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM projudi.VIEW_TEMA vw WHERE 1=1");
		if (codigo != null && !codigo.isEmpty()){
			sql.append(" AND vw.tema_codigo = ?");
			ps.adicionarLong(codigo);
		}
		if (origem != null && !origem.isEmpty()){
			sql.append(" AND vw.tema_origem = ?");
			ps.adicionarString(origem);
		}
		sql.append(" ORDER BY vw.tema_codigo");
		try{
			rs1 = consultar(sql.toString(), ps);
			while (rs1.next()){	
				if (dados == null) dados = new ArrayList<TemaDt>();
				dados.add(getAssociarDt(rs1));
			}			
			
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return dados; 
	}
	
	/**
	 * Consulta o maior código do Tema (precedente) de uma origem (STF, STJ, TJGO)
	 * @param id_origem
	 * @return
	 * @throws Exception
	 */
	public Integer consultarMaiorTemaCodigoPorOrigem(String origem) throws Exception {
		Integer ultimoTemaCodigo = 0;
		ResultSetTJGO rs = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "SELECT max(tema_codigo) as ultimo FROM projudi.VIEW_TEMA where tema_origem = ?";
		ps.adicionarString(origem);
		try{
			rs = consultar(sql, ps);
			ultimoTemaCodigo = rs.next() ? rs.getInt("ultimo") : 0;
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}
		return ultimoTemaCodigo;
	}
	
	/**
	 * Atualiza o valor da situação e data de trânsito do tema
	 * @throws Exception
	 */
	public void alterarSituacaoEDataTransito(TemaDt dados) throws Exception {		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE projudi.tema SET");
		if (ValidacaoUtil.isNaoVazio(dados.getId_TemaSituacao())){
			sql.append(" id_tema_situacao = ?");
			ps.adicionarLong(dados.getId_TemaSituacao());
		}
		if (ValidacaoUtil.isNaoVazio(dados.getDataTransito())){
			sql.append(", data_transito = ?");
			ps.adicionarDateTime(dados.getDataTransito());
		}
		sql.append(" WHERE id_tema = ?");
		ps.adicionarLong(dados.getId()); 
		executarUpdateDelete(sql.toString(), ps);
	}
	
	/**
	 * 
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	private TemaDt getAssociarDt(ResultSetTJGO rs) throws Exception{
		TemaDt dados = new TemaDt();
		associarDt(dados, rs);
		return dados;
	}
	
}
