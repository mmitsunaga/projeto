package br.gov.go.tj.projudi.dt;

public class EstadoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 8920447711828474207L;
    private String Id_Estado;
	private String Estado;


	private String EstadoCodigo;
	private String Id_Pais;
	private String Pais;
	private String Uf;
	private String CodigoTemp;
	private String PaisCodigo;

//---------------------------------------------------------
	public EstadoDtGen() {

		limpar();

	}

	public String getId()  {return Id_Estado;}
	public void setId(String valor ) {if(valor!=null) Id_Estado = valor;}
	public String getEstado()  {return Estado;}
	public void setEstado(String valor ) {if (valor!=null) Estado = valor;}
	public String getEstadoCodigo()  {return EstadoCodigo;}
	public void setEstadoCodigo(String valor ) {if (valor!=null) EstadoCodigo = valor;}
	public String getId_Pais()  {return Id_Pais;}
	public void setId_Pais(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Pais = ""; Pais = "";}else if (!valor.equalsIgnoreCase("")) Id_Pais = valor;}
	public String getPais()  {return Pais;}
	public void setPais(String valor ) {if (valor!=null) Pais = valor;}
	public String getUf()  {return Uf;}
	public void setUf(String valor ) {if (valor!=null) Uf = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getPaisCodigo()  {return PaisCodigo;}
	public void setPaisCodigo(String valor ) {if (valor!=null) PaisCodigo = valor;}


	public void copiar(EstadoDt objeto){
		Id_Estado = objeto.getId();
		Estado = objeto.getEstado();
		EstadoCodigo = objeto.getEstadoCodigo();
		Id_Pais = objeto.getId_Pais();
		Pais = objeto.getPais();
		Uf = objeto.getUf();
		CodigoTemp = objeto.getCodigoTemp();
		PaisCodigo = objeto.getPaisCodigo();
	}

	public void limpar(){
		Id_Estado="";
		Estado="";
		EstadoCodigo="";
		Id_Pais="";
		Pais="";
		Uf="";
		CodigoTemp="";
		PaisCodigo="";
	}


	public String getPropriedades(){
		return "[Id_Estado:" + Id_Estado + ";Estado:" + Estado + ";EstadoCodigo:" + EstadoCodigo + ";Id_Pais:" + Id_Pais + ";Pais:" + Pais + ";Uf:" + Uf + ";CodigoTemp:" + CodigoTemp + ";PaisCodigo:" + PaisCodigo + "]";
	}


} 
