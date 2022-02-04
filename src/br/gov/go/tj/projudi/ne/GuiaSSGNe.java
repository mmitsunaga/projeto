package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArrecadacaoCustaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ne.boletos.BoletoDt;
import br.gov.go.tj.projudi.ps.GuiaSSGPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class GuiaSSGNe extends Negocio {

	private static final long serialVersionUID = 4393897032512008006L;
	
	private GuiaSSGPs guiaSSGPs;
	
	public GuiaSSGNe() {
		obLog = new LogNe();
	}
	
	/**
	 * Método para inserir a guia no SSG.
	 * @param GuiaEmissaoDt
	 * @param List<GuiaItemDt>
	 * @throws Exception
	 */
	public void inserirGuiaSSG(GuiaEmissaoDt guiaEmissaoDt, List listaGuiaItemDt, FabricaConexao obFabricaConexaoPJD) throws MensagemException, Exception {
		
		FabricaConexao obFabricaConexao =null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSSGPs = new  GuiaSSGPs(obFabricaConexao.getConexao());
			
			String idGuiaTipo = guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo();
			
			switch( idGuiaTipo ) {
				case GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU : {
					guiaSSGPs.inserirGuiaInicial2Grau(guiaEmissaoDt, listaGuiaItemDt, obFabricaConexaoPJD);
					break;
				}				
				default: {
					guiaSSGPs.inserirGuia2Grau(guiaEmissaoDt, listaGuiaItemDt, obFabricaConexaoPJD);				
					break;
				}
			}
		
		}
		finally{
            obFabricaConexao.fecharConexao();
        }
	}
	
	public GuiaEmissaoDt consultarGuiaEmissaoSSG(String numeroCompletoGuia) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaEmissaoDt = consultarGuiaEmissaoSSG(numeroCompletoGuia, obFabricaConexao);			
		}
		finally {
			if (obFabricaConexao != null) {
				obFabricaConexao.fecharConexao();
			}
        }
		
		return guiaEmissaoDt;
	}
	
	public GuiaEmissaoDt consultarGuiaEmissaoSSG(String numeroCompletoGuia, FabricaConexao obFabricaConexao) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		
		if( numeroCompletoGuia != null && numeroCompletoGuia.length() > 0 && Funcoes.StringToLong(numeroCompletoGuia) > 0 ) {
			guiaSSGPs = new  GuiaSSGPs(obFabricaConexao.getConexao());
			
			guiaEmissaoDt = guiaSSGPs.consultarGuiaEmissaoSSG(numeroCompletoGuia);
			
			if (guiaEmissaoDt != null) 
			{
				atualizeItensDaGuia(guiaEmissaoDt);
			}
		}
		
		return guiaEmissaoDt;
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
	
	/**
	 * Método para inserir o pagamento da guia do SSG no SAJ.
	 * @param GuiaEmissaoDt
	 * @param List<GuiaItemDt>
	 * @throws Exception
	 */
	public void inserirPagamentoGuiaSSGSAJ(GuiaEmissaoDt guiaEmissaoDt, List listaGuiaItemDt) throws MensagemException, Exception {
		
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
			if( numeroGuiaCompleto != null && numeroGuiaCompleto.length() > 0 && Funcoes.StringToLong(numeroGuiaCompleto) > 0 ) {
				
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
				GuiaSSGPs obPersistencia = new GuiaSSGPs(obFabricaConexao.getConexao());
				boletoDt = obPersistencia.buscaBoletoPorNumero(numeroGuiaCompleto);
				if (boletoDt != null)
				{
					atualizeItensDaGuia(boletoDt);
				}
				
			}
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		
		return boletoDt;
	}
	
	/**
	 * Método para consultar no SSG (2º grau) as guias do processo Projudi.
	 * 
	 * @param ProcessoDt processoDt
	 * @param List<GuiaEmissaoDt> listaGuiaProjudi
	 * 
	 * @return List<GuiaEmissaoDt>
	 * 
	 * @throws Exception
	 */
	public List<GuiaEmissaoDt> consultarGuiaEmissaoSSG(ProcessoDt processoDt, List<GuiaEmissaoDt> listaGuiaProjudi) throws Exception {
		List<GuiaEmissaoDt> listaGuiaEmissaoDt = null;
		List<GuiaEmissaoDt> listaGuiaAuxiliar = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSSGPs = new GuiaSSGPs(obFabricaConexao.getConexao());
			
			if( processoDt != null ) {
				
				// Somente número do processo, dígito e ano...
				String numeroProcessoDigitoAnoProjudi = Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(processoDt.getProcessoNumeroCompleto()), 21).trim().substring(0, 14);
				
				String numeroProcessoSPG = "";				
				if (processoDt.isProcessoImportadoSPG()) {
					// O número do processo com 4 zeros no final...
					numeroProcessoSPG = Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(processoDt.getProcessoNumeroAntigoTemp() + "0000"), 16).trim();
				}
				
				//validacao número zerados
				if( numeroProcessoDigitoAnoProjudi != null && Funcoes.StringToLong(numeroProcessoDigitoAnoProjudi) == 0 && numeroProcessoSPG != null && Funcoes.StringToLong(numeroProcessoSPG) == 0 ) {
					throw new MensagemException("Número Processo Projudi e SPG/SSG zerados.");
				}
				
				if( numeroProcessoDigitoAnoProjudi != null && Funcoes.StringToLong(numeroProcessoDigitoAnoProjudi) > 0 &&
					numeroProcessoSPG != null && Funcoes.StringToLong(numeroProcessoSPG) > 0 ) {
					
					listaGuiaAuxiliar = guiaSSGPs.consultarGuiaEmissaoSSG(numeroProcessoDigitoAnoProjudi, numeroProcessoSPG, listaGuiaProjudi);
					if( listaGuiaAuxiliar != null && !listaGuiaAuxiliar.isEmpty() ) {
						if( listaGuiaEmissaoDt == null ) {
							listaGuiaEmissaoDt = new ArrayList<>();
						}
						listaGuiaEmissaoDt.addAll(listaGuiaAuxiliar);
					}
					
					
					//Consulta guias com o número do processo SPG igual e no padrão CNJ	
					numeroProcessoSPG = processoDt.getAno().trim();
					if( processoDt.getAno().trim().length() < 4 ) {
						numeroProcessoSPG = Funcoes.preencheZeros(Funcoes.StringToLong(numeroProcessoSPG), 4);
					}
					numeroProcessoSPG += Funcoes.completarZeros(processoDt.getProcessoNumeroSimples().trim(), 7);
					numeroProcessoSPG += Funcoes.gerarDigitoNumeroProcessoAntigoSPG(numeroProcessoSPG);
					numeroProcessoSPG += "0000";
					
					listaGuiaAuxiliar = guiaSSGPs.consultarGuiaEmissaoSSG(null, numeroProcessoSPG, listaGuiaProjudi);
					if( listaGuiaAuxiliar != null && !listaGuiaAuxiliar.isEmpty() ) {
						if( listaGuiaEmissaoDt == null ) {
							listaGuiaEmissaoDt = new ArrayList<>();
						}
						listaGuiaEmissaoDt.addAll(listaGuiaAuxiliar);
					}
					
				}
			}
			
		}
		finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Método para ser utilizado no momento de cancelar uma guia no PJD. Em conversa com o Júnio Feitosa e 
	 * Marques (18/04/2018) ficou definido limpar os números de processos das guias no SSG para evitar 
	 * que a parte realize o pagamento de uma guia cancelada e depois ser realizada o rateio no SPG.
	 * 
	 * @param String numeroGuiaCompleto
	 * @param FabricaConexao obFabricaConexaoPJD
	 * @param int tipoLog
	 * @param String idUsuario
	 * @param String ipComputador
	 * @return boolean
	 * @throws Exception
	 */
	public boolean limparNumerosProcessosGuiaSSG(String numeroGuiaCompleto, FabricaConexao obFabricaConexaoPJD, int tipoLog, String idUsuario, String ipComputador) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() ) {
				
				GuiaEmissaoDt guiaEmissaoDtSSG = consultarGuiaEmissaoSSG(numeroGuiaCompleto);
				
				if( guiaEmissaoDtSSG != null && guiaEmissaoDtSSG.getNumeroGuiaCompleto() != null ) {
					obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
					GuiaSSGPs obPersistencia = new GuiaSSGPs(obFabricaConexao.getConexao());
					
					retorno = obPersistencia.limparNumerosProcessosGuiaSSG(guiaEmissaoDtSSG);
					
					//Log
					if( retorno ) {
						LogDt obLogDt = new LogDt("V_SSGUGUIAS", guiaEmissaoDtSSG.getId(), idUsuario, ipComputador, String.valueOf(tipoLog),"", "[Guia com processo limpado no SPG;ISN:"+ guiaEmissaoDtSSG.getId() +";numeroGuiaCompleto:" + guiaEmissaoDtSSG.getNumeroGuiaCompleto() +"]");
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
	
	/**
	 * Método para atualizar a guia no momento da vinculação dela com o processo.
	 * 
	 * @param String isnGuiaSSG
	 * @param String numeroProcesso
	 * @param String processoNumeroAntigoTemp
	 * @param String codigoServentia
	 * @param String comarcaCodigo
	 * @param String idUsuarioLog
	 * @param String ipComputadorLog
	 * @throws Exception
	 */
	public boolean atualizaGuiaVinculadaProcesso(String isnGuiaSSG, String numeroProcesso, String processoNumeroAntigoTemp, String codigoServentia, String comarcaCodigo, String idUsuarioLog, String ipComputadorLog) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSSGPs obPersistencia = new  GuiaSSGPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.atualizaGuiaVinculadaProcesso(isnGuiaSSG, numeroProcesso, processoNumeroAntigoTemp, codigoServentia, comarcaCodigo);
			if( retorno ) {
				LogDt obLogDt = new LogDt("V_SSGUGUIAS", isnGuiaSSG, idUsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.Alterar), "", "[ISN_SSGU_GUIAS:"+isnGuiaSSG+";NUMR_PROJUDI:"+numeroProcesso+";CODG_SERVENTIA:"+codigoServentia+";CODG_COMARCA:"+comarcaCodigo+";DATA_APRESENTACAO:"+Funcoes.BancoData(new Date())+";]");
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
	 * Método para consultar o número do processo SSG na guia.
	 * 
	 * @param String numeroCompletoGuia
	 * @return String
	 * @throws Exception
	 */
	public String consultarNumeroProcessoSSGGuiaEmissaoInicialProjudi(String numeroCompletoGuia) throws Exception {
		String numeroProcessoSPG = null;
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			guiaSSGPs = new  GuiaSSGPs(obFabricaConexao.getConexao());
		
			guiaEmissaoDt = guiaSSGPs.consultarGuiaEmissaoSSG(numeroCompletoGuia);
			
			// Verifica se a guia já foi utilizada no SSG...
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
	 * Método para desfazer cancelamento da guia.
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
	public boolean desfazerCancelamentoGuiaSSG(String numeroGuiaCompleto, String numeroProcessoDigitoAno, FabricaConexao obFabricaConexaoPJD, String idUsuario, String ipComputador) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() ) {
				
				GuiaEmissaoDt guiaEmissaoDtSSG = consultarGuiaEmissaoSSG(numeroGuiaCompleto);
				
				if( guiaEmissaoDtSSG != null && guiaEmissaoDtSSG.getNumeroGuiaCompleto() != null ) {
					obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
					GuiaSSGPs obPersistencia = new GuiaSSGPs(obFabricaConexao.getConexao());
					
					retorno = obPersistencia.desfazerCancelamentoGuiaSSG(guiaEmissaoDtSSG, numeroProcessoDigitoAno);
					
					//Log
					if( retorno ) {
						LogDt obLogDt = new LogDt("V_SSGUGUIAS", guiaEmissaoDtSSG.getId(), idUsuario, ipComputador, String.valueOf(LogTipoDt.Alterar),"", "[Guia com processo limpado no SPG;ISN:"+ guiaEmissaoDtSSG.getId() +";numeroGuiaCompleto:" + guiaEmissaoDtSSG.getNumeroGuiaCompleto() +"]");
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
