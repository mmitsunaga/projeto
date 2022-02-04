package br.gov.go.tj.projudi.dt;

public class ProcessoParteAlcunhaDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 7136494730006795072L;
    private String Id_ProcessoParteAlcunha;
	private String Alcunha;

	private String Id_ProcessoParte;
	private String Nome;
	private String Id_Alcunha;

	private String CodigoTemp;

//---------------------------------------------------------
	public ProcessoParteAlcunhaDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoParteAlcunha;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoParteAlcunha = valor;}
	public String getAlcunha()  {return Alcunha;}
	public void setAlcunha(String valor ) {if (valor!=null) Alcunha = valor;}
	public String getId_ProcessoParte()  {return Id_ProcessoParte;}
	public void setId_ProcessoParte(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoParte = ""; Nome = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoParte = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) {if (valor!=null) Nome = valor;}
	public String getId_Alcunha()  {return Id_Alcunha;}
	public void setId_Alcunha(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Alcunha = ""; Alcunha = "";}else if (!valor.equalsIgnoreCase("")) Id_Alcunha = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoParteAlcunhaDt objeto){
		Id_ProcessoParteAlcunha = objeto.getId();
		Id_ProcessoParte = objeto.getId_ProcessoParte();
		Nome = objeto.getNome();
		Id_Alcunha = objeto.getId_Alcunha();
		Alcunha = objeto.getAlcunha();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoParteAlcunha="";
		Id_ProcessoParte="";
		Nome="";
		Id_Alcunha="";
		Alcunha="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoParteAlcunha:" + Id_ProcessoParteAlcunha + ";Id_ProcessoParte:" + Id_ProcessoParte + ";Nome:" + Nome + ";Id_Alcunha:" + Id_Alcunha + ";Alcunha:" + Alcunha + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
