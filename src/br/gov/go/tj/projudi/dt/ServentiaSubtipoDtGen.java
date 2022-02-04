package br.gov.go.tj.projudi.dt;

public class ServentiaSubtipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -5136292977501036482L;
    private String Id_ServentiaSubtipo;
	private String ServentiaSubtipo;
	
	


	private String ServentiaSubtipoCodigo;
	

//---------------------------------------------------------
	public ServentiaSubtipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ServentiaSubtipo;}
	public void setId(String valor ) {if(valor!=null) Id_ServentiaSubtipo = valor;}
	public String getServentiaSubtipo()  {return ServentiaSubtipo;}
	public void setServentiaSubtipo(String valor ) {if (valor!=null) ServentiaSubtipo = valor;}
	public String getServentiaSubtipoCodigo()  {return ServentiaSubtipoCodigo;}
	public void setServentiaSubtipoCodigo(String valor ) {if (valor!=null) ServentiaSubtipoCodigo = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	
	
	


	public void copiar(ServentiaSubtipoDt objeto){
		Id_ServentiaSubtipo = objeto.getId();
		ServentiaSubtipo = objeto.getServentiaSubtipo();
		ServentiaSubtipoCodigo = objeto.getServentiaSubtipoCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ServentiaSubtipo="";
		ServentiaSubtipo="";
		ServentiaSubtipoCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ServentiaSubtipo:" + Id_ServentiaSubtipo + ";ServentiaSubtipo:" + ServentiaSubtipo + ";ServentiaSubtipoCodigo:" + ServentiaSubtipoCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
