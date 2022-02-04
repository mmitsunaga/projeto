package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

//---------------------------------------------------------
public class ProcessoResponsavelDt extends ProcessoResponsavelDtGen {

	/**
     * 
     */
    private static final long serialVersionUID = 6653616752224057439L;

    public static final int CodigoPermissao = 392;
    public static final int CodigoPermissaoAlterarPromotorResponsavel = 614;

	// Variáveis para controlar possíveis status de ProcessoResponsável
	public static final int INATIVO = -1;
	public static final int ATIVO = 0;
	public static final int PROCESSO_RESPONSAVEL_REDATOR_ATIVO = 1;
	public static final int PROCESSO_RESPONSAVEL_REDATOR_INATIVO = -1;

	public static final int REDATOR_NAO=0;

	private List listaProcessos;
	
	private String serventiaTipoCodigoProcesso;

	private String Redator;
	
	private String nomeUsuario;
	
	public void limpar() {
		super.limpar();
		listaProcessos = null;	
		serventiaTipoCodigoProcesso = "";
	}
	/*
	 * Jesus
	 * Pego o primeiro processo da lista
	 * testo null e tamanho
	 */
	public ProcessoDt getPrimeiroProcessoLista(){
		ProcessoDt proc = null;
		if (listaProcessos!=null && listaProcessos.size()>0){
			proc = (ProcessoDt)listaProcessos.get(0);
		}				
		return proc;
	}

	public List getListaProcessos() {
		return listaProcessos;
	}

	public void setListaProcessos(List listaProcessos) {
		this.listaProcessos = listaProcessos;
	}

	public void addListaProcessos(ProcessoDt processo) {
		if (listaProcessos == null) listaProcessos = new ArrayList();
		listaProcessos.add(processo);
	}
	
	public void setServentiaTipoCodigoProcesso(String serventiaTipoCodigo){
		this.serventiaTipoCodigoProcesso = serventiaTipoCodigo;
	}
	
	public String getServentiaTipoCodigoProcesso(){
		return this.serventiaTipoCodigoProcesso;
	}

	public void setRedator(boolean redator) {
		this.Redator = String.valueOf(redator);
		
	}
	public void setRedator(String redator) {
		this.Redator = redator;
		
	}	
	public String getRedator(){
		return this.Redator;
	}
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	
}
