package br.gov.go.tj.projudi.dt;

public class PermissaoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -9078850872769534643L;
    private String Id_Permissao;
	private String Permissao;
	
	


	private String PermissaoCodigo;
	private String EMenu;
	private String Link;
	private String IrPara;
	private String Titulo;
	private String Id_PermissaoPai;
	private String PermissaoPai;
	private String Id_PermissaoEspecial;
	private String PermissaoEspecial;
	private String Ordenacao;
	
	private String PermissaoCodigoPai;
	private String PermissaoEspecialCodigo;

//---------------------------------------------------------
	public PermissaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_Permissao;}
	public void setId(String valor ) {if(valor!=null) Id_Permissao = valor;}
	public String getPermissao()  {return Permissao;}
	public void setPermissao(String valor ) {if (valor!=null) Permissao = valor;}
	public String getPermissaoCodigo()  {return PermissaoCodigo;}
	public void setPermissaoCodigo(String valor ) {if (valor!=null) PermissaoCodigo = valor;}
	public String getEMenu()  {return EMenu;}
	public void setEMenu(String valor ) {if (valor!=null) EMenu = valor;}
	public String getLink()  {return Link;}
	public void setLink(String valor ) {if (valor!=null) Link = valor;}
	public String getIrPara()  {return IrPara;}
	public void setIrPara(String valor ) {if (valor!=null) IrPara = valor;}
	public String getTitulo()  {return Titulo;}
	public void setTitulo(String valor ) {if (valor!=null) Titulo = valor;}
	public String getId_PermissaoPai()  {return Id_PermissaoPai;}
	public void setId_PermissaoPai(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_PermissaoPai = ""; PermissaoPai = "";}else if (!valor.equalsIgnoreCase("")) Id_PermissaoPai = valor;}
	public String getPermissaoPai()  {return PermissaoPai;}
	public void setPermissaoPai(String valor ) {if (valor!=null) PermissaoPai = valor;}
	public String getId_PermissaoEspecial()  {return Id_PermissaoEspecial;}
	public void setId_PermissaoEspecial(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_PermissaoEspecial = ""; PermissaoEspecial = "";}else if (!valor.equalsIgnoreCase("")) Id_PermissaoEspecial = valor;}
	public String getPermissaoEspecial()  {return PermissaoEspecial;}
	public void setPermissaoEspecial(String valor ) {if (valor!=null) PermissaoEspecial = valor;}
	public String getOrdenacao()  {return Ordenacao;}
	public void setOrdenacao(String valor ) {if (valor!=null) Ordenacao = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getPermissaoCodigoPai()  {return PermissaoCodigoPai;}
	public void setPermissaoCodigoPai(String valor ) {if (valor!=null) PermissaoCodigoPai = valor;}
	public String getPermissaoEspecialCodigo()  {return PermissaoEspecialCodigo;}
	public void setPermissaoEspecialCodigo(String valor ) {if (valor!=null) PermissaoEspecialCodigo = valor;}
	
	
	
	


	public void copiar(PermissaoDt objeto){
		Id_Permissao = objeto.getId();
		Permissao = objeto.getPermissao();
		PermissaoCodigo = objeto.getPermissaoCodigo();
		EMenu = objeto.getEMenu();
		Link = objeto.getLink();
		IrPara = objeto.getIrPara();
		Titulo = objeto.getTitulo();
		Id_PermissaoPai = objeto.getId_PermissaoPai();
		PermissaoPai = objeto.getPermissaoPai();
		Id_PermissaoEspecial = objeto.getId_PermissaoEspecial();
		PermissaoEspecial = objeto.getPermissaoEspecial();
		Ordenacao = objeto.getOrdenacao();
		CodigoTemp = objeto.getCodigoTemp();
		PermissaoCodigoPai = objeto.getPermissaoCodigoPai();
		PermissaoEspecialCodigo = objeto.getPermissaoEspecialCodigo();
	}

	public void limpar(){
		Id_Permissao="";
		Permissao="";
		PermissaoCodigo="";
		EMenu="";
		Link="";
		IrPara="";
		Titulo="";
		Id_PermissaoPai="";
		PermissaoPai="";
		Id_PermissaoEspecial="";
		PermissaoEspecial="";
		Ordenacao="";
		CodigoTemp="";
		PermissaoCodigoPai="";
		PermissaoEspecialCodigo="";
	}


	public String getPropriedades(){
		return "[Id_Permissao:" + Id_Permissao + ";Permissao:" + Permissao + ";PermissaoCodigo:" + PermissaoCodigo + ";EMenu:" + EMenu + ";Link:" + Link + ";IrPara:" + IrPara + ";Titulo:" + Titulo + ";Id_PermissaoPai:" + Id_PermissaoPai + ";PermissaoPai:" + PermissaoPai + ";Id_PermissaoEspecial:" + Id_PermissaoEspecial + ";PermissaoEspecial:" + PermissaoEspecial + ";Ordenacao:" + Ordenacao + ";CodigoTemp:" + CodigoTemp + ";PermissaoCodigoPai:" + PermissaoCodigoPai + ";PermissaoEspecialCodigo:" + PermissaoEspecialCodigo + "]";
	}


} 
