package br.gov.go.tj.projudi.dt;

public class LogTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 3208886732683670648L;
    private String Id_LogTipo;
	private String LogTipo;


	private String LogTipoCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public LogTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_LogTipo;}
	public void setId(String valor ) {if(valor!=null) Id_LogTipo = valor;}
	public String getLogTipo()  {return LogTipo;}
	public void setLogTipo(String valor ) {if (valor!=null) LogTipo = valor;}
	public String getLogTipoCodigo()  {return LogTipoCodigo;}
	public void setLogTipoCodigo(String valor ) {if (valor!=null) LogTipoCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(LogTipoDt objeto){
		Id_LogTipo = objeto.getId();
		LogTipo = objeto.getLogTipo();
		LogTipoCodigo = objeto.getLogTipoCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_LogTipo="";
		LogTipo="";
		LogTipoCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_LogTipo:" + Id_LogTipo + ";LogTipo:" + LogTipo + ";LogTipoCodigo:" + LogTipoCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
