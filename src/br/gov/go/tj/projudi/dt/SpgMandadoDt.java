package br.gov.go.tj.projudi.dt;

import java.util.Date;

public class SpgMandadoDt extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1619272688929267100L;

	public final static String URGENTE_SIM = "S";
	public final static String URGENTE_NAO = "N";
	
	public final static int TIPO_MANDADO_LIMINAR = 1;
	public final static int TIPO_MANDADO_EXECUCAO = 2;
	public final static int TIPO_MANDADO_COMUM = 3;
	public final static int TIPO_MANDADO_ESPECIAL = 4;
	public final static int TIPO_MANDADO_ORDEM_SERVICO = 5;
	public final static int TIPO_MANDADO_TRIBUANL_JURI = 6;
	public final static int TIPO_MANDADO_AVALIACAO = 7;
	public final static int TIPO_MANDADO_EXECUCAO_FISCAL = 8;
		
	String ComarcaCodigo="";
	Date DataEmissao =null;	

	String RegiaoCodigo="";
	String ZonaCodigo="";
	String MandadoNome="";
	int MandadoTipo =3;
	int TipoConsultaEscrivania =5;
	Date DataRecebimentoCentral = null;
	String NumeroProcessoProjudi ="";
	String EscrivaniaCodigo ="";
	String MandadoNumero ="";
	String Urgente="";
	
	public String getComarcaCodigo() {
		return ComarcaCodigo;
	}

	public void setComarcaCodigo(String comarcaCodigo) {
		ComarcaCodigo = comarcaCodigo;
	}

	public Date getDataEmissao() {
		return DataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		DataEmissao = dataEmissao;
	}

	public String getRegiaoCodigo() {
		return RegiaoCodigo;
	}

	public void setRegiaoCodigo(String regiaoCodigo) {
		RegiaoCodigo = regiaoCodigo;
	}

	public String getZonaCodigo() {
		return ZonaCodigo;
	}

	public void setZonaCodigo(String zonaCodigo) {
		ZonaCodigo = zonaCodigo;
	}

	public String getMandadoNome() {
		return MandadoNome;
	}

	public void setMandadoNome(String mandadoNome) {
		MandadoNome = mandadoNome;
	}

	public int getMandadoTipo() {
		return MandadoTipo;
	}

	public void setMandadoTipo(int mandadoTipo) {
		MandadoTipo = mandadoTipo;
	}

	public int getTipoConsultaEscrivania() {
		return TipoConsultaEscrivania;
	}

	public void setTipoConsultaEscrivania(int tipoConsultaEscrivania) {
		TipoConsultaEscrivania = tipoConsultaEscrivania;
	}

	public Date getDataRecebimentoCentral() {
		return DataRecebimentoCentral;
	}

	public void setDataRecebimentoCentral(Date dataRecebimentoCentral) {
		DataRecebimentoCentral = dataRecebimentoCentral;
	}

	public String getNumeroProcessoProjudi() {
		return NumeroProcessoProjudi;
	}

	public void setNumeroProcessoProjudi(String numeroProcessoProjudi) {
		NumeroProcessoProjudi = numeroProcessoProjudi;
	}

	public String getEscrivaniaCodigo() {
		return EscrivaniaCodigo;
	}

	public void setEscrivaniaCodigo(String escrivaniaCodigo) {
		EscrivaniaCodigo = escrivaniaCodigo;
	}

	public String getMandadoNumero() {
		return MandadoNumero;
	}

	public void setMandadoNumero(String mandadoNumero) {
		MandadoNumero = mandadoNumero;
	}

	public String getUrgente() {
		return Urgente;
	}

	public void setUrgente(String urgente) {
		Urgente = urgente;
	}
	
	public SpgMandadoDt(){
		DataRecebimentoCentral = new Date();
	}
	
	public void setId(String id) {
		MandadoNumero = id;		
	}
	
	public String getId() {
		return MandadoNumero;
	}

	public String getPropriedades() {
										
		return "[ComarcaCodigo:" + ComarcaCodigo + ";DataEmissao:" + DataEmissao + ";RegiaoCodigo:" + RegiaoCodigo + ";ZonaCodigo:" + ZonaCodigo + ";MandadoNome:" + MandadoNome + ";MandadoTipo:" + MandadoTipo + ";TipoConsultaEscrivania:" + TipoConsultaEscrivania + ";DataRecebimentoCentral:" + DataRecebimentoCentral + ";NumeroProcessoProjudi:" + NumeroProcessoProjudi + ";EscrivaniaCodigo:" + EscrivaniaCodigo + ";MandadoNumero:" + MandadoNumero + ";Urgente:" + Urgente + "]";
	}

	public void copiar(SpgMandadoDt objeto){
		 ComarcaCodigo = objeto.getComarcaCodigo();
		 DataEmissao= objeto.getDataEmissao();
		 RegiaoCodigo= objeto.getRegiaoCodigo();
		 ZonaCodigo= objeto.getZonaCodigo();
		 MandadoNome= objeto.getMandadoNome();
		 MandadoTipo= objeto.getMandadoTipo();
		 TipoConsultaEscrivania= objeto.getTipoConsultaEscrivania();
		 DataRecebimentoCentral= objeto.getDataRecebimentoCentral();
		 NumeroProcessoProjudi= objeto.getNumeroProcessoProjudi();
		 EscrivaniaCodigo= objeto.getEscrivaniaCodigo();
		 MandadoNumero= objeto.getMandadoNumero();
		 Urgente= objeto.getUrgente();
	}

	public void limpar(){
		 ComarcaCodigo = "";
		 DataEmissao = null;
		 RegiaoCodigo = "";
		 ZonaCodigo = "";
		 MandadoNome = "";
		 MandadoTipo = 3;
		 TipoConsultaEscrivania = 5;
		 DataRecebimentoCentral = null;
		 NumeroProcessoProjudi = "";
		 EscrivaniaCodigo = "";
		 MandadoNumero = "";
		 Urgente = "";
	}

}
