package br.gov.go.tj.projudi.dt;

public class CidadeDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -8597247920245829833L;
    private String Id_Cidade;
	private String Cidade;


	private String CidadeCodigo;
	private String Id_Estado;
	private String Estado;
	private String CodigoTemp;
	private String EstadoCodigo;
	private String Uf;
	private String CodigoSPG;

//---------------------------------------------------------
	public CidadeDtGen() {

		limpar();

	}

	public String getId()  {return Id_Cidade;}
	public void setId(String valor ) {if(valor!=null) Id_Cidade = valor;}
	public String getCidade()  {return Cidade;}
	public void setCidade(String valor ) {if (valor!=null) Cidade = valor;}
	public String getCidadeCodigo()  {return CidadeCodigo;}
	public void setCidadeCodigo(String valor ) {if (valor!=null) CidadeCodigo = valor;}
	public String getId_Estado()  {return Id_Estado;}
	public void setId_Estado(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Estado = ""; Estado = "";}else if (!valor.equalsIgnoreCase("")) Id_Estado = valor;}
	public String getEstado()  {return Estado;}
	public void setEstado(String valor ) {if (valor!=null) Estado = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getEstadoCodigo()  {return EstadoCodigo;}
	public void setEstadoCodigo(String valor ) {if (valor!=null) EstadoCodigo = valor;}
	public String getUf()  {return Uf;}
	public void setUf(String valor ) {if (valor!=null) Uf = valor;}
	public String getCodigoSPG()  {return CodigoSPG;}
	public void setCodigoSPG(String valor ) {if (valor!=null) CodigoSPG = valor;}

	public void copiar(CidadeDt objeto){
		Id_Cidade = objeto.getId();
		Cidade = objeto.getCidade();
		CidadeCodigo = objeto.getCidadeCodigo();
		Id_Estado = objeto.getId_Estado();
		Estado = objeto.getEstado();
		CodigoTemp = objeto.getCodigoTemp();
		EstadoCodigo = objeto.getEstadoCodigo();
		Uf = objeto.getUf();
		CodigoSPG = objeto.getCodigoSPG();
	}

	public void limpar(){
		Id_Cidade="";
		Cidade="";
		CidadeCodigo="";
		Id_Estado="";
		Estado="";
		CodigoTemp="";
		EstadoCodigo="";
		Uf="";
		CodigoSPG="";
	}


	public String getPropriedades(){
		return "[Id_Cidade:" + Id_Cidade + ";Cidade:" + Cidade + ";CidadeCodigo:" + CidadeCodigo + ";Id_Estado:" + Id_Estado + ";Estado:" + Estado + ";CodigoTemp:" + CodigoTemp + ";EstadoCodigo:" + EstadoCodigo + ";Uf:" + Uf + ";CodigoSPG:" + CodigoSPG +  "]";
	}
} 
