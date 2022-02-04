package br.gov.go.tj.projudi.dt;

public class AreaDistribuicaoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 110353630781141185L;
    private String Id_AreaDistribuicao;
	private String AreaDistribuicao;


	private String AreaDistribuicaoCodigo;
	private String Id_Forum;
	private String Forum;
	private String Id_ServentiaSubtipo;
	private String ServentiaSubtipo;
	private String Id_AreaDistribuicaoRelacionada;
	private String AreaDistribuicaoRelacionada;
	private String CodigoTemp;
	private String ComarcaCodigo;
	private String ServentiaSubtipoCodigo;
	private String Id_Comarca;
	private String Comarca;
	private String ForumCodigo;

//---------------------------------------------------------
	public AreaDistribuicaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_AreaDistribuicao;}
	public void setId(String valor ) {if(valor!=null) Id_AreaDistribuicao = valor;}
	public String getAreaDistribuicao()  {return AreaDistribuicao;}
	public void setAreaDistribuicao(String valor ) {if (valor!=null) AreaDistribuicao = valor;}
	public String getAreaDistribuicaoCodigo()  {return AreaDistribuicaoCodigo;}
	public void setAreaDistribuicaoCodigo(String valor ) {if (valor!=null) AreaDistribuicaoCodigo = valor;}
	public String getId_Forum()  {return Id_Forum;}
	public void setId_Forum(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Forum = ""; Forum = "";}else if (!valor.equalsIgnoreCase("")) Id_Forum = valor;}
	public String getForum()  {return Forum;}
	public void setForum(String valor ) {if (valor!=null) Forum = valor;}
	public String getId_ServentiaSubtipo()  {return Id_ServentiaSubtipo;}
	public void setId_ServentiaSubtipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaSubtipo = ""; ServentiaSubtipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaSubtipo = valor;}
	public String getServentiaSubtipo()  {return ServentiaSubtipo;}
	public void setServentiaSubtipo(String valor ) {if (valor!=null) ServentiaSubtipo = valor;}
	public String getId_AreaDistribuicaoRelacionada()  {return Id_AreaDistribuicaoRelacionada;}
	public void setId_AreaDistribuicaoRelacionada(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_AreaDistribuicaoRelacionada = ""; AreaDistribuicaoRelacionada = "";}else if (!valor.equalsIgnoreCase("")) Id_AreaDistribuicaoRelacionada = valor;}
	public String getAreaDistribuicaoRelacionada()  {return AreaDistribuicaoRelacionada;}
	public void setAreaDistribuicaoRelacionada(String valor ) {if (valor!=null) AreaDistribuicaoRelacionada = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getComarcaCodigo()  {return ComarcaCodigo;}
	public void setComarcaCodigo(String valor ) {if (valor!=null) ComarcaCodigo = valor;}
	public String getServentiaSubtipoCodigo()  {return ServentiaSubtipoCodigo;}
	public void setServentiaSubtipoCodigo(String valor ) {if (valor!=null) ServentiaSubtipoCodigo = valor;}
	public String getId_Comarca()  {return Id_Comarca;}
	public void setId_Comarca(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Comarca = ""; Comarca = "";}else if (!valor.equalsIgnoreCase("")) Id_Comarca = valor;}
	public String getComarca()  {return Comarca;}
	public void setComarca(String valor ) {if (valor!=null) Comarca = valor;}
	public String getForumCodigo()  {return ForumCodigo;}
	public void setForumCodigo(String valor ) {if (valor!=null) ForumCodigo = valor;}


	public void copiar(AreaDistribuicaoDt objeto){
		if (objeto != null){
			Id_AreaDistribuicao = objeto.getId();
			AreaDistribuicao = objeto.getAreaDistribuicao();
			AreaDistribuicaoCodigo = objeto.getAreaDistribuicaoCodigo();
			Id_Forum = objeto.getId_Forum();
			Forum = objeto.getForum();
			Id_ServentiaSubtipo = objeto.getId_ServentiaSubtipo();
			ServentiaSubtipo = objeto.getServentiaSubtipo();
			Id_AreaDistribuicaoRelacionada = objeto.getId_AreaDistribuicaoRelacionada();
			AreaDistribuicaoRelacionada = objeto.getAreaDistribuicaoRelacionada();
			CodigoTemp = objeto.getCodigoTemp();
			ComarcaCodigo = objeto.getComarcaCodigo();
			ServentiaSubtipoCodigo = objeto.getServentiaSubtipoCodigo();
			Id_Comarca = objeto.getId_Comarca();
			Comarca = objeto.getComarca();
			ForumCodigo = objeto.getForumCodigo();
		}
	}

	public void limpar(){
		Id_AreaDistribuicao="";
		AreaDistribuicao="";
		AreaDistribuicaoCodigo="";
		Id_Forum="";
		Forum="";
		Id_ServentiaSubtipo="";
		ServentiaSubtipo="";
		Id_AreaDistribuicaoRelacionada="";
		AreaDistribuicaoRelacionada="";
		CodigoTemp="";
		ComarcaCodigo="";
		ServentiaSubtipoCodigo="";
		Id_Comarca="";
		Comarca="";
		ForumCodigo="";
	}


	public String getPropriedades(){
		return "[Id_AreaDistribuicao:" + Id_AreaDistribuicao + ";AreaDistribuicao:" + AreaDistribuicao + ";AreaDistribuicaoCodigo:" + AreaDistribuicaoCodigo + ";Id_Forum:" + Id_Forum + ";Forum:" + Forum + ";Id_ServentiaSubtipo:" + Id_ServentiaSubtipo + ";ServentiaSubtipo:" + ServentiaSubtipo + ";Id_AreaDistribuicaoRelacionada:" + Id_AreaDistribuicaoRelacionada + ";AreaDistribuicaoRelacionada:" + AreaDistribuicaoRelacionada + ";CodigoTemp:" + CodigoTemp + ";ComarcaCodigo:" + ComarcaCodigo + ";ServentiaSubtipoCodigo:" + ServentiaSubtipoCodigo + ";Id_Comarca:" + Id_Comarca + ";Comarca:" + Comarca + ";ForumCodigo:" + ForumCodigo + "]";
	}


} 
