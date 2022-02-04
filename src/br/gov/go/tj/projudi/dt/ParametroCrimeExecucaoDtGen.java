package br.gov.go.tj.projudi.dt;

public class ParametroCrimeExecucaoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2623525417558165045L;
	private String Id_ParametroCrimeExecucao;
	private String CrimeExecucao;

	private String Data;
	private String HediondoProgressao;
	private String HediondoLivramCond;
	private String EquiparaHediondoLivramCond;
	private String Id_CrimeExecucao;

	private String Artigo;
	private String Paragrafo;
	private String Lei;
	private String Inciso;
	private String CodigoTemp;

//---------------------------------------------------------
	public ParametroCrimeExecucaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ParametroCrimeExecucao;}
	public void setId(String valor ) {if(valor!=null) Id_ParametroCrimeExecucao = valor;}
	public String getCrimeExecucao()  {return CrimeExecucao;}
	public void setCrimeExecucao(String valor ) {if (valor!=null) CrimeExecucao = valor;}
	public String getData()  {return Data;}
	public void setData(String valor ) {if (valor!=null) Data = valor;}
	public String getHediondoProgressao()  {return HediondoProgressao;}
	public void setHediondoProgressao(String valor ) {if (valor!=null) HediondoProgressao = valor;}
	public String getHediondoLivramCond()  {return HediondoLivramCond;}
	public void setHediondoLivramCond(String valor ) {if (valor!=null) HediondoLivramCond = valor;}
	public String getEquiparaHediondoLivramCond()  {return EquiparaHediondoLivramCond;}
	public void setEquiparaHediondoLivramCond(String valor ) {if (valor!=null) EquiparaHediondoLivramCond = valor;}
	public String getId_CrimeExecucao()  {return Id_CrimeExecucao;}
	public void setId_CrimeExecucao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_CrimeExecucao = ""; CrimeExecucao = "";}else if (!valor.equalsIgnoreCase("")) Id_CrimeExecucao = valor;}
	public String getArtigo()  {return Artigo;}
	public void setArtigo(String valor ) {if (valor!=null) Artigo = valor;}
	public String getParagrafo()  {return Paragrafo;}
	public void setParagrafo(String valor ) {if (valor!=null) Paragrafo = valor;}
	public String getLei()  {return Lei;}
	public void setLei(String valor ) {if (valor!=null) Lei = valor;}
	public String getInciso()  {return Inciso;}
	public void setInciso(String valor ) {if (valor!=null) Inciso = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ParametroCrimeExecucaoDt objeto){
		 if (objeto==null) return;
		Id_ParametroCrimeExecucao = objeto.getId();
		Data = objeto.getData();
		HediondoProgressao = objeto.getHediondoProgressao();
		HediondoLivramCond = objeto.getHediondoLivramCond();
		EquiparaHediondoLivramCond = objeto.getEquiparaHediondoLivramCond();
		Id_CrimeExecucao = objeto.getId_CrimeExecucao();
		CrimeExecucao = objeto.getCrimeExecucao();
		Artigo = objeto.getArtigo();
		Paragrafo = objeto.getParagrafo();
		Lei = objeto.getLei();
		Inciso = objeto.getInciso();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ParametroCrimeExecucao="";
		Data="";
		HediondoProgressao="";
		HediondoLivramCond="";
		EquiparaHediondoLivramCond="";
		Id_CrimeExecucao="";
		CrimeExecucao="";
		Artigo="";
		Paragrafo="";
		Lei="";
		Inciso="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ParametroCrimeExecucao:" + Id_ParametroCrimeExecucao + ";Data:" + Data + ";HediondoProgressao:" + HediondoProgressao + ";HediondoLivramCond:" + HediondoLivramCond + ";EquiparaHediondoLivramCond:" + EquiparaHediondoLivramCond + ";Id_CrimeExecucao:" + Id_CrimeExecucao + ";CrimeExecucao:" + CrimeExecucao + ";Artigo:" + Artigo + ";Paragrafo:" + Paragrafo + ";Lei:" + Lei + ";Inciso:" + Inciso + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
