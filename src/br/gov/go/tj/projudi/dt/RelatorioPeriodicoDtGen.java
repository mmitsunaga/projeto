package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

public class RelatorioPeriodicoDtGen extends Dados implements Serializable {

	private static final long serialVersionUID = 7838543444159880998L;
	private String Id_RelatorioPeriodico;
	private String RelatorioPeriodico;


	private String CodigoSql;
	private String CamposSql;
	private String CodigoTemp;

//---------------------------------------------------------
	public RelatorioPeriodicoDtGen() {

		limpar();
		
	}

	public String getId()  {return Id_RelatorioPeriodico;}
	public void setId(String valor ) {if(valor!=null) Id_RelatorioPeriodico = valor;}
	public String getRelatorioPeriodico()  {return RelatorioPeriodico;}
	public void setRelatorioPeriodico(String valor ) {if (valor!=null) RelatorioPeriodico = valor;}
	public String getCodigoSql()  {return CodigoSql;}
	public void setCodigoSql(String valor ) {if (valor!=null) CodigoSql = valor;}
	public String getCamposSql()  {return CamposSql;}
	public void setCamposSql(String valor ) {if (valor!=null) CamposSql = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(RelatorioPeriodicoDt objeto){
		 if (objeto==null) return;
		Id_RelatorioPeriodico = objeto.getId();
		RelatorioPeriodico = objeto.getRelatorioPeriodico();
		CodigoSql = objeto.getCodigoSql();
		CamposSql = objeto.getCamposSql();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_RelatorioPeriodico="";
		RelatorioPeriodico="";
		CodigoSql="";
		CamposSql="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_RelatorioPeriodico:" + Id_RelatorioPeriodico + ";RelatorioPeriodico:" + RelatorioPeriodico + ";CodigoSql:" + CodigoSql + ";CamposSql:" + CamposSql + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
