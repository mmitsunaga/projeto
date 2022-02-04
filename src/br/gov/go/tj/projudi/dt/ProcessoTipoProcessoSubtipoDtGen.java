package br.gov.go.tj.projudi.dt;

public class ProcessoTipoProcessoSubtipoDtGen extends Dados{

	private String Id_ProcessoTipoProcessoSubtipo;
    private static final long serialVersionUID = 5755522749418452120L;
	private String ProcessoSubtipo;

	private String Id_ProcessoSubtipo;

	private String Id_ProcessoTipo;
	private String ProcessoTipo;
	private String CodigoTemp;
	private String ProcessoSubtipoCodigo;
	private String ProcessoTipoCodigo;

//---------------------------------------------------------
	public ProcessoTipoProcessoSubtipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoTipoProcessoSubtipo;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoTipoProcessoSubtipo = valor;}
	public String getProcessoSubtipo()  {return ProcessoSubtipo;}
	public void setProcessoSubtipo(String valor ) {if (valor!=null) ProcessoSubtipo = valor;}
	public String getId_ProcessoSubtipo()  {return Id_ProcessoSubtipo;}
	public void setId_ProcessoSubtipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoSubtipo = ""; ProcessoSubtipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoSubtipo = valor;}
	public String getId_ProcessoTipo()  {return Id_ProcessoTipo;}
	public void setId_ProcessoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoTipo = ""; ProcessoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoTipo = valor;}
	public String getProcessoTipo()  {return ProcessoTipo;}
	public void setProcessoTipo(String valor ) {if (valor!=null) ProcessoTipo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getProcessoSubtipoCodigo()  {return ProcessoSubtipoCodigo;}
	public void setProcessoSubtipoCodigo(String valor ) {if (valor!=null) ProcessoSubtipoCodigo = valor;}
	public String getProcessoTipoCodigo()  {return ProcessoTipoCodigo;}
	public void setProcessoTipoCodigo(String valor ) {if (valor!=null) ProcessoTipoCodigo = valor;}


	public void copiar(ProcessoTipoProcessoSubtipoDt objeto){
		 if (objeto==null) return;
		Id_ProcessoTipoProcessoSubtipo = objeto.getId();
		Id_ProcessoSubtipo = objeto.getId_ProcessoSubtipo();
		ProcessoSubtipo = objeto.getProcessoSubtipo();
		Id_ProcessoTipo = objeto.getId_ProcessoTipo();
		ProcessoTipo = objeto.getProcessoTipo();
		CodigoTemp = objeto.getCodigoTemp();
		ProcessoSubtipoCodigo = objeto.getProcessoSubtipoCodigo();
		ProcessoTipoCodigo = objeto.getProcessoTipoCodigo();
	}

	public void limpar(){
		Id_ProcessoTipoProcessoSubtipo="";
		Id_ProcessoSubtipo="";
		ProcessoSubtipo="";
		Id_ProcessoTipo="";
		ProcessoTipo="";
		CodigoTemp="";
		ProcessoSubtipoCodigo="";
		ProcessoTipoCodigo="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoTipoProcessoSubtipo:" + Id_ProcessoTipoProcessoSubtipo + ";Id_ProcessoSubtipo:" + Id_ProcessoSubtipo + ";ProcessoSubtipo:" + ProcessoSubtipo + ";Id_ProcessoTipo:" + Id_ProcessoTipo + ";ProcessoTipo:" + ProcessoTipo + ";CodigoTemp:" + CodigoTemp + ";ProcessoSubtipoCodigo:" + ProcessoSubtipoCodigo + ";ProcessoTipoCodigo:" + ProcessoTipoCodigo + "]";
	}


} 
