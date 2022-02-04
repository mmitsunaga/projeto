package br.gov.go.tj.projudi.dt;

public class GuiaTipoDtGen extends Dados{

	private String Id_GuiaTipo;
    private static final long serialVersionUID = -4437032357105476443L;
	private String GuiaTipo;


	private String GuiaTipoCodigo;
	private String GuiaTipoCodigoExterno;
	private String Ativo;
	private String CodigoTemp;

//---------------------------------------------------------
	public GuiaTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_GuiaTipo;}
	public void setId(String valor ) {if(valor!=null) Id_GuiaTipo = valor;}
	public String getGuiaTipo()  {return GuiaTipo;}
	public void setGuiaTipo(String valor ) {if (valor!=null) GuiaTipo = valor;}
	public String getGuiaTipoCodigo()  {return GuiaTipoCodigo;}
	public void setGuiaTipoCodigo(String valor ) {if (valor!=null) GuiaTipoCodigo = valor;}
	public String getGuiaTipoCodigoExterno()  {return GuiaTipoCodigoExterno;}
	public void setGuiaTipoCodigoExterno(String valor ) {if (valor!=null) GuiaTipoCodigoExterno = valor;}
	public String getAtivo()  {return Ativo;}
	public void setAtivo(String valor ) {if (valor!=null) Ativo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(GuiaTipoDt objeto){
		 if (objeto==null) return;
		Id_GuiaTipo = objeto.getId();
		GuiaTipo = objeto.getGuiaTipo();
		GuiaTipoCodigo = objeto.getGuiaTipoCodigo();
		GuiaTipoCodigoExterno = objeto.getGuiaTipoCodigoExterno();
		Ativo = objeto.getAtivo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_GuiaTipo="";
		GuiaTipo="";
		GuiaTipoCodigo="";
		GuiaTipoCodigoExterno="";
		Ativo="1";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_GuiaTipo:" + Id_GuiaTipo + ";GuiaTipo:" + GuiaTipo + ";GuiaTipoCodigo:" + GuiaTipoCodigo + ";GuiaTipoCodigoExterno:" + GuiaTipoCodigoExterno + ";Ativo:" + Ativo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
