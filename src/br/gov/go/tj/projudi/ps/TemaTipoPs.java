package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.TemaTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.ValidacaoUtil;
//---------------------------------------------------------
public class TemaTipoPs extends TemaTipoPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5418504181057589316L;
	public TemaTipoPs(Connection conexao){
		Conexao = conexao;
	}
	
	
	/**
	 * Consulta o Tipo do Tema pelo seu código ou pelo campo tipo CNJ
	 * @param codigo
	 * @param descricao
	 * @return
	 * @throws Exception 
	 */
	public TemaTipoDt consultarPorCodigoOuTipoCNJ(Integer codigo, String tipoCNJ) throws Exception {
		TemaTipoDt temaTipoDt = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM projudi.tema_tipo WHERE 1=1");				
		if (ValidacaoUtil.isNaoVazio(codigo)){
			sql.append(" AND tema_tipo_codigo = ?"); ps.adicionarLong(codigo);
		}
		if (ValidacaoUtil.isNaoVazio(tipoCNJ)){
			sql.append(" AND tema_tipo_cnj = ?"); ps.adicionarString(tipoCNJ);
		}
		try{
			rs = consultar(sql.toString(), ps);
			if (rs.next()){
				temaTipoDt = new TemaTipoDt();
				associarDt(temaTipoDt, rs);
			}			
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}
		return temaTipoDt;
	}
//

}
