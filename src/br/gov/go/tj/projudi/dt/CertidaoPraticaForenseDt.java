package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;

public class CertidaoPraticaForenseDt extends Dados {
	public static final int PRATICA_FORENSE_MODELO_CODIGO = 140;
	public static final int PRATICA_FORENSE_QUANTITATIVA_MODELO_CODIGO = 141;
	/**
	 * 
	 */
	private static final long serialVersionUID = -5113550476067337863L;
	
	public static final int CodigoPermissao = 447;

	private String nome;
	private String naturalidade;
	private String estadoCivil;
	private String sexo;
	private String identidade;
	private String cpf;
	private String oab;
	private String oabComplemento;
	private String oabUf;
	private String texto;
	private String domicilio;
	private String id_EstadoCivil;
	private String id_Naturalidade;
	private String anoInicial;
	private String anoFinal;
	private String mesInicial;
	private String mesFinal;
	private List listaProcesso;
	private int quantidade;
	private int modeloCodigo;
	private String oabUfCodigo;
	private String[] vetorMes = {"0", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" };
	private String tipo;
	
	public CertidaoPraticaForenseDt() {
		
		nome = "";
		naturalidade = "";
		estadoCivil = "";
		sexo = "";
		identidade = "";
		cpf = "";
		oab = "";
		oabComplemento = "";
		oabUf = "";
		oabUfCodigo = "";
		texto = "";
		domicilio = "";
		id_EstadoCivil = "";
		id_Naturalidade = "";
		mesFinal = "2";
		mesInicial = "1";
		Calendar cal = Calendar.getInstance();  
        int year = cal.get(Calendar.YEAR);  
		anoFinal = String.valueOf(year);
		anoInicial = String.valueOf(year);
		listaProcesso = new ArrayList(5);
		modeloCodigo = 0;
		quantidade = 0;
		tipo = "Quantitativa";
		
		
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		if(tipo != null)
		this.tipo = tipo;
	}



	public String getOabComplemento() {
		return oabComplemento;
	}

	public String getOabUfCodigo() {
		return oabUfCodigo;
	}



	public void setOabUfCodigo(String oabUfCodigo) {
		this.oabUfCodigo = oabUfCodigo;
	}



	public void setOabComplemento(String oabComplemento) {
		if (oabComplemento != null)
		this.oabComplemento = oabComplemento;
	}

	public String getOabUf() {
		return oabUf;
	}

	public void setOabUf(String oabUf) {
		if(oabUf != null)
		this.oabUf = oabUf;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public String getAnoInicial() {
		return anoInicial;
	}



	public void setAnoInicial(String anoInicial) {
		if (anoInicial != null)
		this.anoInicial = anoInicial;
	}



	public String getAnoFinal() {
		return anoFinal;
	}



	public void setAnoFinal(String anoFinal) {
		if (anoFinal != null)
		this.anoFinal = anoFinal;
	}



	public String getMesInicial() {
		return mesInicial;
	}



	public void setMesInicial(String mesInicial) {
		if(mesInicial != null)
		this.mesInicial = mesInicial;
	}



	public String getMesFinal() {
		return mesFinal;
	}



	public void setMesFinal(String mesFinal) {
		if(mesFinal != null)
		this.mesFinal = mesFinal;
	}


	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}



	public String getId_EstadoCivil() {
		return id_EstadoCivil;
	}

	public void setId_EstadoCivil(String idEstadoCivil) {
		if(idEstadoCivil != null)
		id_EstadoCivil = idEstadoCivil;
	}


	public String getId_Naturalidade() {
		return id_Naturalidade;
	}

	public void setId_Naturalidade(String idNaturalidade) {
		if(idNaturalidade != null)
		id_Naturalidade = idNaturalidade;
	}
	
	
	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		if(domicilio != null)
		this.domicilio = domicilio;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		if (texto != null)
		this.texto = texto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		if (nome != null)
		this.nome = nome;
	}

	public String getNaturalidade() {
		return naturalidade;
	}

	public void setNaturalidade(String naturalidade) {
		if(naturalidade != null)
		this.naturalidade = naturalidade;
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
		if(sexo != null)
		this.sexo = sexo;
	}

	public String getIdentidade() {
		return identidade;
	}

	public void setIdentidade(String identidade) {
		if(identidade != null)
		this.identidade = identidade;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		if(cpf != null)
		this.cpf = cpf;
	}

	public String getOabNumero() {
		return oab;
	}
	
	public String getOab() {
		return oab;
	}

	public void setOab(String oab) {
		if(oab != null)
		this.oab = oab;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {

	}

	public void setListaProcesso(List listaProcesso) {
		this.listaProcesso = listaProcesso;
	}
	
	
	
	
	public List getListaProcesso() {
		return this.listaProcesso;
	}

	public int getModeloCodigo() {
		return modeloCodigo;
	}



	@SuppressWarnings("deprecation")
	public String getDataTimeFinal() {
		Date dataFinal = Funcoes.getUltimoDiaMes(this.mesFinal, this.anoFinal);
		dataFinal.setHours(23);
		dataFinal.setMinutes(59);
		dataFinal.setSeconds(59);		
		return Funcoes.DataHora(dataFinal);		
		//return (Funcoes.BancoData(Funcoes.getUltimoDiaMes(this.mesFinal, this.anoFinal)) + " 23:59:59").replaceAll("'", "");
	}



	public String getDataTimeInicial() {
		Calendar calendar = Calendar.getInstance();
		//O mês no objeto Calendar inicia de 0, portando Jan = 0 e Dez = 11
		calendar.set(Funcoes.StringToInt(this.anoInicial), (Funcoes.StringToInt(this.mesInicial) - 1), 1,0,0,0);	
		return Funcoes.DataHora(calendar.getTime());
		//return (this.anoInicial+"-"+this.mesInicial+"-"+"01"+ " 00:00:00").replaceAll("'", "");
	}

	public void setModeloCodigo(int praticaForenseQuantitativaModeloCodigo) {
			this.modeloCodigo = praticaForenseQuantitativaModeloCodigo;
	}
	
	public String getMesTexto(String mesNumero) {
		int i = Funcoes.StringToInt(mesNumero);
		return this.vetorMes[i];
	}
	

}
