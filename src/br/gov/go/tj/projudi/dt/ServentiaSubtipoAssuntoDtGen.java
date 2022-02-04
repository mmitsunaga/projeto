package br.gov.go.tj.projudi.dt;

public class ServentiaSubtipoAssuntoDtGen extends Dados{

	private static final long serialVersionUID = -6836641927363661874L;
	private String Id_ServentiaSubtipoAssunto;
	private String ServentiaSubtipo;

	private String Id_ServentiaSubtipo;

	private String Id_Assunto;
	private String Assunto;
	private String CodigoTemp;
	private String ServentiaSubtipoCodigo;
	private String AssuntoCodigo;
	private String Dispositivo_Legal;
	private String Id_AssuntoPai;
	private String AssuntoPai;

//---------------------------------------------------------
	public ServentiaSubtipoAssuntoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ServentiaSubtipoAssunto;}
	public void setId(String valor ) {if(valor!=null) Id_ServentiaSubtipoAssunto = valor;}
	public String getServentiaSubtipo()  {return ServentiaSubtipo;}
	public void setServentiaSubtipo(String valor ) {if (valor!=null) ServentiaSubtipo = valor;}
	public String getId_ServentiaSubtipo()  {return Id_ServentiaSubtipo;}
	public void setId_ServentiaSubtipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaSubtipo = ""; ServentiaSubtipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaSubtipo = valor;}
	public String getId_Assunto()  {return Id_Assunto;}
	public void setId_Assunto(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Assunto = ""; Assunto = "";}else if (!valor.equalsIgnoreCase("")) Id_Assunto = valor;}
	public String getAssunto()  {return Assunto;}
	public void setAssunto(String valor ) {if (valor!=null) Assunto = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getServentiaSubtipoCodigo()  {return ServentiaSubtipoCodigo;}
	public void setServentiaSubtipoCodigo(String valor ) {if (valor!=null) ServentiaSubtipoCodigo = valor;}
	public String getAssuntoCodigo()  {return AssuntoCodigo;}
	public void setAssuntoCodigo(String valor ) {if (valor!=null) AssuntoCodigo = valor;}
	public String getDispositivo_Legal()  {return Dispositivo_Legal;}
	public void setDispositivo_Legal(String valor ) {if (valor!=null) Dispositivo_Legal = valor;}
	public String getId_AssuntoPai()  {return Id_AssuntoPai;}
	public void setId_AssuntoPai(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_AssuntoPai = ""; AssuntoPai = "";}else if (!valor.equalsIgnoreCase("")) Id_AssuntoPai = valor;}
	public String getAssuntoPai()  {return AssuntoPai;}
	public void setAssuntoPai(String valor ) {if (valor!=null) AssuntoPai = valor;}


	public void copiar(ServentiaSubtipoAssuntoDt objeto){
		 if (objeto==null) return;
		Id_ServentiaSubtipoAssunto = objeto.getId();
		Id_ServentiaSubtipo = objeto.getId_ServentiaSubtipo();
		ServentiaSubtipo = objeto.getServentiaSubtipo();
		Id_Assunto = objeto.getId_Assunto();
		Assunto = objeto.getAssunto();
		CodigoTemp = objeto.getCodigoTemp();
		ServentiaSubtipoCodigo = objeto.getServentiaSubtipoCodigo();
		AssuntoCodigo = objeto.getAssuntoCodigo();
		Dispositivo_Legal = objeto.getDispositivo_Legal();
		Id_AssuntoPai = objeto.getId_AssuntoPai();
		AssuntoPai = objeto.getAssuntoPai();
	}

	public void limpar(){
		Id_ServentiaSubtipoAssunto="";
		Id_ServentiaSubtipo="";
		ServentiaSubtipo="";
		Id_Assunto="";
		Assunto="";
		CodigoTemp="";
		ServentiaSubtipoCodigo="";
		AssuntoCodigo="";
		Dispositivo_Legal="";
		Id_AssuntoPai="";
		AssuntoPai="";
	}


	public String getPropriedades(){
		return "[Id_ServentiaSubtipoAssunto:" + Id_ServentiaSubtipoAssunto + ";Id_ServentiaSubtipo:" + Id_ServentiaSubtipo + ";ServentiaSubtipo:" + ServentiaSubtipo + ";Id_Assunto:" + Id_Assunto + ";Assunto:" + Assunto + ";CodigoTemp:" + CodigoTemp + ";ServentiaSubtipoCodigo:" + ServentiaSubtipoCodigo + ";AssuntoCodigo:" + AssuntoCodigo + ";Dispositivo_Legal:" + Dispositivo_Legal + ";Id_AssuntoPai:" + Id_AssuntoPai + ";AssuntoPai:" + AssuntoPai + "]";
	}


} 
