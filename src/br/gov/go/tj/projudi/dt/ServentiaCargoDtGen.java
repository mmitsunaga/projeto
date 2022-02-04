package br.gov.go.tj.projudi.dt;

public class ServentiaCargoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 4227901157184189898L;
    protected String Id_ServentiaCargo;
    protected String ServentiaCargo;


    protected String Id_Serventia;
    protected String Serventia;
    protected String Id_CargoTipo;
    protected String CargoTipo;
    protected String Id_UsuarioServentiaGrupo;
    protected String UsuarioServentiaGrupo;
    protected String QuantidadeDistribuicao;
    protected String ServentiaCodigo;
    protected String CargoTipoCodigo;
    protected String ServentiaUsuario;
    protected String NomeUsuario;
    protected String GrupoUsuario;
    protected String GrupoUsuarioCodigo;
    protected String GrupoTipoUsuario;
    protected String GrupoTipoUsuarioCodigo;

//---------------------------------------------------------
	public ServentiaCargoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ServentiaCargo;}
	public void setId(String valor ) {if(valor!=null) Id_ServentiaCargo = valor;}
	public String getServentiaCargo()  {return ServentiaCargo;}
	public void setServentiaCargo(String valor ) {if (valor!=null) ServentiaCargo = valor;}
	public String getId_Serventia()  {return Id_Serventia;}
	public void setId_Serventia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Serventia = ""; Serventia = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia = valor;}
	public String getServentia()  {return Serventia;}
	public void setServentia(String valor ) {if (valor!=null) Serventia = valor;}
	public String getId_CargoTipo()  {return Id_CargoTipo;}
	public void setId_CargoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_CargoTipo = ""; CargoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_CargoTipo = valor;}
	public String getCargoTipo()  {return CargoTipo;}
	public void setCargoTipo(String valor ) {if (valor!=null) CargoTipo = valor;}
	public String getId_UsuarioServentiaGrupo()  {return Id_UsuarioServentiaGrupo;}
	public void setId_UsuarioServentiaGrupo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioServentiaGrupo = ""; UsuarioServentiaGrupo = ""; NomeUsuario = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioServentiaGrupo = valor;}
	public String getUsuarioServentiaGrupo()  {return UsuarioServentiaGrupo;}
	public void setUsuarioServentiaGrupo(String valor ) {if (valor!=null) UsuarioServentiaGrupo = valor;}
	public String getQuantidadeDistribuicao()  {return QuantidadeDistribuicao;}
	public void setQuantidadeDistribuicao(String valor ) {if (valor!=null) QuantidadeDistribuicao = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getServentiaCodigo()  {return ServentiaCodigo;}
	public void setServentiaCodigo(String valor ) {if (valor!=null) ServentiaCodigo = valor;}
	public String getCargoTipoCodigo()  {return CargoTipoCodigo;}
	public void setCargoTipoCodigo(String valor ) {if (valor!=null) CargoTipoCodigo = valor;}
	public String getServentiaUsuario()  {return ServentiaUsuario;}
	public void setServentiaUsuario(String valor ) {if (valor!=null) ServentiaUsuario = valor;}
	public String getNomeUsuario()  {return NomeUsuario;}
	public void setNomeUsuario(String valor ) {if (valor!=null) NomeUsuario = valor;}
	public String getGrupoUsuario()  {return GrupoUsuario;}
	public void setGrupoUsuario(String valor ) {if (valor!=null) GrupoUsuario = valor;}
	public String getGrupoUsuarioCodigo()  {return GrupoUsuarioCodigo;}
	public void setGrupoUsuarioCodigo(String valor ) {if (valor!=null) GrupoUsuarioCodigo = valor;}
	
	public String getGrupoTipoUsuario() {
		return GrupoTipoUsuario;
	}

	
	public void setGrupoTipoUsuario(String grupoTipoUsuario) {
		if (grupoTipoUsuario != null)
			GrupoTipoUsuario = grupoTipoUsuario;
	}

	
	public String getGrupoTipoUsuarioCodigo() {
		return GrupoTipoUsuarioCodigo;
	}

	
	public void setGrupoTipoUsuarioCodigo(String grupoTipoUsuarioCodigo) {
		if (grupoTipoUsuarioCodigo != null)
			GrupoTipoUsuarioCodigo = grupoTipoUsuarioCodigo;
	}

	public void copiar(ServentiaCargoDt objeto){
		Id_ServentiaCargo = objeto.getId();
		ServentiaCargo = objeto.getServentiaCargo();
		Id_Serventia = objeto.getId_Serventia();
		Serventia = objeto.getServentia();
		Id_CargoTipo = objeto.getId_CargoTipo();
		CargoTipo = objeto.getCargoTipo();
		Id_UsuarioServentiaGrupo = objeto.getId_UsuarioServentiaGrupo();
		UsuarioServentiaGrupo = objeto.getUsuarioServentiaGrupo();
		QuantidadeDistribuicao = objeto.getQuantidadeDistribuicao();
		CodigoTemp = objeto.getCodigoTemp();
		ServentiaCodigo = objeto.getServentiaCodigo();
		CargoTipoCodigo = objeto.getCargoTipoCodigo();
		ServentiaUsuario = objeto.getServentiaUsuario();
		NomeUsuario = objeto.getNomeUsuario();
		GrupoUsuario = objeto.getGrupoUsuario();
		GrupoTipoUsuario = objeto.getGrupoTipoUsuario();
		GrupoUsuarioCodigo = objeto.getGrupoUsuarioCodigo();
		GrupoTipoUsuario = objeto.getGrupoTipoUsuarioCodigo();
	}

	public void limpar(){
		Id_ServentiaCargo="";
		ServentiaCargo="";
		Id_Serventia="";
		Serventia="";
		Id_CargoTipo="";
		CargoTipo="";
		Id_UsuarioServentiaGrupo="";
		UsuarioServentiaGrupo="";
		QuantidadeDistribuicao="";
		CodigoTemp="";
		ServentiaCodigo="";
		CargoTipoCodigo="";
		ServentiaUsuario="";
		NomeUsuario="";
		GrupoUsuario="";
		GrupoUsuarioCodigo="";
		GrupoTipoUsuario = "";
		GrupoTipoUsuarioCodigo = "";
	}


	public String getPropriedades(){
		return "[Id_ServentiaCargo:" + Id_ServentiaCargo + ";ServentiaCargo:" + ServentiaCargo + ";Id_Serventia:" + Id_Serventia + ";Serventia:" + Serventia + ";Id_CargoTipo:" + Id_CargoTipo + ";CargoTipo:" + CargoTipo + ";Id_UsuarioServentiaGrupo:" + Id_UsuarioServentiaGrupo + ";UsuarioServentiaGrupo:" + UsuarioServentiaGrupo + ";QuantidadeDistribuicao:" + QuantidadeDistribuicao + ";CodigoTemp:" + CodigoTemp + ";ServentiaCodigo:" + ServentiaCodigo + ";CargoTipoCodigo:" + CargoTipoCodigo + ";ServentiaUsuario:" + ServentiaUsuario + ";NomeUsuario:" + NomeUsuario + ";GrupoUsuario:" + GrupoUsuario + ";GrupoUsuarioCodigo:" + GrupoUsuarioCodigo + ";GrupoTipoUsuario:" + GrupoTipoUsuario + ";GrupoTipoUsuarioCodigo:" + GrupoTipoUsuarioCodigo +"]";
	}


} 
