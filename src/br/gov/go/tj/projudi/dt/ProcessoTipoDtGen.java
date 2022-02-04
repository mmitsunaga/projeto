package br.gov.go.tj.projudi.dt;

public class ProcessoTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -6334732774297480247L;
    private String Id_ProcessoTipo;
	private String ProcessoTipo;


	private String ProcessoTipoCodigo;
	private String CodigoTemp;
	private String Publico;
	private String Ordem2Grau;

//---------------------------------------------------------
	public ProcessoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoTipo;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoTipo = valor;}
	public String getProcessoTipo()  {return ProcessoTipo;}
	public void setProcessoTipo(String valor ) {if (valor!=null) ProcessoTipo = valor;}
	public String getProcessoTipoCodigo()  {return ProcessoTipoCodigo;}
	public void setProcessoTipoCodigo(String valor ) {if (valor!=null) ProcessoTipoCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getPublico() {return Publico;}
	public void setPublico(String Publico) {this.Publico = Publico;}
	public String getOrdem2Grau()  {return Ordem2Grau;}
	public void setOrdem2Grau(String valor ) {if (valor!=null) Ordem2Grau = valor;}

	public void copiar(ProcessoTipoDt objeto){
		Id_ProcessoTipo = objeto.getId();
		ProcessoTipo = objeto.getProcessoTipo();
		ProcessoTipoCodigo = objeto.getProcessoTipoCodigo();
		CodigoTemp = objeto.getCodigoTemp();
		Publico = objeto.getPublico();
		Ordem2Grau = objeto.getOrdem2Grau();
	}

	public void limpar(){
		Id_ProcessoTipo="";
		ProcessoTipo="";
		ProcessoTipoCodigo="";
		CodigoTemp="";
		Publico = "";
		Ordem2Grau = "";
	}


	public String getPropriedades(){
		return "[Id_ProcessoTipo:" + Id_ProcessoTipo + ";ProcessoTipo:" + ProcessoTipo + ";ProcessoTipoCodigo:" + ProcessoTipoCodigo + ";Ordem2Grau:" + Ordem2Grau + ";Publico:" + Publico + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
