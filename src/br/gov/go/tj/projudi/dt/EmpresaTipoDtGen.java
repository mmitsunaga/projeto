package br.gov.go.tj.projudi.dt;

public class EmpresaTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -3785443599536761229L;
    private String Id_EmpresaTipo;
	private String EmpresaTipo;

	private String EmpresaTipoCodigo;

	private String CodigoTemp;

//---------------------------------------------------------
	public EmpresaTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_EmpresaTipo;}
	public void setId(String valor ) {if(valor!=null) Id_EmpresaTipo = valor;}
	public String getEmpresaTipo()  {return EmpresaTipo;}
	public void setEmpresaTipo(String valor ) {if (valor!=null) EmpresaTipo = valor;}
	public String getEmpresaTipoCodigo()  {return EmpresaTipoCodigo;}
	public void setEmpresaTipoCodigo(String valor ) {if (valor!=null) EmpresaTipoCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(EmpresaTipoDt objeto){
		 if (objeto==null) return;
		Id_EmpresaTipo = objeto.getId();
		EmpresaTipoCodigo = objeto.getEmpresaTipoCodigo();
		EmpresaTipo = objeto.getEmpresaTipo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_EmpresaTipo="";
		EmpresaTipoCodigo="";
		EmpresaTipo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_EmpresaTipo:" + Id_EmpresaTipo + ";EmpresaTipoCodigo:" + EmpresaTipoCodigo + ";EmpresaTipo:" + EmpresaTipo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
