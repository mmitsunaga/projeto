package br.gov.go.tj.projudi.ne;

import java.util.Iterator;
import java.util.List;

import br.gov.go.tj.projudi.dt.FinanceiroConsultarRepassePrefeituraWebServiceDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.OcorrenciaPrevisaoRepasseWebServiceDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.RelatorioRepassePrefeituraWebServiceDt;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

/**
 * 
 * Classe:     FinanceiroConsultarRepassePrefeituraWebServiceNe.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       07/2015
 *             
 */
public class FinanceiroConsultarRepassePrefeituraWebServiceNe extends Negocio{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3552477206014712492L;

	public RelatorioRepassePrefeituraWebServiceDt obtenhaRelatorioRepassePrefeitura(FinanceiroConsultarRepassePrefeituraWebServiceDt parametros) throws Exception
	{		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);		
		RelatorioRepassePrefeituraWebServiceDt relatorio = new RelatorioRepassePrefeituraWebServiceDt();
		
		String[][] listaRegistros = new GuiaNe().consultarGuiaProcessualPaga(String.valueOf(parametros.getDataMovimento().getDia()), String.valueOf(parametros.getDataMovimento().getMes()), String.valueOf(parametros.getDataMovimento().getAno()));
		
		try
		{
			GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
			boolean possuiGuiaSemRepasse = false;
			
			boolean possuiGuiaSemPagamento = false;
			relatorio.setDataPagamentoConfirmado(new TJDataHora());
			
			if (listaRegistros != null) {
				
				for (int i=0; i< listaRegistros.length; i++ ) {
					if (listaRegistros[i].length >= 7) {					
						OcorrenciaPrevisaoRepasseWebServiceDt ocorrenciaPrevisaoRepasse = new OcorrenciaPrevisaoRepasseWebServiceDt(listaRegistros[i][0], listaRegistros[i][1], listaRegistros[i][2], listaRegistros[i][3], listaRegistros[i][4], listaRegistros[i][5], listaRegistros[i][6]);
						relatorio.adicioneRepasse(ocorrenciaPrevisaoRepasse);						
						
						//Consulta a guia emissão no projudi.
						GuiaEmissaoDt guiaEmissaoDt = guiaEmissaoNe.consultarGuiaEmissaoNumeroGuia(ocorrenciaPrevisaoRepasse.getNumeroGuia(), obFabricaConexao);
						
						//Atualiza o número do processo na guia.
						AtualizeNumeroDoProcessoNaGuia(guiaEmissaoDt);
						
						//Verifica se a guia existente na prefeitura é desse processo.
						if (guiaEmissaoDt == null || !Funcoes.formataNumeroCompletoProcesso(guiaEmissaoDt.getNumeroProcesso()).equalsIgnoreCase(Funcoes.formataNumeroCompletoProcesso(ocorrenciaPrevisaoRepasse.getNumeroProcesso()))) {
							ProcessoDt processoDt = new ProcessoNe().consultarProcessoNumeroCompleto(Funcoes.formataNumeroCompletoProcesso(ocorrenciaPrevisaoRepasse.getNumeroProcesso()),null);
							if (processoDt != null) {
								guiaEmissaoDt = guiaEmissaoNe.consultarUltimaGuiaEmissao(processoDt.getId(), GuiaTipoDt.ID_PREFEITURA_AUTOMATICA , obFabricaConexao);
								AtualizeNumeroDoProcessoNaGuia(guiaEmissaoDt, processoDt);	
							} else if (!Funcoes.formataNumeroProcessoDigitoAno(guiaEmissaoDt.getNumeroProcesso()).equalsIgnoreCase(Funcoes.formataNumeroProcessoDigitoAno(ocorrenciaPrevisaoRepasse.getNumeroProcesso()))) {
								throw new MensagemException("Não existe processo cadastrado com o número " + Funcoes.formataNumeroCompletoProcesso(guiaEmissaoDt.getNumeroProcesso()) + ".");
							}
						}
						
						//Verifica repasse.
						if (guiaEmissaoDt != null && guiaEmissaoDt.getDataRepasse() != null && guiaEmissaoDt.getDataRepasse().trim().length() > 0) {							
							if (i==0) relatorio.setDataRepasseConfirmado(new TJDataHora(Funcoes.FormatarData(guiaEmissaoDt.getDataRepasse())));
						} else {
							possuiGuiaSemRepasse = true;
						}			
						
						//Verifica se o pagamento foi informado através da execução automática
						if (guiaEmissaoDt != null) {
							if (Funcoes.StringToInt(guiaEmissaoDt.getId_GuiaStatus()) == GuiaStatusDt.AGUARDANDO_PAGAMENTO ||
								Funcoes.StringToInt(guiaEmissaoDt.getId_GuiaStatus()) == GuiaStatusDt.ESTORNO_BANCARIO ||
								guiaEmissaoDt.getDataRecebimento() == null) {
								possuiGuiaSemPagamento = true;
							} else {
								if (i==0) relatorio.setDataPagamentoConfirmado(new TJDataHora(Funcoes.FormatarData(guiaEmissaoDt.getDataRecebimento())));
							}	
						}							
					}
				}
				
				if (possuiGuiaSemRepasse) relatorio.setDataRepasseConfirmado(null);
				if (possuiGuiaSemPagamento) relatorio.setDataPagamentoConfirmado(null);				
			}
		}
		finally 
		{
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();			
		}
		
