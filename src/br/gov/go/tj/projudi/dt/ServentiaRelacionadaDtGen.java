package br.gov.go.tj.projudi.dt;

public class ServentiaRelacionadaDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3905664265773078328L;
	private String Id_ServentiaRelacionada;
	private String ServentiaRelacionada;


	private String Id_ServentiaPrincipal;
	private String ServentiaPrincipal;
	private String Id_ServentiaRelacao;
	private String ServentiaRelacao;
	private String Id_ServentiaTipoRelacionada;
	private String ServentiaTipoCodigoRelacionada;
	private String ServentiaTipoRelacionada;
	private String CodigoTemp;

//---------------------------------------------------------
	public ServentiaRelacionadaDtGen() {

		limpar();

	}

	public String getId()  {return Id_ServentiaRelacionada;}
	public void setId(String valor ) {if(valor!=null) Id_ServentiaRelacionada = valor;}
	public String getServentiaRelacionada()  {return ServentiaRelacionada;}
	public void setServentiaRelacionada(String valor ) {if (valor!=null) ServentiaRelacionada = valor;}
	public String getId_ServentiaPrincipal()  {return Id_ServentiaPrincipal;}
	public void setId_ServentiaPrincipal(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaPrincipal = ""; ServentiaPrincipal = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaPrincipal = valor;}
	public String getServentiaPrincipal()  {return ServentiaPrincipal;}
	public void setServentiaPrincipal(String valor ) {if (valor!=null) ServentiaPrincipal = valor;}
	public String getId_ServentiaRelacao()  {return Id_ServentiaRelacao;}
	public void setId_ServentiaRelacao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaRelacao = ""; ServentiaRelacao = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaRelacao = valor;}
	public String getServentiaRelacao()  {return ServentiaRelacao;}
	public void setServentiaRelacao(String valor ) {if (valor!=null) ServentiaRelacao = valor;}
	public String getId_ServentiaTipoRelacionada()  {return Id_ServentiaTipoRelacionada;}
	public void setId_ServentiaTipoRelacionada(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaTipoRelacionada = ""; ServentiaTipoCodigoRelacionada = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaTipoRelacionada = valor;}
	public String getServentiaTipoCodigoRelacionada()  {return ServentiaTipoCodigoRelacionada;}
	public void setServentiaTipoCodigoRelacionada(String valor ) {if (valor!=null) ServentiaTipoCodigoRelacionada = valor;}
	public String getServentiaTipoRelacionada()  {return ServentiaTipoRelacionada;}
	public void setServentiaTipoRelacionada(String valor ) {if (valor!=null) ServentiaTipoRelacionada = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ServentiaRelacionadaDt objeto){
		 if (objeto==null) return;
		Id_ServentiaRelacionada = objeto.getId();
		ServentiaRelacionada = objeto.getServentiaRelacionada();
		Id_ServentiaPrincipal = objeto.getId_ServentiaPrincipal();
		ServentiaPrincipal = objeto.getServentiaPrincipal();
		Id_ServentiaRelacao = objeto.getId_ServentiaRelacao();
		ServentiaRelacao = objeto.getServentiaRelacao();
		Id_ServentiaTipoRelacionada = objeto.getId_ServentiaTipoRelacionada();
		ServentiaTipoCodigoRelacionada = objeto.getServentiaTipoCodigoRelacionada();
		ServentiaTipoRelacionada = objeto.getServentiaTipoRelacionada();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ServentiaRelacionada="";
		ServentiaRelacionada="";
		Id_ServentiaPrincipal="";
		ServentiaPrincipal="";
		Id_ServentiaRelacao="";
		ServentiaRelacao="";
		Id_ServentiaTipoRelacionada="";
		ServentiaTipoCodigoRelacionada="";
		ServentiaTipoRelacionada="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ServentiaRelacionada:" + Id_ServentiaRelacionada + ";ServentiaRelacionada:" + ServentiaRelacionada + ";Id_ServentiaPrincipal:" + Id_ServentiaPrincipal + ";ServentiaPrincipal:" + ServentiaPrincipal + ";Id_ServentiaRelacao:" + Id_ServentiaRelacao + ";ServentiaRelacao:" + ServentiaRelacao + ";Id_ServentiaTipoRelacionada:" + Id_ServentiaTipoRelacionada + ";ServentiaTipoCodigoRelacionada:" + ServentiaTipoCodigoRelacionada + ";ServentiaTipoRelacionada:" + ServentiaTipoRelacionada + ";CodigoTemp:" + CodigoTemp + "]";
	}
} 
