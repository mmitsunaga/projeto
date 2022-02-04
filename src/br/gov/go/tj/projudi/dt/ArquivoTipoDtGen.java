package br.gov.go.tj.projudi.dt;

public class ArquivoTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 324030669750743500L;
    private String Id_ArquivoTipo;
	private String ArquivoTipo;


	private String ArquivoTipoCodigo;
	private String Publico;
	private String Dje;
	private String CodigoTemp;
	
	
//---------------------------------------------------------
	public ArquivoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ArquivoTipo;}
	public void setId(String valor ) {if(valor!=null) Id_ArquivoTipo = valor;}
	public String getArquivoTipo()  {return ArquivoTipo;}
	public void setArquivoTipo(String valor ) {if (valor!=null) ArquivoTipo = valor;}
	public String getArquivoTipoCodigo()  {return ArquivoTipoCodigo;}
	public void setArquivoTipoCodigo(String valor ) {if (valor!=null) ArquivoTipoCodigo = valor;}
	public String getPublico()  {return Publico;}
	public void setPublico(String valor ) {if (valor!=null) Publico = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getDje() { return Dje;}
	public void setDje(String _dje) { if (_dje!=null) this.Dje = _dje;}

	public void copiar(ArquivoTipoDt objeto){
		 if (objeto==null) return;
		Id_ArquivoTipo = objeto.getId();
		ArquivoTipo = objeto.getArquivoTipo();
		ArquivoTipoCodigo = objeto.getArquivoTipoCodigo();
		Publico = objeto.getPublico();
		Dje = objeto.getDje();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ArquivoTipo="";
		ArquivoTipo="";
		ArquivoTipoCodigo="";
		Publico="";
		Dje="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ArquivoTipo:" + Id_ArquivoTipo + ";ArquivoTipo:" + ArquivoTipo + ";ArquivoTipoCodigo:" + ArquivoTipoCodigo + ";Publico:" + Publico + ";Dje:" + Dje + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
