package br.gov.go.tj.projudi.dt;

public class ProcessoParteAdvogadoDtGen extends Dados{

	private String Id_ProcessoParteAdvogado;
	private String NomeParte;

    private static final long serialVersionUID = -4301411796420137894L;
	private String NomeAdvogado;
	private String Id_UsuarioServentiaAdvogado;
	private String Id_Serventia;
	private String Serventia;
	private String UsuarioAdvogado;
	private String Id_ProcessoParte;

	private String DataEntrada;
	private String DataSaida;
	private String Principal;
	private String CodigoTemp;
	private String OabNumero;
	private String OabComplemento;
	private String Id_Processo;
	private String ProcessoNumero;
	private String Id_ProcessoParteTipo;
	private String ProcessoParteTipo;
	
//---------------------------------------------------------
	public ProcessoParteAdvogadoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoParteAdvogado;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoParteAdvogado = valor;}
	public String getNomeParte()  {return NomeParte;}
	public void setNomeParte(String valor ) {if (valor!=null) NomeParte = valor;}
	public String getNomeAdvogado()  {return NomeAdvogado;}
	public void setNomeAdvogado(String valor ) {if (valor!=null) NomeAdvogado = valor;}
	public String getId_UsuarioServentiaAdvogado()  {return Id_UsuarioServentiaAdvogado;}
	public void setId_UsuarioServentiaAdvogado(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioServentiaAdvogado = ""; Id_Serventia = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioServentiaAdvogado = valor;}
	public String getId_Serventia()  {return Id_Serventia;}
	public void setId_Serventia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Serventia = ""; Serventia = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia = valor;}
	public String getServentia()  {return Serventia;}
	public void setServentia(String valor ) {if (valor!=null) Serventia = valor;}
	public String getUsuarioAdvogado()  {return UsuarioAdvogado;}
	public void setUsuarioAdvogado(String valor ) {if (valor!=null) UsuarioAdvogado = valor;}
	public String getId_ProcessoParte()  {return Id_ProcessoParte;}
	public void setId_ProcessoParte(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoParte = ""; NomeParte = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoParte = valor;}
	public String getDataEntrada()  {return DataEntrada;}
	public void setDataEntrada(String valor ) {if (valor!=null) DataEntrada = valor;}
	public String getDataSaida()  {return DataSaida;}
	public void setDataSaida(String valor ) {if (valor!=null) DataSaida = valor;}
	public String getPrincipal()  {return Principal;}
	public void setPrincipal(String valor ) {if (valor!=null) Principal = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getOabNumero()  {return OabNumero;}
	public void setOabNumero(String valor ) {if (valor!=null) OabNumero = valor;}
	public String getOabComplemento()  {return OabComplemento;}
	public void setOabComplemento(String valor ) {if (valor!=null) OabComplemento = valor;}
	public String getId_Processo()  {return Id_Processo;}
	public void setId_Processo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Processo = ""; ProcessoNumero = "";}else if (!valor.equalsIgnoreCase("")) Id_Processo = valor;}
	public String getProcessoNumero()  {return ProcessoNumero;}
	public void setProcessoNumero(String valor ) {if (valor!=null) ProcessoNumero = valor;}
	public String getId_ProcessoParteTipo()  {return Id_ProcessoParteTipo;}
	public void setId_ProcessoParteTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoParteTipo = ""; ProcessoParteTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoParteTipo = valor;}
	public String getProcessoParteTipo()  {return ProcessoParteTipo;}
	public void setProcessoParteTipo(String valor ) {if (valor!=null) ProcessoParteTipo = valor;}
	
	public void copiar(ProcessoParteAdvogadoDt objeto){
		 if (objeto==null) return;
		Id_ProcessoParteAdvogado = objeto.getId();
		NomeAdvogado = objeto.getNomeAdvogado();
		Id_UsuarioServentiaAdvogado = objeto.getId_UsuarioServentiaAdvogado();
		Id_Serventia = objeto.getId_Serventia();
		Serventia = objeto.getServentia();
		UsuarioAdvogado = objeto.getUsuarioAdvogado();
		Id_ProcessoParte = objeto.getId_ProcessoParte();
		NomeParte = objeto.getNomeParte();
		DataEntrada = objeto.getDataEntrada();
		DataSaida = objeto.getDataSaida();
		Principal = objeto.getPrincipal();
		CodigoTemp = objeto.getCodigoTemp();
		OabNumero = objeto.getOabNumero();
		OabComplemento = objeto.getOabComplemento();
		Id_Processo = objeto.getId_Processo();
		ProcessoNumero = objeto.getProcessoNumero();
		Id_ProcessoParteTipo = objeto.getId_ProcessoParteTipo();
		ProcessoParteTipo = objeto.getProcessoParteTipo();		
	}

	public void limpar(){
		Id_ProcessoParteAdvogado="";
		NomeAdvogado="";
		Id_UsuarioServentiaAdvogado="";
		Id_Serventia="";
		Serventia="";
		UsuarioAdvogado="";
		Id_ProcessoParte="";
		NomeParte="";
		DataEntrada="";
		DataSaida="";
		Principal="";
		CodigoTemp="";
		OabNumero="";
		OabComplemento="";
		Id_Processo="";
		ProcessoNumero="";
		Id_ProcessoParteTipo="";
		ProcessoParteTipo="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoParteAdvogado:" + Id_ProcessoParteAdvogado + ";NomeAdvogado:" + NomeAdvogado + ";Id_UsuarioServentiaAdvogado:" + Id_UsuarioServentiaAdvogado + ";Id_Serventia:" + Id_Serventia + ";Serventia:" + Serventia + ";UsuarioAdvogado:" + UsuarioAdvogado + ";Id_ProcessoParte:" + Id_ProcessoParte + ";NomeParte:" + NomeParte + ";DataEntrada:" + DataEntrada + ";DataSaida:" + DataSaida + ";Principal:" + Principal + ";CodigoTemp:" + CodigoTemp + ";OabNumero:" + OabNumero + ";OabComplemento:" + OabComplemento + ";Id_Processo:" + Id_Processo + ";ProcessoNumero:" + ProcessoNumero + ";Id_ProcessoParteTipo:" + Id_ProcessoParteTipo + ";ProcessoParteTipo:" + ProcessoParteTipo + "]";
	}


} 
