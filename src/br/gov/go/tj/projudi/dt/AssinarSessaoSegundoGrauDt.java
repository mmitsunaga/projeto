package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.gov.go.tj.projudi.ne.AudienciaNe;
import br.gov.go.tj.projudi.ne.ProcessoParteNe;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class AssinarSessaoSegundoGrauDt implements Serializable {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5755700481880050434L;

	private static final String separadorAssinatura = "__@---";
	private static final String separadorAssinaturaNomeConteudo = "<@_@-@>";
	private static final String identificadorAcordao = "(Relatório, Voto e Acórdão)";
	private static final String identificadorEmenta = "(Ementa)";
		
	private String id_UsuarioLog;
	private String ipComputadorLog;
	
	private List<SessaoSegundoGrauProcessoDt> listaSessoesSegundoGrau;
	
	private List<PendenciaArquivoDt> listaPendenciaArquivo;
		
	private String conteudoArquivos;
	
	private boolean isAcaoAssinatura;
	
	private Map<String, AudienciaMovimentacaoDt> mapAnalisePendencia;
	private List<AudienciaMovimentacaoDt> listaAnaliseAcordaoEmenta;
	
	public AssinarSessaoSegundoGrauDt() {
		listaSessoesSegundoGrau = new ArrayList<SessaoSegundoGrauProcessoDt>();
		listaPendenciaArquivo = new ArrayList<PendenciaArquivoDt>();
		mapAnalisePendencia = new LinkedHashMap<String, AudienciaMovimentacaoDt>();
		listaAnaliseAcordaoEmenta = new ArrayList<AudienciaMovimentacaoDt>();
		limpar();
	}
	
	public void limpar() {
		listaSessoesSegundoGrau.clear();
		listaPendenciaArquivo.clear();
		mapAnalisePendencia.clear();	
		listaAnaliseAcordaoEmenta.clear();
		isAcaoAssinatura = false;
		conteudoArquivos = "";		
	}
	
	public String getId_UsuarioLog() {
		return id_UsuarioLog;
	}

	public void setId_UsuarioLog(String id_UsuarioLog) {
		if (id_UsuarioLog != null) this.id_UsuarioLog = id_UsuarioLog;
	}

	public String getIpComputadorLog() {
		return ipComputadorLog;
	}

	public void setIpComputadorLog(String ipComputadorLog) {
		if (ipComputadorLog != null) this.ipComputadorLog = ipComputadorLog;
	}
	
	public List<SessaoSegundoGrauProcessoDt> getListaSessoesSegundoGrau() {
		return listaSessoesSegundoGrau;
	}

	public void adicioneListaSessoesSegundoGrau(SessaoSegundoGrauProcessoDt sessaoSegundoGrauProcessoDt) {
		this.listaSessoesSegundoGrau.add(sessaoSegundoGrauProcessoDt);
	}
	
	public List<SessaoSegundoGrauProcessoDt> getListaSessoesSegundoGrau(String audienciasProcesso[]) throws Exception {
		List<String> tempListIdAudienciasProcesso = new ArrayList<String>();
		List<SessaoSegundoGrauProcessoDt> tempPreAnalises = new ArrayList<SessaoSegundoGrauProcessoDt>();
		ProcessoParteNe processoParteNe = new ProcessoParteNe();
		String id_AudienciasProcesso;
		for (int i = 0; i < audienciasProcesso.length; i++) {
			id_AudienciasProcesso = (String) audienciasProcesso[i];			
			tempListIdAudienciasProcesso.add(id_AudienciasProcesso);
		}
		
		for(SessaoSegundoGrauProcessoDt sessaoSegundoGrauProcessoDt : listaSessoesSegundoGrau) {
			if (tempListIdAudienciasProcesso.contains(sessaoSegundoGrauProcessoDt.getAudienciaProcessoDt().getId())){
				sessaoSegundoGrauProcessoDt.setId_UsuarioLog(this.getId_UsuarioLog());
				sessaoSegundoGrauProcessoDt.setIpComputadorLog(this.getIpComputadorLog());
				
				if (sessaoSegundoGrauProcessoDt.isSessaoVirtual()) {
					//lrcampos 11/02/2020 - Adiciona classe do processo caso tenha recurso/recurso secundario.
					sessaoSegundoGrauProcessoDt.getAudienciaProcessoDt().setProcessoTipo(processoParteNe.consultaClasseProcessoIdAudiProc(sessaoSegundoGrauProcessoDt.getAudienciaProcessoDt().getId()));
				}
				
				tempPreAnalises.add(sessaoSegundoGrauProcessoDt);
			}	
		}
		
		return tempPreAnalises;
	}
		
	public void atualizeListaSessoesSelecionadas(String audienciasProcesso[]) throws Exception{
		List<String> tempListIdAudienciasProcesso = new ArrayList<String>();
		List<SessaoSegundoGrauProcessoDt> tempPreAnalises = new ArrayList<SessaoSegundoGrauProcessoDt>();
		List<PendenciaArquivoDt> tempListaPendenciaArquivo = new ArrayList<PendenciaArquivoDt>();
		ProcessoParteNe processoParteNe = new ProcessoParteNe();
		String id_AudienciasProcesso;
		for (int i = 0; i < audienciasProcesso.length; i++) {
			id_AudienciasProcesso = (String) audienciasProcesso[i];			
			tempListIdAudienciasProcesso.add(id_AudienciasProcesso);
		}
		
		for(SessaoSegundoGrauProcessoDt sessaoSegundoGrauProcessoDt : listaSessoesSegundoGrau) {
			if (tempListIdAudienciasProcesso.contains(sessaoSegundoGrauProcessoDt.getAudienciaProcessoDt().getId())){
				sessaoSegundoGrauProcessoDt.setId_UsuarioLog(this.getId_UsuarioLog());
				sessaoSegundoGrauProcessoDt.setIpComputadorLog(this.getIpComputadorLog());
				
				if (sessaoSegundoGrauProcessoDt.isSessaoVirtual()) {
					//lrcampos 11/02/2020 - Adiciona classe do processo caso tenha recurso/recurso secundario.
					sessaoSegundoGrauProcessoDt.getAudienciaProcessoDt().setProcessoTipo(processoParteNe.consultaClasseProcessoIdAudiProc(sessaoSegundoGrauProcessoDt.getAudienciaProcessoDt().getId()));
				}
				
				tempPreAnalises.add(sessaoSegundoGrauProcessoDt);
				
				tempListaPendenciaArquivo.add(sessaoSegundoGrauProcessoDt.getPendenciaArquivoDtRelatorioEVoto());
				tempListaPendenciaArquivo.add(sessaoSegundoGrauProcessoDt.getPendenciaArquivoDtEmenta());
			}	
		}
		
		listaPendenciaArquivo.clear();
		listaPendenciaArquivo = tempListaPendenciaArquivo;
		listaSessoesSegundoGrau.clear();
		listaSessoesSegundoGrau = tempPreAnalises;
	}

	public void setAcaoAssinatura(boolean isAcaoAssinatura) {
		this.isAcaoAssinatura = isAcaoAssinatura;
	}

	public boolean isAcaoAssinatura() {
		return isAcaoAssinatura;
	}
	
	public void prepareDadosAssinatura(List<AudienciaMovimentacaoDt> listaAnaliseAcordaoEmenta) throws MensagemException {			
		conteudoArquivos = "";

		this.mapAnalisePendencia.clear();
		this.listaAnaliseAcordaoEmenta = listaAnaliseAcordaoEmenta;
		
		for(AudienciaMovimentacaoDt audienciaMovimentacaoDt : listaAnaliseAcordaoEmenta) {
			//Acórdão
			String chaveMap = obtenhaChaveMapAcordao(audienciaMovimentacaoDt);
			
			if(conteudoArquivos.length() > 0) {
				conteudoArquivos += separadorAssinatura;
			}
			conteudoArquivos += chaveMap+separadorAssinaturaNomeConteudo+audienciaMovimentacaoDt.getTextoEditor();
			
			//testar chave
			if (this.mapAnalisePendencia.containsKey(chaveMap)){
				 throw new MensagemException("Problema ao montar lista de arquivos a serem assinados (Acórdão).");
			} else {
				this.mapAnalisePendencia.put(chaveMap, audienciaMovimentacaoDt);
			}
			
			//Ementa
			chaveMap = obtenhaChaveMapEmenta(audienciaMovimentacaoDt);	
			
			conteudoArquivos += separadorAssinatura;
			conteudoArquivos += chaveMap+separadorAssinaturaNomeConteudo+audienciaMovimentacaoDt.getTextoEditorEmenta();
			
			//testar chave
			if (this.mapAnalisePendencia.containsKey(chaveMap)){
				 throw new MensagemException("Problema ao montar lista de arquivos a serem assinados (Ementa).");
			} else {
				this.mapAnalisePendencia.put(chaveMap, audienciaMovimentacaoDt);
			}	
			
			if (audienciaMovimentacaoDt.getListaArquivos() == null) audienciaMovimentacaoDt.setListaArquivos(new ArrayList());
			else audienciaMovimentacaoDt.getListaArquivos().clear();
		}
	}	
	
	private String obtenhaChaveMapAcordao(AudienciaMovimentacaoDt audienciaMovimentacaoDt){
		return Funcoes.formataNumeroProcesso(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt().getProcessoNumeroCompleto()) + " " + identificadorAcordao + " : " + audienciaMovimentacaoDt.getNomeArquivo();
	}
	
	private String obtenhaChaveMapEmenta(AudienciaMovimentacaoDt audienciaMovimentacaoDt){
		audienciaMovimentacaoDt.setNomeArquivoEmenta((AudienciaNe.prefixoArquivoEmenta + audienciaMovimentacaoDt.getNomeArquivoEmenta()).trim());
		return Funcoes.formataNumeroProcesso(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt().getProcessoNumeroCompleto()) + " " + identificadorEmenta + " : " + audienciaMovimentacaoDt.getNomeArquivoEmenta();
	}	

	public String getConteudoArquivos() {
		return conteudoArquivos;
	}
	
	public List<AudienciaMovimentacaoDt> getListaAudienciaMovimentacaoDt() {
		return this.listaAnaliseAcordaoEmenta;
	}
	
	@SuppressWarnings("unchecked")
	public void setArquivoAssinado(String chave, ArquivoDt dtArquivo) {
		if(chave != null){
			AudienciaMovimentacaoDt audienciaMovimentacaoDt = (AudienciaMovimentacaoDt)this.mapAnalisePendencia.get(chave);
			if (audienciaMovimentacaoDt != null) {
				if (dtArquivo.getNomeArquivo().trim().toUpperCase().startsWith(AudienciaNe.nomeArquivoEmenta.toUpperCase())) {
					dtArquivo.setArquivoTipoCodigo(String.valueOf(ArquivoTipoDt.EMENTA));
				} else if (dtArquivo.getNomeArquivo().trim().length() > 
				           AudienciaNe.prefixoArquivoEmenta.length() && dtArquivo.getNomeArquivo().trim().substring(0, AudienciaNe.prefixoArquivoEmenta.length()).equalsIgnoreCase(AudienciaNe.prefixoArquivoEmenta)) {
					dtArquivo.setArquivoTipoCodigo(String.valueOf(ArquivoTipoDt.EMENTA));
				} else {
					dtArquivo.setArquivoTipoCodigo(String.valueOf(ArquivoTipoDt.RELATORIO_VOTO));
				}
				
				dtArquivo.setId_ArquivoTipo(audienciaMovimentacaoDt.getId_ArquivoTipo());
				dtArquivo.setArquivoTipo(audienciaMovimentacaoDt.getArquivoTipo());
				
				audienciaMovimentacaoDt.getListaArquivos().add(dtArquivo);
			}
		}	
	}

	public static boolean isArquivoSessaoSegundoGrau(String chave) {
		try 
		{
			if (chave == null) return false;
			return (chave.trim().contains(identificadorAcordao) || chave.trim().contains(identificadorEmenta));			
		}catch (Exception e) {
			return false;
		}
	}
}