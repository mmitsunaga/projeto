package br.gov.go.tj.projudi.dt;

public class UsuarioServentiaDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -8995910592099911628L;
    private String Id_UsuarioServentia;
	private String UsuarioServentia;
	private String Id_Usuario;
	private String Usuario;
	private String Id_Serventia;
	private String Serventia;
	private String Id_Comarca;
	private String Comarca;
	private String Id_EnderecoServentia;
	private String EnderecoServentia;
	private String Id_BairroServentia;
	private String BairroServentia;
	private String Id_CidadeServentia;
	private String CidadeServentia;
	private String Ativo;
	private String ServentiaCodigo;
	private String ServentiaCodigoExterno;
	private String ComarcaCodigo;
	private String BairroCodigoServentia;
	private String CidadeCodigoServentia;
	private String Nome;
	private String Id_UsuarioServentiaChefe;
	private String UsuarioServentiaChefe;
	

//---------------------------------------------------------
	public UsuarioServentiaDtGen() {

		limpar();

	}

	public String getId()  {return Id_UsuarioServentia;}
	public void setId(String valor ) {if(valor!=null) Id_UsuarioServentia = valor;}
	public String getUsuarioServentia()  {return UsuarioServentia;}
	public void setUsuarioServentia(String valor ) {if (valor!=null) UsuarioServentia = valor;}
	public String getId_Usuario()  {return Id_Usuario;}
	public void setId_Usuario(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Usuario = ""; Usuario = "";}else if (!valor.equalsIgnoreCase("")) Id_Usuario = valor;}
	public String getUsuario()  {return Usuario;}
	public void setUsuario(String valor ) {if (valor!=null) Usuario = valor;}
	public String getId_Serventia()  {return Id_Serventia;}
	public void setId_Serventia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Serventia = ""; Serventia = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia = valor;}
	public String getServentia()  {return Serventia;}
	public void setServentia(String valor ) {if (valor!=null) Serventia = valor;}
	public String getId_Comarca()  {return Id_Comarca;}
	public void setId_Comarca(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Comarca = ""; Comarca = "";}else if (!valor.equalsIgnoreCase("")) Id_Comarca = valor;}
	public String getComarca()  {return Comarca;}
	public void setComarca(String valor ) {if (valor!=null) Comarca = valor;}
	public String getId_EnderecoServentia()  {return Id_EnderecoServentia;}
	public void setId_EnderecoServentia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_EnderecoServentia = ""; EnderecoServentia = "";}else if (!valor.equalsIgnoreCase("")) Id_EnderecoServentia = valor;}
	public String getEnderecoServentia()  {return EnderecoServentia;}
	public void setEnderecoServentia(String valor ) {if (valor!=null) EnderecoServentia = valor;}
	public String getId_BairroServentia()  {return Id_BairroServentia;}
	public void setId_BairroServentia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_BairroServentia = ""; BairroServentia = "";}else if (!valor.equalsIgnoreCase("")) Id_BairroServentia = valor;}
	public String getBairroServentia()  {return BairroServentia;}
	public void setBairroServentia(String valor ) {if (valor!=null) BairroServentia = valor;}
	public String getId_CidadeServentia()  {return Id_CidadeServentia;}
	public void setId_CidadeServentia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_CidadeServentia = ""; CidadeServentia = "";}else if (!valor.equalsIgnoreCase("")) Id_CidadeServentia = valor;}
	public String getCidadeServentia()  {return CidadeServentia;}
	public void setCidadeServentia(String valor ) {if (valor!=null) CidadeServentia = valor;}
	public String getAtivo()  {return Ativo;}
	public void setAtivo(String valor ) {if (valor!=null) Ativo = valor;}
	public String getServentiaCodigo()  {return ServentiaCodigo;}
	public void setServentiaCodigo(String valor ) {if (valor!=null) ServentiaCodigo = valor;}
	public String getServentiaCodigoExterno()  {return ServentiaCodigoExterno;}
	public void setServentiaCodigoExterno(String valor ) {if (valor!=null) ServentiaCodigoExterno = valor;}
	public String getComarcaCodigo()  {return ComarcaCodigo;}
	public void setComarcaCodigo(String valor ) {if (valor!=null) ComarcaCodigo = valor;}
	public String getBairroCodigoServentia()  {return BairroCodigoServentia;}
	public void setBairroCodigoServentia(String valor ) {if (valor!=null) BairroCodigoServentia = valor;}
	public String getCidadeCodigoServentia()  {return CidadeCodigoServentia;}
	public void setCidadeCodigoServentia(String valor ) {if (valor!=null) CidadeCodigoServentia = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) {if (valor!=null) Nome = valor;}
	public String getId_UsuarioServentiaChefe()  {return Id_UsuarioServentiaChefe;}
	public void setId_UsuarioServentiaChefe(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioServentiaChefe = ""; UsuarioServentiaChefe = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioServentiaChefe = valor;}
	public String getUsuarioServentiaChefe()  {return UsuarioServentiaChefe;}
	public void setUsuarioServentiaChefe(String valor ) {if (valor!=null) UsuarioServentiaChefe = valor;}	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	public void copiar(UsuarioServentiaDt objeto){
		Id_UsuarioServentia = objeto.getId();
		UsuarioServentia = objeto.getUsuarioServentia();
		Id_Usuario = objeto.getId_Usuario();
		Usuario = objeto.getUsuario();
		Id_Serventia = objeto.getId_Serventia();
		Serventia = objeto.getServentia();
		Id_Comarca = objeto.getId_Comarca();
		Comarca = objeto.getComarca();
		Id_EnderecoServentia = objeto.getId_EnderecoServentia();
		EnderecoServentia = objeto.getEnderecoServentia();
		Id_BairroServentia = objeto.getId_BairroServentia();
		BairroServentia = objeto.getBairroServentia();
		Id_CidadeServentia = objeto.getId_CidadeServentia();
		CidadeServentia = objeto.getCidadeServentia();
		Ativo = objeto.getAtivo();
		ServentiaCodigo = objeto.getServentiaCodigo();
		ServentiaCodigoExterno = objeto.getServentiaCodigoExterno();
		ComarcaCodigo = objeto.getComarcaCodigo();
		BairroCodigoServentia = objeto.getBairroCodigoServentia();
		CidadeCodigoServentia = objeto.getCidadeCodigoServentia();
		Nome = objeto.getNome();
		Id_UsuarioServentiaChefe = objeto.getId_UsuarioServentiaChefe();
		UsuarioServentiaChefe = objeto.getUsuarioServentiaChefe();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_UsuarioServentia="";
		UsuarioServentia="";
		Id_Usuario="";
		Usuario="";
		Id_Serventia="";
		Serventia="";
		Id_Comarca="";
		Comarca="";
		Id_EnderecoServentia="";
		EnderecoServentia="";
		Id_BairroServentia="";
		BairroServentia="";
		Id_CidadeServentia="";
		CidadeServentia="";
		Ativo="";
		ServentiaCodigo="";
		ServentiaCodigoExterno="";
		ComarcaCodigo="";
		BairroCodigoServentia="";
		CidadeCodigoServentia="";
		Nome="";
		Id_UsuarioServentiaChefe="";
		UsuarioServentiaChefe="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_UsuarioServentia:" + Id_UsuarioServentia + ";UsuarioServentia:" + UsuarioServentia + ";Id_Usuario:" + Id_Usuario + ";Usuario:" + Usuario + ";Id_Serventia:" + Id_Serventia + ";Serventia:" + Serventia + ";Id_Comarca:" + Id_Comarca + ";Comarca:" + Comarca + ";Id_EnderecoServentia:" + Id_EnderecoServentia + ";EnderecoServentia:" + EnderecoServentia + ";Id_BairroServentia:" + Id_BairroServentia + ";BairroServentia:" + BairroServentia + ";Id_CidadeServentia:" + Id_CidadeServentia + ";CidadeServentia:" + CidadeServentia + ";Ativo:" + Ativo + ";ServentiaCodigo:" + ServentiaCodigo + ";ServentiaCodigoExterno:" + ServentiaCodigoExterno + ";ComarcaCodigo:" + ComarcaCodigo + ";BairroCodigoServentia:" + BairroCodigoServentia + ";CidadeCodigoServentia:" + CidadeCodigoServentia + ";Nome:" + Nome + ";Id_UsuarioServentiaChefe:" + Id_UsuarioServentiaChefe + ";UsuarioServentiaChefe:" + UsuarioServentiaChefe + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