		return relatorio;
	}

	public StringBuilder salveRepasseGuiasProjudi(FinanceiroConsultarRepassePrefeituraWebServiceDt financeiroConsultarRepassePrefeituraDt) throws MensagemException, Exception {
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		LogNe logNe = new LogNe();
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		StringBuilder mensagemRetorno = new StringBuilder();
		try
		{
			GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
			
			Iterator<OcorrenciaPrevisaoRepasseWebServiceDt> iterator = financeiroConsultarRepassePrefeituraDt.getRelatorio().getIteratorOcorrenciaRepasseDiarias();
			OcorrenciaPrevisaoRepasseWebServiceDt objTemp = null;
			
			while (iterator.hasNext()){
				objTemp = iterator.next();
				
				//Consulta a guia emissão no projudi.
				GuiaEmissaoDt guiaEmissaoDt = guiaEmissaoNe.consultarGuiaEmissaoNumeroGuia(objTemp.getNumeroGuia(), obFabricaConexao);
				
				if (guiaEmissaoDt != null) {
					//Atualiza a guia no projudi.
					AtualizeNumeroDoProcessoNaGuia(guiaEmissaoDt);
					
					guiaEmissaoDt = verifiqueEAtualizeGuiaNoProcesso(guiaEmissaoNe, guiaEmissaoDt, objTemp, mensagemRetorno, obFabricaConexao);
					
					if (guiaEmissaoDt == null) continue;
					
					//Atualiza a data do repasse no projudi
					guiaEmissaoNe.atualizarRepasse(guiaEmissaoDt.getNumeroGuiaCompleto(), objTemp.getValorcustas(), financeiroConsultarRepassePrefeituraDt.getDataMovimento(), obFabricaConexao);
					
					obLogDt = new LogDt("GuiaEmissao", guiaEmissaoDt.getNumeroGuiaCompleto(), financeiroConsultarRepassePrefeituraDt.getId_Usuario(), financeiroConsultarRepassePrefeituraDt.getIpComputadorLog(), String.valueOf(LogTipoDt.RepassePrefeituraGoiania), "", financeiroConsultarRepassePrefeituraDt.getDataMovimento().getDataHoraFormatadayyyyMMdd());
					logNe.salvar(obLogDt, obFabricaConexao);
					
					if (objTemp.isMovimentoEhPagamento()) {// Pagamento					
						//Consultando os itens da guia no projudi
						List listaGuiaItemDt = guiaEmissaoNe.consultarGuiaItens(guiaEmissaoDt.getId(), guiaEmissaoDt.getId_GuiaTipo());
						
						if (!guiaEmissaoDt.isGuiaPaga()) {
							new GuiaNe().atualizarGuiaPagaPrefeituraTipoMovimentoPagamento(guiaEmissaoDt, financeiroConsultarRepassePrefeituraDt.getDataMovimento(), objTemp.getValorcustas(), objTemp.getDataArrecadacao().getDataHoraFormatadaaaaa_MM_ddHHmmss(), objTemp.getNumeroBancoPagamento(), objTemp.getNumeroAgenciaPagamento(), guiaEmissaoNe, logNe);
						}
						
						//Envia Guia paga para Cadastro no SPG/SAJ
						guiaSPGNe.inserirPagamentoGuiaSPGSAJ(guiaEmissaoDt, listaGuiaItemDt, objTemp.getDataArrecadacao(), objTemp.getValorcustas(), objTemp.getNumeroBancoPagamento(), objTemp.getNumeroAgenciaPagamento());
						//guiaSPGNe.atualizarPagamentoGuiaPrefeituraDeGoianiaSAJ(guiaEmissaoDt, listaGuiaItemDt, objTemp.getDataArrecadacao(), objTemp.getValorcustas(), objTemp.getNumeroBancoPagamento(), objTemp.getNumeroAgenciaPagamento());
					} else {// Estorno
						//Envia Guia para Baixa no SPG
						guiaSPGNe.estornarPagamentoDevolucaoCheque(guiaEmissaoDt);					
					}	
				}
			}
		}
		finally 
		{
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();			
		}
		
		return mensagemRetorno;
	}
	
	private void AtualizeNumeroDoProcessoNaGuia(GuiaEmissaoDt guiaEmissaoDt) throws Exception
	{
		if (guiaEmissaoDt == null || guiaEmissaoDt.getProcessoDt() != null) return;
		guiaEmissaoDt.setNumeroProcesso("");
		ProcessoDt processoDt = new ProcessoNe().consultarIdCompleto(guiaEmissaoDt.getId_Processo());
		AtualizeNumeroDoProcessoNaGuia(guiaEmissaoDt, processoDt);
	}
	
	private void AtualizeNumeroDoProcessoNaGuia(GuiaEmissaoDt guiaEmissaoDt, ProcessoDt processoDt) throws Exception {
		if (guiaEmissaoDt != null && processoDt != null) {
			guiaEmissaoDt.setNumeroProcesso(Funcoes.retiraVirgulaPonto(processoDt.getProcessoNumeroCompleto()));
			guiaEmissaoDt.setProcessoDt(processoDt);
		}
	}
	
	public StringBuilder salvePagamentoGuiasProjudi(FinanceiroConsultarRepassePrefeituraWebServiceDt financeiroConsultarRepassePrefeituraDt) throws MensagemException, Exception {
		LogNe logNe = new LogNe();
		GuiaNe guiaNe = new GuiaNe();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		StringBuilder mensagemRetorno = new StringBuilder();
		try
		{
			GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
			
			Iterator<OcorrenciaPrevisaoRepasseWebServiceDt> iterator = financeiroConsultarRepassePrefeituraDt.getRelatorio().getIteratorOcorrenciaRepasseDiarias();
			OcorrenciaPrevisaoRepasseWebServiceDt objTemp = null;
			
			while (iterator.hasNext()){
				objTemp = iterator.next();
				
				//Consulta a guia emissão no projudi.
				GuiaEmissaoDt guiaEmissaoDt = guiaEmissaoNe.consultarGuiaEmissaoNumeroGuia(objTemp.getNumeroGuia(), obFabricaConexao);
				
				if (guiaEmissaoDt != null) {
					//Atualiza a guia no projudi.
					AtualizeNumeroDoProcessoNaGuia(guiaEmissaoDt);
					
					guiaEmissaoDt = verifiqueEAtualizeGuiaNoProcesso(guiaEmissaoNe, guiaEmissaoDt, objTemp, mensagemRetorno, obFabricaConexao);
					
					if (guiaEmissaoDt == null) continue;
					
					if (objTemp.isMovimentoEhPagamento()) {// Pagamento					
						guiaNe.atualizarGuiaPagaPrefeituraTipoMovimentoPagamento(guiaEmissaoDt, financeiroConsultarRepassePrefeituraDt.getDataMovimento(), objTemp.getValorcustas(), objTemp.getDataArrecadacao().getDataHoraFormatadaaaaa_MM_ddHHmmss(), objTemp.getNumeroBancoPagamento(), objTemp.getNumeroAgenciaPagamento(), guiaEmissaoNe, logNe);
					} else {// Estorno
						guiaNe.atualizarGuiaPagaPrefeituraTipoMovimentoEstorno(guiaEmissaoDt, guiaEmissaoNe, logNe);	
					}	
				}
			}
		}
		finally 
		{
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();			
		}
		
		return mensagemRetorno;
	}
	
	private GuiaEmissaoDt verifiqueEAtualizeGuiaNoProcesso(GuiaEmissaoNe guiaEmissaoNe, 
			                                               GuiaEmissaoDt guiaEmissaoDt, 
			                                               OcorrenciaPrevisaoRepasseWebServiceDt objTemp, 
			                                               StringBuilder mensagemRetorno,
			                                               FabricaConexao obFabricaConexao) throws Exception {
		String numeroDoProcessoFormatado = Funcoes.formataNumeroCompletoProcesso(objTemp.getNumeroProcesso()); 
		if (guiaEmissaoDt.getProcessoDt() != null && 
			!numeroDoProcessoFormatado.equalsIgnoreCase(guiaEmissaoDt.getProcessoDt().getProcessoNumeroCompleto())) {						
			
			ProcessoDt processoDt = new ProcessoNe().consultarProcessoNumeroCompleto(numeroDoProcessoFormatado,null);
			if (processoDt == null) {
				if (Funcoes.formataNumeroProcessoDigitoAno(objTemp.getNumeroProcesso()).equalsIgnoreCase(Funcoes.formataNumeroProcessoDigitoAno(guiaEmissaoDt.getProcessoDt().getProcessoNumeroCompleto())))
					return guiaEmissaoDt;
				
				if (mensagemRetorno.length() > 0) mensagemRetorno.append("\n");
 				mensagemRetorno.append("Não existe processo cadastrado com o número " + numeroDoProcessoFormatado + ".");
				//throw new MensagemException(mensagemRetorno.toString());
			}
			
			guiaEmissaoDt = guiaEmissaoNe.consultarGuiaPrefeituraGoianiaAguardandoPagamento(processoDt.getId(), obFabricaConexao);
			
			if (guiaEmissaoDt == null) guiaEmissaoDt = guiaEmissaoNe.consultarGuiaPrefeituraGoianiaPaga(processoDt.getId(), obFabricaConexao);
			
			if (guiaEmissaoDt == null) {
				if (mensagemRetorno.length() > 0) mensagemRetorno.append("\n");
 				mensagemRetorno.append("Não existe guia da prefeitura com status aguardando pagamento do processo número " + numeroDoProcessoFormatado + ".");
				//throw new MensagemException(mensagemRetorno.toString());				
			}
			
			AtualizeNumeroDoProcessoNaGuia(guiaEmissaoDt, processoDt);
		}
		return guiaEmissaoDt;
	}
}
