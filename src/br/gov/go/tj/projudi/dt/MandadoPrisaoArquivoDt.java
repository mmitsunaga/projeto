package br.gov.go.tj.projudi.dt;

public class MandadoPrisaoArquivoDt extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7695467721545496800L;
	private String Id_MandadoPrisaoArquivo;
	private String Id_MandadoPrisao;
	private String Id_Arquivo;

//---------------------------------------------------------
	public MandadoPrisaoArquivoDt() {

		limpar();

	}

	public String getId()  {return Id_MandadoPrisaoArquivo;}
	public void setId(String valor ) {if(valor!=null) Id_MandadoPrisaoArquivo = valor;}
	
	public String getId_MandadoPrisao()  {return Id_MandadoPrisao;}
	public void setId_MandadoPrisao(String valor ) {if (valor!=null) Id_MandadoPrisao = valor;	}
	
	public String getId_Arquivo()  {return Id_Arquivo;}
	public void setId_Arquivo(String valor ) {if (valor!=null) Id_Arquivo = valor;}
	
	public void copiar(MandadoPrisaoArquivoDt objeto){
		 if (objeto==null) return;
		Id_MandadoPrisaoArquivo = objeto.getId();
		Id_MandadoPrisao = objeto.getId_MandadoPrisao();
		Id_Arquivo = objeto.getId_Arquivo();
	}

	public void limpar(){
		Id_MandadoPrisaoArquivo="";
		Id_MandadoPrisao="";
		Id_Arquivo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_MandadoPrisaoArquivo:" + Id_MandadoPrisaoArquivo + 
				";Id_MandadoPrisao:" + Id_MandadoPrisao + 
				";Id_Arquivo:" + Id_Arquivo  + "]";
	}

} 
