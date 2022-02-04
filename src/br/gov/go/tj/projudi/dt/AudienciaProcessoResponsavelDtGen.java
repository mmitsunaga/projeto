package br.gov.go.tj.projudi.dt;

public class AudienciaProcessoResponsavelDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -4995019356614313538L;
    private String Id_AudienciaProcessoResponsavel;
	private String ProcessoNumero;

	private String Id_AudienciaProcesso;

	private String Id_UsuarioServentia;
	private String Usuario;
	private String Id_CargoTipo;
	private String CargoTipo;
	private String CodigoTemp;
	private String DataAgendada;
	private String Nome;

//---------------------------------------------------------
	public AudienciaProcessoResponsavelDtGen() {

		limpar();

	}

	public String getId()  {return Id_AudienciaProcessoResponsavel;}
	public void setId(String valor ) {if(valor!=null) Id_AudienciaProcessoResponsavel = valor;}
	public String getProcessoNumero()  {return ProcessoNumero;}
	public void setProcessoNumero(String valor ) {if (valor!=null) ProcessoNumero = valor;}
	public String getId_AudienciaProcesso()  {return Id_AudienciaProcesso;}
	public void setId_AudienciaProcesso(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_AudienciaProcesso = ""; ProcessoNumero = "";}else if (!valor.equalsIgnoreCase("")) Id_AudienciaProcesso = valor;}
	public String getId_UsuarioServentia()  {return Id_UsuarioServentia;}
	public void setId_UsuarioServentia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioServentia = ""; Usuario = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioServentia = valor;}
	public String getUsuario()  {return Usuario;}
	public void setUsuario(String valor ) {if (valor!=null) Usuario = valor;}
	public String getId_CargoTipo()  {return Id_CargoTipo;}
	public void setId_CargoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_CargoTipo = ""; CargoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_CargoTipo = valor;}
	public String getCargoTipo()  {return CargoTipo;}
	public void setCargoTipo(String valor ) {if (valor!=null) CargoTipo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getDataAgendada()  {return DataAgendada;}
	public void setDataAgendada(String valor ) {if (valor!=null) DataAgendada = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) {if (valor!=null) Nome = valor;}


	public void copiar(AudienciaProcessoResponsavelDt objeto){
		Id_AudienciaProcessoResponsavel = objeto.getId();
		Id_AudienciaProcesso = objeto.getId_AudienciaProcesso();
		ProcessoNumero = objeto.getProcessoNumero();
		Id_UsuarioServentia = objeto.getId_UsuarioServentia();
		Usuario = objeto.getUsuario();
		Id_CargoTipo = objeto.getId_CargoTipo();
		CargoTipo = objeto.getCargoTipo();
		CodigoTemp = objeto.getCodigoTemp();
		DataAgendada = objeto.getDataAgendada();
		Nome = objeto.getNome();
	}

	public void limpar(){
		Id_AudienciaProcessoResponsavel="";
		Id_AudienciaProcesso="";
		ProcessoNumero="";
		Id_UsuarioServentia="";
		Usuario="";
		Id_CargoTipo="";
		CargoTipo="";
		CodigoTemp="";
		DataAgendada="";
		Nome="";
	}


	public String getPropriedades(){
		return "[Id_AudienciaProcessoResponsavel:" + Id_AudienciaProcessoResponsavel + ";Id_AudienciaProcesso:" + Id_AudienciaProcesso + ";ProcessoNumero:" + ProcessoNumero + ";Id_UsuarioServentia:" + Id_UsuarioServentia + ";Usuario:" + Usuario + ";Id_CargoTipo:" + Id_CargoTipo + ";CargoTipo:" + CargoTipo + ";CodigoTemp:" + CodigoTemp + ";DataAgendada:" + DataAgendada + ";Nome:" + Nome + "]";
	}


} 
