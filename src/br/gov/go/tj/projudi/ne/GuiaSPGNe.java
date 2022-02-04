package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArrecadacaoCustaDt;
import br.gov.go.tj.projudi.dt.CertidaoGuiaDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.InfoRepasseSPGDt;
import br.gov.go.tj.projudi.dt.LocomocaoDt;
import br.gov.go.tj.projudi.dt.LocomocaoSPGDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.NaturezaSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.SajaDocumentoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.boletos.BoletoDt;
import br.gov.go.tj.projudi.ps.GuiaSPGPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

public class GuiaSPGNe extends Negocio {

	private static final long serialVersionUID = -8780498124092406386L;
	
	public static final String TIPO_INTERLOCUTORIA_APELACAO = "3";
	
	//Op��es de status no SPGA_GUIAS que s�o de pedido de ressarcimento
	public static final int PEDIDO_RESSARCIMENTO_TIPO_4 = 4;
	public static final int PEDIDO_RESSARCIMENTO_TIPO_5 = 5;
	public static final int PEDIDO_RESSARCIMENTO_TIPO_6 = 6;
	public static final int PEDIDO_RESSARCIMENTO_TIPO_7 = 7;
	public static final int PEDIDO_RESSARCIMENTO_TIPO_8 = 8;
	public static final int PEDIDO_RESSARCIMENTO_TIPO_9 = 9;
	
	private GuiaSPGPs guiaSPGPs;
	
	public GuiaSPGNe() {
		obLog = new LogNe();
	}

	/**
	 * M�todo para inserir a guia no SPG.
	 * @param GuiaEmissaoDt
	 * @param List<GuiaItemDt>
	 * @param FabricaConexao obFabricaConexaoPJD
	 * @throws Exception
	 */
	public void inserirGuiaSPG(GuiaEmissaoDt guiaEmissaoDt, List listaGuiaItemDt, FabricaConexao obFabricaConexaoPJD) throws MensagemException, Exception {
		
		FabricaConexao obFabricaConexao =null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			String idGuiaTipo = guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo();
			
			switch( idGuiaTipo ) {
				case GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU :				
				{
					guiaSPGPs.inserirGuiaInicial(guiaEmissaoDt, listaGuiaItemDt, obFabricaConexaoPJD);
					
					break;
				}
				
				case GuiaTipoDt.ID_FINAL :
				case GuiaTipoDt.ID_FINAL_ZERO : {
					guiaSPGPs.inserirGuiaFinal(guiaEmissaoDt, listaGuiaItemDt);
					
					break;
				}
				
				case GuiaTipoDt.ID_LOCOMOCAO : {
					guiaSPGPs.inserirGuiaLocomocao(guiaEmissaoDt, listaGuiaItemDt);
					
					break;
				}
				
				case GuiaTipoDt.ID_FINAL_EXECUCAO_SENTENCA :
				case GuiaTipoDt.ID_FINAL_EXECUCAO_QUEIXA_CRIME :{
					guiaSPGPs.inserirGuiaLocomocao(guiaEmissaoDt, listaGuiaItemDt);
					
					break;
				}
				
				case GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU : {
					//TODO Fred: Somente liberar depois de receber a especifica��o da Mirian
					guiaSPGPs.inserirGuiaComplementar(guiaEmissaoDt, listaGuiaItemDt, null);
					
					break;
				}
				
				case GuiaTipoDt.ID_RECURSO :
				case GuiaTipoDt.ID_RECURSO_INOMINADO : 
				case GuiaTipoDt.ID_RECURSO_INOMINADO_QUEIXA_CRIME : {
					guiaSPGPs.inserirGuiaComplementar(guiaEmissaoDt, listaGuiaItemDt, TIPO_INTERLOCUTORIA_APELACAO);
					
					break;
				}
				
				case GuiaTipoDt.ID_PREFEITURA_AUTOMATICA :
				{
					guiaSPGPs.inserirGuiaPrefeituraDeGoiania(guiaEmissaoDt, listaGuiaItemDt);
					
					break;
				}
				
				default : {
					guiaSPGPs.inserirGuiaComplementar(guiaEmissaoDt, listaGuiaItemDt, null);
					
					break;
				}
				
			}
		
		}
		finally{
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
	}
	
	public void cancelarGuiaSPG(GuiaEmissaoDt guiaEmissaoDt) throws MensagemException, Exception {
		FabricaConexao obFabricaConexao =null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			String idGuiaTipo = guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo();
			
			switch( idGuiaTipo ) {
				case GuiaTipoDt.ID_PREFEITURA_AUTOMATICA :
				{
					guiaSPGPs.cancelarGuiaPrefeituraDeGoiania(guiaEmissaoDt);
					
					break;
				}
				
				default : {										
					break;
				}
				
			}
		
		}
		finally{
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
	}
	
	/**
	 * M�todo para consultar se a guia existe no SPG.
	 * Retorna true se sim e false caso n�o exista.
	 * 
	 * @param String numeroGuia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaPresenteSPG(String numeroGuia, GuiaEmissaoDt guiaEmissaoDt) throws MensagemException, Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
			retorno = guiaSPGPs.isGuiaPresenteSPG(numeroGuia, guiaEmissaoDt);
		
		}
		finally{
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
		
		return retorno;
	}
	
	/**
	 * M�todo para atualizar o tipo da guia no spg.
	 * @param String numeroGuia
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @throws Exception
	 */
	public void atualizarTipoGuiaSPG(String numeroGuia, GuiaEmissaoDt guiaEmissaoDt) throws MensagemException, Exception {
		
		guiaSPGPs.atualizarTipoGuiaSPG(numeroGuia, guiaEmissaoDt);
		
	}
	
	/**
	 * M�todo Alterar o valor da taxa judici�ria da guia no spg.
	 * @param String numeroGuia
	 * @param String codigoArrecadacao
	 * @param String valorCorreto
	 * @throws Exception
	 */
	public void atualizarTaxaJudiciariaGuiaSPG(String numeroGuia, String codigoArrecadacao, String valorCorreto, GuiaEmissaoDt guiaEmissaoDt) throws MensagemException, Exception {
		guiaSPGPs.atualizarTaxaJudiciariaGuiaSPG(numeroGuia, codigoArrecadacao, valorCorreto, guiaEmissaoDt);
	}
	
