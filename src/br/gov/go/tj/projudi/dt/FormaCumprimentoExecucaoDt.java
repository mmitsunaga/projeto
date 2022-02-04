package br.gov.go.tj.projudi.dt;

public class FormaCumprimentoExecucaoDt extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 6499490171521947504L;
    public static final int CodigoPermissao = 815;
    
    private String Id_FormaCumprimentoExecucao;
	private String FormaCumprimentoExecucao;
	private String FormaCumprimentoExecucaoCodigo;
	private String CodigoTemp;

	public FormaCumprimentoExecucaoDt() {

		limpar();

	}

	public String getId()  {return Id_FormaCumprimentoExecucao;}
	public void setId(String valor ) {if(valor!=null) Id_FormaCumprimentoExecucao = valor;}
	public String getFormaCumprimentoExecucao()  {return FormaCumprimentoExecucao;}
	public void setFormaCumprimentoExecucao(String valor ) {if (valor!=null) FormaCumprimentoExecucao = valor;}
	public String getFormaCumprimentoExecucaoCodigo()  {return FormaCumprimentoExecucaoCodigo;}
	public void setFormaCumprimentoExecucaoCodigo(String valor ) {if (valor!=null) FormaCumprimentoExecucaoCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(FormaCumprimentoExecucaoDt objeto){
		Id_FormaCumprimentoExecucao = objeto.getId();
		FormaCumprimentoExecucao = objeto.getFormaCumprimentoExecucao();
		FormaCumprimentoExecucaoCodigo = objeto.getFormaCumprimentoExecucaoCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_FormaCumprimentoExecucao="";
		FormaCumprimentoExecucao="";
		FormaCumprimentoExecucaoCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_FormaCumprimentoExecucao:" + Id_FormaCumprimentoExecucao + ";FormaCumprimentoExecucao:" + FormaCumprimentoExecucao + ";FormaCumprimentoExecucaoCodigo:" + FormaCumprimentoExecucaoCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
