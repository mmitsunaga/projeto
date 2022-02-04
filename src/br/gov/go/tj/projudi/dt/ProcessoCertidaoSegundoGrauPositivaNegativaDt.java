package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoCertidaoSegundoGrauPositivaNegativaDt extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1573965384092098801L;
	private String ano;
	private String faseAtual;
	private String camara;
	private String sistema;
	private String digitoVerificador;
	private String processoTipo;
	private String numero;
	private String forumCodigo;
	private String relator;
	private String id_Processo;
	private String protoColoOrigem;
	private String requerente;
	private String promovidoNome;
	private String promovidoCpf;
	private String promovidoCnpj;
	private String promovidoNomeMae;
	private String promovidoDataNascimento;
	private String promoventeNome;
	private List promoventeAdvogado;
	private List promovidoAdvogado;

	
	public ProcessoCertidaoSegundoGrauPositivaNegativaDt() {
		ano = "";
		faseAtual = "";
		camara = "";
		sistema = "";
		digitoVerificador = "";
		processoTipo = "";
		numero = "";
		forumCodigo = "";
		relator = "";
		id_Processo = "";
		protoColoOrigem = "";
		promovidoNome = "";
		promovidoCpf = "";
		promovidoCnpj = "";
		promovidoNomeMae = "";
		promovidoDataNascimento = "";
		promoventeNome = "";
		promoventeAdvogado = new ArrayList(5);
		promovidoAdvogado = new ArrayList(5);
	}
	
	public String getAno() {
		return ano;
	}

	public String getSistema() {
		return sistema;
	}


	public String getDigitoVerificador() {
		return digitoVerificador;
	}

	public String getProcessoTipo() {
		return processoTipo;
	}

	public String getForumCodigo() {
		return forumCodigo;
	}

	public String getRelator() {
		return relator;
	}
	

	public String getProtoColoOrigem() {
		return protoColoOrigem;
	}

	public void setProtoColoOrigem(String protoColoOrigem) {
		this.protoColoOrigem = protoColoOrigem;
	}

	public String getFaseAtual() {
		return faseAtual;
	}

	public void setFaseAtual(String faseAtual) {
		this.faseAtual = faseAtual;
	}

	public String getCamara() {
		return camara;
	}

	public void setCamara(String camara) {
		this.camara = camara;
	}


	/**
	 * Retorna o número completo de um processo, obedecendo a padronização do CNJ
	 */
	public String getProcessoNumeroCompleto() {
		if (getDigitoVerificador() == null || getDigitoVerificador().equals("")) {
			return this.getProcessoNumero();
		} else {
			return (Funcoes.completarZeros(getProcessoNumero(), 7) + "."
					+ Funcoes.completarZeros(getDigitoVerificador(), 2) + "."
					+ getAno() + "." + Configuracao.JTR + "." + Funcoes
					.completarZeros(getForumCodigo(), 4));
		}
	}
	
	public String getProcessoNumeroDigito() {
		String stRetorno = getProcessoNumero();
		if (getDigitoVerificador().length() > 0) stRetorno += "." + getDigitoVerificador();
		return stRetorno;
	}

	@Override
	public void setId(String id) {
		

	}

	@Override
	public String getId() {
		return null;
	}

	public void setId_Processo(String id) {
		this.id_Processo = id;
	}
	
	public String getId_Processo() {
		return this.id_Processo;
	}
	
	public String getRequerente() {
		return requerente;
	}

	public void setRequerente(String requerente) {
		this.requerente = requerente;
	}

	public void setProcessoNumero(String string) {
		this.numero = string;
	}
	
	public String getProcessoNumero() {
		return  this.numero;
	}

	public void setProcessoTipo(String string) {
		this.processoTipo = string;
		
	}

	public void setDigitoVerificador(String string) {
		this.digitoVerificador = string;
	}

	public void setForumCodigo(String string) {
	this.forumCodigo = string;		
	}

	public void setAno(String string) {
		this.ano = string;
	}

	public void setSistema(String string) {
		this.sistema = string;
	}
	
	//Protocolo de Origem

	public void setRelator(String string) {
		this.relator = string;
	}

	public void setIdProcessoPartePromovido(String string) {
		
		
	}

	public void setPromovidoNome(String string) {
		promovidoNome = string;		
	}
	
	public String getPromovidoNome() {
		return promovidoNome == null ? "" : promovidoNome;
	}
	public void setPromovidodaSexo(String string) {		
	}

	public void setPromovidoCnpj(String string) {
		promovidoCnpj = string;		
	}

	public void setPromovidoCpf(String string) {
		promovidoCpf = string;		
	}
	
	public String getPromovidoCpf() {
		return promovidoCpf == null ? "" : promovidoCpf;
	}
	
	public String getPromovidoCpfCnpj() {
		return promovidoCpf == null ? promovidoCnpj == null ? "" : promovidoCnpj : promovidoCpf;
	}
	
	public boolean isPessoaJuridica() {
		return (this.promovidoCpf == null || this.promovidoCpf.equals("")) && this.promovidoCnpj != null;
	}
	public String getPromovidoCnpj() {
		return promovidoCnpj == null ? "" : promovidoCnpj;
	}
	public void setPromovidoNomeMae(String string) {
		promovidoNomeMae = string;		
	}
	public String getPromovidoNomeMae() {
		return promovidoNomeMae == null ? "": promovidoNomeMae;
	}

	public void setPromovidoDataNascimento(String string) {
		promovidoDataNascimento = string;		
	}
	
	public String getPromovidoDataNascimento() {
		return promovidoDataNascimento == null ? "" : promovidoDataNascimento;
	}
	

	public void setPromoventeNome(String promoventeNome) {
		this.promoventeNome = promoventeNome;
	}

	public void addPromoventeAdvogado(String string) {
		if (!this.promoventeAdvogado.contains(string))
			this.promoventeAdvogado.add(string);	
		}

	public String getPromoventeNome() {
		return promoventeNome;
	}

	public void addPromovidoAdvogado(String string) {
		if(!promovidoAdvogado.contains(string))
			this.promovidoAdvogado.add(string);
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


}
