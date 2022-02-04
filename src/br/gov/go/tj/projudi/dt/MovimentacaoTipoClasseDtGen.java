package br.gov.go.tj.projudi.dt;

public class MovimentacaoTipoClasseDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4023019004052512989L;
	private String Id_MovimentacaoTipoClasse;
	private String MovimentacaoTipoClasse;


	private String MovimentacaoTipoClasseCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public MovimentacaoTipoClasseDtGen() {

		limpar();

	}

	public String getId()  {return Id_MovimentacaoTipoClasse;}
	public void setId(String valor ) {if(valor!=null) Id_MovimentacaoTipoClasse = valor;}
	public String getMovimentacaoTipoClasse()  {return MovimentacaoTipoClasse;}
	public void setMovimentacaoTipoClasse(String valor ) {if (valor!=null) MovimentacaoTipoClasse = valor;}
	public String getMovimentacaoTipoClasseCodigo()  {return MovimentacaoTipoClasseCodigo;}
	public void setMovimentacaoTipoClasseCodigo(String valor ) {if (valor!=null) MovimentacaoTipoClasseCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(MovimentacaoTipoClasseDt objeto){
		 if (objeto==null) return;
		Id_MovimentacaoTipoClasse = objeto.getId();
		MovimentacaoTipoClasse = objeto.getMovimentacaoTipoClasse();
		MovimentacaoTipoClasseCodigo = objeto.getMovimentacaoTipoClasseCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_MovimentacaoTipoClasse="";
		MovimentacaoTipoClasse="";
		MovimentacaoTipoClasseCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_MovimentacaoTipoClasse:" + Id_MovimentacaoTipoClasse + ";MovimentacaoTipoClasse:" + MovimentacaoTipoClasse + ";MovimentacaoTipoClasseCodigo:" + MovimentacaoTipoClasseCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
