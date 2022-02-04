package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


/**
 * Classe de persistência para a tabela ARQ_INDEX
 * @author mmitsunaga
 *
 */
public class ArquivoIndexacaoPs extends Persistencia {

	private static final long serialVersionUID = -2144588598090262034L;
	
	public ArquivoIndexacaoPs(Connection conexao){
    	Conexao = conexao;
	}
	
	/**
	 * Lista os id´s dos arquivos elegíveis para indexação no elasticsearch.
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<Integer> listarIdsArquivosParaIndexacaoNoElasticSearch(int offset, int limit) throws Exception {
		List<Integer> lista = new ArrayList<Integer>();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();		
		sql.append(" SELECT a.*, ROWNUM rnum FROM (");
		sql.append(" 	SELECT ai.id_arq FROM projudi.arq_index ai INNER JOIN projudi.arq a ON a.id_arq = ai.id_arq AND a.data_indexacao IS NULL WHERE status = '0' AND ai.id_arq > ? ORDER BY ai.id_arq ASC");
		sql.append(" ) a WHERE ROWNUM <= ?");
		ps.adicionarLong(offset);
		ps.adicionarLong(limit);		
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				lista.add(rs.getInt("id_arq"));
			}
		} finally {
	        try{if (rs != null) rs.close();} catch(Exception e1) {}
		}
		return lista;
	}
	
	/**
	 * Atualiza o status do registro para 1, indicando que o arquivo já foi indexado.
	 * @param id_arq
	 * @param status
	 * @throws Exception
	 */
	public void atualizarStatus (String id, String status) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE projudi.arq_index SET status = ? WHERE id_arq = ?");		
		ps.adicionarString(status);
		ps.adicionarLong(id);
		executarUpdateDelete(sql.toString(),ps);
	}

}
