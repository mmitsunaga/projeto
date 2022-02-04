package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.TemaSituacaoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.ValidacaoUtil;
//---------------------------------------------------------
public class TemaSituacaoPs extends TemaSituacaoPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2739411252909611258L;
	public TemaSituacaoPs(Connection conexao){
		Conexao = conexao;
	}
	
	/**
	 * Consulta a Situação do Tema pelo seu código ou pelo campo Situacao CNJ
	 * @param codigo
	 * @param descricao
	 * @return
	 * @throws Exception 
	 */
	public TemaSituacaoDt consultarPorCodigoOuSituacaoCNJ(Integer codigo, String situacaoCNJ) throws Exception{
		TemaSituacaoDt temaSituacaoDt = null;
		ResultSetTJGO rs = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM projudi.tema_situacao WHERE 1=1");				
		if (ValidacaoUtil.isNaoVazio(codigo)){
			sql.append(" AND tema_situacao_codigo = ?"); ps.adicionarLong(codigo);
		}
		if (ValidacaoUtil.isNaoVazio(situacaoCNJ)){
			sql.append(" AND tema_situacao_cnj = ?"); ps.adicionarString(situacaoCNJ);
		}
		try{
			rs = consultar(sql.toString(), ps);
			if (rs.next()){
				temaSituacaoDt = new TemaSituacaoDt();
				associarDt(temaSituacaoDt, rs);
			}			
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}
		return temaSituacaoDt;
	}
	
}
