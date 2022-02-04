package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioAtrasoEntregaPostalDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.ValidacaoUtil;

/**
 * 
 * @author mmitsunaga
 *
 */
public class RelatorioAtrasoEntregaPostalPs extends Persistencia {

	private static final long serialVersionUID = 6435787364328221077L;
	
	public RelatorioAtrasoEntregaPostalPs(Connection conexao) {
		Conexao = conexao;
	}
	
	public RelatorioAtrasoEntregaPostalPs() {
		
	}
	
	
	/**
	 * Consulta os processos e informações da pendência de postagem, para obter o total de dias de atraso.
	 * @param idServentia
	 * @param diasEspera
	 * @return
	 * @throws Exception
	 */
	public List<RelatorioAtrasoEntregaPostalDt> consultarAtrasoEntregaPostal(String idComarca, String idServentia, String diasEspera) throws Exception {
		
		List<RelatorioAtrasoEntregaPostalDt> lista = new ArrayList<>();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();			
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT vw.id_pend_correios");
		sql.append(" , vw.cod_rastreamento");
		sql.append(" , TO_CHAR(vw.data_expedicao, 'DD/MM/YYYY') AS data_postagem");
		sql.append(" , TO_CHAR(vw.data_entrega, 'DD/MM/YYYY') AS data_entrega");
		sql.append(" , TRUNC(SYSDATE - vw.data_entrega) AS dias_espera");
		sql.append(" , vw.mao_propria");
		sql.append(" , vw.id_proc");
		sql.append(" , p.proc_numero");
		sql.append(" , p.digito_verificador");
		sql.append(" , p.ano");
		sql.append(" , p.forum_codigo");
		sql.append(" , pp.nome");
		sql.append(" , vw.id_pend_tipo");
		sql.append(" , pt.pend_tipo");
		sql.append(" , p.id_serv");
		sql.append(" , s.serv");		
		sql.append(" FROM (");
		
		// Analisa as tabelas PEND e PEND_CORREIOS
		sql.append(" SELECT pc.id_pend_correios");
		sql.append(" , pc.data_expedicao");
		sql.append(" , pc.data_entrega");
		sql.append(" , pc.cod_rastreamento");
		sql.append(" , pc.mao_propria");
		sql.append(" , pe.id_pend");
		sql.append(" , pe.id_proc");
		sql.append(" , pe.id_proc_parte");
		sql.append(" , pe.data_inicio");
		sql.append(" , pe.id_pend_tipo");  
		sql.append(" FROM projudi.pend_correios pc");
		sql.append(" JOIN projudi.pend pe ON pe.id_pend = pc.id_pend AND pe.id_pend_status IN (" + PendenciaStatusDt.ID_CUMPRIDA + "," + PendenciaStatusDt.ID_NAO_CUMPRIDA + ") AND pe.data_fim IS NULL");
		sql.append(" WHERE pc.data_entrega IS NOT NULL"); 
		
		sql.append(") vw");
		
		sql.append(" INNER JOIN projudi.pend_tipo pt ON pt.id_pend_tipo = vw.id_pend_tipo");
		sql.append(" INNER JOIN projudi.proc_parte pp ON pp.id_proc_parte = vw.id_proc_parte");
		sql.append(" INNER JOIN projudi.proc p ON p.id_proc = pp.id_proc");
		sql.append(" INNER JOIN projudi.serv s ON p.id_serv = s.id_serv");
		
		sql.append(" WHERE s.id_comarca = ?");
		ps.adicionarLong(idComarca);
		
		if (ValidacaoUtil.isNaoVazio(idServentia)){		
			sql.append(" AND p.id_serv = ?");
			ps.adicionarLong(idServentia);			
		}
		
		sql.append(" GROUP BY vw.id_pend_correios");
		sql.append(" , vw.cod_rastreamento");
		sql.append(" , vw.data_expedicao");
		sql.append(" , vw.data_entrega");
		sql.append(" , vw.mao_propria");
		sql.append(" , vw.id_proc");
		sql.append(" , p.proc_numero");
		sql.append(" , p.digito_verificador");
		sql.append(" , p.ano");
		sql.append(" , p.forum_codigo");
		sql.append(" , pp.nome");
		sql.append(" , vw.id_pend_tipo");
		sql.append(" , pt.pend_tipo");
		sql.append(" , p.id_serv");
		sql.append(" , s.serv");
		
		sql.append(" HAVING TRUNC(SYSDATE - vw.data_entrega) > ?");
		ps.adicionarLong(diasEspera);
		
		sql.append(" ORDER BY s.serv, dias_espera DESC");
				
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()){				
				ProcessoDt processo = new ProcessoDt();
				processo.setProcessoNumero(rs.getString("PROC_NUMERO"));
				processo.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
				processo.setAno(rs.getString("ANO"));
				processo.setForumCodigo(rs.getString("FORUM_CODIGO"));				
				RelatorioAtrasoEntregaPostalDt data = new RelatorioAtrasoEntregaPostalDt();
				data.setIdServentia(rs.getString("ID_SERV"));
				data.setServentia(rs.getString("SERV"));
				data.setProcessoDt(processo);
				data.setCodigoRastreamento(rs.getString("COD_RASTREAMENTO"));
				data.setDataPostagem(rs.getString("DATA_POSTAGEM"));
				data.setDataEntrega(rs.getString("DATA_ENTREGA"));
				data.setMaoPropria(rs.getString("MAO_PROPRIA"));
				data.setDiasEspera(rs.getString("DIAS_ESPERA"));
				data.setIdPendTipo(rs.getString("ID_PEND_TIPO"));
				data.setPendTipo(rs.getString("PEND_TIPO"));
				data.setNomeParte(rs.getString("NOME"));
				lista.add(data);				
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e1) {}
		}
		
		return lista;
	}
	
}
