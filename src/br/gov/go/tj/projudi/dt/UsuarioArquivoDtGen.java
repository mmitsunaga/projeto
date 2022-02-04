package br.gov.go.tj.projudi.dt;

public class UsuarioArquivoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 779347939145049425L;
    private String Id_UsuarioArquivo;
	private String Usuario;

	private String Id_Usuario;

	private String Id_Arquivo;
	private String NomeArquivo;
	private String CodigoTemp;

//---------------------------------------------------------
	public UsuarioArquivoDtGen() {

		limpar();

	}

	public String getId()  {return Id_UsuarioArquivo;}
	public void setId(String valor ) {if(valor!=null) Id_UsuarioArquivo = valor;}
	public String getUsuario()  {return Usuario;}
	public void setUsuario(String valor ) {if (valor!=null) Usuario = valor;}
	public String getId_Usuario()  {return Id_Usuario;}
	public void setId_Usuario(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Usuario = ""; Usuario = "";}else if (!valor.equalsIgnoreCase("")) Id_Usuario = valor;}
	public String getId_Arquivo()  {return Id_Arquivo;}
	public void setId_Arquivo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Arquivo = ""; NomeArquivo = "";}else if (!valor.equalsIgnoreCase("")) Id_Arquivo = valor;}
	public String getNomeArquivo()  {return NomeArquivo;}
	public void setNomeArquivo(String valor ) {if (valor!=null) NomeArquivo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(UsuarioArquivoDt objeto){
		Id_UsuarioArquivo = objeto.getId();
		Id_Usuario = objeto.getId_Usuario();
		Usuario = objeto.getUsuario();
		Id_Arquivo = objeto.getId_Arquivo();
		NomeArquivo = objeto.getNomeArquivo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_UsuarioArquivo="";
		Id_Usuario="";
		Usuario="";
		Id_Arquivo="";
		NomeArquivo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_UsuarioArquivo:" + Id_UsuarioArquivo + ";Id_Usuario:" + Id_Usuario + ";Usuario:" + Usuario + ";Id_Arquivo:" + Id_Arquivo + ";NomeArquivo:" + NomeArquivo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
