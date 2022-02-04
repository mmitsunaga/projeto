package br.gov.go.tj.projudi.dt;

public class ContaUsuarioDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 4761433030839317566L;
    private String Id_ContaUsuario;
	private String ContaUsuario;
	private String Id_Usuario;
	private String Usuario;
	private String Id_Agencia;
	private String Agencia;
	private String Ativa;	
	
	private String DvContaUsuario;
	private String OperacaoContaUsuario;
	
	//---------------------------------------------------------

	public ContaUsuarioDtGen() {

		limpar();

	}

	public String getId()  {return Id_ContaUsuario;}
	public void setId(String valor ) {if(valor!=null) Id_ContaUsuario = valor;}
	public String getContaUsuario()  {return ContaUsuario;}
	public void setContaUsuario(String valor ) {if (valor!=null) ContaUsuario = valor;}
	public String getId_Usuario()  {return Id_Usuario;}
	public void setId_Usuario(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Usuario = ""; Usuario = "";}else if (!valor.equalsIgnoreCase("")) Id_Usuario = valor;}
	public String getUsuario()  {return Usuario;}
	public void setUsuario(String valor ) {if (valor!=null) Usuario = valor;}
	
	public String getId_Agencia()  {return Id_Agencia;}
	public void setId_Agencia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Agencia = ""; Agencia = "";}else if (!valor.equalsIgnoreCase("")) Id_Agencia = valor;}
	
	public String getAgencia()  {return Agencia;}
	public void setAgencia(String valor ) {if (valor!=null) Agencia = valor;}
	
	public String getAtiva()  {return Ativa;}
	public void setAtiva(String valor ) {if (valor!=null) Ativa = valor;}	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	public String getDvContaUsuario()  {return DvContaUsuario;}
	public void setDvContaUsuario(String valor ) {if (valor!=null) DvContaUsuario = valor;}	
	public String getOperacaoContaUsuario()  {return OperacaoContaUsuario;}
	public void setOperacaoContaUsuario(String valor ) {if (valor!=null) OperacaoContaUsuario = valor;}	
	


	public void copiar(ContaUsuarioDt objeto){
		Id_ContaUsuario = objeto.getId();
		ContaUsuario = objeto.getContaUsuario();
		Id_Usuario = objeto.getId_Usuario();
		Usuario = objeto.getUsuario();
		Id_Agencia = objeto.getId_Agencia();
		Agencia = objeto.getAgencia();
		Ativa = objeto.getAtiva();
		CodigoTemp = objeto.getCodigoTemp();
		
		OperacaoContaUsuario = objeto.getOperacaoContaUsuario();
		DvContaUsuario = objeto.getDvContaUsuario();

	}

	public void limpar(){
		Id_ContaUsuario="";
		ContaUsuario="";
		Id_Usuario="";
		Usuario="";
		Id_Agencia="";
		Agencia="";
		Ativa="";
		CodigoTemp="";
		
		DvContaUsuario = "";
		OperacaoContaUsuario = "";
		
	}

	public String getPropriedades(){
		return "[Id_ContaUsuario:" + Id_ContaUsuario + ";OperacaoContaUsuario:" + OperacaoContaUsuario + ";ContaUsuario:" + ContaUsuario + "; DvContaUsuario:" + DvContaUsuario + ";Id_Usuario:" + Id_Usuario + ";Usuario:" + Usuario + ";Id_Agencia:" + Id_Agencia + ";Agencia:" + Agencia + ";Ativa:" + Ativa + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
