package br.gov.go.tj.projudi.dt;

public class ForumDtGen extends Dados{

    private static final long serialVersionUID = 6855006651568618172L;
    private String Id_Forum;
	private String Forum;
	private String ForumCodigo;
	private String Id_Comarca;
	private String Comarca;
	private String Id_Endereco;
    private String CodigoTemp;
	private String ComarcaCodigo;

	public ForumDtGen() {

		limpar();

	}

	public String getId()  {return Id_Forum;}
	public void setId(String valor ) {if(valor!=null) Id_Forum = valor;}
	public String getForum()  {return Forum;}
	public void setForum(String valor ) {if (valor!=null) Forum = valor;}
	public String getForumCodigo()  {return ForumCodigo;}
	public void setForumCodigo(String valor ) {if (valor!=null) ForumCodigo = valor;}
	public String getId_Comarca()  {return Id_Comarca;}
	public void setId_Comarca(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Comarca = ""; Comarca = "";}else if (!valor.equalsIgnoreCase("")) Id_Comarca = valor;}
	public String getComarca()  {return Comarca;}
	public void setComarca(String valor ) {if (valor!=null) Comarca = valor;}
	public String getId_Endereco()  {return Id_Endereco;}
	public void setId_Endereco(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Endereco = "";}else if (!valor.equalsIgnoreCase("")) Id_Endereco = valor;}
	
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getComarcaCodigo()  {return ComarcaCodigo;}
	public void setComarcaCodigo(String valor ) {if (valor!=null) ComarcaCodigo = valor;}


	public void copiar(ForumDt objeto){
		Id_Forum = objeto.getId();
		Forum = objeto.getForum();
		ForumCodigo = objeto.getForumCodigo();
		Id_Comarca = objeto.getId_Comarca();
		Comarca = objeto.getComarca();
		Id_Endereco = objeto.getId_Endereco();
		CodigoTemp = objeto.getCodigoTemp();
		ComarcaCodigo = objeto.getComarcaCodigo();		
	}

	public void limpar(){
		Id_Forum="";
		Forum="";
		ForumCodigo="";
		Id_Comarca="";
		Comarca="";
		Id_Endereco="";
		CodigoTemp="";
		ComarcaCodigo="";		
	}


	public String getPropriedades(){
		return "[Id_Forum:" + Id_Forum + ";Forum:" + Forum + ";ForumCodigo:" + ForumCodigo + ";Id_Comarca:" + Id_Comarca + ";Comarca:" + Comarca + ";Id_Endereco:" + Id_Endereco + ";CodigoTemp:" + CodigoTemp + ";ComarcaCodigo:" + ComarcaCodigo + "]";
	}
	
} 
