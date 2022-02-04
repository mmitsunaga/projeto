package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class NumeroProcessoDt extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1651871941186749122L;
	
	@Override
	public void setId(String id) {
	}

	@Override
	public String getId() {
		
		return null;
	}
	
	private long numero; 
	public long getNumero() {
		return numero;
	}

	public void setNumero(long numero) {
		this.numero = numero;
	}

	private long digito;
	public long getDigito() {
		return digito;
	}

	public void setDigito(long digito) {
		this.digito = digito;
	}

	private long ano; 
	public long getAno() {
		return ano;
	}

	public void setAno(long ano) {
		this.ano = ano;
	}

	private long jtrParte1;
	public long getJtrParte1() {
		return jtrParte1;
	}

	public void setJtrParte1(long jtrParte1) {
		this.jtrParte1 = jtrParte1;
	}

	private long jtrParte2;
	public long getJtrParte2() {
		return jtrParte2;
	}

	public void setJtrParte2(long jtrParte2) {
		this.jtrParte2 = jtrParte2;
	}

	private long forum;
	
	public long getForum() {
		return forum;
	}

	public void setForum(long forum) {
		this.forum = forum;
	}

	public NumeroProcessoDt() {}
	
	public NumeroProcessoDt(String numeroCompletoDoProcesso) throws MensagemException
	{
		this.setNumeroCompletoProcesso(numeroCompletoDoProcesso);
	}
	
	public NumeroProcessoDt(String numeroDigito, String ano, String forum) throws MensagemException
	{
		String numeroProcessoStrTemp = Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(numeroDigito), 9); 
		this.numero = Funcoes.StringToLong(numeroProcessoStrTemp.substring(0, 7) ); 
		this.digito  = Funcoes.StringToLong(numeroProcessoStrTemp.substring(7, 9));	
		this.ano = Funcoes.StringToLong(ano); 
		this.jtrParte1 = 8; 
		this.jtrParte2 = 9;
		this.forum = Funcoes.StringToLong(forum);		
	}
	
	public boolean isValido()
	{
		return Funcoes.validaProcessoNumero(getNumeroCompletoProcesso());		
	}
	
	/***
	 * 5000280.28.2010.8.09.0059
	 * @return
	 */
	public String getNumeroCompletoProcesso()
	{
		//NumberFormat format = new DecimalFormat("000");
		if (this.numero > 0 && this.ano > 0 && this.jtrParte1 > 0 && this.jtrParte2 > 0)
			return String.format("%07d.%02d.%04d.%s.%02d.%04d", numero, digito, ano, jtrParte1, jtrParte2, forum); //"5000280.28.2010.8.09.0059";
		return "";
	}
	
	/**
	 * 5000280-28.2010.8.09.0059
	 * @return
	 */
	public String getNumeroCompletoProcessoNovaFormatacao()
	{
		//NumberFormat format = new DecimalFormat("000");
		if (this.numero > 0 && this.ano > 0 && this.jtrParte1 > 0 && this.jtrParte2 > 0)
			return String.format("%07d-%02d.%04d.%s.%02d.%04d", numero, digito, ano, jtrParte1, jtrParte2, forum); //"5000280-28.2010.8.09.0059";
		return "";
	}
	
	public void setNumeroCompletoProcesso(String numeroCompletoDoProcesso) throws MensagemException
	{
		if (!Funcoes.validaProcessoNumero(numeroCompletoDoProcesso))
			throw new MensagemException("Número do processo inválido.");
		
		this.setNumeroCompletoProcessoSemValidacao(numeroCompletoDoProcesso);
	}
	
	public void setNumeroCompletoProcessoSemValidacao(String numeroCompletoProcesso)
	{
		//Extrai do número do processo as informações necessárias para validação...
		numero = Funcoes.StringToLong(numeroCompletoProcesso.substring(0,7) ); 
		digito  = Funcoes.StringToLong(numeroCompletoProcesso.substring(8, 10));
		ano = Funcoes.StringToLong(numeroCompletoProcesso.substring(11, 15)); 
		jtrParte1 = Funcoes.StringToLong((numeroCompletoProcesso.substring(16, 17)).replace(".", "")); 
		jtrParte2 = Funcoes.StringToLong((numeroCompletoProcesso.substring(18, 20)).replace(".", ""));
		forum = Funcoes.StringToLong(numeroCompletoProcesso.substring(21, 25));		
	}
}
