package br.gov.go.tj.projudi.dt;

public class AssuntoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 409988492542580633L;
	protected String Id_Assunto;
	protected String Assunto;


	protected String AssuntoCodigo;
	protected String Id_AssuntoPai;
	protected String AssuntoPai;
	protected String IsAtivo;
	protected String DispositivoLegal;
	protected String Artigo;
	protected String Id_CnjAssunto;
	protected String Sigla;
	protected String CodigoTemp;

//---------------------------------------------------------
	public AssuntoDtGen() {

		limpar();

	}

	public String getId()  {return Id_Assunto;}
	public void setId(String valor ) { if(valor!=null) Id_Assunto = valor;}
	public String getAssunto()  {return Assunto;}
	public void setAssunto(String valor ) { if (valor!=null) Assunto = valor;}
	public String getAssuntoCodigo()  {return AssuntoCodigo;}
	public void setAssuntoCodigo(String valor ) { if (valor!=null) AssuntoCodigo = valor;}
	public String getId_AssuntoPai()  {return Id_AssuntoPai;}
	public void setId_AssuntoPai(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_AssuntoPai = ""; AssuntoPai = "";}else if (!valor.equalsIgnoreCase("")) Id_AssuntoPai = valor;}
	public String getAssuntoPai()  {return AssuntoPai;}
	public void setAssuntoPai(String valor ) { if (valor!=null) AssuntoPai = valor;}
	public String getIsAtivo()  {return IsAtivo;}
	public void setIsAtivo(String valor ) { if (valor!=null) IsAtivo = valor;}
	public String getDispositivoLegal()  {return DispositivoLegal;}
	public void setDispositivoLegal(String valor ) { if (valor!=null) DispositivoLegal = valor;}
	public String getArtigo()  {return Artigo;}
	public void setArtigo(String valor ) { if (valor!=null) Artigo = valor;}
	public String getId_CnjAssunto()  {return Id_CnjAssunto;}
	public void setId_CnjAssunto(String valor ) { if (valor!=null) Id_CnjAssunto = valor;}
	public String getSigla()  {return Sigla;}
	public void setSigla(String valor ) { if (valor!=null) Sigla = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}


	public void copiar(AssuntoDt objeto){
		 if (objeto==null) return;
		Id_Assunto = objeto.getId();
		Assunto = objeto.getAssunto();
		AssuntoCodigo = objeto.getAssuntoCodigo();
		Id_AssuntoPai = objeto.getId_AssuntoPai();
		AssuntoPai = objeto.getAssuntoPai();
		IsAtivo = objeto.getIsAtivo();
		DispositivoLegal = objeto.getDispositivoLegal();
		Artigo = objeto.getArtigo();
		Id_CnjAssunto = objeto.getId_CnjAssunto();
		Sigla = objeto.getSigla();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Assunto="";
		Assunto="";
		AssuntoCodigo="";
		Id_AssuntoPai="";
		AssuntoPai="";
		IsAtivo="";
		DispositivoLegal="";
		Artigo="";
		Id_CnjAssunto="";
		Sigla="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Assunto:" + Id_Assunto + ";Assunto:" + Assunto + ";AssuntoCodigo:" + AssuntoCodigo + ";Id_AssuntoPai:" + Id_AssuntoPai + ";AssuntoPai:" + AssuntoPai + ";IsAtivo:" + IsAtivo + ";DispositivoLegal:" + DispositivoLegal + ";Artigo:" + Artigo + ";Id_CnjAssunto:" + Id_CnjAssunto + ";Sigla:" + Sigla + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
