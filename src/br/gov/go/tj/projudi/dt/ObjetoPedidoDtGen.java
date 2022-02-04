package br.gov.go.tj.projudi.dt;

public class ObjetoPedidoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 3477101025936208442L;
    private String Id_ObjetoPedido;
	private String ObjetoPedido;
	
	


	private String ObjetoPedidoCodigo;
	

//---------------------------------------------------------
	public ObjetoPedidoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ObjetoPedido;}
	public void setId(String valor ) {if(valor!=null) Id_ObjetoPedido = valor;}
	public String getObjetoPedido()  {return ObjetoPedido;}
	public void setObjetoPedido(String valor ) {if (valor!=null) ObjetoPedido = valor;}
	public String getObjetoPedidoCodigo()  {return ObjetoPedidoCodigo;}
	public void setObjetoPedidoCodigo(String valor ) {if (valor!=null) ObjetoPedidoCodigo = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	
	
	


	public void copiar(ObjetoPedidoDt objeto){
		Id_ObjetoPedido = objeto.getId();
		ObjetoPedido = objeto.getObjetoPedido();
		ObjetoPedidoCodigo = objeto.getObjetoPedidoCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ObjetoPedido="";
		ObjetoPedido="";
		ObjetoPedidoCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ObjetoPedido:" + Id_ObjetoPedido + ";ObjetoPedido:" + ObjetoPedido + ";ObjetoPedidoCodigo:" + ObjetoPedidoCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
