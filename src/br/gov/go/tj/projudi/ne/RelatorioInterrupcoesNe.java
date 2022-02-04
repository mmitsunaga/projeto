package br.gov.go.tj.projudi.ne;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.gov.go.tj.projudi.dt.ParametroRelatorioInterrupcaoDt;
import br.gov.go.tj.projudi.dt.RelatorioInterrupcao;
import br.gov.go.tj.projudi.ps.RelatorioInterrupcoesPs;
import br.gov.go.tj.projudi.util.enumeradoresSeguros.EnumSistemaPingdom;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;
import br.gov.go.tj.utils.pdf.InterfaceJasper;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

/**
 * 
 * Classe:     RelatorioInterrupcoesNe.java
 * Autor:      M�rcio Mendon�a Gomes 
 * Data:       08/2010
 * Finalidade:  
 *             
 */
public class RelatorioInterrupcoesNe extends Negocio{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5900292809576332766L;
	
	/**
     * Obtem da base de dados as interrup��es, caso existam, do per�odo informado
     * 
     * @param ParametroRelatorioInterrupcaoDt paramRelInterrupcoesDt
     *  
     */
	public RelatorioInterrupcao obtenhaRelatorioInterrupcoes(ParametroRelatorioInterrupcaoDt paramRelInterrupcoesDt) throws Exception
	{		
		RelatorioInterrupcoesPs persistencia = null;
		FabricaConexao obFabricaConexao = null;
		try
		{
			 obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			 
			 persistencia = new  RelatorioInterrupcoesPs(obFabricaConexao.getConexao());
			
			 return persistencia.obtenhaInterrupcoes(paramRelInterrupcoesDt);			 
		}		
		finally{
			obFabricaConexao.fecharConexao();
		}	
	}
	
	/**
     * Executa o processamento autom�tico obtendo do webservice pingdom 
     * as indisponibilidades, caso existam, do per�odo entre a data posterior
     * � da �ltima interrup��o e a data do dia anterior � data do sistema (ontem)     
     *  
     */
	public void executeProcessamentoAutomatico() throws Exception{
		executeProcessamentoAutomaticoProjudi();
		//executeProcessamentoAutomaticoPJE();
	}
	
	/**
     * Executa o processamento autom�tico obtendo do webservice pingdom 
     * as indisponibilidades do Projudi, caso existam, do per�odo entre a data posterior
     * � da �ltima interrup��o e a data do dia anterior � data do sistema (ontem)     
     *  
     */
	public void executeProcessamentoAutomaticoProjudi() throws Exception{
		RelatorioInterrupcoesPs persistencia = null;
		FabricaConexao obFabricaConexao = null;
		try
		{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			obFabricaConexao.iniciarTransacao();
			 
			persistencia = new  RelatorioInterrupcoesPs(obFabricaConexao.getConexao());			
			
			persistencia.executeProcessamentoAutomatico(EnumSistemaPingdom.projudi);
			 
			obFabricaConexao.finalizarTransacao();
		}
		catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		}
		finally{
			obFabricaConexao.fecharConexao();
		}	
	}
	
	/**
     * Executa o processamento autom�tico obtendo do webservice pingdom 
     * as indisponibilidades do PJE, caso existam, do per�odo entre a data posterior
     * � da �ltima interrup��o e a data do dia anterior � data do sistema (ontem)     
     *  
     */
