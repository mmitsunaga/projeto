package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;



public class ProcessoCertidaoPositivaNegativaDt extends ProcessoCertidaoDt {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5674384610537564150L;
	private List promovidoAdvogado;
	private String valor;
	private List promoventeAdvogado;
	private String idProcessoPartePromovido;
	protected String sistema;

	
	


	public ProcessoCertidaoPositivaNegativaDt() {
		super();
		this.promovidoAdvogado = new ArrayList(5);
		this.promoventeNome = new ArrayList(5);
		this.promoventeAdvogado = new ArrayList(5);
		this.assunto = new ArrayList(5);
		this.idProcessoPartePromovido = "";
		this.sistema = "";

	}
	
	
	
	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		if (sistema != null)
			this.sistema = sistema;
	}

	public List getPromovidoAdvogado() {
		return promovidoAdvogado;
	}

	/**
	 * Retorna o número completo de um processo, obedecendo a padronização do
	 * CNJ
	 */
	public String getProcessoNumeroCompleto() {
		if (getDigitoVerificador() == null || getDigitoVerificador().equals("")) {
			return this.getProcessoNumero();
		} else {
			return (Funcoes.completarZeros(getProcessoNumero(), 7) + "." + Funcoes.completarZeros(getDigitoVerificador(), 2) + "." + getAno() + "." + Configuracao.JTR + "." + Funcoes.completarZeros(getForumCodigo(), 4));
		}
	}
	
	public void setPromovidoAdvogado(List ParteBuscadaAdvogado) {
		this.promovidoAdvogado = ParteBuscadaAdvogado;
	}

	public void setProcessoNumero(String processoNumero) {
		this.processoNumero = processoNumero;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public List getPromoventeAdvogado() {
		return promoventeAdvogado;
	}

	public void setPromoventeAdvogado(List ParteContrariaAdvogado) {
		this.promoventeAdvogado = ParteContrariaAdvogado;
	}

	public void addPromovidoAdvogado(String advogado) {
		if(!promovidoAdvogado.contains(advogado))
		this.promovidoAdvogado.add(advogado);
	}

	public String promovidoAdvogadoToString() {
		if(promovidoAdvogado.size() == 0)
			return null;
		StringBuilder sb = new StringBuilder();
		Iterator it = promovidoAdvogado.iterator();
		while (it.hasNext()) {
			String advgd = (String) it.next();
			sb.append(advgd + ", ");
		}
		int tamanho = sb.length();
		sb.delete(tamanho - 2, tamanho - 1);
		return sb.toString();
	}
	
	public String promoventeAdvogadoToString() {
		if(promoventeAdvogado.size() == 0)
			return null;
		StringBuilder sb = new StringBuilder();
		Iterator it = promoventeAdvogado.iterator();
		while (it.hasNext()) {
			String advgd = (String) it.next();
			sb.append(advgd + ", ");
		}
		int tamanho = sb.length();
		sb.delete(tamanho - 2, tamanho - 1);
		return sb.toString();
	}

	public void addPromoventeAdvogado(String ParteContrariaAdvogado) {
		if (!this.promoventeAdvogado.contains(ParteContrariaAdvogado))
			this.promoventeAdvogado.add(ParteContrariaAdvogado);
	}
	
	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {
	}

	public String getIdProcessoPartePromovido() {
		return idProcessoPartePromovido;
	}

	public void setIdProcessoPartePromovido(String idProcessoPartePromovido) {
		this.idProcessoPartePromovido = idProcessoPartePromovido;
	}
	

}
