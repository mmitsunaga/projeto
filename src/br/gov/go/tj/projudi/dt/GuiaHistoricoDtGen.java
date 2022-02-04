package br.gov.go.tj.projudi.dt;

public class GuiaHistoricoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4881007824194522199L;
	
	protected String Id_GuiaHistorico;
	protected String NumeroGuiaCompleto;

	protected String Id_Guia;

	protected String Valor;
	protected String Data;
	protected String Itens;

//---------------------------------------------------------
	public GuiaHistoricoDtGen() {

		limpar();

	}

	public String getId()  {return Id_GuiaHistorico;}
	public void setId(String valor ) {if(valor!=null) Id_GuiaHistorico = valor;}
	public String getNumeroGuiaCompleto()  {return NumeroGuiaCompleto;}
	public void setNumeroGuiaCompleto(String valor ) {if (valor!=null) NumeroGuiaCompleto = valor;}
	public String getId_Guia()  {return Id_Guia;}
	public void setId_Guia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Guia = ""; NumeroGuiaCompleto = "";}else if (!valor.equalsIgnoreCase("")) Id_Guia = valor;}
	public String getValor()  {return Valor;}
	public void setValor(String valor ) {if (valor!=null) Valor = valor;}
	public String getData()  {return Data;}
	public void setData(String valor ) {if (valor!=null) Data = valor;}
	public String getItens()  {return Itens;}
	public void setItens(String valor ) {if (valor!=null) Itens = valor;}


	public void copiar(GuiaHistoricoDt objeto){
		 if (objeto==null) return;
		Id_GuiaHistorico = objeto.getId();
		Id_Guia = objeto.getId_Guia();
		NumeroGuiaCompleto = objeto.getNumeroGuiaCompleto();
		Valor = objeto.getValor();
		Data = objeto.getData();
		Itens = objeto.getItens();
	}

	public void limpar(){
		Id_GuiaHistorico="";
		Id_Guia="";
		NumeroGuiaCompleto="";
		Valor="";
		Data="";
		Itens="";
	}


	public String getPropriedades(){
		return "[Id_GuiaHistorico:" + Id_GuiaHistorico + ";Id_Guia:" + Id_Guia + ";NumeroGuiaCompleto:" + NumeroGuiaCompleto + ";Valor:" + Valor + ";Data:" + Data + ";Itens:" + Itens + "]";
	}


} 
