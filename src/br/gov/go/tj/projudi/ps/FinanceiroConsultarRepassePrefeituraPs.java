package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.FinanceiroConsultarRepassePrefeituraDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.OcorrenciaPrevisaoRepasseDt;
import br.gov.go.tj.projudi.dt.RelatorioRepassePrefeituraDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.TJDataHora;
/**
 * 
 * Classe:     FinanceiroConsultarRepassePrefeituraPs.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       06/2015  
 *             
 */
public class FinanceiroConsultarRepassePrefeituraPs extends Persistencia {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2140001759517228301L;
	
	
	public FinanceiroConsultarRepassePrefeituraPs(Connection conexao){		
    	Conexao = conexao;
	}
	
	
	/**
     * Obtem da base de dados as guias pagas no período informado
     * 
     * @param FinanceiroConsultarRepassePrefeituraDt financeiroConsultarRepassePrefeituraDt
     *  
     */
	public RelatorioRepassePrefeituraDt obtenhaRelatorioPrevisaoRepassePrefeitura(FinanceiroConsultarRepassePrefeituraDt financeiroConsultarRepassePrefeituraDt) throws Exception{	
	
		//Cria o relatório de retorno
		RelatorioRepassePrefeituraDt relatorio = new RelatorioRepassePrefeituraDt(financeiroConsultarRepassePrefeituraDt);
		
		//obtem da base de dados os registros
		carregueGuiasPagas(relatorio);
		
		return relatorio;
	}
	private void carregueGuiasPagas(RelatorioRepassePrefeituraDt relatorio) throws Exception{
		for (TJDataHora dataMovimento : relatorio.getListaDataMovimentoConsulta()) {
			carregueGuiasPagas(dataMovimento, relatorio);
		}
	}
	
	private void carregueGuiasPagas(TJDataHora dataMovimento, RelatorioRepassePrefeituraDt relatorio) throws Exception{		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		TJDataHora dataPrevistaRepasse = relatorio.getDataPrevisaoRepasse(dataMovimento);
		
		String sql = "SELECT GE.ID_GUIA_EMIS, GE.NUMERO_GUIA_COMPLETO, GE.ID_PROC, GE.VALOR_RECEBIMENTO, ";
		sql += " (SELECT TRIM((trim(TO_CHAR(PC.PROC_NUMERO, '0000000')) || trim(TO_CHAR(PC.DIGITO_VERIFICADOR,'00')) || PC.ANO || '809' || trim(TO_CHAR(PC.FORUM_CODIGO, '0000')))) FROM VIEW_PROC PC WHERE PC.ID_PROC = GE.ID_PROC AND ROWNUM = 1) AS NUMERO_PROC_COMPLETO,";
		sql += " (SELECT SUM(VALOR_CALCULADO) AS TOTAL FROM PROJUDI.GUIA_ITEM GI WHERE GI.ID_GUIA_EMIS = GE.ID_GUIA_EMIS) AS VALOR_TOTAL_GUIA, DATA_RECEBIMENTO";
		sql += " FROM PROJUDI.GUIA_EMIS GE";
		sql += " WHERE GE.ID_GUIA_MODELO = ? "; ps.adicionarLong(relatorio.getParametrosUtilizados().getIdGuiaModelo());		
		sql += " AND GE.ID_GUIA_STATUS IN (?, ?, ?, ?, ?, ?) "; 
		ps.adicionarLong(GuiaStatusDt.PAGA_CANCELADA); 
		ps.adicionarLong(GuiaStatusDt.PAGO); 
		ps.adicionarLong(GuiaStatusDt.PAGO_APOS_VENCIMENTO); 
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR);
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR);
		ps.adicionarLong(GuiaStatusDt.PAGO_ON_LINE);
		sql += " AND GE.DATA_MOVIMENTO = ? "; ps.adicionarDate(dataMovimento);
		sql += " AND NOT GE.VALOR_RECEBIMENTO IS NULL ";
		
		ResultSetTJGO rs = null;
		try{
			rs = consultar(sql, ps);
			if( rs != null ) {
				while( rs.next() ) {
					OcorrenciaPrevisaoRepasseDt ocorrencia = new OcorrenciaPrevisaoRepasseDt(rs.getString("ID_GUIA_EMIS"), rs.getString("NUMERO_GUIA_COMPLETO"), 
							                                                                 rs.getString("ID_PROC"), rs.getString("NUMERO_PROC_COMPLETO"), 
							                                                                 dataMovimento, new TJDataHora(rs.getDateTime("DATA_RECEBIMENTO")), dataPrevistaRepasse, 
							                                                                 rs.getString("VALOR_TOTAL_GUIA"), rs.getString("VALOR_RECEBIMENTO"));					
					relatorio.adicionePrevisaoDeRepasse(ocorrencia);
				}
			}
		
		}
		finally{
			try{if (rs != null) rs.close();} catch(Exception e1) {}
		}		
	}
}