//	public void executeProcessamentoAutomaticoPJE() throws Exception{
//		RelatorioInterrupcoesPs persistencia = null;
//		FabricaConexao obFabricaConexao = null;
//		try
//		{
//			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//			obFabricaConexao.iniciarTransacao();
//			 
//			persistencia = new  RelatorioInterrupcoesPs(obFabricaConexao.getConexao());
//			
//			persistencia.executeProcessamentoAutomatico(EnumSistemaPingdom.pje);
//			 
//			obFabricaConexao.finalizarTransacao();
//		}
//		catch(Exception e) {
//			obFabricaConexao.cancelarTransacao();
//			throw e;
//		}
//		finally{
//			obFabricaConexao.fecharConexao();
//		}	
//	}
	
	/**
     * Retorna um array de bytes contendo o relat�rio em PDF de interru��es passados como par�metro
     * 
     * @param RelatorioInterrupcao relatorio
     * @param String diretorioProjeto
     * @param String nomeSolicitante
	 * @throws Exception 
     *  
     */
	public byte[] relListagemDeInterrupcoes(RelatorioInterrupcao relatorio,String diretorioProjeto,String nomeSolicitante) throws Exception{
		
		byte[] temp = null;
		ByteArrayOutputStream baos = null;	
						
		try{		
			
		    InterfaceJasper ei = new InterfaceJasper(relatorio.obtenhaDetalhesImpressaoPDF());

		    // PATH PARA OS ARQUIVOS JASPER DO RELATORIO
		    // MOVIMENTACAO*****************************************************
		    String pathJasper = Funcoes.getPathRelatorio(diretorioProjeto,"ListagemIndisponibilidades.jasper");  

		    // par�metros do relat�rio
		    Map parametros = obtemParametrosJasper(relatorio, diretorioProjeto, nomeSolicitante);

		    JasperPrint jp = JasperFillManager.fillReport(pathJasper, parametros, ei);

		    JRPdfExporter jr = new JRPdfExporter();
		    jr.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		    baos = new ByteArrayOutputStream();
		    jr.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, baos);
		    jr.exportReport();

		    temp = baos.toByteArray();
		
		} finally{		    
		    baos = null;
		}
		return temp;
	}

	/**
     * Prepara e retorna os par�metros que ser�o utilizados pelo Jasper
     * 
     * @param RelatorioInterrupcao relatorio
     * @param String diretorioProjeto
     * @param String nomeSolicitante
     *  
     */
	private Map obtemParametrosJasper(RelatorioInterrupcao relatorio, 
			                          String diretorioProjeto, 
			                          String nomeSolicitante){
		
		Map parametros = new HashMap();
		parametros.put("nomeSistema", relatorio.getParametrosUtilizados().getNomeDoSistema());
		parametros.put("nomeSistemaTitulo", relatorio.getParametrosUtilizados().getNomeDoSistemaCompleto());
		parametros.put("urlSistema", relatorio.getParametrosUtilizados().getURLDoSistema());
		parametros.put("diaMesAnoFinal", relatorio.getParametrosUtilizados().getPeriodoFinalUtilizado().getDataFormatadaddMMyyyy());
		parametros.put("caminhoLogo", Funcoes.getCaminhoLogotipoRelatorio(diretorioProjeto));
		parametros.put("percentualUltimas24Horas", relatorio.getPercentualDeDisponibilidadeNasUltimas24Horas());
		parametros.put("percentualPeriodo", relatorio.getPercentualDeDisponibilidadeNoPeriodo());
		
		String periodoUtilizado = relatorio.getParametrosUtilizados().getPeriodoInicialUtilizado().getDataFormatadaddMMyyyy();
		periodoUtilizado  += " a ";
		periodoUtilizado += relatorio.getParametrosUtilizados().getPeriodoFinalUtilizado().getDataFormatadaddMMyyyy();
		if (relatorio.getParametrosUtilizados().getPeriodoFinalUtilizado().isHoje()) {
			periodoUtilizado += " �s " + new TJDataHora().getHoraFormatadaHHmm();
		} 
		
		parametros.put("periodoRelatorio", periodoUtilizado);
		
		parametros.put("totalDeInterrupcoes", String.valueOf(relatorio.getQuantidadeDeInterrupcoes()));		    
		parametros.put("nomeSolicitante", nomeSolicitante);
		parametros.put("dataAtual", new Date());
		
		return parametros;
	}
}
