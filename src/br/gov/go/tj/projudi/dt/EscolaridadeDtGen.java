package br.gov.go.tj.projudi.dt;

public class EscolaridadeDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3521109923946059177L;
	private String Id_Escolaridade;
	private String Escolaridade;


	private String EscolaridadeCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public EscolaridadeDtGen() {

		limpar();

	}

	public String getId()  {return Id_Escolaridade;}
	public void setId(String valor ) {if(valor!=null) Id_Escolaridade = valor;}
	public String getEscolaridade()  {return Escolaridade;}
	public void setEscolaridade(String valor ) {if (valor!=null) Escolaridade = valor;}
	public String getEscolaridadeCodigo()  {return EscolaridadeCodigo;}
	public void setEscolaridadeCodigo(String valor ) {if (valor!=null) EscolaridadeCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(EscolaridadeDt objeto){
		 if (objeto==null) return;
		Id_Escolaridade = objeto.getId();
		Escolaridade = objeto.getEscolaridade();
		EscolaridadeCodigo = objeto.getEscolaridadeCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Escolaridade="";
		Escolaridade="";
		EscolaridadeCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Escolaridade:" + Id_Escolaridade + ";Escolaridade:" + Escolaridade + ";EscolaridadeCodigo:" + EscolaridadeCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
