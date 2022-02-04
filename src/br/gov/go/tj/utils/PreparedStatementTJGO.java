package br.gov.go.tj.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PreparedStatementTJGO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -839994215119159750L;

	private List listaCampos = null;
	
	private String valoresLog = "";
	
	public PreparedStatementTJGO() throws Exception {
		listaCampos = new ArrayList();
	}
	
	public void limpar(){
		listaCampos.clear();
	}
	
	public void adicionarString(String valor) throws Exception{
		if (stringIsNula(valor, false)){
			listaCampos.add(new CampoTJGO(valor, Types.VARCHAR, true));
		}else{
			//se estiver usando palavras reservadas será lancando um erro.
			Funcoes.validarString(valor);
			valor = Funcoes.removeEspacosExcesso(valor);
			valor = Funcoes.removerCaracteresControleEspeciais(valor);
			
			listaCampos.add(new CampoTJGO(valor, Types.VARCHAR, false));
		}
	}
	
	public void adicionarStringNull(){
		listaCampos.add(new CampoTJGO("", Types.VARCHAR, true));		
	}
	
	public void adicionarClob(String valor){
		if (stringIsNula(valor, false)) listaCampos.add(new CampoTJGO(valor, Types.CLOB, true));
		else listaCampos.add(new CampoTJGO(valor, Types.CLOB, false));
	}
	
	public void adicionarClobNull(){
		listaCampos.add(new CampoTJGO("", Types.CLOB, true));		
	}

	public void adicionarByte(byte[] valor) {
		listaCampos.add(new CampoTJGO(valor, Types.BLOB, (valor == null || valor.length == 0)));
	}
	
	public void adicionarByteNull() {
		listaCampos.add(new CampoTJGO("", Types.BLOB, true));
	}	
	
	public void adicionarLong(long valor) throws Exception {
		listaCampos.add(new CampoTJGO(valor, Types.BIGINT, false));
	}
	
	public void adicionarLong(String valor) throws Exception{
		if (stringIsNula(valor, true)) listaCampos.add(new CampoTJGO("", Types.BIGINT, true));
		else {
			listaCampos.add(new CampoTJGO(Funcoes.StringToLong(valor.trim(),0), Types.BIGINT, false));				
		}
	}
	
	public void adicionarLongNull(){
		listaCampos.add(new CampoTJGO("", Types.BIGINT, true));
	}
	
	public void adicionarDate(String valor) throws Exception{
		if (stringIsNula(valor, true)) listaCampos.add(new CampoTJGO(valor, Types.DATE, true));
		else{
			listaCampos.add(new CampoTJGO(Funcoes.StringToDate(valor.trim()), Types.DATE, false));
			
		}
	}
	
	public void adicionarDate(Date valor){
		listaCampos.add(new CampoTJGO(valor, Types.DATE, (valor == null)));		
	}
	
	public void adicionarDate(TJDataHora valor){
		if (valor == null) adicionarDateNull();
		else adicionarDate(valor.getDate());
	}	
	
	public void adicionarDateNull() {
		listaCampos.add(new CampoTJGO("", Types.DATE, true));
	}
	
	public void adicionarDateTime(String valor) throws Exception, MensagemException{
		if (stringIsNula(valor, true)) listaCampos.add(new CampoTJGO(valor, Types.TIMESTAMP, true));
		else{
				if (valor.contains("-")) {
					listaCampos.add(new CampoTJGO(Funcoes.Stringyyyy_MM_ddToDateTime(valor.trim()), Types.TIMESTAMP, false));	
				} else {
					listaCampos.add(new CampoTJGO(Funcoes.StringToDateTime(valor.trim()), Types.TIMESTAMP, false));	
				}				
			
		}
	}	
	
	public void adicionarDateTime(Date valor) throws Exception {
		listaCampos.add(new CampoTJGO(valor, Types.TIMESTAMP, (valor == null)));		
	}
	
	public void adicionarDateTimeNull() throws Exception {
		listaCampos.add(new CampoTJGO("", Types.TIMESTAMP, true));		
	}
	
	public void adicionarDateTimePrimeiraHoraDia(String valor) throws Exception{
		if (stringIsNula(valor, true)) listaCampos.add(new CampoTJGO(valor, Types.TIMESTAMP, true));
		else{
			Date ValorDate = null;
			
			if (valor.length() <= 10) ValorDate = Funcoes.StringToDate(valor.trim());
			else ValorDate = Funcoes.StringToDateTime(valor.trim());
			
			TJDataHora DataHora = new TJDataHora(ValorDate);
			DataHora.atualizePrimeiraHoraDia();
			
			listaCampos.add(new CampoTJGO(DataHora.getDate(), Types.TIMESTAMP, false));
		}
	}
	
	public void adicionarDateTimeUltimaHoraDia(String valor) throws Exception{
		if (stringIsNula(valor, true)) listaCampos.add(new CampoTJGO(valor, Types.TIMESTAMP, true));
		else{
			Date ValorDate = null;
			
			if (valor.length() <= 10) ValorDate = Funcoes.StringToDate(valor.trim());
			else ValorDate = Funcoes.StringToDateTime(valor.trim());
			
			TJDataHora DataHora = new TJDataHora(ValorDate);
			DataHora.atualizeUltimaHoraDia();
			
			listaCampos.add(new CampoTJGO(DataHora.getDate(), Types.TIMESTAMP, false));
		}
	}
	
	public void adicionarDateTime(TJDataHora valor) throws Exception {
		if (valor == null) adicionarDateTimeNull();
		else adicionarDateTime(valor.getDate());
	}
	
	public void adicionarBoolean(String valor){
		if (stringIsNula(valor, true)) listaCampos.add(new CampoTJGO("", Types.BOOLEAN, true));
		else listaCampos.add(new CampoTJGO(Funcoes.BancoLogico(valor.trim()).equalsIgnoreCase("1"), Types.BOOLEAN, false));		
	}
	
	public void adicionarBoolean(boolean valor){		
		listaCampos.add(new CampoTJGO(valor, Types.BOOLEAN, false));
	}
	
	public void adicionarBooleanNull(){		
		listaCampos.add(new CampoTJGO("", Types.BOOLEAN, true));
	}
	
	public void adicionarDecimal(String valor) throws Exception{
		
		if (stringIsNula(valor, true)) 
			//Este método será extinto, estou utilizando aqui o type Real como alias apenas para manter o funcionamento atual, posteriormente iremos utilizar apenas o bigdecimal
			listaCampos.add(new CampoTJGO("", Types.REAL, true));			
		else{
			//Number num = NumberFormat.getCurrencyInstance().parse(valor.trim());
			listaCampos.add(new CampoTJGO(Funcoes.BancoDecimal(valor) , Types.REAL, false));
		}		
	}	
	
	public void adicionarDecimalNull(){		
		listaCampos.add(new CampoTJGO("", Types.REAL, true));
	}
	
	public void adicionarDouble(double valor) throws Exception{			
		//Number num = NumberFormat.getCurrencyInstance().parse(valor.trim());
		listaCampos.add(new CampoTJGO(valor , Types.DOUBLE, false));
	}	
	
	public void adicionarDouble(String valor) throws Exception{
	
		if (stringIsNula(valor, true)) 
			//Este método será extinto, estou utilizando aqui o type Real como alias apenas para manter o funcionamento atual, posteriormente iremos utilizar apenas o bigdecimal
			listaCampos.add(new CampoTJGO("", Types.REAL, true));			
		else{
			//Number num = NumberFormat.getCurrencyInstance().parse(valor.trim());
			listaCampos.add(new CampoTJGO(Funcoes.StringToDouble(valor) , Types.REAL, false));
		}		
	}	
	
	public void adicionarBigDecimal(BigDecimal valor) throws Exception {
		listaCampos.add(new CampoTJGO(valor, Types.DECIMAL, false));
	}
	
	public void adicionarBigDecimal(String valor) throws Exception{
		if (stringIsNula(valor, true)) listaCampos.add(new CampoTJGO("", Types.DECIMAL, true));
		else {
			listaCampos.add(new CampoTJGO(new BigDecimal(Funcoes.BancoDecimal(valor)), Types.DECIMAL, false));				
		}
	}
	
	public void adicionarBigDecimalNull(){
		listaCampos.add(new CampoTJGO("", Types.DECIMAL, true));
	}
	
	private boolean stringIsNula(String valor, boolean verificaStringVazia){
		if (valor == null) return true;
		if (verificaStringVazia && valor.trim().length() == 0) return true;
		//return valor.trim().equalsIgnoreCase("null");
		return false;
	}
	
	public void adicionarNull(int tipo){
		listaCampos.add(new CampoTJGO("", tipo, true));
	}
	
	public String getValoresLog(){
		valoresLog = "(";
		CampoTJGO campo = null;
		for (int i = 0; i < this.listaCampos.size(); i++){	
			campo = (CampoTJGO) this.listaCampos.get(i);
			if (campo.isNulo()) valoresLog += ", NULL";		
			else valoresLog += ", " + campo.getValor();			
		}
		valoresLog = valoresLog.replace("(,", "(");
		valoresLog += ")";		
		return valoresLog;
	}
	
	public List getListaParametros(){
		return this.listaCampos;
	}
	
	public class CampoTJGO implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -3227282177348254693L;
		private Object valor;
		private	int tipo;
		boolean isNulo;
		
		public CampoTJGO(Object valor, int tipo, boolean isNulo){
			this.valor = valor;
			this.tipo = tipo;
			this.isNulo = isNulo;
		}
		
		public Object getValor(){
			return this.valor;
		}
		
		public int getTipo(){
			return this.tipo;
		}
		
		public boolean isNulo(){
			return this.isNulo;
		}
		
	}	
	
}