package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.TemaOrigemDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.ValidacaoUtil;
//---------------------------------------------------------
public class TemaOrigemPs extends TemaOrigemPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1358765440446000130L;
	
	public TemaOrigemPs(Connection conexao){
		Conexao = conexao;
	}
	
	/**
	 * Consulta um Tema Origem pelo seu código ou pela descrição
	 * @param codigo
	 * @param descricao
	 * @return
	 * @throws Exception 
	 */
	public TemaOrigemDt consultarPorCodigoOuDescricao(Integer codigo, String descricao) throws Exception{
		TemaOrigemDt temaOrigemDt = null;
		ResultSetTJGO rs = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM projudi.tema_origem WHERE 1=1");				
		if (ValidacaoUtil.isNaoVazio(codigo)){
			sql.append(" AND tema_origem_codigo = ?"); ps.adicionarLong(codigo);
		}
		if (ValidacaoUtil.isNaoVazio(descricao)){
			sql.append(" AND tema_origem = ?"); ps.adicionarString(descricao);
		}
		try{
			rs = consultar(sql.toString(), ps);
			if (rs.next()){
				temaOrigemDt = new TemaOrigemDt();
				associarDt(temaOrigemDt, rs);
			}			
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}
		return temaOrigemDt;
	}
	
	
//

}
