package br.gov.go.tj.projudi.dt;

public class GuiaModeloDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -4880634432148965978L;
    private String Id_GuiaModelo;
	private String GuiaModelo;


	private String GuiaModeloCodigo;
	private String Id_GuiaTipo;
	private String GuiaTipo;
	private String Id_ProcessoTipo;
	private String ProcessoTipo;
	private String Id_NaturezaSPG;
	private String NaturezaSPG;
	private String CodigoTemp;

//---------------------------------------------------------
	public GuiaModeloDtGen() {

		limpar();

	}

	public String getId()  {return Id_GuiaModelo;}
	public void setId(String valor ) {if(valor!=null) Id_GuiaModelo = valor;}
	public String getGuiaModelo()  {return GuiaModelo;}
	public void setGuiaModelo(String valor ) {if (valor!=null) GuiaModelo = valor;}
	public String getGuiaModeloCodigo()  {return GuiaModeloCodigo;}
	public void setGuiaModeloCodigo(String valor ) {if (valor!=null) GuiaModeloCodigo = valor;}
	public String getId_GuiaTipo()  {return Id_GuiaTipo;}
	public void setId_GuiaTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_GuiaTipo = ""; GuiaTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_GuiaTipo = valor;}
	public String getGuiaTipo()  {return GuiaTipo;}
	public void setGuiaTipo(String valor ) {if (valor!=null) GuiaTipo = valor;}
	public String getId_ProcessoTipo()  {return Id_ProcessoTipo;}
	public void setId_ProcessoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoTipo = ""; ProcessoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoTipo = valor;}
	public String getProcessoTipo()  {return ProcessoTipo;}
	public void setProcessoTipo(String valor ) {if (valor!=null) ProcessoTipo = valor;}
	public String getId_NaturezaSPG()  {return Id_NaturezaSPG;}
	public void setId_NaturezaSPG(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_NaturezaSPG = ""; NaturezaSPG = "";}else if (!valor.equalsIgnoreCase("")) Id_NaturezaSPG = valor;}
	public String getNaturezaSPG()  {return NaturezaSPG;}
	public void setNaturezaSPG(String valor ) {if (valor!=null) NaturezaSPG = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(GuiaModeloDt objeto){
		 if (objeto==null) return;
		Id_GuiaModelo = objeto.getId();
		GuiaModelo = objeto.getGuiaModelo();
		GuiaModeloCodigo = objeto.getGuiaModeloCodigo();
		Id_GuiaTipo = objeto.getId_GuiaTipo();
		GuiaTipo = objeto.getGuiaTipo();
		Id_ProcessoTipo = objeto.getId_ProcessoTipo();
		ProcessoTipo = objeto.getProcessoTipo();
		Id_NaturezaSPG = objeto.getId_NaturezaSPG();
		NaturezaSPG = objeto.getNaturezaSPG();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_GuiaModelo="";
		GuiaModelo="";
		GuiaModeloCodigo="";
		Id_GuiaTipo="";
		GuiaTipo="";
		Id_ProcessoTipo="";
		ProcessoTipo="";
		Id_NaturezaSPG="";
		NaturezaSPG="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_GuiaModelo:" + Id_GuiaModelo + ";GuiaModelo:" + GuiaModelo + ";GuiaModeloCodigo:" + GuiaModeloCodigo + ";Id_GuiaTipo:" + Id_GuiaTipo + ";GuiaTipo:" + GuiaTipo + ";Id_ProcessoTipo:" + Id_ProcessoTipo + ";ProcessoTipo:" + ProcessoTipo + ";Id_NaturezaSPG:" + Id_NaturezaSPG + ";NaturezaSPG:" + NaturezaSPG + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
