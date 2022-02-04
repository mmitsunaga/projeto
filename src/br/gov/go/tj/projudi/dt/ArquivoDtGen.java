package br.gov.go.tj.projudi.dt;

public class ArquivoDtGen extends Dados{

    private static final long serialVersionUID = -77070121319410512L;
    protected String Id_Arquivo;
	private String NomeArquivo;


	private String Id_ArquivoTipo;
	private String ArquivoTipo;
	private String ContentType;
	private String Arquivo;
	protected String Caminho;
	private String DataInsercao;
	protected String UsuarioAssinador;
	private String Recibo;
	private String CodigoTemp;
	private String ArquivoTipoCodigo;

	public ArquivoDtGen() {

		limpar();

	}

	public String getId()  {return Id_Arquivo;}
	public void setId(String valor ) {if(valor!=null) Id_Arquivo = valor;}
	public String getNomeArquivo()  {return NomeArquivo;}
	public void setNomeArquivo(String valor) {
		if (valor != null) 
			NomeArquivo = valor;
	}
	public String getId_ArquivoTipo()  {return Id_ArquivoTipo;}
	public void setId_ArquivoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ArquivoTipo = ""; ArquivoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ArquivoTipo = valor;}
	public String getArquivoTipo()  {return ArquivoTipo;}
	public void setArquivoTipo(String valor ) {if (valor!=null) ArquivoTipo = valor;}
	public String getContentType()  {return ContentType;}
	public void setContentType(String valor ) {if (valor!=null) ContentType = valor;}
	public String getArquivo()  {return Arquivo;}
	public void setArquivo(String valor ) {if (valor!=null) Arquivo = valor;}
	public String getCaminho()  {return Caminho;}
	public void setCaminho(String valor ) {if (valor!=null) Caminho = valor;}
	public String getDataInsercao()  {return DataInsercao;}
	public void setDataInsercao(String valor ) {if (valor!=null) DataInsercao = valor;}
	public String getUsuarioAssinador()  {return UsuarioAssinador;}
	public void setUsuarioAssinador(String valor ) {if (valor!=null) UsuarioAssinador = valor;}
	public String getRecibo()  {return Recibo;}
	public void setRecibo(String valor ) {if (valor!=null) Recibo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getArquivoTipoCodigo()  {return ArquivoTipoCodigo;}
	public void setArquivoTipoCodigo(String valor ) {if (valor!=null) ArquivoTipoCodigo = valor;}


	public void copiar(ArquivoDt objeto){
		Id_Arquivo = objeto.getId();
		NomeArquivo = objeto.getNomeArquivo();
		Id_ArquivoTipo = objeto.getId_ArquivoTipo();
		ArquivoTipo = objeto.getArquivoTipo();
		ContentType = objeto.getContentType();
		Arquivo = objeto.getArquivo();
		Caminho = objeto.getCaminho();
		DataInsercao = objeto.getDataInsercao();
		UsuarioAssinador = objeto.getUsuarioAssinador();
		Recibo = objeto.getRecibo();
		CodigoTemp = objeto.getCodigoTemp();
		ArquivoTipoCodigo = objeto.getArquivoTipoCodigo();
	}

	public void limpar(){
		Id_Arquivo="";
		NomeArquivo="";
		Id_ArquivoTipo="";
		ArquivoTipo="";
		ContentType="";
		Arquivo="";
		Caminho="";
		DataInsercao="";
		UsuarioAssinador="";
		Recibo="";
		CodigoTemp="";
		ArquivoTipoCodigo="";
	}


	public String getPropriedades(){
		return "[Id_Arquivo:" + Id_Arquivo + ";NomeArquivo:" + NomeArquivo + ";Id_ArquivoTipo:" + Id_ArquivoTipo + ";ArquivoTipo:" + ArquivoTipo + ";ContentType:" + ContentType + ";Arquivo:" + Arquivo + ";Caminho:" + Caminho + ";DataInsercao:" + DataInsercao + ";UsuarioAssinador:" + UsuarioAssinador + ";Recibo:" + Recibo + ";CodigoTemp:" + CodigoTemp + ";ArquivoTipoCodigo:" + ArquivoTipoCodigo + "]";
	}


} 
