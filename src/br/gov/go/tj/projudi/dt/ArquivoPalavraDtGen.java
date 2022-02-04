package br.gov.go.tj.projudi.dt;

public class ArquivoPalavraDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -269362480851239425L;
    private String Id_ArquivoPalavra;
	private String NomeArquivo;

	private String Id_Arquivo;

	private String Id_Palavra1;
	private String Palavra1;
	private String Id_Palavra2;
	private String Palavra2;
	private String CodigoTemp;

//---------------------------------------------------------
	public ArquivoPalavraDtGen() {

		limpar();

	}

	public String getId()  {return Id_ArquivoPalavra;}
	public void setId(String valor ) {if(valor!=null) Id_ArquivoPalavra = valor;}
	public String getNomeArquivo()  {return NomeArquivo;}
	public void setNomeArquivo(String valor ) {if (valor!=null) NomeArquivo = valor;}
	public String getId_Arquivo()  {return Id_Arquivo;}
	public void setId_Arquivo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Arquivo = ""; NomeArquivo = "";}else if (!valor.equalsIgnoreCase("")) Id_Arquivo = valor;}
	public String getId_Palavra1()  {return Id_Palavra1;}
	public void setId_Palavra1(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Palavra1 = ""; Palavra1 = "";}else if (!valor.equalsIgnoreCase("")) Id_Palavra1 = valor;}
	public String getPalavra1()  {return Palavra1;}
	public void setPalavra1(String valor ) {if (valor!=null) Palavra1 = valor;}
	public String getId_Palavra2()  {return Id_Palavra2;}
	public void setId_Palavra2(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Palavra2 = ""; Palavra2 = "";}else if (!valor.equalsIgnoreCase("")) Id_Palavra2 = valor;}
	public String getPalavra2()  {return Palavra2;}
	public void setPalavra2(String valor ) {if (valor!=null) Palavra2 = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ArquivoPalavraDt objeto){
		Id_ArquivoPalavra = objeto.getId();
		Id_Arquivo = objeto.getId_Arquivo();
		NomeArquivo = objeto.getNomeArquivo();
		Id_Palavra1 = objeto.getId_Palavra1();
		Palavra1 = objeto.getPalavra1();
		Id_Palavra2 = objeto.getId_Palavra2();
		Palavra2 = objeto.getPalavra2();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ArquivoPalavra="";
		Id_Arquivo="";
		NomeArquivo="";
		Id_Palavra1="";
		Palavra1="";
		Id_Palavra2="";
		Palavra2="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ArquivoPalavra:" + Id_ArquivoPalavra + ";Id_Arquivos:" + Id_Arquivo + ";NomeArquivo:" + NomeArquivo + ";Id_Palavra1:" + Id_Palavra1 + ";Palavra1:" + Palavra1 + ";Id_Palavra2:" + Id_Palavra2 + ";Palavra2:" + Palavra2 + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
