package br.gov.go.tj.projudi.dt;

public class PendenciaTipoRelacionadaDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 643285561154737754L;
	private String Id_PendenciaTipoRelacionada;
	private String PendenciaTipoPrincipal;

	private String Id_PendenciaTipoPrincipal;

	private String Id_PendenciaTipoRelacao;
	private String PendenciaTipoRelacao;
	private String CodigoTemp;
	private String PendenciaTipoCodigoPrincipal;
	private String PendenciaTipoCodigoRelacao;

//---------------------------------------------------------
	public PendenciaTipoRelacionadaDtGen() {

		limpar();

	}

	public String getId()  {return Id_PendenciaTipoRelacionada;}
	public void setId(String valor ) {if(valor!=null) Id_PendenciaTipoRelacionada = valor;}
	public String getPendenciaTipoPrincipal()  {return PendenciaTipoPrincipal;}
	public void setPendenciaTipoPrincipal(String valor ) {if (valor!=null) PendenciaTipoPrincipal = valor;}
	public String getId_PendenciaTipoPrincipal()  {return Id_PendenciaTipoPrincipal;}
	public void setId_PendenciaTipoPrincipal(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_PendenciaTipoPrincipal = ""; PendenciaTipoPrincipal = "";}else if (!valor.equalsIgnoreCase("")) Id_PendenciaTipoPrincipal = valor;}
	public String getId_PendenciaTipoRelacao()  {return Id_PendenciaTipoRelacao;}
	public void setId_PendenciaTipoRelacao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_PendenciaTipoRelacao = ""; PendenciaTipoRelacao = "";}else if (!valor.equalsIgnoreCase("")) Id_PendenciaTipoRelacao = valor;}
	public String getPendenciaTipoRelacao()  {return PendenciaTipoRelacao;}
	public void setPendenciaTipoRelacao(String valor ) {if (valor!=null) PendenciaTipoRelacao = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getPendenciaTipoCodigoPrincipal()  {return PendenciaTipoCodigoPrincipal;}
	public void setPendenciaTipoCodigoPrincipal(String valor ) {if (valor!=null) PendenciaTipoCodigoPrincipal = valor;}
	public String getPendenciaTipoCodigoRelacao()  {return PendenciaTipoCodigoRelacao;}
	public void setPendenciaTipoCodigoRelacao(String valor ) {if (valor!=null) PendenciaTipoCodigoRelacao = valor;}


	public void copiar(PendenciaTipoRelacionadaDt objeto){
		 if (objeto==null) return;
		Id_PendenciaTipoRelacionada = objeto.getId();
		Id_PendenciaTipoPrincipal = objeto.getId_PendenciaTipoPrincipal();
		PendenciaTipoPrincipal = objeto.getPendenciaTipoPrincipal();
		Id_PendenciaTipoRelacao = objeto.getId_PendenciaTipoRelacao();
		PendenciaTipoRelacao = objeto.getPendenciaTipoRelacao();
		CodigoTemp = objeto.getCodigoTemp();
		PendenciaTipoCodigoPrincipal = objeto.getPendenciaTipoCodigoPrincipal();
		PendenciaTipoCodigoRelacao = objeto.getPendenciaTipoCodigoRelacao();
	}

	public void limpar(){
		Id_PendenciaTipoRelacionada="";
		Id_PendenciaTipoPrincipal="";
		PendenciaTipoPrincipal="";
		Id_PendenciaTipoRelacao="";
		PendenciaTipoRelacao="";
		CodigoTemp="";
		PendenciaTipoCodigoPrincipal="";
		PendenciaTipoCodigoRelacao="";
	}


	public String getPropriedades(){
		return "[Id_PendenciaTipoRelacionada:" + Id_PendenciaTipoRelacionada + ";Id_PendenciaTipoPrincipal:" + Id_PendenciaTipoPrincipal + ";PendenciaTipoPrincipal:" + PendenciaTipoPrincipal + ";Id_PendenciaTipoRelacao:" + Id_PendenciaTipoRelacao + ";PendenciaTipoRelacao:" + PendenciaTipoRelacao + ";CodigoTemp:" + CodigoTemp + ";PendenciaTipoCodigoPrincipal:" + PendenciaTipoCodigoPrincipal + ";PendenciaTipoCodigoRelacao:" + PendenciaTipoCodigoRelacao + "]";
	}


} 