	public boolean isConexaoSPG_OK() throws MensagemException, Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao =null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
			retorno = true;
		}
		catch(Exception e) {
			retorno = false;
		}
		finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
		
		return retorno;
	}
	
	/**
	 * M�todo para inserir o pagamento da guia do SPG no SAJ.
	 * @param GuiaEmissaoDt
	 * @param List<GuiaItemDt>
	 * @param Date - Data de Pagamento
	 * @throws Exception
	 */
	public void inserirPagamentoGuiaSPGSAJ(GuiaEmissaoDt guiaEmissaoDt, List listaGuiaItemDt, TJDataHora dataPagamento, String valorTotalGuia, String numeroBancoPagamento, String numeroAgenciaPagamento) throws MensagemException, Exception {
		
		FabricaConexao obFabricaConexao =null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			String idGuiaTipo = guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo();
			
			switch( idGuiaTipo ) {
				
				case GuiaTipoDt.ID_PREFEITURA_AUTOMATICA :
					{
						guiaSPGPs.inserirPagamentoGuiaPrefeituraDeGoianiaSAJ(guiaEmissaoDt, listaGuiaItemDt, dataPagamento, valorTotalGuia, numeroBancoPagamento, numeroAgenciaPagamento);
						guiaSPGPs.atualizeIndicadorGuiaPrefeituraDeGoiania(guiaEmissaoDt);
						
						break;
					}
					
					default : {
						throw new MensagemException("O tipo da guia " + guiaEmissaoDt.getNumeroGuiaCompleto() + " n�o � prefeitura autom�tica, tipo " + idGuiaTipo + "-" + guiaEmissaoDt.getGuiaModeloDt().getGuiaTipo() + " Status: " + guiaEmissaoDt.getGuiaStatus() + " Processo: " + guiaEmissaoDt.getNumeroProcesso() + ".");
					}
				
			}
		
		}
		finally{
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
	}
	
	public void inserirPagamentoRajadaGuiaSPGSAJ(GuiaEmissaoDt guiaEmissaoDt, 
			                                     TJDataHora dataHoraGeracao, 
			                                     Double ValorPago, 
			                                     FabricaConexao obFabricaConexaoAdabas) throws MensagemException, Exception {		
		FabricaConexao obFabricaConexao = null;
		
		try{
			if (obFabricaConexaoAdabas != null) {
				obFabricaConexao = obFabricaConexaoAdabas;
			} else {
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			}			
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			String isnGuiaSAJ = guiaSPGPs.obtenhaISNGuiaSAJ(guiaEmissaoDt);
			
			if (isnGuiaSAJ == null || isnGuiaSAJ.trim().length() == 0) {
				guiaSPGPs.inserirPagamentoRajadaGuiaSAJ(guiaEmissaoDt, dataHoraGeracao, ValorPago);	
			} else {
				String codigoDocumento = guiaSPGPs.obtenhaCodigoDocumento(guiaEmissaoDt);
				if (codigoDocumento != null && codigoDocumento.trim().startsWith("19000101")) {
					guiaSPGPs.excluaSajaDocumento(isnGuiaSAJ);
					guiaSPGPs.inserirPagamentoRajadaGuiaSAJ(guiaEmissaoDt, dataHoraGeracao, ValorPago);	
				}
			}
		}
		finally{
			if (obFabricaConexaoAdabas == null && obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
	}
	
	public boolean pagamentoRajadaGuiaSPGSAJFoiEstornado(GuiaEmissaoDt guiaEmissaoDt, FabricaConexao obFabricaConexaoAdabas) throws MensagemException, Exception {
		boolean foiEstornado = false;
		FabricaConexao obFabricaConexao = null;
		
		try{
			if (obFabricaConexaoAdabas != null) {
				obFabricaConexao = obFabricaConexaoAdabas;
			} else {
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			}			
			guiaSPGPs = new GuiaSPGPs(obFabricaConexao.getConexao());
			
			String isnGuiaSAJ = guiaSPGPs.obtenhaISNGuiaSAJ(guiaEmissaoDt);
			
			if (isnGuiaSAJ == null || isnGuiaSAJ.trim().length() == 0) {
				foiEstornado = true;
			} else {
				String codigoDocumento = guiaSPGPs.obtenhaCodigoDocumento(guiaEmissaoDt);
				if (codigoDocumento != null && 
					codigoDocumento.trim().startsWith("19000101")) {
					guiaSPGPs.excluaSajaDocumento(isnGuiaSAJ);
					foiEstornado = true;
				}
			}
		}
		finally{
			if (obFabricaConexaoAdabas == null && obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
		
		return foiEstornado;
	}
	
	public String obtenhaISNGuiaSAJ(GuiaEmissaoDt guiaEmissaoDt, FabricaConexao obFabricaConexaoAdabas) throws MensagemException, Exception {
		FabricaConexao obFabricaConexao = null;
		
		try{
			if (obFabricaConexaoAdabas != null) {
				obFabricaConexao = obFabricaConexaoAdabas;
			} else {
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			}			
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			return guiaSPGPs.obtenhaISNGuiaSAJ(guiaEmissaoDt);
		}
		finally{
			if (obFabricaConexaoAdabas == null && obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
	}
	
	public void atualizarParaTransmitidaGuiaSPG(GuiaEmissaoDt guiaEmissaoDt) throws MensagemException, Exception {
		FabricaConexao obFabricaConexao =null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			String idGuiaTipo = guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo();
			
			switch( idGuiaTipo ) {
				case GuiaTipoDt.ID_PREFEITURA_AUTOMATICA :
				{
					guiaSPGPs.atualizarParaTransmitidaGuiaPrefeituraDeGoiania(guiaEmissaoDt);
					
					break;
				}
				
				default : {										
					break;
				}
				
			}
		
		}
		finally{
            if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
	}
	
	public void atualizarParaRecalculadaGuiaSPG(GuiaEmissaoDt guiaEmissaoDt) throws MensagemException, Exception {
		FabricaConexao obFabricaConexao =null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			String idGuiaTipo = guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo();
			
			switch( idGuiaTipo ) {
				case GuiaTipoDt.ID_PREFEITURA_AUTOMATICA :
				{
					guiaSPGPs.atualizarParaRecalculadaGuiaPrefeituraDeGoiania(guiaEmissaoDt);
					
					break;
				}
				
				default : {										
					break;
				}
				
			}
		
		}
		finally{
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
	}

	public void estornarPagamentoDevolucaoCheque(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		FabricaConexao obFabricaConexao =null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			String idGuiaTipo = guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo();
			
			switch( idGuiaTipo ) {
				case GuiaTipoDt.ID_PREFEITURA_AUTOMATICA :
				{
					guiaSPGPs.atualizarParaEstornoPagamentoDevolucaoChequeGuiaPrefeituraDeGoiania(guiaEmissaoDt);
					
					break;
				}
				
				default : {										
					break;
				}
				
			}
		
		}
		finally{
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
		
	}

	public List<LocomocaoDt> consultarLocomocaoNaoUtilizada(List<LocomocaoDt> locomocoesNaoUtilizadasProjudi, String comarcaCodigo, boolean validaOficialVinculadoLocomocao) throws Exception {
		return consultarLocomocao(locomocoesNaoUtilizadasProjudi, comarcaCodigo, true, validaOficialVinculadoLocomocao);
	}
	
	public List<LocomocaoDt> consultarLocomocaoNaoUtilizadaGuiaComplementar(List<LocomocaoDt> locomocoesComplementaresProjudi, String comarcaCodigo, boolean validaOficialVinculadoLocomocao) throws Exception {
		return consultarLocomocao(locomocoesComplementaresProjudi, comarcaCodigo, false, validaOficialVinculadoLocomocao);
	}
	
	public List<LocomocaoSPGDt> consultarLocomocoesNaoUtilizadas(String numeroGuia, String comarcaCodigo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			List<String> numerosDeGuia = new ArrayList<String>();
			numerosDeGuia.add(numeroGuia);
			
			return guiaSPGPs.consultarLocomocaoNaoUtilizada(numerosDeGuia, comarcaCodigo);
		
		} finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
	}
	
	private List<LocomocaoDt> consultarLocomocao(List<LocomocaoDt> locomocoesProjudi, String comarcaCodigo, boolean somenteNaoUtilizadas, boolean validaOficialVinculadoLocomocao) throws Exception {
		List<LocomocaoDt> locomocoesNaoUtilizadasProjudiESPG = new ArrayList<LocomocaoDt>();
		
		FabricaConexao obFabricaConexao =null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
		
			ordeneListaLocomocaoPorIdLocomocaoDt(locomocoesProjudi);
			
			if (locomocoesProjudi != null && locomocoesProjudi.size() > 0) {
				List<String> numerosDeGuia = new ArrayList<String>();
				for (LocomocaoDt locomocaoDt : locomocoesProjudi) {
					if (!numerosDeGuia.contains(locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt().getNumeroGuiaCompleto())) {
						numerosDeGuia.add(locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt().getNumeroGuiaCompleto());
					}
				}
				
				List<LocomocaoSPGDt> locomocoesNaoUtilizadasSPG = guiaSPGPs.consultarLocomocaoNaoUtilizada(numerosDeGuia, comarcaCodigo);
				
				for (LocomocaoDt locomocaoDt : locomocoesProjudi) {
					for (LocomocaoSPGDt locomocaoSPGDt : locomocoesNaoUtilizadasSPG) {
						if (!locomocaoSPGDt.isJaVerificada() && Funcoes.StringToLong(locomocaoSPGDt.getIdProjudi()) == Funcoes.StringToLong(locomocaoDt.getId())) {
							
							locomocaoSPGDt.setJaVerificada(true);
							locomocaoDt.setLocomocaoSPGDt(locomocaoSPGDt);
							
							if(validaOficialVinculadoLocomocao) {
								if (somenteNaoUtilizadas && locomocaoSPGDt.getNumeroMandado() == 0 && locomocaoSPGDt.getNumeroGuiaComplementar() == 0) {
									locomocoesNaoUtilizadasProjudiESPG.add(locomocaoDt);	 
								} else if (!somenteNaoUtilizadas && locomocaoSPGDt.getNumeroGuiaComplementar() > 0) {
									locomocoesNaoUtilizadasProjudiESPG.add(locomocaoDt);	
								}
							}
							else {
								locomocoesNaoUtilizadasProjudiESPG.add(locomocaoDt);
							}
							
							break;
						}
					}
				}
			}
		
		} finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
		
		return locomocoesNaoUtilizadasProjudiESPG;
	}
	
	private void ordeneListaLocomocaoPorIdLocomocaoDt(List<LocomocaoDt> locomocoesNaoUtilizadasProjudi) {
		//Sorting
		Collections.sort(locomocoesNaoUtilizadasProjudi, new Comparator<LocomocaoDt>() {
	        @Override
	        public int compare(LocomocaoDt custa1, LocomocaoDt custa2)
	        {
	        	return custa1.getId().compareTo(custa2.getId());
	        }
	    });
	}

	public void cancelarGuiaEmitidaLocomocaoComplementar(GuiaEmissaoDt guiaEmissaoDt, List<LocomocaoDt> locomocoesUtilizadasComplementarProjudi) throws MensagemException, Exception {
		
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			guiaSPGPs.cancelarGuiaEmitidaLocomocaoComplementar(guiaEmissaoDt, locomocoesUtilizadasComplementarProjudi);
		}
		finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
	}
	
	/**
	 * M�todo para consultar Guias Emitidas pelo SPG do processo.
	 * @param FabricaConexao obFabricaConexao
	 * @param ProcessoDt processoDt
	 * @param List<GuiaEmissaoDt> listaGuiaEmissaoDt
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaEmissaoSPG(ProcessoDt processoDt, List<GuiaEmissaoDt> listaGuiaProjudi) throws Exception {
		List listaGuiaEmissaoDt = null;
		List listaGuiaAuxiliar = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
		
			if (processoDt != null) {	
				
				String comarcaCodigo = obtenhaComarcaCodigo(processoDt);
				
				if( comarcaCodigo != null ) {
					// Somente n�mero do processo, d�gito e ano...
					String numeroProcessoDigitoAnoProjudi = Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(processoDt.getProcessoNumeroCompleto()), 21).trim().substring(0, 14);
					
					String numeroProcessoSPG = "";				
					if (processoDt.isProcessoImportadoSPG()) {
						// O n�mero do processo com 4 zeros no final...
						numeroProcessoSPG = Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(processoDt.getProcessoNumeroAntigoTemp() + "0000"), 16).trim();
					}
					
					//validacao n�mero zerados
					if( numeroProcessoDigitoAnoProjudi != null && Funcoes.StringToLong(numeroProcessoDigitoAnoProjudi) == 0 && numeroProcessoSPG != null && Funcoes.StringToLong(numeroProcessoSPG) == 0 ) {
						throw new MensagemException("N�mero Processo Projudi e SPG/SSG zerados.");
					}
					
					listaGuiaAuxiliar = guiaSPGPs.consultarGuiaEmissaoSPG(numeroProcessoDigitoAnoProjudi, numeroProcessoSPG, comarcaCodigo, listaGuiaProjudi);
					if( listaGuiaAuxiliar != null && !listaGuiaAuxiliar.isEmpty() ) {
						if( listaGuiaEmissaoDt == null ) {
							listaGuiaEmissaoDt = new ArrayList<>();
						}
						listaGuiaEmissaoDt.addAll(listaGuiaAuxiliar);
					}
					
					//Consulta no ambiente contr�rio ao anterior
					String remoto = "";
					//Existe essa nega��o no if abaixo para pesquisar no ambiente oposto do que foi pesquisado acima.
					if( !comarcaCodigo.equals(ComarcaDt.GOIANIA) ) {
						remoto = ComarcaDt.GOIANIA;
					}
					else {
						remoto = ComarcaDt.APARECIDA_DE_GOIANIA;
					}
					listaGuiaAuxiliar = guiaSPGPs.consultarGuiaEmissaoSPG(numeroProcessoDigitoAnoProjudi, numeroProcessoSPG, remoto, listaGuiaProjudi);
					if( listaGuiaAuxiliar != null && !listaGuiaAuxiliar.isEmpty() ) {
						if( listaGuiaEmissaoDt == null ) {
							listaGuiaEmissaoDt = new ArrayList<>();
						}
						listaGuiaEmissaoDt.addAll(listaGuiaAuxiliar);
					}
					
					//Cria lista com a soma das guias recebidas como parametro e com as novas consultadas
					//para serem exclu�das da consulta abaixo e evitar duplica��o
					List<GuiaEmissaoDt> listaGuiaSomadas = new ArrayList<GuiaEmissaoDt>();
					if(listaGuiaProjudi != null) {
						listaGuiaSomadas.addAll(listaGuiaProjudi);
					}
					if(listaGuiaEmissaoDt != null) {
						listaGuiaSomadas.addAll(listaGuiaEmissaoDt);
					}
					
					//Consulta guias com o n�mero do processo SPG igual e no padr�o CNJ
					numeroProcessoSPG = processoDt.getAno().trim();
					if( processoDt.getAno().trim().length() < 4 ) {
						numeroProcessoSPG = Funcoes.preencheZeros(Funcoes.StringToLong(numeroProcessoSPG), 4);
					}
					numeroProcessoSPG += Funcoes.completarZeros(processoDt.getProcessoNumeroSimples().trim(), 7);
					numeroProcessoSPG += Funcoes.gerarDigitoNumeroProcessoAntigoSPG(numeroProcessoSPG);
					numeroProcessoSPG += "0000";
					
					listaGuiaAuxiliar = guiaSPGPs.consultarGuiaEmissaoSPG(null, numeroProcessoSPG, comarcaCodigo, listaGuiaSomadas);
					if( listaGuiaAuxiliar != null && !listaGuiaAuxiliar.isEmpty() ) {
						if( listaGuiaEmissaoDt == null ) {
							listaGuiaEmissaoDt = new ArrayList<>();
						}
						listaGuiaEmissaoDt.addAll(listaGuiaAuxiliar);
					}

				}
			}
		
		} finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * M�todo para consultar Guias Emitidas pelo SPG do processo.
	 * @param FabricaConexao obFabricaConexao
	 * @param String idProcesso
	 * @param List listaId_GuiaTipo
	 * @return List
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoSPG(ProcessoDt processoDt, String isnGuia) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
		
			if (processoDt != null) {
				
				String comarcaCodigo = obtenhaComarcaCodigo(processoDt);
				
				guiaEmissaoDt = guiaSPGPs.consultarGuiaEmissaoSPG(isnGuia, comarcaCodigo);
				
				if( guiaEmissaoDt == null ) {
					if( comarcaCodigo.equals(ComarcaDt.GOIANIA) ) {
						comarcaCodigo = ComarcaDt.APARECIDA_DE_GOIANIA;
					}
					else {
						comarcaCodigo = ComarcaDt.GOIANIA;
					}
					
					guiaEmissaoDt = guiaSPGPs.consultarGuiaEmissaoSPG(isnGuia, comarcaCodigo);
				}
				
        		atualizeItensDaGuia(guiaEmissaoDt);        		
			}
		
		} finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
		
		return guiaEmissaoDt;
	}
	
	/**
	 * M�todo para consultar Guias Emitidas pelo SPG do processo.
	 * @param String idProcesso
	 * @param List listaId_GuiaTipo
	 * @param FabricaConexao obFabricaConexaoProjudi
	 * @return List
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoSPG(ProcessoDt processoDt, String isnGuia, FabricaConexao obFabricaConexaoProjudi) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
		
			if (processoDt != null) {
				
				String comarcaCodigo = obtenhaComarcaCodigo(processoDt, obFabricaConexaoProjudi);

        		guiaEmissaoDt = guiaSPGPs.consultarGuiaEmissaoSPG(isnGuia, comarcaCodigo);
        		
        		atualizeItensDaGuia(guiaEmissaoDt, obFabricaConexaoProjudi);        		
			}
		
		} finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Fred TODO 13/02/2017: M�todo que n�o deixa utilizar guia spg que tem somente itens de locomo��es.
	 * 
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @throws Exception
	 */
	private boolean valideGuiaInicialComplementarSPG(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		boolean possuiSomenteLocomocoesEPostagem = true;
				
		if (guiaEmissaoDt != null && guiaEmissaoDt.getListaGuiaItemDt() != null && guiaEmissaoDt.getListaGuiaItemDt().size() > 0) {
			
			possuiSomenteLocomocoesEPostagem = this.possuiSomenteItensLocomocao(guiaEmissaoDt.getListaGuiaItemDt());
			
//			if (possuiSomenteLocomocoesEPostagem) {
//				throw new MensagemException("Guia n�o pode ser utilizada, pois s� possui itens de locomo��o ou postagem. <br /><br /> Caso seja uma guia de locomo��o emitida para um processo que foi digitalizado, por favor, utilize a op��o do menu de \"Vincular Guia de Locomo��o do SPG\".");
//			}
		}
		else {
			throw new MensagemException("Guia n�o possui itens vinculados.");
		}
		
		return possuiSomenteLocomocoesEPostagem;
	}
	
	/**
	 * M�todo para verificar se os itens s�o somente de locomo��o
	 * @param List<GuiaItemDt> listaGuiaItemDt
	 * @return boolean
	 */
	public boolean possuiSomenteItensLocomocao(List<GuiaItemDt> listaGuiaItemDt) {
		boolean retorno = true;
		if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
			for (GuiaItemDt guiaItemDt : listaGuiaItemDt) {
				
				if( guiaItemDt.getCodigoArrecadacao() != null && guiaItemDt.getCodigoArrecadacao().trim().length() > 0 ) {
					if (!(Funcoes.StringToLong(guiaItemDt.getCodigoArrecadacao()) == CustaDt.REGIMENTO_LOCOMOCAO_CONTA_VINCULADA || 
					      Funcoes.StringToLong(guiaItemDt.getCodigoArrecadacao()) == CustaDt.REGIMENTO_LOCOMOCAO_PENHORA || 
					      Funcoes.StringToLong(guiaItemDt.getCodigoArrecadacao()) == CustaDt.REGIMENTO_LOCOMOCAO_TRIBUNAL || 
					      Funcoes.StringToLong(guiaItemDt.getCodigoArrecadacao()) == CustaDt.REGIMENTO_LOCOMOCAO_AVALIACAO ||
					      Funcoes.StringToLong(guiaItemDt.getCodigoArrecadacao()) == CustaDt.REGIMENTO_POSTAGEM)) {
						
						retorno = false;
						break;
					}
				}
			}
		}
		return retorno;
	}
	
	private void atualizeItensDaGuia(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		if (guiaEmissaoDt != null && guiaEmissaoDt.getListaGuiaItemDt() != null) {
			CustaNe custaNe = new CustaNe();
			for (GuiaItemDt guiaItemDt : guiaEmissaoDt.getListaGuiaItemDt()) {        				
				
				CustaDt custaDt = null;
				
				switch( guiaItemDt.getCodigoArrecadacao() ) {
					case ArrecadacaoCustaDt.CODIGO_ARRECADACAO_LOCOMOCAO: {
						custaDt = custaNe.consultarId(String.valueOf(CustaDt.CUSTAS_LOCOMOCAO));
						break;
					}
					case ArrecadacaoCustaDt.CODIGO_ARRECADACAO_LOCOMOCAO_TRIBUNAL: {
						custaDt = custaNe.consultarId(String.valueOf(CustaDt.LOCOMOCAO_PARA_TRIBUNAL));
						break;
					}
					default: {
						custaDt = custaNe.consultarCodigoArrecadacao(guiaItemDt.getCodigoArrecadacao());
					}
				}
				
				guiaItemDt.setCustaDt(custaDt);
				
				if (guiaItemDt.getCustaDt() == null) {
					custaDt = new CustaDt();
					custaDt.setArrecadacaoCusta(guiaItemDt.getArrecadacaoCusta());
					custaDt.setCodigoRegimento("0");
					custaDt.setCodigoArrecadacao(guiaItemDt.getCodigoArrecadacao());
					
					guiaItemDt.setCustaDt(custaDt);
				}
				
			}	
		}
	}
	
	private void atualizeItensDaGuia(GuiaEmissaoDt guiaEmissaoDt, FabricaConexao obFabricaConexaoProjudi) throws Exception {
		if (guiaEmissaoDt != null && guiaEmissaoDt.getListaGuiaItemDt() != null) {
			CustaNe custaNe = new CustaNe();
			for (GuiaItemDt guiaItemDt : guiaEmissaoDt.getListaGuiaItemDt()) {        				
				guiaItemDt.setCustaDt(custaNe.consultarCodigoArrecadacao(guiaItemDt.getCodigoArrecadacao(), obFabricaConexaoProjudi));        				
				if (guiaItemDt.getCustaDt() == null) {
					CustaDt custaDt = new CustaDt();
					custaDt.setArrecadacaoCusta(guiaItemDt.getArrecadacaoCusta());
					custaDt.setCodigoRegimento("0");
					custaDt.setCodigoArrecadacao(guiaItemDt.getCodigoArrecadacao());
					
					guiaItemDt.setCustaDt(custaDt);
				}
			}	
		}	
	}
	
	private String obtenhaComarcaCodigo(ProcessoDt processoDt) throws Exception {
		ServentiaDt serventiaDt = new ServentiaNe().consultarServentiaCodigo(processoDt.getServentiaCodigo());
		ComarcaDt comarcaDt = null;
		String comarcaCodigo = null;
						
		if (serventiaDt != null) comarcaDt = new ComarcaNe().consultarId(serventiaDt.getId_Comarca());
		if (comarcaDt != null) comarcaCodigo = comarcaDt.getComarcaCodigo();
		
		return comarcaCodigo;
	}
	
	private String obtenhaComarcaCodigo(ProcessoDt processoDt, FabricaConexao obFabricaConexaoProjudi) throws Exception {
		ServentiaDt serventiaDt = new ServentiaNe().consultarServentiaCodigo(processoDt.getServentiaCodigo(), obFabricaConexaoProjudi);
		ComarcaDt comarcaDt = null;
		String comarcaCodigo = null;
						
		if (serventiaDt != null) comarcaDt = new ComarcaNe().consultarId(serventiaDt.getId_Comarca(), obFabricaConexaoProjudi);
		if (comarcaDt != null) comarcaCodigo = comarcaDt.getComarcaCodigo();
		
		return comarcaCodigo;
	}
	
	public GuiaEmissaoDt consultarGuiaEmissaoInicialSPG(String numeroCompletoGuia, ProcessoDt processoDt) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			
			if( numeroCompletoGuia.contains(".") || numeroCompletoGuia.contains("-") ) {
				throw new MensagemException("N�mero da Guia informado n�o pode conter pontos(.) [Erro6]");
			}
			
			if( numeroCompletoGuia != null && numeroCompletoGuia.length() > 0 && Funcoes.StringToLong(numeroCompletoGuia) == 0 ) {
				throw new MensagemException("N�mero da Guia deve conter somente n�mero.");
			}
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			guiaEmissaoDt = guiaSPGPs.consultarGuiaEmissaoInicialSPG(numeroCompletoGuia);
			
			if (guiaEmissaoDt != null) {
				// Verifica se a guia j� foi utilizada no SPG...
				if (guiaEmissaoDt.getNumeroProcessoSPG() != null && guiaEmissaoDt.getNumeroProcessoSPG().trim().length() > 0) {
				
					if (
							!(
									(guiaEmissaoDt.getNumeroProcesso() == null || guiaEmissaoDt.getNumeroProcesso().trim().length() == 0) 
									&& processoDt != null 
									&& processoDt.getProcessoNumeroAntigoTemp() != null 
									&& (processoDt.getProcessoNumeroAntigoTemp().trim()+"0000").equalsIgnoreCase(guiaEmissaoDt.getNumeroProcessoSPG())
							)
						) {
						
						//Altera��o da mensagem realizada a pedido do Marques depois da ocorr�ncia 2018/15286.
						String mensagemException = "Guia j� utilizada no processo SPG n�mero " + guiaEmissaoDt.getNumeroProcessoSPG().trim();
						
						if (guiaEmissaoDt.getNumeroProcesso() != null && guiaEmissaoDt.getNumeroProcesso().trim().length() > 0) {
							mensagemException += " e processo Projudi " + guiaEmissaoDt.getNumeroProcesso();
						}
						
						mensagemException += ".";
						
						throw new MensagemException(mensagemException);
					}
				}
				
				// Verifica se a guia j� foi utilizada no Projudi...
				if (guiaEmissaoDt.getNumeroProcesso() != null && guiaEmissaoDt.getNumeroProcesso().trim().length() > 0) {
					// Somente n�mero do processo, d�gito e ano...
					String numeroProcessoDigitoAnoProjudi = Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(guiaEmissaoDt.getNumeroProcesso()), 14).trim();
					String numeroProcesso = numeroProcessoDigitoAnoProjudi.substring(0, 8);
					String digitoProcesso = numeroProcessoDigitoAnoProjudi.substring(8, 10);
					String anoProcesso = numeroProcessoDigitoAnoProjudi.substring(10, 14);
					
					String numeroProcessoCompletoProjudi = new ProcessoNe().consultarNumeroCompletoDoProcesso(numeroProcesso, digitoProcesso, anoProcesso);
					// Verifica se a guia j� foi utilizada no SPG...
					if (numeroProcessoCompletoProjudi != null && numeroProcessoCompletoProjudi.trim().length() > 0) 
						throw new MensagemException("Guia j� utilizada no processo Projudi n�mero " + numeroProcessoCompletoProjudi + ".");
					
					guiaEmissaoDt.setNumeroProcesso("");
				}
				
				if( !valideGuiaInicialComplementarSPG(guiaEmissaoDt) ) {
					atualizeItensDaGuia(guiaEmissaoDt);
				}
	    		
	    		if (guiaEmissaoDt.getNaturezaSPGCodigo() != null && guiaEmissaoDt.getNaturezaSPGCodigo().trim().length() > 0) {
	    			NaturezaSPGDt naturezaSPGDt = new NaturezaSPGNe().consultarCodigo(guiaEmissaoDt.getNaturezaSPGCodigo());
	    			if (naturezaSPGDt != null) {
	    				guiaEmissaoDt.setId_NaturezaSPG(naturezaSPGDt.getId());
	    				guiaEmissaoDt.setNaturezaSPG(naturezaSPGDt.getNaturezaSPG());
	    			}
	    		}		    		
			}
			
		} finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
		
		return guiaEmissaoDt;
	}
	
	public GuiaEmissaoDt consultarGuiaEmissaoComplementarSPG(String numeroCompletoGuia, String id_processoProjudi) throws Exception {
		GuiaEmissaoDt guiaEmissaoComplementarDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			
			if( numeroCompletoGuia.contains(".") || numeroCompletoGuia.contains("-") ) {
				throw new MensagemException("N�mero da Guia informado n�o pode conter pontos(.) [Erro7]");
			}
			
			if( numeroCompletoGuia != null && numeroCompletoGuia.length() > 0 && Funcoes.StringToLong(numeroCompletoGuia) == 0 ) {
				throw new MensagemException("N�mero da Guia deve conter somente n�meros.");
			}
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
		
			guiaEmissaoComplementarDt = guiaSPGPs.consultarGuiaEmissaoComplementarSPG(numeroCompletoGuia);
			
			if (guiaEmissaoComplementarDt != null) 
			{
				GuiaEmissaoDt guiaEmissaoInicialSPGDt = null;				
				if (guiaEmissaoComplementarDt.getId_GuiaEmissaoPrincipal() != null && guiaEmissaoComplementarDt.getId_GuiaEmissaoPrincipal().trim().length() > 0)  {
					guiaEmissaoInicialSPGDt = guiaSPGPs.consultarGuiaEmissaoSPG(guiaEmissaoComplementarDt.getId_GuiaEmissaoPrincipal());
				}
				
				if (guiaEmissaoInicialSPGDt == null)
					throw new MensagemException("Guia inicial principal n�o encontrada no SPG.");
				
				ProcessoDt processoDt = new ProcessoNe().consultarId(id_processoProjudi);
				if (processoDt == null)
					throw new MensagemException("Processo projudi n�o encontrado.");
				
				GuiaEmissaoDt guiaEmissaoInicialProjudiDt = new GuiaEmissaoNe().consultarGuiaEmissaoNumeroGuia(guiaEmissaoInicialSPGDt.getNumeroGuiaCompleto());
				
				if (guiaEmissaoInicialProjudiDt != null) {
					if ((guiaEmissaoInicialProjudiDt.getId_Processo() == null || guiaEmissaoInicialProjudiDt.getId_Processo().trim().length() == 0))
						throw new MensagemException("Guia inicial principal n�o vinculada a nenhum processo Judicial, favor vincular a guia inicial primeiro.");
					
					// Verifica se a guia inicial j� foi utilizada no Projudi...
					if (!(guiaEmissaoInicialProjudiDt.getId_Processo().trim().equalsIgnoreCase(id_processoProjudi.trim()))) 					
						throw new MensagemException("Guia inicial principal j� vinculada a outro processo Judicial, favor verificar. N�mero do processo: " + processoDt.getProcessoNumeroCompleto() + ".");
				}				
				
				guiaEmissaoComplementarDt.setNumeroProcesso("");
				
				if( !valideGuiaInicialComplementarSPG(guiaEmissaoComplementarDt) ) {
					atualizeItensDaGuia(guiaEmissaoComplementarDt);
				}
	    		
	    		if (guiaEmissaoComplementarDt.getNaturezaSPGCodigo() != null && guiaEmissaoComplementarDt.getNaturezaSPGCodigo().trim().length() > 0) {
	    			NaturezaSPGDt naturezaSPGDt = new NaturezaSPGNe().consultarCodigo(guiaEmissaoComplementarDt.getNaturezaSPGCodigo());
	    			if (naturezaSPGDt != null) {
	    				guiaEmissaoComplementarDt.setId_NaturezaSPG(naturezaSPGDt.getId());
	    				guiaEmissaoComplementarDt.setNaturezaSPG(naturezaSPGDt.getNaturezaSPG());
	    			}
	    		}		    		
			}
			
		}
		finally {
			if (obFabricaConexao != null) {
				obFabricaConexao.fecharConexao();
			}
        }
		
		return guiaEmissaoComplementarDt;
	}
	
	public GuiaEmissaoDt consultarGuiaEmissaoInicialSPGSemValidacao(String numeroCompletoGuia) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			
			if( numeroCompletoGuia.contains(".") || numeroCompletoGuia.contains("-") ) {
				throw new MensagemException("N�mero da Guia informado n�o pode conter pontos(.) [Erro8]");
			}
			
			if( numeroCompletoGuia != null && numeroCompletoGuia.length() > 0 && Funcoes.StringToLong(numeroCompletoGuia) == 0 ) {
				throw new MensagemException("N�mero da Guia deve conter somente n�meros.");
			}
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
		
			guiaEmissaoDt = guiaSPGPs.consultarGuiaEmissaoSPG(numeroCompletoGuia);
			
			if (guiaEmissaoDt != null) 
			{
				atualizeItensDaGuia(guiaEmissaoDt);
	    		
	    		/*if (guiaEmissaoDt.getNaturezaSPGCodigo() != null && guiaEmissaoDt.getNaturezaSPGCodigo().trim().length() > 0) {
	    			NaturezaSPGDt naturezaSPGDt = new NaturezaSPGNe().consultarCodigo(guiaEmissaoDt.getNaturezaSPGCodigo());
	    			if (naturezaSPGDt != null) {
	    				guiaEmissaoDt.setId_NaturezaSPG(naturezaSPGDt.getId());
	    				guiaEmissaoDt.setNaturezaSPG(naturezaSPGDt.getNaturezaSPG());
	    			}
	    		}*/
			}
			
		}
		finally {
			if (obFabricaConexao != null) {
				obFabricaConexao.fecharConexao();
			}
        }
		
		return guiaEmissaoDt;
	}
	
	public GuiaEmissaoDt consultarGuiaEmissaoInicialSPGNumeroProcesso(String numeroProcessoCompleto) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			// Somente n�mero do processo, d�gito e ano...
			String numeroProcessoDigitoAnoProjudi = Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(numeroProcessoCompleto), 21).trim().substring(0, 14);
		
			guiaEmissaoDt = guiaSPGPs.consultarGuiaEmissaoInicialSPGNumeroProcessoProjudi(numeroProcessoDigitoAnoProjudi);
			
			if (guiaEmissaoDt != null) 
			{
				atualizeItensDaGuia(guiaEmissaoDt);
			}
			
		}
		finally {
			if (obFabricaConexao != null) {
				obFabricaConexao.fecharConexao();
			}
        }
		
		return guiaEmissaoDt;
	}
	
	public GuiaEmissaoDt consultarGuiaEmissaoSPG(String numeroCompletoGuia) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaEmissaoDt = consultarGuiaEmissaoSPG(numeroCompletoGuia, obFabricaConexao);
			if( guiaEmissaoDt != null ) {
				guiaEmissaoDt.setGuiaEnviadaCadin((new ProcessoParteDebitoCadinNe()).isGuiaEnviadaCadin(guiaEmissaoDt.getId()));
				if (guiaEmissaoDt.getNumeroProcesso() != null && guiaEmissaoDt.getNumeroProcesso().trim().length() > 0) {
					ProcessoDt processoDt = (new ProcessoNe()).consultarProcessoNumeroCompleto(guiaEmissaoDt.getNumeroProcesso());
					if (processoDt != null) {
						guiaEmissaoDt.setProcessoPossuiGuiaEnviadaCadin((new ProcessoParteDebitoCadinNe()).isProcessoPossuiGuiaEnviadaCadin(processoDt.getId()));	
					}						
				}				
			}
		}
		finally {
			if (obFabricaConexao != null) {
				obFabricaConexao.fecharConexao();
			}
        }
		
		return guiaEmissaoDt;
	}
	
	public GuiaEmissaoDt consultarGuiaEmissaoSPG(String numeroCompletoGuia, FabricaConexao obFabricaConexao) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		
		if( numeroCompletoGuia.contains(".") || numeroCompletoGuia.contains("-") ) {
			throw new MensagemException("N�mero da Guia informado n�o pode conter pontos(.) [Erro9]");
		}
		
		if( numeroCompletoGuia != null && numeroCompletoGuia.length() > 0 && Funcoes.StringToLong(numeroCompletoGuia) == 0 ) {
			throw new MensagemException("N�mero da Guia deve conter somente n�meros.");
		}
				
		guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
		
		guiaEmissaoDt = guiaSPGPs.consultarGuiaEmissaoSPG(numeroCompletoGuia);
		
		if (guiaEmissaoDt != null) 
		{
			atualizeItensDaGuia(guiaEmissaoDt);
		}
		
		return guiaEmissaoDt;
	}
	
	public String consultarNumeroProcessoSPGGuiaEmissaoInicialProjudi(String numeroCompletoGuia) throws Exception {
		String numeroProcessoSPG = null;
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			
			if( numeroCompletoGuia.contains(".") ) {
				throw new MensagemException("N�mero da Guia informado n�o pode conter pontos(.) [Erro15]");
			}
			
			if( numeroCompletoGuia != null && numeroCompletoGuia.length() > 0 && Funcoes.StringToLong(numeroCompletoGuia) == 0 ) {
				throw new MensagemException("N�mero da Guia deve conter somente n�meros.");
			}
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSPGPs = new  GuiaSPGPs(obFabricaConexao.getConexao());
		
			guiaEmissaoDt = guiaSPGPs.consultarGuiaEmissaoInicialSPG(numeroCompletoGuia);
			
			// Verifica se a guia j� foi utilizada no SPG...
			if (guiaEmissaoDt != null) numeroProcessoSPG = guiaEmissaoDt.getNumeroProcessoSPG();			
		}
		finally {
			if (obFabricaConexao != null) {
				obFabricaConexao.fecharConexao();
			}
        }
		
		return numeroProcessoSPG;
	}
	
	/**
	 * M�todo para consultar Guia no SPG pelo n�mero da Guia Completo.
	 * @param String numeroGuiaCompleto
	 * @param String comarcaCodigo
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaSPGByNumeroGuiaCompleto(String numeroGuiaCompleto, String comarcaCodigo) throws Exception {
		GuiaEmissaoDt guiaEmissaoSPGDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			guiaEmissaoSPGDt = obPersistencia.consultarGuiaSPGByNumeroGuiaCompleto(numeroGuiaCompleto, comarcaCodigo);
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return guiaEmissaoSPGDt;
	}
	
	/**
	 * M�todo para consultar Guia no SPG pelo n�mero da Guia Completo da capital.
	 * @param String numeroGuiaCompleto
	 * @param String comarcaCodigo
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaSPGByNumeroGuiaCompletoCapital(String numeroGuiaCompleto) throws Exception {
		GuiaEmissaoDt guiaEmissaoSPGDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			guiaEmissaoSPGDt = obPersistencia.consultarGuiaSPGByNumeroGuiaCompleto(numeroGuiaCompleto, ComarcaDt.GOIANIA);
			
			if( guiaEmissaoSPGDt != null && guiaEmissaoSPGDt.getInfoLocalCertidaoSPG() != null && !guiaEmissaoSPGDt.getInfoLocalCertidaoSPG().isEmpty() ) {
				guiaEmissaoSPGDt.setComarcaCodigo(new ComarcaNe().extrairComarcaCodigoInfoLocalCertidaoSPG(guiaEmissaoSPGDt.getInfoLocalCertidaoSPG()));
				if( guiaEmissaoSPGDt.getComarcaCodigo() != null && !guiaEmissaoSPGDt.getComarcaCodigo().isEmpty() ) {
					guiaEmissaoSPGDt.setGuiaEmitidaSPGCapital(guiaEmissaoSPGDt.getComarcaCodigo() == null || guiaEmissaoSPGDt.getComarcaCodigo().trim().length() == 0 || guiaEmissaoSPGDt.getComarcaCodigo().equalsIgnoreCase(ComarcaDt.GOIANIA.trim()));
				}
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return guiaEmissaoSPGDt;
	}
	
	/**
	 * M�todo para consultar Guia no SPG pelo n�mero da Guia Completo da capital.
	 * @param String numeroGuiaCompleto
	 * @param String comarcaCodigo
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaSPGByNumeroGuiaCompletoInterior(String numeroGuiaCompleto) throws Exception {
		GuiaEmissaoDt guiaEmissaoSPGDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			guiaEmissaoSPGDt = obPersistencia.consultarGuiaSPGByNumeroGuiaCompleto(numeroGuiaCompleto, ComarcaDt.APARECIDA_DE_GOIANIA);
			
			if( guiaEmissaoSPGDt != null && guiaEmissaoSPGDt.getInfoLocalCertidaoSPG() != null && !guiaEmissaoSPGDt.getInfoLocalCertidaoSPG().isEmpty() ) {
				guiaEmissaoSPGDt.setComarcaCodigo(new ComarcaNe().extrairComarcaCodigoInfoLocalCertidaoSPG(guiaEmissaoSPGDt.getInfoLocalCertidaoSPG()));
				if( guiaEmissaoSPGDt.getComarcaCodigo() != null && !guiaEmissaoSPGDt.getComarcaCodigo().isEmpty() ) {
					guiaEmissaoSPGDt.setGuiaEmitidaSPGCapital(guiaEmissaoSPGDt.getComarcaCodigo() == null || guiaEmissaoSPGDt.getComarcaCodigo().trim().length() == 0 || guiaEmissaoSPGDt.getComarcaCodigo().equalsIgnoreCase(ComarcaDt.GOIANIA.trim()));
				}
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return guiaEmissaoSPGDt;
	}
	
	/**
	 * M�todo que agiliza consultando nas duas bases do SPG(capital e interior)
	 * 
	 * @param String numeroGuiaCompleto
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaSPGCapitalInterior(String numeroGuiaCompleto) throws Exception {
		GuiaEmissaoDt guiaSPG = null;
		
		if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() ) {
			
			//Consulta a guia no spg na base capital 
			guiaSPG = this.consultarGuiaSPGByNumeroGuiaCompletoCapital(numeroGuiaCompleto);
			
			//Caso n�o exista, consulta a guia no spg na base interior
			if (guiaSPG == null) {
				guiaSPG = this.consultarGuiaSPGByNumeroGuiaCompletoInterior(numeroGuiaCompleto);
			}
			
		}
		
		return guiaSPG;
	}
	
	/**
	 * M�todo que agiliza consultando nas duas bases do SPG(capital e interior)
	 * 
	 * @param String numeroGuiaCompleto
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 * @author fogomes
	 */
	public GuiaEmissaoDt consultarGuiaSPGPeloNumeroGuiaCompletoCapitaleInterior(String numeroGuiaCompleto) throws Exception {
		
		GuiaEmissaoDt guiaEmissaoSPGDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() ) {
				
				//Consulta a guia no SPG (capital) 
				guiaEmissaoSPGDt = obPersistencia.consultarGuiaSPGPeloNumeroGuiaCompletoCapital(numeroGuiaCompleto);
				// MUDAR ESSA VERIFICA��O. POIS ELA PODE VIR N�O NULA MESMO N�O TENDO ACHADO NA CAPITAL
				//Caso n�o exista, consulta a guia no spg na base interior
				//if (guiaEmissaoSPGDt == null) { // MUDAR ESSA VERIFICA��O. POIS ELA PODE VIR N�O NULA MESMO N�O TENDO ACHADO NA CAPITAL
					//guiaEmissaoSPGDt = obPersistencia.consultarGuiaSPGPeloNumeroGuiaCompletoInterior(numeroGuiaCompleto);
				//}
				
				if (guiaEmissaoSPGDt == null || ( guiaEmissaoSPGDt.getNumeroProcesso() == null || guiaEmissaoSPGDt.getNumeroProcesso().isEmpty() )) {
					
					guiaEmissaoSPGDt = obPersistencia.consultarGuiaSPGPeloNumeroGuiaCompletoInterior(numeroGuiaCompleto);
				}
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		return guiaEmissaoSPGDt;
	}
	
	/**
	 * M�todo para consultar a data de repasse da guia.
	 * @param String isnGuia
	 * @param String comarcaCodigo
	 * @return String dataRepasse
	 * @throws Exception
	 */
	public String consultarISNInfoRepasseByISNGuia(String isnGuia, String comarcaCodigo) throws Exception {
		String dataRepasse = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			dataRepasse = obPersistencia.consultarISNInfoRepasseByISNGuia(isnGuia, comarcaCodigo);
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return dataRepasse;
	}
	
	/**
	 * M�todo para atualizar a guia no momento da vincula��o dela com o processo
	 * @param String isnGuiaSPG
	 * @param String numeroProcesso
	 * @param String processoNumeroAntigoTemp
	 * @param String codigoServentia
	 * @param String comarcaCodigo
	 * @param String idUsuarioLog
	 * @param String ipComputadorLog
	 * @throws Exception
	 */
	public boolean atualizaGuiaVinculadaProcesso(String isnGuiaSPG, String numeroProcesso, String processoNumeroAntigoTemp, String codigoServentia, String comarcaCodigo, String idUsuarioLog, String ipComputadorLog) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.atualizaGuiaVinculadaProcesso(isnGuiaSPG, numeroProcesso, processoNumeroAntigoTemp, codigoServentia, comarcaCodigo);
			if( retorno ) {
				LogDt obLogDt = new LogDt("V_SPGAGUIAS", isnGuiaSPG, idUsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.Alterar), "", "[ISN_SPGA_GUIAS:"+isnGuiaSPG+";NUMR_PROJUDI:"+numeroProcesso+";INFO_LOCAL_CERTIDAO:"+codigoServentia+";DATA_APRESENTACAO:"+Funcoes.BancoData(new Date())+";]");
				obLogDt.setId("");
				obLog.salvar(obLogDt);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * M�todo para limpar a guia no spg para receber processo do projudi
	 * @param String isnGuiaSPG
	 * @param String numeroProcesso
	 * @param String comarcaCodigo
	 * @param String idUsuarioLog
	 * @param String ipComputadorLog
	 * @throws Exception
	 */
	public boolean limpaGuiaVinculadaProcesso(String isnGuiaSPG, String numeroProcesso, String comarcaCodigo, String idUsuarioLog, String ipComputadorLog) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.limpaGuiaVinculadaProcesso(isnGuiaSPG, numeroProcesso, comarcaCodigo);
			if( retorno ) {
				LogDt obLogDt = new LogDt("V_SPGAGUIAS", isnGuiaSPG, idUsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.Alterar), "", "[ISN_SPGA_GUIAS:"+isnGuiaSPG+";NUMR_PROJUDI:"+numeroProcesso+";LIMPAR_GUIA_PARA_UTILIZAR_PROJUDI]");
				obLogDt.setId("");
				obLog.salvar(obLogDt);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * M�todo para inserir info repasse.
	 * 
	 * @param String isnGuia
	 * @param String cdgServentiaComarca
	 * @param TJDataHora dataRepasse
	 * @param String percentualRepasse
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param String idUsuarioLog
	 * @param String ipComputadorLog
	 * @param String logIdProcessoRedistribuicaoEmLote
	 * 
	 * @throws Exception
	 */
	public void inserirGuiaInfoRepasse(String isnGuia, String cdgServentiaComarca, TJDataHora dataRepasse, String percentualRepasse, GuiaEmissaoDt guiaEmissaoDt, String idUsuarioLog, String ipComputadorLog, String logIdProcessoRedistribuicaoEmLote) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			//valor default 0%
			String percentual = "0";
			if( percentualRepasse != null && !percentualRepasse.isEmpty() ) {
				percentual = percentualRepasse;
			}
			
			obPersistencia.inserirGuiaInfoRepasse(isnGuia, cdgServentiaComarca, dataRepasse, percentual, guiaEmissaoDt);
			
			LogDt obLogDt = new LogDt("V_SPGAGUIAS_INFO_REPASSE", isnGuia, idUsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.Incluir), "", "[FUNCIONALIDADE_REDISTRIBUICAO(EMAIL:Voltando o menu aberto para todas redistribuicoes)"+logIdProcessoRedistribuicaoEmLote+";ISN_SPGA_GUIAS:"+isnGuia+";CODG_ESCRIVANIA:"+cdgServentiaComarca+";DATA_REPASSE:"+dataRepasse+";PERC_REPASSE:"+percentual+";NUMERO_GUIA:"+guiaEmissaoDt.getNumeroGuiaCompleto()+"]");
			obLogDt.setId("");
			obLog.salvar(obLogDt);
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	/**
	 * M�todo para inserir info repasse.
	 * 
	 * @param String isnGuia
	 * @param String cdgServentiaComarca
	 * @param TJDataHora dataRepasse
	 * @param String comarcaCodigo
	 * @param String numeroGuiaCompleto
	 * @param String idUsuarioLog
	 * @param String ipComputadorLog
	 * @throws Exception
	 */
	public void inserirGuiaInfoRepasse(String isnGuia, String cdgServentiaComarca, TJDataHora dataRepasse, String comarcaCodigo, String numeroGuiaCompleto, String idUsuarioLog, String ipComputadorLog) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			obPersistencia.inserirGuiaInfoRepasse(isnGuia, cdgServentiaComarca, dataRepasse, comarcaCodigo);
			
			LogDt obLogDt = new LogDt("V_SPGAGUIAS_INFO_REPASSE", isnGuia, idUsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.Incluir), "", "[ISN_SPGA_GUIAS:"+isnGuia+";CODG_ESCRIVANIA:"+cdgServentiaComarca+";DATA_REPASSE:"+dataRepasse+";NUMERO_GUIA:"+numeroGuiaCompleto+";PERC_REPASSE:100]");
			obLogDt.setId("");
			obLog.salvar(obLogDt);
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	/**
	 * M�todo que faz o rollback no momento do cadastro do processo apagando dados do processo que n�o existe da transa��o do projudi.
	 * @throws Exception
	 */
	public boolean rollbackAtualizaGuiaVinculadaProcesso(String isnGuiaSPG, String numeroProcesso, String codigoServentia, String comarcaCodigo, String idUsuarioLog, String ipComputadorLog) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.rollbackAtualizaGuiaVinculadaProcesso(isnGuiaSPG, numeroProcesso, codigoServentia, comarcaCodigo);
			if( retorno ) {
				LogDt obLogDt = new LogDt("V_SPGAGUIAS", isnGuiaSPG, idUsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.Alterar), "", "[ISN_SPGA_GUIAS:"+isnGuiaSPG+";NUMR_PROJUDI:"+numeroProcesso+";INFO_LOCAL_CERTIDAO:"+codigoServentia+";ROLLBACK(OK);]");
				obLogDt.setId("");
				obLog.salvar(obLogDt);
			}
			else {
				LogDt obLogDt = new LogDt("V_SPGAGUIAS", isnGuiaSPG, idUsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.Erro), "", "[ISN_SPGA_GUIAS:"+isnGuiaSPG+";NUMR_PROJUDI:"+numeroProcesso+";INFO_LOCAL_CERTIDAO:"+codigoServentia+";ROLLBACK(NAO ALTERADO);]");
				obLogDt.setId("");
				obLog.salvar(obLogDt);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * M�todo que faz o delete no momento do cadastro do processo apagando dados do processo que n�o existe da transa��o do projudi.
	 * @throws Exception
	 */
	public void rollbackInserirGuiaInfoRepasse(String isnGuia, GuiaEmissaoDt guiaEmissaoDt, String idUsuarioLog, String ipComputadorLog) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			InfoRepasseSPGDt infoRepasseSPGDt = obPersistencia.consultarInfoRepasseByISNGuia(isnGuia, guiaEmissaoDt.getComarcaCodigo());
			
			if( infoRepasseSPGDt != null && isnGuia != null && infoRepasseSPGDt.getIsn().equals(isnGuia) ) {
				
				boolean deletaInfoRepasse = false;
				
				//Verifica se tem as condi��es do J�nior feitosa
				if( infoRepasseSPGDt.getCodgEscrivania() == null || infoRepasseSPGDt.getCodgEscrivania().trim().equals("39000") ) {
					deletaInfoRepasse = true;
				}
				
				if( !deletaInfoRepasse && infoRepasseSPGDt.getCodgEscrivania() != null && !infoRepasseSPGDt.getCodgEscrivania().trim().equals("39000") ) {
					if( infoRepasseSPGDt.getDataRepasse() != null && infoRepasseSPGDt.getDataRepasseTJDataHora().getDataHoraFormatadayyyyMMdd().equals(new TJDataHora().getDataHoraFormatadayyyyMMdd()) ) {
						deletaInfoRepasse = true;
					}
				}
				
				if( deletaInfoRepasse ) {
					obPersistencia.rollbackInserirGuiaInfoRepasse(isnGuia, guiaEmissaoDt);
				}
			}
			
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	public String obtenhaISNGuiaSPG(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			return obPersistencia.obtenhaISNGuiaSPG(guiaEmissaoDt);			
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
	}

	public List<String> consultarGuiasProjudiPagasNoDia(TJDataHora dataPagamento) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			return obPersistencia.consultarGuiasProjudiPagasNoDia(dataPagamento);			
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	public TJDataHora consultarDataDePagamentoGuia(String numeroGuiaCompleto) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			return obPersistencia.consultarDataDePagamentoGuia(numeroGuiaCompleto);			
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	public InfoRepasseSPGDt consultarInfoRepasseByISNGuia(String isnGuia, String comarcaCodigo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		InfoRepasseSPGDt infoRepasseSPGDt = null;
		
		try {
			if( isnGuia != null && comarcaCodigo != null ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
				GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
				
				infoRepasseSPGDt = obPersistencia.consultarInfoRepasseByISNGuia(isnGuia, comarcaCodigo);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return infoRepasseSPGDt;
	}
	
	/**
	 * M�todo para consultar lista de info_repasse pelo isn da guia.
	 * @param String isnGuia
	 * @param String comarcaCodigo
	 * @return List<InfoRepasseSPGDt> listaInfoRepasseSPGDt
	 * @throws Exception
	 */
	public List<InfoRepasseSPGDt> consultarListaInfoRepasseByISNGuia(String isnGuia, String comarcaCodigo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		List<InfoRepasseSPGDt> listaInfoRepasseSPGDt = null;
		try {
			if( isnGuia != null && comarcaCodigo != null ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
				GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
				
				listaInfoRepasseSPGDt = obPersistencia.consultarListaInfoRepasseByISNGuia(isnGuia, comarcaCodigo);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return listaInfoRepasseSPGDt;
	}
	
	/**
	 * M�todo para consultar guia de processo digitalizado que ainda n�o tem vincula��o com o PJD.
	 * @param String numeroProcessoSPG
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 */
	public List<GuiaEmissaoDt> consultarGuiasSPGProcessoSPG(String numeroProcessoSPG) throws Exception {
		List<GuiaEmissaoDt> listaGuiasSPGDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( numeroProcessoSPG != null && !numeroProcessoSPG.isEmpty() ) {
				
				if( numeroProcessoSPG.contains(".") ) {
					throw new MensagemException("N�mero do Processo informado n�o pode conter pontos(.) [Erro10]");
				}
				
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
				GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
				
				listaGuiasSPGDt = obPersistencia.consultarGuiasSPGProcesso(numeroProcessoSPG);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return listaGuiasSPGDt;
	}
	
	/**
	 * M�todo para consultar guias parceladas de guia de refer�ncia.
	 * @param String numeroGuiaReferencia
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 */
	public List<GuiaEmissaoDt> consultarGuiasSPGParceladas(String numeroGuiaReferencia) throws Exception {
		List<GuiaEmissaoDt> listaGuiasSPGDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( numeroGuiaReferencia != null && !numeroGuiaReferencia.isEmpty() ) {
				
				if( numeroGuiaReferencia.contains(".") ) {
					throw new MensagemException("N�mero da Guia informado n�o pode conter pontos(.) [Erro12]");
				}
				
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
				GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
				
				listaGuiasSPGDt = obPersistencia.consultarGuiasSPGParceladas(numeroGuiaReferencia);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return listaGuiasSPGDt;
	}
	
	public List<GuiaEmissaoDt> consultarGuiasSPGProcessoSPG(String numeroProcessoSPG, FabricaConexao obFabricaConexao) throws Exception {
		List<GuiaEmissaoDt> listaGuiasSPGDt = null;
		if( numeroProcessoSPG != null && !numeroProcessoSPG.isEmpty() ) {
			
			if( numeroProcessoSPG.contains(".") ) {
				throw new MensagemException("N�mero do Processo informado n�o pode conter pontos(.) [Erro11]");
			}
			
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());			
			listaGuiasSPGDt = obPersistencia.consultarGuiasSPGProcesso(numeroProcessoSPG);
			for (Object guiaEmissaoDtObj : listaGuiasSPGDt) {
				GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt)guiaEmissaoDtObj;
				atualizeItensDaGuia(guiaEmissaoDt);
			}			
		}		
		return listaGuiasSPGDt;
	}
	
	public void atualizeIndicadorGuiaPrefeituraDeGoiania(GuiaEmissaoDt guiaEmissaoDt, FabricaConexao obFabricaConexao) throws Exception {
		GuiaSPGPs obPersistencia = new GuiaSPGPs(obFabricaConexao.getConexao());		
		obPersistencia.atualizeIndicadorGuiaPrefeituraDeGoiania(guiaEmissaoDt);
	}
	
	public void atualizeInfoLocalCertidao(GuiaEmissaoDt guiaEmissaoDt, FabricaConexao obFabricaConexao) throws Exception {
		GuiaSPGPs obPersistencia = new GuiaSPGPs(obFabricaConexao.getConexao());		
		obPersistencia.atualizeInfoLocalCertidao(guiaEmissaoDt);
	}
	
	public void atualizeInfoLocalCertidao(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			obPersistencia.atualizeInfoLocalCertidao(guiaEmissaoDt);
		}
		finally {
			if( obFabricaConexao != null ) obFabricaConexao.fecharConexao();			
		}
		
	}
	
	public boolean atualizaDataApresentacao(String isn, String comarcaCodigo) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( isn != null && !isn.isEmpty() ) {				
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
				GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
				
				retorno = obPersistencia.atualizaDataApresentacao(isn, comarcaCodigo);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	public boolean atualizaDataApresentacao(String isn, String comarcaCodigo, FabricaConexao obFabricaConexao) throws Exception {
		boolean retorno = false;
				
		if( isn != null && !isn.isEmpty() ) {
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			obPersistencia.atualizaDataApresentacao(isn, comarcaCodigo);
		}
		
		return retorno;
	}
	
	public boolean vinculeLocomocaoSPGAoProjudi(String isnGuia, boolean isCapital, int posicaoVetor, String idProjudi) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.vinculeLocomocaoSPGAoProjudi(isnGuia, isCapital, posicaoVetor, idProjudi);
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * M�todo para consultar a porcentagem do �ltimo repasse feito da guia.
	 * 
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @return String (n�mero da porcentagem)
	 * @throws Exception
	 */
	public String consultarUltimaPorcentagemRepassadaInfoRepasse(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		String retorno = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( guiaEmissaoDt == null ) {
				throw new MensagemException("Guia n�o identificada.");
			}
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.consultarUltimaPorcentagemRepassadaInfoRepasse(guiaEmissaoDt);
			
			if( retorno != null ) {
				retorno = retorno.trim();
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	public void retireDataApresentacao(String numeroCompletoGuia) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = this.consultarGuiaEmissaoSPG(numeroCompletoGuia);
		if (guiaEmissaoDt != null) this.retireDataApresentacao(guiaEmissaoDt);
	}
	
	public void retireDataApresentacao(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( guiaEmissaoDt != null && guiaEmissaoDt.getId() != null && !guiaEmissaoDt.getId().isEmpty() ) {				
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
				GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
				
				obPersistencia.retireDataApresentacao(guiaEmissaoDt);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	/**
	 * M�todo para cadastrar o info_repasse no momento da redistribui��o do processo.
	 * 
	 * - 10/05/2019 - Altera��o realizada para mostrar todos as op��es para todos os processos.
		De acordo com o email do dia 02/05/2019 cujo t�tulo do email � "Projudi - Repasse das Custas na Redistribui��o"
		Com base na informa��o do J�nior Feitosa, ser� tamb�m cadastrado o info_repasse sem data de repasse quando a guia n�o estiver paga. Esta altera��o 
		visa ajustar os casos em que o juiz redistribua o processo com a guia aguardando deferimento de assistencia.
	 * 
	 * @param FabricaConexao obFabricaConexao
	 * @param String idProcesso
	 * @param String serventiaCodigoExterno
	 * @param String porcentagemRepasse
	 * @param String idUsuarioLog
	 * @param String ipComputadorLog
	 * @param String logIdProcessoRedistribuicaoEmLote
	 * 
	 * @throws Exception
	 */
	public void inserirInfoRepasseRedistribuicaoProcesso(FabricaConexao obFabricaConexao, String idProcesso, String serventiaCodigoExterno, String porcentagemRepasse, String idUsuarioLog, String ipComputadorLog, String logIdProcessoRedistribuicaoEmLote) throws Exception {
		
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		
		if( serventiaCodigoExterno == null || serventiaCodigoExterno.isEmpty() ) {
			throw new MensagemException("C�digo da Serventia no SPG n�o encontrado para cadastrar Repasse no SPG.");
		}
		if( porcentagemRepasse == null || porcentagemRepasse.isEmpty() ) {
			throw new MensagemException("Porcentagem n�o encontrado para cadastrar Repasse no SPG.");
		}
		
		List listaGuiaEmissaoDt = guiaEmissaoNe.consultarGuiaEmissaoInicial_ComplementarQualquerStatus(obFabricaConexao, idProcesso);
		if( listaGuiaEmissaoDt != null && listaGuiaEmissaoDt.size() > 0 ) {
			for( int i = 0; i < listaGuiaEmissaoDt.size(); i++ ) {
				GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) listaGuiaEmissaoDt.get(i);
				
				if( guiaEmissaoDt != null && guiaEmissaoDt.getNumeroGuiaCompleto() != null && !guiaEmissaoDt.getNumeroGuiaCompleto().isEmpty() ) {
					
					if( guiaEmissaoDt.getId_Comarca() != null && !guiaEmissaoDt.getId_Comarca().isEmpty() ) {
						ComarcaDt comarcaDt = new ComarcaNe().consultarId(guiaEmissaoDt.getId_Comarca(), obFabricaConexao);
						
						if( comarcaDt != null && !comarcaDt.getComarcaCodigo().isEmpty() ) {
							guiaEmissaoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
						}
					}
					
					GuiaEmissaoDt guiaSPG = null;
					
					//Consulta no Capital
					guiaSPG = this.consultarGuiaSPGByNumeroGuiaCompletoCapital(guiaEmissaoDt.getNumeroGuiaCompleto());
					guiaEmissaoDt.setComarcaCodigo(ComarcaDt.GOIANIA);
					
					//Consulta no Interior
					if( guiaSPG == null ) {
						guiaSPG = this.consultarGuiaSPGByNumeroGuiaCompletoInterior(guiaEmissaoDt.getNumeroGuiaCompleto());
						guiaEmissaoDt.setComarcaCodigo(ComarcaDt.APARECIDA_DE_GOIANIA);
					}
					
//					//*******************************
//					//Altera��o realizada a pedido da Ana, Miriam e J�nior Feitosa.
//					//Consultar na guia qual o �ltimo repasse, se o repasse foi de 50% e o algoritmo identificar 100%, deixar repassar somente 50% 
//					String porcentagemRepasseParaCadatrar = porcentagemRepasse;
//					
//					String ultimoRepasseGuia = this.consultarUltimaPorcentagemRepassadaInfoRepasse(guiaSPG);
//					if( ultimoRepasseGuia != null && !ultimoRepasseGuia.isEmpty() ) {
//						Long ultimoRepasseGuiaValorNumerico = Funcoes.StringToLong(ultimoRepasseGuia);
//						Long repasseRecebidoParaCadastroValorNumerico = Funcoes.StringToLong(porcentagemRepasse);
//						if( ultimoRepasseGuiaValorNumerico < repasseRecebidoParaCadastroValorNumerico ) {
//							
//							LogDt obLogDt = new LogDt("V_SPGAGUIAS_INFO_REPASSE", guiaSPG.getId(), idUsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.Informacao), "", "[ATENCAO: DIVERGENCIA COM O ULTIMO PERCENTUAL REPASSE IDENTIFICADO NA GUIA:"+ultimoRepasseGuiaValorNumerico+";REPASSE CALCULADO PELO ALGORITMO:"+repasseRecebidoParaCadastroValorNumerico+";NUMERO_GUIA:"+guiaEmissaoDt.getNumeroGuiaCompleto()+"]");
//							obLogDt.setId("");
//							obLog.salvar(obLogDt);
//							
//							porcentagemRepasseParaCadatrar = Long.toString(ultimoRepasseGuiaValorNumerico);
//						}
//					}
//					//*******************************
					
					if( guiaSPG != null && guiaSPG.getId() != null && !guiaSPG.getId().isEmpty() ) {
						
						//Guia est� paga?
						// - 10/05/2019: 
						//Com base na informa��o do J�nior Feitosa, ser� tamb�m cadastrado o info_repasse sem data de repasse quando a guia n�o estiver paga. Esta altera��o 
						//visa ajustar os casos em que o juiz redistribua o processo com a guia aguardando deferimento de assistencia.
						TJDataHora dataRepasse = null;
						if( guiaEmissaoNe.isGuiaPaga(obFabricaConexao, guiaEmissaoDt) ) {
							dataRepasse = new TJDataHora();
						}
						
						this.inserirGuiaInfoRepasse(guiaSPG.getId(), serventiaCodigoExterno, dataRepasse, porcentagemRepasse, guiaEmissaoDt, idUsuarioLog, ipComputadorLog, logIdProcessoRedistribuicaoEmLote);
					}
				}
			}
		}
		
	}
	
	/**
	 * M�todo para consultar o �ltimo repasse feito.
	 * 
	 * @param String idProcesso
	 * @return String porcentagemRepasse
	 * @throws Exception
	 */
	public String consultarValorUltimoRepasse(String idProcesso) throws Exception {
		String retorno = "100.00";
		
		List listaGuiaEmissaoDt = new GuiaEmissaoNe().consultarGuiaEmissaoInicial_Complementar(idProcesso);
		if( listaGuiaEmissaoDt != null && listaGuiaEmissaoDt.size() > 0 ) {
			for( int i = 0; i < listaGuiaEmissaoDt.size(); i++ ) {
				GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) listaGuiaEmissaoDt.get(i);
				
				GuiaEmissaoDt guiaSPG = null;
				
				//Consulta no Capital
				guiaSPG = this.consultarGuiaSPGByNumeroGuiaCompletoCapital(guiaEmissaoDt.getNumeroGuiaCompleto());
				
				//Consulta no Interior
				if( guiaSPG == null ) {
					guiaSPG = this.consultarGuiaSPGByNumeroGuiaCompletoInterior(guiaEmissaoDt.getNumeroGuiaCompleto());
				}
				
				//Comentado para permitir escolher qualquer porcentagem.
//				if( guiaSPG != null && guiaSPG.getId() != null && !guiaSPG.getId().isEmpty() ) {
//					String porcentagemRepasse = this.consultarUltimaPorcentagemRepassadaInfoRepasse(guiaSPG);
//					
//					if( porcentagemRepasse != null && !porcentagemRepasse.isEmpty() ) {
//						if( porcentagemRepasse.equals("50") ) {
//							retorno = porcentagemRepasse;
//						}
//						else {
//							if( porcentagemRepasse.equals("0") ) {
//								retorno = porcentagemRepasse;
//							}
//						}
//					}
//				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * Consultar boleto.
	 * 
	 * @param numeroGuiaCompleto
	 * @return
	 * @throws Exception
	 */
	public BoletoDt buscaBoletoPorNumero(String numeroGuiaCompleto) throws Exception {
		BoletoDt boletoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( numeroGuiaCompleto.contains(".") || numeroGuiaCompleto.contains("-") ) {
				throw new MensagemException("N�mero da Guia informado n�o pode conter pontos(.) [Erro13]");
			}
			
			if( numeroGuiaCompleto != null && numeroGuiaCompleto.length() > 0 && Funcoes.StringToLong(numeroGuiaCompleto) == 0 ) {
				throw new MensagemException("N�mero da Guia deve conter somente n�meros.");
			}
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new GuiaSPGPs(obFabricaConexao.getConexao());
			boletoDt = obPersistencia.buscaBoletoPorNumero(numeroGuiaCompleto);
			if (boletoDt != null)
			{
				atualizeItensDaGuia(boletoDt);
			}
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		
		return boletoDt;
	}
	
	/**
	 * M�todo para ser utilizado no momento de cancelar uma guia no PJD. Em conversa com o J�nio Feitosa e 
	 * Marques (18/04/2018) ficou definido limpar os n�meros de processos das guias no SPG para evitar 
	 * que a parte realize o pagamento de uma guia cancelada e depois ser realizada o rateio no SPG.
	 * 
	 * Alterado depois de conversa com o Leandro Prezotto.
	 * Agora ser� alterado INFO_INTERNET para 5 para identificar que a guia est� cancelada no SPG.
	 * O valor original � 2 (guias do projudi s�rie 50).
	 * 
	 * @param String numeroGuiaCompleto
	 * @param boolean alteraInfoInternet
	 * @param FabricaConexao obFabricaConexaoPJD
	 * @param int tipoLog
	 * @param String idUsuario
	 * @param String ipComputador
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean limparNumerosProcessosGuiaSPG(String numeroGuiaCompleto, boolean alteraInfoInternet, FabricaConexao obFabricaConexaoPJD, int tipoLog, String idUsuario, String ipComputador) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() ) {
				//GuiaEmissaoDt guiaEmissaoDtSPG = consultarGuiaSPGCapitalInterior(numeroGuiaCompleto);
				
				GuiaEmissaoDt guiaEmissaoDtSPG = consultarGuiaSPGByNumeroGuiaCompletoCapital(numeroGuiaCompleto);
				if( guiaEmissaoDtSPG != null ) {
					guiaEmissaoDtSPG.setComarcaCodigo(ComarcaDt.GOIANIA);
				}
				
				if( guiaEmissaoDtSPG == null ) {
					guiaEmissaoDtSPG = consultarGuiaSPGByNumeroGuiaCompletoInterior(numeroGuiaCompleto);
					if( guiaEmissaoDtSPG != null ) {
						guiaEmissaoDtSPG.setComarcaCodigo(ComarcaDt.APARECIDA_DE_GOIANIA);
					}
				}
				
				if( guiaEmissaoDtSPG != null && guiaEmissaoDtSPG.getNumeroGuiaCompleto() != null ) {
					obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
					GuiaSPGPs obPersistencia = new GuiaSPGPs(obFabricaConexao.getConexao());
					
					retorno = obPersistencia.limparNumerosProcessosGuiaSPG(guiaEmissaoDtSPG, alteraInfoInternet);
					
					//Log
					if( retorno ) {
						LogDt obLogDt = new LogDt("V_SPGAGUIAS", guiaEmissaoDtSPG.getId(), idUsuario, ipComputador, String.valueOf(tipoLog),"", "[Guia com processo limpado no SPG;ISN:"+ guiaEmissaoDtSPG.getId() +";numeroGuiaCompleto:" + guiaEmissaoDtSPG.getNumeroGuiaCompleto() +"]");
						obLog.salvar(obLogDt, obFabricaConexaoPJD);
					}
				}
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	public boolean atualizaGuiaVinculadaProcesso(String isnGuiaSPG, String numeroProcesso, String comarcaCodigo, String numeroGuiaCompleto, String idUsuarioLog, String ipComputadorLog) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.atualizaGuiaVinculadaProcesso(isnGuiaSPG, numeroProcesso, comarcaCodigo, numeroGuiaCompleto);
			if( retorno ) {
				LogDt obLogDt = new LogDt("V_SPGAGUIAS", isnGuiaSPG, idUsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.Alterar), "", "[ISN_SPGA_GUIAS:"+isnGuiaSPG+";NUMR_PROJUDI:"+numeroProcesso+";NUMR_GUIA:"+numeroGuiaCompleto+"]");
				obLogDt.setId("");
				obLog.salvar(obLogDt);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	public boolean atualizaGuiaVinculadaProcessoHibrido(String isnGuiaSPG, String numeroProcessoPJD, String numeroProcessoAntigoSPG, String comarcaCodigo, String numeroGuiaCompleto, String idUsuarioLog, String ipComputadorLog) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.atualizaGuiaVinculadaProcessoHibrido(isnGuiaSPG, numeroProcessoPJD, numeroProcessoAntigoSPG, comarcaCodigo, numeroGuiaCompleto);
			if( retorno ) {
				LogDt obLogDt = new LogDt("V_SPGAGUIAS", isnGuiaSPG, idUsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.Alterar), "", "[ISN_SPGA_GUIAS:"+isnGuiaSPG+";NUMR_PROJUDI:"+numeroProcessoPJD+";NUMR_PROCESSO:"+numeroProcessoAntigoSPG+";NUMR_GUIA:"+numeroGuiaCompleto+"]");
				obLogDt.setId("");
				obLog.salvar(obLogDt);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	public SajaDocumentoDt obtenhaSajaDocumento(String numeroGuiaSemSerie, String serie) throws Exception {
		SajaDocumentoDt retorno = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.obtenhaSajaDocumento(numeroGuiaSemSerie, serie);			
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	public void excluaSajaDocumento(String isnGuiaSAJ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			obPersistencia.excluaSajaDocumento(isnGuiaSAJ);			
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	/**
	 * M�todo para desfazer cancelamento de guia.
	 * 
	 * @param String numeroGuiaCompleto
	 * @param String numeroProcessoDigitoAno
	 * @param FabricaConexao obFabricaConexaoPJD
	 * @param String idUsuario
	 * @param String ipComputador
	 * 
	 * @return boolean
	 * 
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean desfazerCancelamentoGuiaSPG(String numeroGuiaCompleto, String numeroProcessoDigitoAno, FabricaConexao obFabricaConexaoPJD, String idUsuario, String ipComputador) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() ) {
				
				GuiaEmissaoDt guiaEmissaoDtSPG = consultarGuiaSPGByNumeroGuiaCompletoCapital(numeroGuiaCompleto);
				if( guiaEmissaoDtSPG != null ) {
					guiaEmissaoDtSPG.setComarcaCodigo(ComarcaDt.GOIANIA);
				}
				
				if( guiaEmissaoDtSPG == null ) {
					guiaEmissaoDtSPG = consultarGuiaSPGByNumeroGuiaCompletoInterior(numeroGuiaCompleto);
					if( guiaEmissaoDtSPG != null ) {
						guiaEmissaoDtSPG.setComarcaCodigo(ComarcaDt.APARECIDA_DE_GOIANIA);
					}
				}
				
				if( guiaEmissaoDtSPG != null && guiaEmissaoDtSPG.getNumeroGuiaCompleto() != null ) {
					obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
					GuiaSPGPs obPersistencia = new GuiaSPGPs(obFabricaConexao.getConexao());
					
					retorno = obPersistencia.desfazerCancelamentoGuiaSPG(guiaEmissaoDtSPG, numeroProcessoDigitoAno);
					
					//Log
					if( retorno ) {
						LogDt obLogDt = new LogDt("V_SPGAGUIAS", guiaEmissaoDtSPG.getId(), idUsuario, ipComputador, String.valueOf(LogTipoDt.Alterar),"", "[Motivo: Desfazendo Cancelamento Guia.;ISN:"+ guiaEmissaoDtSPG.getId() +";numeroGuiaCompleto:" + guiaEmissaoDtSPG.getNumeroGuiaCompleto() +";numeroProcessoProjudi:"+numeroProcessoDigitoAno+"]");
						obLog.salvar(obLogDt, obFabricaConexaoPJD);
					}
				}
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
}
