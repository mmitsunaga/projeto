package br.gov.go.tj.utils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 
 * Classe:     TJDataHora.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       02/2012
 * Finalidade: Encapsular um objeto Calendar. 
 *             
 */
public class TJDataHora implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6562750294822665980L;
	
	private Calendar data;	
	
	public TJDataHora() {
		//Construtor padrão, armazena a data atual do sistema, utilizando a zona GMT Brasil
		this.data = Calendar.getInstance();
		this.data.setTime(new Date());
		this.atualizeZonaBrasil();
	}
	
	public TJDataHora(Calendar data){		
		this.data = data;
		this.atualizeZonaBrasil();
	}
	
	public TJDataHora(long milisegundos){
		this();
		this.data.setTimeInMillis(milisegundos);		
	}
	
	public TJDataHora(Date data) throws Exception {
		this();
		this.data.setTime(data);		
	}
	
	public TJDataHora(String dataDDMMAAAA) throws MensagemException {
		this();
		Date data = null;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
	    try{
	    	data = format.parse(dataDDMMAAAA);
		} catch(ParseException e) {
			throw new MensagemException("Impossível converter a data " + dataDDMMAAAA + " para um Date (dd/MM/yyyy).");
		}
		this.data.setTime(data);	
	}
	
	public TJDataHora(String dataDDMMAAAA, String horaHHMMSS) throws MensagemException {
		this();
		Date data = null;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");		
	    try{
	    	data = format.parse(dataDDMMAAAA + " " + horaHHMMSS);
		} catch(ParseException e) {
			throw new MensagemException("Impossível converter a data " + dataDDMMAAAA + " " + horaHHMMSS + " para um Date (dd/MM/yyyy HH:mm:ss).");
		}
		this.data.setTime(data);	
	}
	
	public void atualizeZonaBrasil(){
		this.atualizeZona("America/Sao_Paulo");
	}
	
	public void atualizeZona(String zonaGMT){
		this.atualizeZona(TimeZone.getTimeZone(zonaGMT));
	}
	
	public void atualizeZona(TimeZone zonaGMT){
		this.data.setTimeZone(zonaGMT);
	}
	
	public Calendar getCalendar(){
		return this.data;
	}
	
	public Date getDate(){
		return this.data.getTime();
	}
	
	public void setData(Calendar data){
		this.data = data;
		this.atualizeZonaBrasil();
	}
	
	public void setData(Date data){
		this.data.setTime(data);
		this.atualizeZonaBrasil();
	}
	
	public void setDataddMMaaaa(String data) throws Exception{
		this.data.setTime(Funcoes.StringToDate(data));
		this.atualizeZonaBrasil();
	}
	
	public void setDataddMMaaaaSemSeparador(String data) throws Exception{
		this.data.setTime(Funcoes.StringDDMMAAAAToDate(data));
		this.atualizeZonaBrasil();
	}
	
	public void setDataddMMaaaaHHmmssSemSeparador(String dataHora) throws Exception{
		this.data.setTime(Funcoes.StringDDMMAAAAHHmmssToDate(dataHora));
		this.atualizeZonaBrasil();
	}
	
	public void setDataddMMaaaaHHmmss(String data) throws Exception{
		this.data.setTime(Funcoes.StringToDateTime(data));
		this.atualizeZonaBrasil();
	}
	
	public void setDataaaaa_MM_ddHHmmss(String data_aaaa_MM_dd) throws Exception{
		this.data.setTime(Funcoes.Stringyyyy_MM_ddToDateTime(data_aaaa_MM_dd));
		this.atualizeZonaBrasil();
	}
	
	public void setDataaaaaMMdd(String data) throws Exception{
		this.data.setTime(Funcoes.StringAAAAMMDDToDate(data));
		this.atualizeZonaBrasil();
	}
	
	public void setData(int ano, int mes, int dia){
		this.setData(ano, mes, dia, true);
	}
	
	public void setData(int ano, int mes, int dia, boolean ehPrimeiraHora){
		this.data = Funcoes.obtenhaCalendar(ano, mes, dia);
		this.atualizeZonaBrasil();
		if (ehPrimeiraHora){
			this.atualizePrimeiraHoraDia();
		}
		else{
			this.atualizeUltimaHoraDia();
		}				
	}
	
	public void setData(int ano, int mes, int dia, int hora, int minuto, int segundo){
		this.data = Funcoes.obtenhaCalendar(ano, mes, dia,hora, minuto, segundo);
		this.atualizeZonaBrasil();
	}
	
	public void setDataUnixTimeStamp(long unixTimeStamp)
	{
		this.data.setTimeInMillis(unixTimeStamp * 1000);
	}
	
	public void atualizePrimeiraHoraDia(){
		this.data = Funcoes.calculePrimeraHora(this.data);
	}
	
	public void atualizeUltimaHoraDia(){
		this.data = Funcoes.calculeUltimaHora(this.data);
	}
	
	public String getDataFormatadaMMyyyy(){
		SimpleDateFormat df = new SimpleDateFormat("MM/yyyy");
		return df.format(this.data.getTime());
	}
	
	public String getDataFormatadaddMMyyyy(){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.format(this.data.getTime());
	}
	
	public String getDataFormatadaddMMyyyySemSeparador(){
		SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
		return df.format(this.data.getTime());
	}
	
	public String getDataFormatadaddMMyyyyHHmmss(){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		return df.format(this.data.getTime());
	}
	
	public String getDataFormatadaddMMyyyyHHmmssSemSeparador(){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return df.format(this.data.getTime());
	}
	
	public String getDataFormatadaEEEEEddMMMMMyyyy() {
		SimpleDateFormat df = new SimpleDateFormat("EEEEE, dd 'de' MMMMM 'de' yyyy");
		return df.format(this.data.getTime());		
	}
	
	public long getDataFormatadaUnixTimeStamp(){		
		return this.data.getTime().getTime() / 1000;		
	}
	
	public String getHoraFormatadaHHmm(){
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		return df.format(this.data.getTime());
	}
	
	public void setHora(int hora){
		this.data.set(Calendar.HOUR_OF_DAY, hora);
	}
	
	public void setMinuto(int minuto){
		this.data.set(Calendar.MINUTE, minuto);
	}
	
	public int getSegundo(){
		return (this.data.get(Calendar.SECOND));
	}
	
	public void setSegundo(int segundo){
		this.data.set(Calendar.SECOND, segundo);
	}
	
	public String getDataHoraFormatadayyyyMMddHHmmss(){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(this.data.getTime());
	}
	
	public String getDataHoraFormatadayyyyMMddHHmm(){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		return df.format(this.data.getTime());
	}
	
	public void setDataHoraFormatadayyyyMMddHHmmss(String aaaaMMddHHmmss) throws Exception {
		this.data.setTime(Funcoes.StringAAAAMMDDHHmmssToDate(aaaaMMddHHmmss));
		this.atualizeZonaBrasil();
	}
	
	public String getDataHoraFormatadaaaaa_MM_ddHHmmss(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(this.data.getTime());
	}
	
	public String getDataHoraFormatadayyyyMMdd(){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(this.data.getTime());
	}
	
	public int getDataHorayyyyMMdd(){
		String datayyyyMMdd = this.getDataHoraFormatadayyyyMMdd();
		return Funcoes.StringToInt(datayyyyMMdd);
	}
	
	public void adicioneAno(int quantidade){
		this.data.add(Calendar.YEAR, quantidade);
	}
	
	public void adicioneMes(int quantidade){
		this.data.add(Calendar.MONTH, quantidade);
	}
	
	public void adicioneDia(int quantidade){
		this.data.add(Calendar.DAY_OF_MONTH, quantidade);
	}
	
	public void adicioneHora(int quantidade){
		this.data.add(Calendar.HOUR, quantidade);
	}
	
	public void adicioneMilisegundos(int quantidade){
		this.data.add(Calendar.MILLISECOND, quantidade);
	}
	
	public boolean ehApos(TJDataHora dataComparacao){
		return this.ehApos(dataComparacao.getCalendar());
	}
	
	public boolean ehApos(Calendar dataComparacao){
		return this.data.after(dataComparacao);		
	}	
	
	public void atualizeDataEmMilisegundos(long tempoMilisegundos){
		this.data.setTimeInMillis(tempoMilisegundos);
	}	
	
	public int getDia(){
		return this.data.get(Calendar.DAY_OF_MONTH);
	}
	
	public void setDia(int dia){
		this.data.set(Calendar.DAY_OF_MONTH, dia);				         
	}
	
	//O mês do Calendar inicia de 0
	public int getMes(){
		return (this.data.get(Calendar.MONTH) + 1);
	}
	
	//O mês do Calendar inicia de 0
	public void setMes(int mes){
		this.data.set(Calendar.MONTH, (mes - 1));
	}
	
	public int getAno(){
		return this.data.get(Calendar.YEAR);
	}
	
	public void setAno(int ano){
		this.data.set(Calendar.YEAR, ano);
	}
	
	public static TJDataHora CloneObjeto(TJDataHora objOrigem){
		TJDataHora objRetorno = new TJDataHora();
		objRetorno.atualizeDataEmMilisegundos(objOrigem.getCalendar().getTimeInMillis());
		return objRetorno;
	}
	
	public int getHora(){
		return (this.data.get(Calendar.HOUR_OF_DAY));
	}
	
	public int getMinuto() throws Exception {
		return (this.data.get(Calendar.MINUTE));
	}
	
	@Override
	public boolean equals(java.lang.Object objOrigem){		
		try{
			TJDataHora objTemp = (TJDataHora)objOrigem;
			return this.equals(objTemp);
		}
		catch(Exception e){
			return false;
		}
	}
	
	public boolean equals(TJDataHora objOrigem){		
		return (this.getDate().equals(objOrigem.getDate()));
	}
	
	public boolean equals(Calendar objOrigem){		
		return (this.getDate().equals(objOrigem.getTime()));
	}	
	
	public boolean equals(Date objOrigem){		
		return (this.getDate().equals(objOrigem));
	}
	
	public boolean isSabado()
	{
		return this.data.get(Calendar.DAY_OF_WEEK) == 7;
	}
	
	public boolean isDomingo()
	{
		return this.data.get(Calendar.DAY_OF_WEEK) == 1;
	}
	
	public boolean isHoje()
	{
		return this.getDataHoraFormatadayyyyMMdd().equals(new TJDataHora().getDataHoraFormatadayyyyMMdd());
	}
	
	public String getDataFormatadaddMMyyyyHHmm(){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
		return df.format(this.data.getTime());
	}
	
	public String getDataFormatadaDDMMyyyyHHmmss(){
		SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
		return df.format(this.data.getTime());
	}
	
	public String getDataFormatadaHHmm(){
		SimpleDateFormat df = new SimpleDateFormat("HHmm");
		return df.format(this.data.getTime());
	}
	
	@Override
	public String toString(){
		return this.getDataFormatadaddMMyyyyHHmmss();		
	}
}
