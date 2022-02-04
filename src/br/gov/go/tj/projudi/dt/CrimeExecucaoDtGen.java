package br.gov.go.tj.projudi.dt;

public class CrimeExecucaoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -4071599992180004502L;
    private String Id_CrimeExecucao;
	private String CrimeExecucao;
	private String CrimeExecucaoCodigo;

	private String Lei;
	private String Artigo;
	private String Paragrafo;
	private String Inciso;
	private String CodigoTemp;

//---------------------------------------------------------
	public CrimeExecucaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_CrimeExecucao;}
	public void setId(String valor ) {if(valor!=null) Id_CrimeExecucao = valor;}
	public String getCrimeExecucao()  {return CrimeExecucao;}
	public void setCrimeExecucao(String valor ) {if (valor!=null) CrimeExecucao = valor;}
	public String getCrimeExecucaoCodigo()  {return CrimeExecucaoCodigo;}
	public void setCrimeExecucaoCodigo(String valor ) {if (valor!=null) CrimeExecucaoCodigo = valor;}
	public String getLei()  {return Lei;}
	public void setLei(String valor ) {if (valor!=null) Lei = valor;}
	public String getArtigo()  {return Artigo;}
	public void setArtigo(String valor ) {if (valor!=null) Artigo = valor;}
	public String getParagrafo()  {return Paragrafo;}
	public void setParagrafo(String valor ) {if (valor!=null) Paragrafo = valor;}
	public String getInciso()  {return Inciso;}
	public void setInciso(String valor ) {if (valor!=null) Inciso = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(CrimeExecucaoDt objeto){
		Id_CrimeExecucao = objeto.getId();
		CrimeExecucao = objeto.getCrimeExecucao();
		Lei = objeto.getLei();
		Artigo = objeto.getArtigo();
		Paragrafo = objeto.getParagrafo();
		Inciso = objeto.getInciso();
		CodigoTemp = objeto.getCodigoTemp();
		CrimeExecucaoCodigo = objeto.getCrimeExecucaoCodigo();
	}

	public void limpar(){
		Id_CrimeExecucao="";
		CrimeExecucao="";
		Lei="";
		Artigo="";
		Paragrafo="";
		Inciso="";
		CodigoTemp="";
		CrimeExecucaoCodigo = "";
	}


	public String getPropriedades(){
		return "[Id_CrimeExecucao:" + Id_CrimeExecucao + ";CrimeExecucao:" + CrimeExecucao + ";Lei:" + Lei + ";Artigo:" + Artigo + ";Paragrafo:" + Paragrafo + ";Inciso:" + Inciso + ";CodigoTemp:" + CodigoTemp + ";CrimeExecucaoCodigo:" + CrimeExecucaoCodigo + "]";
	}


} 
