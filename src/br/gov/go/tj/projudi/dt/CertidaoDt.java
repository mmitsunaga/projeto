package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;


public class CertidaoDt extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8466107772291837362L;

	public static final int CodigoPermissao = 447;
	
	public static final boolean isGratuitaEmissaoPrimeiroGrau = true;	
	
	private String requerente;
	private String cpfCnpj;
	private String id_Domicilio;
	private String domicilio;
	private String rg;
	private String id_Naturalidade;
	private String naturalidade;
	private String id_EstadoCivil;
	private String estadoCivil;
	private String sexo;
	private String id_Profissao;
	private String profissao;
	private String nomeMae;
	private String nomePai;
	private String dataNascimento;
	private List listaProcesso;
	private String comarcaCodigo;
	private String certidaoTipo;
	private String processoNumeroCompleto;
	private String numeroGuia;
	private String id_Modelo;
	private String modelo;
	
	public CertidaoDt() {
		limpar();
		listaProcesso = new ArrayList();		
	}
	
	public CertidaoDt(String requerente, 
			          String cpf, 
			          String idDomicilio, 
			          String domicilio, 
			          String rg, 
			          String idNaturalidade, 
			          String naturalidade, 
			          String idEstadoCivil, 
			          String estadoCivil, 
			          String sexo, 
			          String idProfissao, 
			          String profissao, 
			          String nomeMae, 
			          List listaProcesso, 
			          String comarcaCodigo, 
			          String certidaoTipo, 
			          String nomePai, 
			          String dataNascimento,
			          String numeroGuia,
			          String id_Modelo,
			          String modelo) {
		super();
		this.requerente = requerente;
		this.cpfCnpj = cpf;
		id_Domicilio = idDomicilio;
		this.domicilio = domicilio;
		this.rg = rg;
		id_Naturalidade = idNaturalidade;
		this.naturalidade = naturalidade;
		id_EstadoCivil = idEstadoCivil;
		this.estadoCivil = estadoCivil;
		this.sexo = sexo;
		id_Profissao = idProfissao;
		this.profissao = profissao;
		this.nomeMae = nomeMae;
		this.nomePai = nomePai;
		this.listaProcesso = listaProcesso;
		this.comarcaCodigo = comarcaCodigo;
		this.certidaoTipo = certidaoTipo;
		this.listaProcesso = new ArrayList();
		this.dataNascimento = dataNascimento;
		this.numeroGuia = numeroGuia;
		this.id_Modelo = id_Modelo;
		this.modelo = modelo;
	}


	public void limpar() {
		this.requerente = "";
		this.cpfCnpj = "";
		this.domicilio = "";
		this.rg = "";
		this.naturalidade = "";
		this.estadoCivil = "";
		this.sexo = "";
		this.profissao = "";
		this.id_EstadoCivil = "";
		this.id_Naturalidade = "";
		this.id_Domicilio = "";
		this.id_Profissao = "";
		this.comarcaCodigo = "";
		this.dataNascimento = "";
		this.nomeMae = "";
		this.nomePai = "";
		this.listaProcesso = new ArrayList();
		this.processoNumeroCompleto = "";
		this.numeroGuia = "";
		this.id_Modelo = "";
		this.modelo = "";
		
	}

	public void setListaProcesso(List listaProcesso) {
		this.listaProcesso = listaProcesso;
	}

	public String getId_Profissao() {
		return id_Profissao;
	}
	
	public String getCertidaoTipo() {
		return certidaoTipo;
	}

	public void setId_Profissao(String idProfissao) {
		if (idProfissao != null)
		id_Profissao = idProfissao;
	}

	public void addProcesso(Object proc) {
		getListaProcesso().add(proc);
	}

	public boolean removeProcesso(Object proc) {
		return getListaProcesso().remove(proc);
	}
	public void removeProcesso(int pos) {
		 getListaProcesso().remove(pos);
	}
	public String getId_Domicilio() {
		return id_Domicilio;
	}
	
	public String getNomeMae() {
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		if (nomeMae != null) this.nomeMae = nomeMae;
	}

	public String getNomePai() {
		return nomePai;
	}

	public void setNomePai(String nomePai) {
		if (nomePai != null) this.nomePai = nomePai;
	}

	public void setId_Domicilio(String idDomicilio) {
		if (idDomicilio != null)
		id_Domicilio = idDomicilio;
	}

	public String getId_Naturalidade() {
		return id_Naturalidade;
	}

	public void setId_Naturalidade(String idNaturalidade) {
		if (idNaturalidade != null)
		id_Naturalidade = idNaturalidade;
	}

	public String getId_EstadoCivil() {
		return id_EstadoCivil;
	}

	public void setId_EstadoCivil(String idEstadoCivil) {
		if (idEstadoCivil != null)
		id_EstadoCivil = idEstadoCivil;
	}
	
	public List getListaProcesso() {
		return listaProcesso;
	}

	public String getRequerente() {
		return requerente;
	}

	public void setRequerente(String nome) {
		if (nome != null)
			this.requerente = nome;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpf) {
		if (cpf != null)
			this.cpfCnpj = cpf;
	}


	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		if (domicilio != null)
			this.domicilio = domicilio;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		if (rg != null)
			this.rg = rg;
	}
	
	public String getNaturalidade() {
		return naturalidade;
	}

	public void setNaturalidade(String nacionalidade) {
		if (nacionalidade != null)
		this.naturalidade = nacionalidade;
	}

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		if (estadoCivil != null)
		this.estadoCivil = estadoCivil;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		if (sexo != null)
		this.sexo = sexo;
	}
	
	public String getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		if( profissao != null)
		this.profissao = profissao;
	}
	

	public String getComarcaCodigo() {
		return comarcaCodigo;
	}

	public void setComarcaCodigo(String comarcaCodigo) {
		if (comarcaCodigo != null)
		this.comarcaCodigo = comarcaCodigo;
	}

	public void setCertidaoTipo(String certidaoTipo) {
		this.certidaoTipo = certidaoTipo;
	}
	
	public boolean isNegativaPositiva() {
		int tipo = Funcoes.StringToInt(this.getCertidaoTipo());
		return tipo == ArquivoTipoDt.CERTIDAO_NEGATIVA_POSITIVA_CIVEL || tipo == ArquivoTipoDt.CERTIDAO_NEGATIVA_POSITIVA_CRIMINAL;
	}
	public boolean isFolhaCorrida() {
		int tipo = Funcoes.StringToInt(this.getCertidaoTipo());
		return tipo == ArquivoTipoDt.CERTIDAO_ANTECEDENTE_CRIMINAL; 
	}
	@Override
	public String getId() {
		
		return null;
	}

	@Override
	public void setId(String id) {
		

	}
	
	public String getDataNascimento() {
		return this.dataNascimento;
	}

	public void setDataNascimento(String data) {
		if (data != null) this.dataNascimento = data;
	}

	public String getProcessoNumeroCompleto() {
		return processoNumeroCompleto;
	}

	public void setProcessoNumeroCompleto(String processoNumeroCompleto) {
		if (processoNumeroCompleto != null) this.processoNumeroCompleto = processoNumeroCompleto;
	}

	public String getArea() {
		
		return String.valueOf(this.getCertidaoTipo().equals(String.valueOf(ArquivoTipoDt.CERTIDAO_NEGATIVA_POSITIVA_CRIMINAL)) ? AreaDt.CRIMINAL : AreaDt.CIVEL);
	}
	
	public String getNumeroGuia() {
		return numeroGuia;
	}

	public void setNumeroGuia(String numeroGuia) {
		if( numeroGuia != null)
			this.numeroGuia = numeroGuia;
	}
	
	public String getId_Modelo() {
		return id_Modelo;
	}

	public void setId_Modelo(String id_Modelo) {
		if( id_Modelo != null)
			this.id_Modelo = id_Modelo;
	}
	
	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		if( modelo != null)
			this.modelo = modelo;
	}
}

