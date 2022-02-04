package br.gov.go.tj.projudi.dt;

public class ServentiaCargoEscalaStatusDtGen extends Dados{

	private static final long serialVersionUID = 2484286497594239598L;
	private String Id_ServentiaCargoEscalaStatus;
	private String ServentiaCargoEscalaStatus;
	private String ServentiaCargoEscalaStatusCodigo;
	private String Ativo;
	private String CodigoTemp;

	public ServentiaCargoEscalaStatusDtGen() {

		limpar();

	}

	public String getId()  {return Id_ServentiaCargoEscalaStatus;}
	public void setId(String valor ) {if(valor!=null) Id_ServentiaCargoEscalaStatus = valor;}
	public String getServentiaCargoEscalaStatus()  {return ServentiaCargoEscalaStatus;}
	public void setServentiaCargoEscalaStatus(String valor ) {if (valor!=null) ServentiaCargoEscalaStatus = valor;}
	public String getServentiaCargoEscalaStatusCodigo()  {return ServentiaCargoEscalaStatusCodigo;}
	public void setServentiaCargoEscalaStatusCodigo(String valor ) {if (valor!=null) ServentiaCargoEscalaStatusCodigo = valor;}
	public String getAtivo()  {return Ativo;}
	public void setAtivo(String valor ) {if (valor!=null) Ativo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ServentiaCargoEscalaStatusDt objeto){
		 if (objeto==null) return;
		Id_ServentiaCargoEscalaStatus = objeto.getId();
		ServentiaCargoEscalaStatus = objeto.getServentiaCargoEscalaStatus();
		ServentiaCargoEscalaStatusCodigo = objeto.getServentiaCargoEscalaStatusCodigo();
		Ativo = objeto.getAtivo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ServentiaCargoEscalaStatus="";
		ServentiaCargoEscalaStatus="";
		ServentiaCargoEscalaStatusCodigo="";
		Ativo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ServentiaCargoEscalaStatus:" + Id_ServentiaCargoEscalaStatus + ";ServentiaCargoEscalaStatus:" + ServentiaCargoEscalaStatus + ";ServentiaCargoEscalaStatusCodigo:" + ServentiaCargoEscalaStatusCodigo + ";Ativo:" + Ativo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
