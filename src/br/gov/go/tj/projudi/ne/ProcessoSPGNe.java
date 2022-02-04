package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.NumeroProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteEnderecoSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoSPGDt;
import br.gov.go.tj.projudi.ps.GuiaSPGPs;
import br.gov.go.tj.projudi.ps.ProcessoSPGPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.MensagemException;

public class ProcessoSPGNe extends Negocio {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2465052305875446215L;

	public ProcessoSPGDt consulteProcesso(String numeroProcessoCompleto) throws Exception{
		ProcessoSPGDt processoDt = null;
		
		if (numeroProcessoCompleto != null && numeroProcessoCompleto.trim().length() > 0){
			FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.ADABASPROCESSOS);	
			try{
				NumeroProcessoDt numeroProcessoCompletoDt = new NumeroProcessoDt();
				numeroProcessoCompletoDt.setNumeroCompletoProcessoSemValidacao(numeroProcessoCompleto);
				
				processoDt = new ProcessoSPGPs(obFabricaConexao).consulteProcesso(numeroProcessoCompletoDt);
			}
			finally{
				obFabricaConexao.fecharConexao();
			}
		}	
		
		return processoDt;
	}
	
	public ProcessoSPGDt consulteProcesso(String numeroProcessoCompleto, FabricaConexao obFabricaConexao) throws Exception{
		ProcessoSPGDt processoDt = null;
		
		if (numeroProcessoCompleto != null && numeroProcessoCompleto.trim().length() > 0){
			NumeroProcessoDt numeroProcessoCompletoDt = new NumeroProcessoDt(numeroProcessoCompleto);
			
			processoDt = new ProcessoSPGPs(obFabricaConexao).consulteProcesso(numeroProcessoCompletoDt);
		}	
		
		return processoDt;
	}
	
	public ProcessoSPGDt consulteProcessoNumeroSPG(String numeroProcessoSPG) throws Exception{
		ProcessoSPGDt processoDt = null;
		
		if (numeroProcessoSPG != null && numeroProcessoSPG.trim().length() > 0){
			
			if( numeroProcessoSPG.contains(".") ) {
				throw new MensagemException("Número do Processo informado não pode conter pontos(.) [Erro1]");
			}
			
			FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.ADABASPROCESSOS);	
			try{
				processoDt = new ProcessoSPGPs(obFabricaConexao).consulteProcesso(numeroProcessoSPG);
			}
			finally{
				obFabricaConexao.fecharConexao();
			}
		}	
		
		return processoDt;
	}
	
	public ProcessoSPGDt consulteProcessoNumeroSPG(String numeroProcessoSPG, FabricaConexao obFabricaConexao) throws Exception{
		ProcessoSPGDt processoDt = null;
		
		if (numeroProcessoSPG != null && numeroProcessoSPG.trim().length() > 0){
			
			if( numeroProcessoSPG.contains(".") ) {
				throw new MensagemException("Número do Processo informado não pode conter pontos(.) [Erro2]");
			}
			
			processoDt = new ProcessoSPGPs(obFabricaConexao).consulteProcesso(numeroProcessoSPG);
		}	
		
		return processoDt;
	}
	
	public ProcessoParteEnderecoSPGDt obtenhaEnderecoParte(String isnParte, boolean isLocal, FabricaConexao obFabricaConexao) throws Exception{
		ProcessoParteEnderecoSPGDt dados = null;
		
		if (isnParte != null && isnParte.trim().length() > 0){
			dados = new ProcessoSPGPs(obFabricaConexao).obtenhaEnderecoParte(isnParte, isLocal);
		}	
		
		return dados;
	}
	
	public void retireDataHibrido(NumeroProcessoDt numeroProcessoCompletoDt) throws Exception{
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.ADABASPROCESSOS);	
		try{
			new ProcessoSPGPs(obFabricaConexao).retireDataHibrido(numeroProcessoCompletoDt);
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
	} 
	
	/**
	 * Método que agiliza consultando nas duas bases do SPG (capital e interior)
	 * 
	 * @param String numeroGuiaCompleto
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 * @author fogomes
	 */
	public GuiaEmissaoDt consultarNumeroProcessoCNJ(String numeroGuia) throws Exception {
		
		GuiaEmissaoDt guiaEmissaoSPGDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			GuiaSPGPs obPersistencia = new  GuiaSPGPs(obFabricaConexao.getConexao());
			
			if( numeroGuia != null && !numeroGuia.isEmpty() ) {
				
				//Consulta a guia no SPG (capital)
				guiaEmissaoSPGDt = obPersistencia.consultarGuiaSPGPeloNumeroGuiaCompletoCapital(numeroGuia);
				// MUDAR ESSA VERIFICAÇÃO. POIS ELA PODE VIR NÃO NULA MESMO NÃO TENDO ACHADO NA CAPITAL
				//Caso não exista, consulta a guia no spg na base interior
				//if (guiaEmissaoSPGDt == null) { // MUDAR ESSA VERIFICAÇÃO. POIS ELA PODE VIR NÃO NULA MESMO NÃO TENDO ACHADO NA CAPITAL
					//guiaEmissaoSPGDt = obPersistencia.consultarGuiaSPGPeloNumeroGuiaCompletoInterior(numeroGuiaCompleto);
				//}
				// COMO FAZER ELE CONSULTAR NO LUGAR CORRETO E SALVAR O GUIA-EMISSAO-SPGDT QUE ESTEJA PREENCHIDO
				
//				if (guiaEmissaoSPGDt == null){
//					guiaEmissaoSPGDt = obPersistencia.consultarGuiaSPGPeloNumeroGuiaCompletoInterior(numeroGuia);
//				} else {
//					if(guiaEmissaoSPGDt.getNumeroGuiaCompleto().isEmpty() || guiaEmissaoSPGDt.getNumrCpfCgc().isEmpty()) {
//						guiaEmissaoSPGDt = obPersistencia.consultarGuiaSPGPeloNumeroGuiaCompletoInterior(numeroGuia);
//					}
//				}
				// TESTAR ESSA VERIFICAÇÃO
				if (guiaEmissaoSPGDt == null || (guiaEmissaoSPGDt.getNumeroProcesso().isEmpty() || guiaEmissaoSPGDt.getNumeroProcesso() == null)) {
					
					guiaEmissaoSPGDt = obPersistencia.consultarGuiaSPGPeloNumeroGuiaCompletoInterior(numeroGuia);
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
	
	public void insereDataHibrido(NumeroProcessoDt numeroProcessoCompletoDt) throws Exception{
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.ADABASPROCESSOS);	
		try{
			new ProcessoSPGPs(obFabricaConexao).insereDataHibrido(numeroProcessoCompletoDt);
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
	}
}
