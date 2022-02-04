package br.gov.go.tj.projudi.dt;

public class PendenciaTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 1762763394304185848L;
    private String Id_PendenciaTipo;
	private String PendenciaTipo;


	private String PendenciaTipoCodigo;
	private String Id_ArquivoTipo;
	private String ArquivoTipo;
	private String CodigoTemp;

//---------------------------------------------------------
	public PendenciaTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_PendenciaTipo;}
	public void setId(String valor ) {if(valor!=null) Id_PendenciaTipo = valor;}
	public String getPendenciaTipo()  {return PendenciaTipo;}
	public void setPendenciaTipo(String valor ) {if (valor!=null) PendenciaTipo = valor;}
	public String getPendenciaTipoCodigo()  {return PendenciaTipoCodigo;}
	public void setPendenciaTipoCodigo(String valor ) {if (valor!=null) PendenciaTipoCodigo = valor;}
	public String getId_ArquivoTipo()  {return Id_ArquivoTipo;}
	public void setId_ArquivoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ArquivoTipo = ""; ArquivoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ArquivoTipo = valor;}
	public String getArquivoTipo()  {return ArquivoTipo;}
	public void setArquivoTipo(String valor ) {if (valor!=null) ArquivoTipo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(PendenciaTipoDt objeto){
		Id_PendenciaTipo = objeto.getId();
		PendenciaTipo = objeto.getPendenciaTipo();
		PendenciaTipoCodigo = objeto.getPendenciaTipoCodigo();
		Id_ArquivoTipo = objeto.getId_ArquivoTipo();
		ArquivoTipo = objeto.getArquivoTipo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_PendenciaTipo="";
		PendenciaTipo="";
		PendenciaTipoCodigo="";
		Id_ArquivoTipo="";
		ArquivoTipo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_PendenciaTipo:" + Id_PendenciaTipo + ";PendenciaTipo:" + PendenciaTipo + ";PendenciaTipoCodigo:" + PendenciaTipoCodigo + ";Id_ArquivoTipo:" + Id_ArquivoTipo + ";ArquivoTipo:" + ArquivoTipo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
