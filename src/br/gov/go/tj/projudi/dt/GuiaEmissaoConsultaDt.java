package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

public class GuiaEmissaoConsultaDt extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3406132614263247495L;
	
	public static final int CodigoPermissao = 818;

	private NumeroProcessoDt numeroProcessoDt;
	private ProcessoGuiaEmissaoConsultaDt processoGuiaEmissaoConsultaDt;
	private List<GuiaEmissaoCompletaDt> listaDeGuiaEmitidas;
	private UsuarioDt usuarioDt;
	
	public GuiaEmissaoConsultaDt()
	{
		this.setListaDeGuiasEmitidas(new ArrayList<GuiaEmissaoCompletaDt>());
		this.numeroProcessoDt = new NumeroProcessoDt();
	}
	
	@Override
	public void setId(String id) {}

	@Override
	public String getId() {return null; }
	
	public NumeroProcessoDt getNumeroProcessoCompletoDt() {
		return numeroProcessoDt;
	}

	public void setNumeroProcessoCompletoDt(NumeroProcessoDt numeroProcessoDt) {
		this.numeroProcessoDt = numeroProcessoDt;
	}

	public List<GuiaEmissaoCompletaDt> getListaDeGuiasEmitidas() {
		return listaDeGuiaEmitidas;
	}

	public void setListaDeGuiasEmitidas(List<GuiaEmissaoCompletaDt> listaDeGuiasEmitidas) {
		this.listaDeGuiaEmitidas = listaDeGuiasEmitidas;
	}
	
	public void adicioneGuiaEmitida(GuiaEmissaoCompletaDt guiaEmitida) {
		if (this.listaDeGuiaEmitidas == null) this.listaDeGuiaEmitidas = new ArrayList<GuiaEmissaoCompletaDt>();
		this.listaDeGuiaEmitidas.add(guiaEmitida);
	}

	public ProcessoGuiaEmissaoConsultaDt getProcessoGuiaEmissaoConsultaDt() {
		return processoGuiaEmissaoConsultaDt;
	}

	public void setProcessoGuiaEmissaoConsultaDt(ProcessoGuiaEmissaoConsultaDt processoPJEDt) {
		this.processoGuiaEmissaoConsultaDt = processoPJEDt;
	}

	public UsuarioDt getUsuarioDt() {
		return usuarioDt;
	}

	public void setUsuarioDt(UsuarioDt usuarioDt) {
		this.usuarioDt = usuarioDt;
	}

	public String getId_Processo() {
		if (processoGuiaEmissaoConsultaDt!=null )
			return processoGuiaEmissaoConsultaDt.getId();
		return null;
	}	
}
