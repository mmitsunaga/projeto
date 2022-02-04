package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class AssinarPendenciaDt implements Serializable {	
	
	private static final long serialVersionUID = -6198682144310779326L;
	private static final String separadorAssinatura = "__@---";
	private static final String separadorAssinaturaNomeConteudo = "<@_@-@>";
	private String id_UsuarioLog;
	private String ipComputadorLog;
	private String tipoPendencia;
	private String numeroProcesso;
	private List listaPendenciaArquivo;
	private String nomeArquivos;
	private String conteudoArquivos;
	private boolean isAcaoAssinatura;
	private Map mapAnalisePendencia;
	private boolean ehParaImprimir;
	
	public AssinarPendenciaDt() {
		listaPendenciaArquivo = new ArrayList();
		mapAnalisePendencia = new LinkedHashMap();
		limpar();
	}
	
	public void limpar() {
		listaPendenciaArquivo.clear();
		mapAnalisePendencia.clear();		
		tipoPendencia = "";
		isAcaoAssinatura = false;
		nomeArquivos = "";
		conteudoArquivos = "";
		limparProcesso();
		ehParaImprimir = false;
	}
	
	public void limparProcesso() {
		numeroProcesso = "";
	}

	public void copiar(AnalisePendenciaDt objeto) {		
		tipoPendencia = objeto.getId_PendenciaTipo();
		numeroProcesso = objeto.getNumeroProcesso();
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
	
	public String getTipoPendencia() {
		return tipoPendencia;
	}

	public void setTipoPendencia(String tipoPendencia) {
		if (tipoPendencia != null) if (tipoPendencia.equalsIgnoreCase("null")) {
			this.tipoPendencia = "";
		} else if (!tipoPendencia.equalsIgnoreCase("")) this.tipoPendencia = tipoPendencia;
	}
	
	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		if (numeroProcesso != null) if (numeroProcesso.equalsIgnoreCase("null")) {
			this.numeroProcesso = "";
		} else if (!numeroProcesso.equalsIgnoreCase("")) this.numeroProcesso = numeroProcesso;
	}

	public void setListaPendenciaArquivo(List listaPendenciaArquivo) {
		if (listaPendenciaArquivo != null) this.listaPendenciaArquivo = listaPendenciaArquivo;
	}

	public List getListaPendenciaArquivo() {
		return listaPendenciaArquivo;
	}
	
	public void atualizeListaPendenciasSelecionadas(String pendencias[]){
		List tempListIdPendencias = new ArrayList();
		List tempPreAnalises = new ArrayList();
					
		String id_Pendencia;
		for (int i = 0; i < pendencias.length; i++) {
			id_Pendencia = (String) pendencias[i].trim();			
			tempListIdPendencias.add(id_Pendencia);
		}
		
		for(int i = 0 ; i< listaPendenciaArquivo.size();i++) {
			PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt)listaPendenciaArquivo.get(i);
			
			if (tempListIdPendencias.contains(pendenciaArquivoDt.getPendenciaDt().getId())){
				pendenciaArquivoDt.setId_UsuarioLog(this.getId_UsuarioLog());
				pendenciaArquivoDt.setIpComputadorLog(this.getIpComputadorLog());
				tempPreAnalises.add(pendenciaArquivoDt);
			}		
			
		}
		
		listaPendenciaArquivo.clear();
		listaPendenciaArquivo = tempPreAnalises; 
	}

	public void setAcaoAssinatura(boolean isAcaoAssinatura) {
		this.isAcaoAssinatura = isAcaoAssinatura;
	}

	public boolean isAcaoAssinatura() {
		return isAcaoAssinatura;
	}
	
	public void prepareDadosAssinatura(List listaAnalisePendencia) throws MensagemException {			
	//	nomeArquivos = "";
		conteudoArquivos = "";
		//alteração leandro
		this.mapAnalisePendencia.clear();
		
		for(int i = 0 ; i< listaAnalisePendencia.size();i++) {
			AnalisePendenciaDt analisePendenciaDt = (AnalisePendenciaDt)listaAnalisePendencia.get(i);
			
			String chaveMap = obtenhaChaveMap(analisePendenciaDt);	
			//nomeArquivos = separadorAssinatura;
//			if(nomeArquivos.length() > 0) {
//				nomeArquivos += separadorAssinatura;
//			}
			
			//nomeArquivos = chaveMap;
			
			if(conteudoArquivos.length() > 0) {
				conteudoArquivos += separadorAssinatura;
			}
			conteudoArquivos += chaveMap+separadorAssinaturaNomeConteudo+analisePendenciaDt.getTextoEditor();
			
			//testar chave
			if (this.mapAnalisePendencia.containsKey(chaveMap)){
				 throw new MensagemException("Problema ao montar lista de arquivos a serem assinados.");
			} else {
				this.mapAnalisePendencia.put(chaveMap, analisePendenciaDt);
			}
		}
		
	}
	
	private String obtenhaChaveMap(AnalisePendenciaDt analisePendenciaDt) throws MensagemException{
		return Funcoes.formataNumeroProcesso( analisePendenciaDt.getNumeroPrimeiroProcessoListaFechar())+"."+ analisePendenciaDt.getId_Pendencia() + "_:_" + analisePendenciaDt.getNomeArquivo();
	}

	public String getNomeArquivos() {
		return nomeArquivos;
	}

	public String getConteudoArquivos() {
		return conteudoArquivos;
	}
	
	public List getListaAnalisePendencia(){
		return Funcoes.converterMapParaList(mapAnalisePendencia);
	}
	
	public void atualizeArquivosAssinados(Map mapArquivosAssinados){
		ArquivoDt dtArquivo = null;
		AnalisePendenciaDt analisePendenciaDt = null;
		List listaArquivos = null;
		
		Set<String> chaves = mapArquivosAssinados.keySet();
		for (String chave : chaves){		
			if(chave != null){
				analisePendenciaDt = (AnalisePendenciaDt)this.mapAnalisePendencia.get(chave);
				
				if (analisePendenciaDt != null) {					
					dtArquivo = (ArquivoDt)mapArquivosAssinados.get(chave);									
					dtArquivo.setId_ArquivoTipo(analisePendenciaDt.getId_ArquivoTipo());
					dtArquivo.setArquivoTipo(analisePendenciaDt.getArquivoTipo());					
					
					listaArquivos = new ArrayList();					
					listaArquivos.add(dtArquivo);
					analisePendenciaDt.setListaArquivos(listaArquivos);
				}
			}			                  
		}		
	}

	public boolean isEhParaImprimir() {
		return ehParaImprimir;
	}

	public void setEhParaImprimir(boolean ehParaImprimir) {
		this.ehParaImprimir = ehParaImprimir;
	}

	public void setArquivoAssinado(String chave, ArquivoDt dtArquivo) {
		if(chave != null){
			AnalisePendenciaDt analisePendenciaDt = (AnalisePendenciaDt)this.mapAnalisePendencia.get(chave);
			if (analisePendenciaDt != null) {					
				dtArquivo.setId_ArquivoTipo(analisePendenciaDt.getId_ArquivoTipo());
				dtArquivo.setArquivoTipo(analisePendenciaDt.getArquivoTipo());					
				
				ArrayList listaArquivos = new ArrayList();					
				listaArquivos.add(dtArquivo);
				analisePendenciaDt.setListaArquivos(listaArquivos);
			}
		}	
	}
	
	public int getQuantidadeConclucoesPendentes() {
		if (this.mapAnalisePendencia != null){
			return this.mapAnalisePendencia.size();
		}
		return 0; 
	}
	
	public AnaliseConclusaoDt getProximaConclusaoPendente() {
		if (this.mapAnalisePendencia != null && !mapAnalisePendencia.isEmpty()){
			Map.Entry<String,AnaliseConclusaoDt> entry = (Entry<String, AnaliseConclusaoDt>) this.mapAnalisePendencia.entrySet().iterator().next();
			return  (AnaliseConclusaoDt) this.mapAnalisePendencia.remove(entry.getKey());
		}
		
		return null;
	}
	
	public AnalisePendenciaDt getProximaPendenciaPendente() {
		if (this.mapAnalisePendencia != null && !mapAnalisePendencia.isEmpty()){
			Map.Entry<String,AnalisePendenciaDt> entry = (Entry<String, AnalisePendenciaDt>) this.mapAnalisePendencia.entrySet().iterator().next();
			return  (AnalisePendenciaDt) this.mapAnalisePendencia.remove(entry.getKey());
		}
		
		return null;
	}
}