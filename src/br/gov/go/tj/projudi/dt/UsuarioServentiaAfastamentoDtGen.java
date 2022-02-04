package br.gov.go.tj.projudi.dt;

public class UsuarioServentiaAfastamentoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -2950079028530537191L;
    
    private String Id_UsuarioServentiaAfastamento;
	private String Usuario;
	private String NomeUsuario;
	private String Id_Usuario;
	private String Id_UsuarioServentia;
	private String Id_Afastamento;
	private String Afastamento;
	private String DataInicio;
	private String MotivoInicio;
	private String Id_UsuServCadastrador;
	private String DataFim;
	private String MotivoFim;
	private String Id_UsuServFinalizador;
	

//---------------------------------------------------------
	public UsuarioServentiaAfastamentoDtGen() {

		limpar();

	}

	public String getId()  {return Id_UsuarioServentiaAfastamento;}
	public void setId(String valor ) {if(valor!=null) Id_UsuarioServentiaAfastamento = valor;}
	public String getUsuario()  {return Usuario;}
	public void setUsuario(String valor ) {if (valor!=null) Usuario = valor;}	
	public String getNomeUsuario()  {return NomeUsuario;}
	public void setNomeUsuario(String valor ) {if (valor!=null) NomeUsuario = valor;}
	public String getId_Usuario()  {return Id_Usuario;}
	public void setId_Usuario(String valor ) {if (valor!=null) Id_Usuario = valor;}
	public String getId_UsuarioServentia()  {return Id_UsuarioServentia;}
	public void setId_UsuarioServentia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioServentia = ""; Usuario = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioServentia = valor;}
	public String getId_Afastamento()  {return Id_Afastamento;}
	public void setId_Afastamento(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Afastamento = ""; Afastamento = "";}else if (!valor.equalsIgnoreCase("")) Id_Afastamento = valor;}
	public String getAfastamento()  {return Afastamento;}
	public void setAfastamento(String valor ) {if (valor!=null) Afastamento = valor;}
	public String getDataInicio()  {return DataInicio;}
	public void setDataInicio(String valor ) {if (valor!=null) DataInicio = valor;}
	public String getDataFim()  {return DataFim;}
	public void setDataFim(String valor ) {if (valor!=null) DataFim = valor;}
	public String getId_UsuarioServentiaAfastamento() {return Id_UsuarioServentiaAfastamento;}
	public void setId_UsuarioServentiaAfastamento(String id_UsuarioServentiaAfastamento) {Id_UsuarioServentiaAfastamento = id_UsuarioServentiaAfastamento;}
	public String getMotivoInicio() {return MotivoInicio;}
	public void setMotivoInicio(String motivoInicio) {if (motivoInicio == null) MotivoInicio = ""; else MotivoInicio = motivoInicio;}
	public String getId_UsuServCadastrador() {return Id_UsuServCadastrador;}
	public void setId_UsuServCadastrador(String id_UsuServCadastrador) {Id_UsuServCadastrador = id_UsuServCadastrador;}
	public String getMotivoFim() {return MotivoFim;}
	public void setMotivoFim(String motivoFim) {if (motivoFim == null) MotivoFim = ""; else MotivoFim = motivoFim;}
	public String getId_UsuServFinalizador() {return Id_UsuServFinalizador;}
	public void setId_UsuServFinalizador(String id_UsuServFinalizador) {Id_UsuServFinalizador = id_UsuServFinalizador;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	
	

	public void copiar(UsuarioServentiaAfastamentoDt objeto){
		Id_UsuarioServentiaAfastamento = objeto.getId();
		Id_UsuarioServentia = objeto.getId_UsuarioServentia();
		Usuario = objeto.getUsuario();
		Id_Afastamento = objeto.getId_Afastamento();
		Afastamento = objeto.getAfastamento();
		DataInicio = objeto.getDataInicio();
		MotivoInicio = objeto.getMotivoInicio();
		Id_UsuServCadastrador = objeto.getId_UsuServCadastrador();
		DataFim = objeto.getDataFim();
		MotivoFim = objeto.getMotivoFim();
		Id_UsuServFinalizador = objeto.getId_UsuServFinalizador();
		CodigoTemp = objeto.getCodigoTemp();
		Id_Usuario = objeto.getId_Usuario();
	}

	public void limpar(){
		Id_UsuarioServentiaAfastamento="";
		Id_UsuarioServentia="";
		Usuario="";
		Id_Afastamento="";
		Afastamento="";
		DataInicio="";
		MotivoInicio="";
		Id_UsuServCadastrador="";
		DataFim="";
		MotivoFim="";
		Id_UsuServFinalizador="";
		CodigoTemp="";
		Id_Usuario = "";
		NomeUsuario = "";
	}


	public String getPropriedades(){
		return "[Id_UsuarioAfastamento:" + Id_UsuarioServentiaAfastamento + ";Id_Usuario:" + Id_Usuario + ";Id_UsuarioServentia:" + Id_UsuarioServentia + ";Usuario:" + Usuario + ";Id_Afastamento:" + Id_Afastamento + ";Afastamento:" + Afastamento + ";DataInicio:" + DataInicio + ";MotivoInicio:" + MotivoInicio + ";Id_UsuServCadastrador:" + Id_UsuServCadastrador + ";DataFim:" + DataFim + ";MotivoFim:" + MotivoFim + ";Id_UsuServFinalizador:" + Id_UsuServFinalizador + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
