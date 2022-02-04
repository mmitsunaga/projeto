package br.gov.go.tj.projudi.dt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public abstract class ProcessoCertidaoDt extends Dados {

	private static final long serialVersionUID = -6161420087010555002L;

	private String Id_Processo;
	private String promovidoNome;
	private String promovidoSexo;
	private String promovidoCpf;
	private String promovidoCnpj;
	private String promovidoRg;
	private String promovidoNomeMae;
	private String promovidoNomePai;
	private String promovidoDataNascimento;
	protected String processoNumero;
	private String digitoVerificador;
	private String processoTipo;
	private boolean segredoJustica;
	private String dataRecebimento;
	private String serventia;
	protected List promoventeNome;
	private String ano;
	private String forumCodigo;
	protected List assunto;
	private String tipo;
	private String averbacao;
	private String desmembrado;
	private String conversao;
	private String comarca;
	private String processoTipoCodigo;
	
	private String certidaoPromoventeNome;
	private String nomeAdvogadoPromovente;
	private String nomeAdvogadoPromovido;

	public ProcessoCertidaoDt() {
		super();
		 
		Id_Processo = "";
		 promovidoNome= "";
		 promovidoSexo= "";
		 promovidoCpf= "";
		 promovidoCnpj= "";
		 promovidoRg= "";
		 promovidoNomeMae= "";
		 promovidoNomePai = "";
		 promovidoDataNascimento= "";
		 processoNumero= "";
		 digitoVerificador= "";
		 processoTipo= "";
		 segredoJustica= false;
		 dataRecebimento= "";
		 serventia= "";
		 promoventeNome= null;
		 ano= "";
		 forumCodigo= "";
		 assunto= null;
		 tipo = "";
		 averbacao = "";
		 desmembrado = "";
		 conversao = "";
		 comarca = "";
		 processoTipoCodigo = "";
		 certidaoPromoventeNome = "";
		 nomeAdvogadoPromovente = "";
		 nomeAdvogadoPromovido = "";
	}


	/**
	 * @param buscadaTipo the buscadaTipo to set
	 */
	public void setBuscadaProcessoParteTipo(int buscadaTipo) {
	
	}

	public void setPromovidoDataNascimento(String dataNascimento) {
		if(dataNascimento != null)
		this.promovidoDataNascimento = dataNascimento;
	}
	public String getPromovidoDataNascimento() {
		return this.promovidoDataNascimento;
	}
	
	/**
	 * Retorna a data de nascimento do promovido no formado "yyyyMMdd".
	 * @return data de nascimento
	 * @author hmgodinho
	 */
	public String getPromovidoDataNascimentoAnoMesDia(){
		if(this.getPromovidoDataNascimento() != null && !this.getPromovidoDataNascimento().trim().equals("")) {
			String[] data = this.getPromovidoDataNascimento().split("/");
			if(data.length == 3) {
				String dia = "", mes = "", ano = "";
				//se o dia tiver menos de 2 dígitos, completa com 0 no começo
				if(data[0].trim().length() < 2) {
					dia = "0" + data[0].trim();  
				} else {
					dia = data[0].trim();
				}
				//se o mês tiver menos de 2 dígitos, completa com 0 no começo
				if(data[1].trim().length() < 2) {
					mes = "0" + data[1].trim();  
				} else {
					mes = data[1].trim();
				}
				//se o ano tiver os 4 dígitos já retora o ano
				if(data[2].trim().length() == 4) {
					ano = data[2].trim();
				} else while(data[2].trim().length() < 4) {
					//se for menor que 4 dígitos, preenche o ano com 0 no começo quantas vezes for necessário
					data[2] = "0" + data[2].trim();
					ano = data[2].trim();  
				}
				return ano + mes + dia;
			}
		}
		return "";
	}

	/**
	 * Retorna o número completo de um processo, obedecendo a padronização do CNJ
	 */
	public String getProcessoNumeroCompleto() {
		if (getDigitoVerificador() == null || getDigitoVerificador().equals("")) {
			return this.getProcessoNumero();
		} else {
			return (Funcoes.completarZeros(getProcessoNumero(), 7) + "-"
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
	
	public boolean isPessoaJuridica() {
		return (this.promovidoCpf == null || this.promovidoCpf.equals("")) && this.promovidoCnpj != null;
	}
	
	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		if (ano != null)
		this.ano = ano;
	}

	public String getForumCodigo() {
		return forumCodigo;
	}

	public void setForumCodigo(String forumCodigo) {
		if (forumCodigo != null)
		this.forumCodigo = forumCodigo;
	}

	public String getPromovidoNome() {
		if (isSegredoJustica())
			return promovidoNome != null ? Funcoes.iniciaisNome(promovidoNome): null;
		else
			return promovidoNome;
	}

	public void setPromovidoNome(String ParteBuscadaNome) {
		this.promovidoNome = ParteBuscadaNome;
	}

	public String getPromovidoSexo() {
		return promovidoSexo;
	}

	public void setPromovidodaSexo(String ParteBuscadaSexo) {
		this.promovidoSexo = ParteBuscadaSexo;
	}

	public String getPromovidoCpf() {
		return promovidoCpf;
	}

	public void setPromovidoCpf(String ParteBuscadaCpf) {
		this.promovidoCpf = ParteBuscadaCpf;
	}

	public String getPromovidoCnpj() {
		return promovidoCnpj;
	}

	public void setPromovidoCnpj(String ParteBuscadaCnpj) {
		this.promovidoCnpj = ParteBuscadaCnpj;
	}
	
	public String getCpfCnpjFormatado() {
		String retorno = "";
		if (this.promovidoCnpj != null && this.promovidoCnpj.length() > 1) {
			 retorno = this.promovidoCnpj.contains("-") ? this.promovidoCnpj : Funcoes.formataCNPJ(this.promovidoCnpj) ;
		} else if (this.promovidoCpf != null && this.promovidoCpf.length() > 1) {
			retorno = this.promovidoCpf.contains("-") ? this.promovidoCpf : Funcoes.formataCPF(this.promovidoCpf) ;
		} else {
			retorno =  "-";
		}
				 
		return retorno;
	}

	public String getPromovidoRg() {
		return promovidoRg;
	}

	public void setPromovidoRg(String ParteBuscadaRg) {
		this.promovidoRg = ParteBuscadaRg;
	}

	public String getPromovidoNomeMae() {
		return promovidoNomeMae;
	}

	public void setPromovidoNomeMae(String ParteBuscadaNomeMae) {
		this.promovidoNomeMae = ParteBuscadaNomeMae;
	}
	
	public String getPromovidoNomePai() {
		return promovidoNomePai;
	}

	public void setPromovidoNomePai(String promovidoNomePai) {
		this.promovidoNomePai = promovidoNomePai;
	}

	public String getProcessoNumero() {
		
		return processoNumero;
	}

	public String getDigitoVerificador() {
		return digitoVerificador;
	}

	public void setDigitoVerificador(String digitoVerificador) {
		this.digitoVerificador = digitoVerificador;
	}

	public String getProcessoTipo() {
		return processoTipo;
	}

	public void setProcessoTipo(String processoTipo) {
		this.processoTipo = processoTipo;
	}

	public boolean isSegredoJustica() {
		return segredoJustica;
	}

	public void setSegredoJustica(boolean segredoJustica) {
		this.segredoJustica = segredoJustica;
	}

	public String getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(String dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}
	
	/**
	 * Método que retorna a data de recebimento do processo formatada para ser apresentada na certidão.
	 * @return string com a data
	 * @author hmgodinho
	 */
	public String getDataRecebimentoCertidao() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return sdf.format(dataRecebimento);
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		this.serventia = serventia;
	}
		
	public String getCertidaoPromoventeNome() {
		if (isSegredoJustica()) {
			return Funcoes.iniciaisNome(certidaoPromoventeNome);
		} else {
			return certidaoPromoventeNome;
		}
	}


	public void setCertidaoPromoventeNome(String certidaoPromoventeNome) {
		this.certidaoPromoventeNome = certidaoPromoventeNome;
	}


	public List getPromoventeNome() {
		if (!isSegredoJustica()) {
			List novaLista = new ArrayList(5);
			Iterator i = promoventeNome.iterator();
			while (i.hasNext()) {
				String nome = (String) i.next();
				if(nome != null)
				novaLista.add(Funcoes.iniciaisNome(nome));
			}
			return novaLista;
		} else {
			return promoventeNome;
		}
	}
	
	/**
	 * Método que retorna o nome do primeiro promovente localizado no processo.
	 * @return string 
	 * @author hmgodinho
	 */
	public String getPrimeiroPromoventeNome() {
		if(!promoventeNome.isEmpty()) {
			if(isSegredoJustica()){
				return Funcoes.iniciaisNome(String.valueOf(promoventeNome.get(0)));
			} else {
				return String.valueOf(promoventeNome.get(0));
			}
		} else {
			return "";
		}
	}
	
	public void setParteContrariaNome(List ParteContrariaNome) {
		this.promoventeNome = ParteContrariaNome;
	}

	public void addPromovente(String ParteContraria) {
		if (promoventeNome == null)
			promoventeNome = new ArrayList(5);
		if(!promoventeNome.contains(ParteContraria))
		this.promoventeNome.add(ParteContraria);
	}

	public String promoventeToString() {
		StringBuilder sb = new StringBuilder();
		Iterator it;
		if (isSegredoJustica()) {
			List novaLista = new ArrayList(5);
			Iterator i = promoventeNome.iterator();
			while (i.hasNext()) {
				String nome = (String) i.next();
				if(nome != null)
				novaLista.add(Funcoes.iniciaisNome(nome));
			}
			it = novaLista.iterator();
		} else {
			it = promoventeNome.iterator();
		}
		while (it.hasNext()) {
			String ParteContraria = (String) it.next();
			sb.append(ParteContraria + ", ");
		}
		if (promoventeNome.size() >= 1) {
		int tamanho = sb.length();
		sb.delete(tamanho - 2, tamanho - 1);
		}
		return sb.toString();
	}

	public String assuntoToString() {
		if(assunto.size() == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		Iterator it = assunto.iterator();
		while (it.hasNext()) {
			String advgd = (String) it.next();
			sb.append(advgd + ", ");
		}
		if (assunto.size() >= 1) {
		int tamanho = sb.length();
		sb.delete(tamanho - 2, tamanho - 1);
		}
		return sb.toString();
	}
	
	/**
	 * Método que retorna uma string com todos os assuntos do processo para ser apresetada na certidão
	 * @return string
	 * @author hmgodinho
	 */
	public String getAssuntoCertidao() {
		if(assunto.size() == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		Iterator it = assunto.iterator();
		while (it.hasNext()) {
			String ass = (String) it.next();
			if(ass != null){
				if(ass != null && ass.contains("->")){
					ass = ass.replace("->", ", ");
				}
				sb.append(ass + "; ");
			}
		}
		if (assunto.size() > 1) {
			int tamanho = sb.length();
			sb.delete(tamanho - 2, tamanho - 1);
		}
		return sb.toString();
	}

	public void addAssunto(String assunto) {
		if (!this.assunto.contains(assunto))
			this.assunto.add(assunto);
	}

	/**
	 * @return the assunto
	 */
	public List getAssunto() {
		return assunto;
	}

	/**
	 * @param assunto the assunto to set
	 */
	public void setAssunto(List assunto) {
		this.assunto = assunto;
	}

	public String getId_Processo() {
		return Id_Processo;
	}

	public void setId_Processo(String idProcesso) {
		if (idProcesso != null)
			Id_Processo = idProcesso;
	}
	
	public void setTipo(String tipo) {
		if (tipo != null) 
			this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setAverbacao(String averbacao) {
		if (averbacao != null) 
			this.averbacao = averbacao;
	}

	public String getAverbacao() {
		return averbacao;
	}
	
	public void setDesmembrado(String desmembrado) {
		if (desmembrado != null)
			this.desmembrado = desmembrado;
	}

	public String getDesmembrado() {
		return desmembrado;
	}

	public void setConversao(String conversao) {
		if (conversao != null)
			this.conversao = conversao;
	}

	public String getConversao() {
		return conversao;
	}

	public void setComarca(String comarca) {
		if (comarca != null)
			this.comarca = comarca;
	}

	public String getComarca() {
		return comarca;
	}

	public String getProcessoTipoCodigo() {
		return processoTipoCodigo;
	}

	public void setProcessoTipoCodigo(String processoTipoCodigo) {
		this.processoTipoCodigo = processoTipoCodigo;
	}

	public String getNomeAdvogadoPromovente() {
		return nomeAdvogadoPromovente;
	}

	public void setNomeAdvogadoPromovente(String nomeAdvogadoPromovente) {
		this.nomeAdvogadoPromovente = nomeAdvogadoPromovente;
	}

	public String getNomeAdvogadoPromovido() {
		return nomeAdvogadoPromovido;
	}

	public void setNomeAdvogadoPromovido(String nomeAdvogadoPromovido) {
		this.nomeAdvogadoPromovido = nomeAdvogadoPromovido;
	}
	
}