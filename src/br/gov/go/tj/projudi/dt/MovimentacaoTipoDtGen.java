package br.gov.go.tj.projudi.dt;

public class MovimentacaoTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 6695982308633374512L;
    protected String Id_MovimentacaoTipo;
	protected String MovimentacaoTipo;
	
	


	protected String MovimentacaoTipoCodigo;
	

//---------------------------------------------------------
	public MovimentacaoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_MovimentacaoTipo;}
	public void setId(String valor ) {if(valor!=null) Id_MovimentacaoTipo = valor;}
	public String getMovimentacaoTipo()  {return MovimentacaoTipo;}
	public void setMovimentacaoTipo(String valor ) {if (valor!=null) MovimentacaoTipo = valor;}
	public String getMovimentacaoTipoCodigo()  {return MovimentacaoTipoCodigo;}
	public void setMovimentacaoTipoCodigo(String valor ) {if (valor!=null) MovimentacaoTipoCodigo = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	
	
	


	public void copiar(MovimentacaoTipoDt objeto){
		Id_MovimentacaoTipo = objeto.getId();
		MovimentacaoTipo = objeto.getMovimentacaoTipo();
		MovimentacaoTipoCodigo = objeto.getMovimentacaoTipoCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_MovimentacaoTipo="";
		MovimentacaoTipo="";
		MovimentacaoTipoCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_MovimentacaoTipo:" + Id_MovimentacaoTipo + ";MovimentacaoTipo:" + MovimentacaoTipo + ";MovimentacaoTipoCodigo:" + MovimentacaoTipoCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
