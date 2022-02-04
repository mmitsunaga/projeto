package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.gov.go.tj.utils.ValidacaoUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author mmitsunaga
 *
 */
public class ComunicacaoDJEDt implements Serializable {
	
	private static final long serialVersionUID = -1381797944399846498L;
	
	/**
	 * Código da classe, conforme o SGT
	 */
	@JsonProperty("codigo_classe")
	private String codigoClasse = "";
	
	/**
	 * Número do processo, conforme a numeração única do CNJ. Somente números.
	 */
	@JsonProperty("numero_processo")
	private String numeroProcesso = "";
	
	@JsonProperty("sigla_tribunal")
	private String siglaTribunal = "TJGO";
	
	/**
	 * "E" para Edital e "D" para Diário Eletrônico
	 */
	@JsonProperty
	private String meio = "";
	
	/**
	 * Link para o inteiro teor da comunicação
	 */
	@JsonProperty
	private String link = "";
	
	/**
	 * Teor da comunicação
	 */
	@JsonProperty
	private String texto = "";
	
	@JsonProperty("tipo_documento")
	private String tipoDocumento = "";
	
	/**
	 * Órgão gerador da comunicação
	 */
	@JsonProperty
	private String orgao = "";
	
	/**
	 * Data em que a comunicação será disponibilizada, no formato aaaa-mm-dd. 
	 * Deverá ser maior que a data de hoje. Não será aceita comunicação disponibilizada em finais de semana.
	 */
	@JsonProperty("data_disponibilizacao")
	private String dataDisponibilizacao = "";
	
	/**
	 * "C" para Citação, "E" para Edital e "I" para Intimação
	 */
	@JsonProperty("tipo_comunicacao")
	private String tipoComunicao = "C";
	
	@JsonProperty("destinatarios")
	private List<Destinatario> destinatarios;
	
	@JsonProperty("advogados")
	private List<Advogado> advogados;
	
	@JsonIgnore	
	private String arqId;
	
	@JsonIgnore
	private String moviId;
	
	public class Destinatario {
		
		@JsonProperty
		private String nome = "";
		
		@JsonProperty("cpf_cnpj")
		private String cpfCnpj = "";
		
		/**
		 * "A" para polo ativo e "P" para polo passivo
		 */
		@JsonProperty
		private String polo = "";
		
		public Destinatario(String nome, String cpfCnpj, String polo) {
			this.nome = ValidacaoUtil.isNaoVazio(nome) ? nome.toUpperCase() : "";
			this.cpfCnpj = cpfCnpj;
			this.polo = polo;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = ValidacaoUtil.isNaoVazio(nome) ? nome.toUpperCase() : "";
		}

		public String getCpfCnpj() {
			return cpfCnpj;
		}

		public void setCpfCnpj(String cpfCnpj) {
			this.cpfCnpj = cpfCnpj;
		}

		public String getPolo() {
			return polo;
		}

		public void setPolo(String polo) {
			this.polo = polo;
		}
		
	}
	
	public class Advogado {
		
		@JsonProperty("nome")
		private String nome = "";
		
		@JsonProperty("numero_oab")
		private String oab = "";
		
		@JsonProperty("uf_oab")
		private String ufOab = "";
		
		public Advogado(String nome, String oab, String uf) {
			this.nome = ValidacaoUtil.isNaoVazio(nome) ? nome.toUpperCase() : "";
			this.oab = oab;
			this.ufOab = uf;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = ValidacaoUtil.isNaoVazio(nome) ? nome.toUpperCase() : "";
		}

		public String getOab() {
			return oab;
		}

		public void setOab(String oab) {
			this.oab = oab;
		}

		public String getUfOab() {
			return ufOab;
		}

		public void setUfOab(String ufOab) {
			this.ufOab = ufOab;
		}
		
	}
	
	public String getCodigoClasse() {
		return codigoClasse;
	}

	public void setCodigoClasse(String codigoClasse) {
		this.codigoClasse = codigoClasse;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getSiglaTribunal() {
		return siglaTribunal;
	}

	public void setSiglaTribunal(String siglaTribunal) {
		this.siglaTribunal = siglaTribunal;
	}

	public String getMeio() {
		return meio;
	}

	public void setMeio(String meio) {
		this.meio = meio;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = ValidacaoUtil.isNaoVazio(tipoDocumento) ? tipoDocumento : "";
	}

	public String getOrgao() {
		return orgao;
	}

	public void setOrgao(String orgao) {
		this.orgao = orgao;
	}

	public String getDataDisponibilizacao() {
		return dataDisponibilizacao;
	}

	public void setDataDisponibilizacao(String dataDisponibilizacao) {
		this.dataDisponibilizacao = dataDisponibilizacao;
	}

	public String getTipoComunicao() {
		return tipoComunicao;
	}

	public void setTipoComunicao(String tipoComunicao) {
		this.tipoComunicao = tipoComunicao;
	}
	
	public String getArqId() {
		return arqId;
	}
	
	public void setArqId(String arqId) {
		this.arqId = arqId;
	}
	
	public String getMoviId() {
		return moviId;
	}

	public void setMoviId(String moviId) {
		this.moviId = moviId;
	}

	public List<Destinatario> getDestinatarios() {
		return destinatarios;
	}

	public List<Advogado> getAdvogados() {
		return advogados;
	}
	
	public void addDestinatario(String nome, String cpfCnpj, String polo){
		if (ValidacaoUtil.isNulo(destinatarios)) destinatarios = new ArrayList<>();
		Optional<Destinatario> destinatario = destinatarios.stream().filter( d -> d.getNome().equals(nome.trim().toUpperCase())).findFirst();
		if (!destinatario.isPresent()) destinatarios.add(new Destinatario(nome, cpfCnpj, polo));
	}
	
	public void addAdvogado(String nome, String oab, String uf){
		if (ValidacaoUtil.isNulo(advogados)) advogados = new ArrayList<>();
		Optional<Advogado> advogado = advogados.stream().filter( a -> a.getNome().equals(nome.trim().toUpperCase())).findFirst();		
		if (!advogado.isPresent()) advogados.add(new Advogado(nome, oab, uf));
	}
	
}