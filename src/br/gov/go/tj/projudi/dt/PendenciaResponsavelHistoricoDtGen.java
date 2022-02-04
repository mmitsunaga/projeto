package br.gov.go.tj.projudi.dt;

public class PendenciaResponsavelHistoricoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4857295655352951919L;
	private String Id_PendenciaResponsavelHistorico;
	private String PendenciaResponsavelHistorico;


	private String Id_Pendencia;
	private String Pendendencia;
	private String Id_ServentiaCargo;
	private String ServentiaCargo;
	private String DataInicio;
	private String DataFim;
	private String Nome;
	private String Id_ServentiaGrupo;
	private String ServentiaGrupo;
	private String Atividade;
	private boolean envia_Magistrado;
	private String CodigoTemp;

//---------------------------------------------------------
	public PendenciaResponsavelHistoricoDtGen() {

		limpar();

	}

	public String getId()  {return Id_PendenciaResponsavelHistorico;}
	public void setId(String valor ) {if(valor!=null) Id_PendenciaResponsavelHistorico = valor;}
	public String getPendenciaResponsavelHistorico()  {return PendenciaResponsavelHistorico;}
	public void setPendenciaResponsavelHistorico(String valor ) {if (valor!=null) PendenciaResponsavelHistorico = valor;}
	public String getId_Pendencia()  {return Id_Pendencia;}
	public void setId_Pendencia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Pendencia = ""; Pendendencia = "";}else if (!valor.equalsIgnoreCase("")) Id_Pendencia = valor;}
	public String getPendendencia()  {return Pendendencia;}
	public void setPendendencia(String valor ) {if (valor!=null) Pendendencia = valor;}
	public String getId_ServentiaCargo()  {return Id_ServentiaCargo;}
	public void setId_ServentiaCargo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaCargo = ""; ServentiaCargo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaCargo = valor;}
	public String getServentiaCargo()  {return ServentiaCargo;}
	public void setServentiaCargo(String valor ) {if (valor!=null) ServentiaCargo = valor;}
	public String getDataInicio()  {return DataInicio;}
	public void setDataInicio(String valor ) {if (valor!=null) DataInicio = valor;}
	public String getDataFim()  {return DataFim;}
	public void setDataFim(String valor ) {if (valor!=null) DataFim = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		if (nome != null) Nome = nome;
	}

	public String getId_ServentiaGrupo() {
		return Id_ServentiaGrupo;
	}

	public void setId_ServentiaGrupo(String idServGrupo) {
		if (idServGrupo != null) Id_ServentiaGrupo = idServGrupo;
	}

	public String getServentiaGrupo() {
		return ServentiaGrupo;
	}

	public void setServentiaGrupo(String servGrupo) {
		if (servGrupo != null) ServentiaGrupo  = servGrupo;
	}

	public String getAtividade() {
		return Atividade;
	}

	public void setAtividade(String atividade) {
		if (atividade != null) Atividade = atividade;
	}
	
	public boolean isEnviaMagistrado() {
		return envia_Magistrado;
	}

	public void setEnviaMagistrado(boolean enviaDesembargador) {
		envia_Magistrado = enviaDesembargador;
	}

	public void copiar(PendenciaResponsavelHistoricoDt objeto){
		 if (objeto==null) return;
		Id_PendenciaResponsavelHistorico = objeto.getId();
		PendenciaResponsavelHistorico = objeto.getPendenciaResponsavelHistorico();
		Id_Pendencia = objeto.getId_Pendencia();
		Pendendencia = objeto.getPendendencia();
		Id_ServentiaCargo = objeto.getId_ServentiaCargo();
		ServentiaCargo = objeto.getServentiaCargo();
		DataInicio = objeto.getDataInicio();
		DataFim = objeto.getDataFim();
		CodigoTemp = objeto.getCodigoTemp();
		Id_ServentiaGrupo= objeto.getId_ServentiaGrupo();
		ServentiaGrupo= objeto.getServentiaGrupo();
		Atividade= objeto.getAtividade();
		envia_Magistrado = objeto.isEnviaMagistrado();
	}

	public void limpar(){
		Id_PendenciaResponsavelHistorico="";
		PendenciaResponsavelHistorico="";
		Id_Pendencia="";
		Pendendencia="";
		Id_ServentiaCargo="";
		ServentiaCargo="";
		DataInicio="";
		DataFim="";
		CodigoTemp="";
		Id_ServentiaGrupo="";
		ServentiaGrupo="";
		Atividade="";
		envia_Magistrado = false;
	}


	public String getPropriedades(){
		return "[Id_PendenciaResponsavelHistorico:" + Id_PendenciaResponsavelHistorico + ";PendenciaResponsavelHistorico:" + PendenciaResponsavelHistorico + ";Id_Pendencia:" + Id_Pendencia + ";Pendendencia:" + Pendendencia + ";Id_ServentiaCargo:" + Id_ServentiaCargo + ";ServentiaCargo:" + ServentiaCargo + ";DataInicio:" + DataInicio + ";DataFim:" + DataFim + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
