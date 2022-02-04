package br.gov.go.tj.projudi.dt;

public class MandadoJudicialDtGen extends Dados{

	private static final long serialVersionUID = 3779117081849074788L;
	private String Id_MandadoJudicial;
	private String Id_MandadoTipo;
	private String Id_ProcessoParte;
	private String Id_EnderecoParte;
	private String Id_Pendencia;
	private String Id_Area;
	private String Id_Zona;
	private String Id_Regiao;
	private String Id_Bairro;
	private String Id_Escala;
	private String Id_MandadoJudicialStatus;
	private String Id_UsuarioServentia_1;
	private String Id_UsuarioServentia_2;
	private String Id_ServentiaCargoEscala;
	private String MandadoTipo;
	private String ProcessoParte;
	private String EnderecoParte;
	private String Pendencia;
	private String Area;
	private String Zona;
	private String Regiao;
	private String Bairro;
	private String Escala;
	private String MandadoJudicialStatus;
	private String Valor;
	private String Assistencia;
	private String CodigoTemp;
	private String LocomocoesFrutiferas;
	private String LocomocoesInfrutiferas;
	private String LocomocaoHoraMarcada;
	private String idMandJudPagamentoStatus;
	private String Id_Modelo;
	private String resolutivo;

	public MandadoJudicialDtGen() {

		limpar();

	}

	public String getId()  {return Id_MandadoJudicial;}
	public void setId(String valor ) {if(valor!=null) Id_MandadoJudicial = valor;}
	public String getId_MandadoTipo()  {return Id_MandadoTipo;}
	public void setId_MandadoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_MandadoTipo = ""; MandadoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_MandadoTipo = valor;}
	public String getMandadoTipo()  {return MandadoTipo;}
	public void setMandadoTipo(String valor ) {if (valor!=null) MandadoTipo = valor;}
	public String getId_MandadoJudicialStatus()  {return Id_MandadoJudicialStatus;}
	public void setId_MandadoJudicialStatus(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_MandadoJudicialStatus = ""; MandadoJudicialStatus = "";}else if (!valor.equalsIgnoreCase("")) Id_MandadoJudicialStatus = valor;}
	public String getMandadoJudicialStatus()  {return MandadoJudicialStatus;}
	public void setMandadoJudicialStatus(String valor ) {if (valor!=null) MandadoJudicialStatus = valor;}
	public String getId_Area()  {return Id_Area;}
	public void setId_Area(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Area = ""; Area = "";}else if (!valor.equalsIgnoreCase("")) Id_Area = valor;}
	public String getArea()  {return Area;}
	public void setArea(String valor ) {if (valor!=null) Area = valor;}
	public String getId_Zona()  {return Id_Zona;}
	public void setId_Zona(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Zona = ""; Zona = "";}else if (!valor.equalsIgnoreCase("")) Id_Zona = valor;}
	public String getZona()  {return Zona;}
	public void setZona(String valor ) {if (valor!=null) Zona = valor;}
	public String getId_Regiao()  {return Id_Regiao;}
	public void setId_Regiao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Regiao = ""; Regiao = "";}else if (!valor.equalsIgnoreCase("")) Id_Regiao = valor;}
	public String getRegiao()  {return Regiao;}
	public void setRegiao(String valor ) {if (valor!=null) Regiao = valor;}
	public String getId_Bairro()  {return Id_Bairro;}
	public void setId_Bairro(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Bairro = ""; Bairro = "";}else if (!valor.equalsIgnoreCase("")) Id_Bairro = valor;}
	public String getBairro()  {return Bairro;}
	public void setBairro(String valor ) {if (valor!=null) Bairro = valor;}
	public String getId_ProcessoParte()  {return Id_ProcessoParte;}
	public void setId_ProcessoParte(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoParte = ""; ProcessoParte = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoParte = valor;}
	public String getProcessoParte()  {return ProcessoParte;}
	public void setProcessoParte(String valor ) {if (valor!=null) ProcessoParte = valor;}
	public String getId_EnderecoParte()  {return Id_EnderecoParte;}
	public void setId_EnderecoParte(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_EnderecoParte = ""; EnderecoParte = "";}else if (!valor.equalsIgnoreCase("")) Id_EnderecoParte = valor;}
	public String getEnderecoParte()  {return EnderecoParte;}
	public void setEnderecoParte(String valor ) {if (valor!=null) EnderecoParte = valor;}
	public String getId_Pendencia()  {return Id_Pendencia;}
	public void setId_Pendencia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Pendencia = ""; Pendencia = "";}else if (!valor.equalsIgnoreCase("")) Id_Pendencia = valor;}
	public String getPendencia()  {return Pendencia;}
	public void setPendencia(String valor ) {if (valor!=null) Pendencia = valor;}
	public String getId_Escala()  {return Id_Escala;}
	public void setId_Escala(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Escala = ""; Id_UsuarioServentia_1 = "";}else if (!valor.equalsIgnoreCase("")) Id_Escala = valor;}
	public String getId_UsuarioServentia_1()  {return Id_UsuarioServentia_1;}
	public void setId_UsuarioServentia_1(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioServentia_1 = ""; Id_UsuarioServentia_2 = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioServentia_1 = valor;}
	public String getId_UsuarioServentia_2()  {return Id_UsuarioServentia_2;}
	public void setId_UsuarioServentia_2(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioServentia_2 = ""; Escala = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioServentia_2 = valor;}
	public String getEscala()  {return Escala;}
	public void setEscala(String valor ) {if (valor!=null) Escala = valor;}
	public String getValor()  {return Valor;}
	public void setValor(String valor ) {if (valor!=null) Valor = valor;}
	public String getResolutivo()  {return resolutivo;}
	public void setResolutivo(String valor ) {if (valor!=null) resolutivo = valor;}
	public String getAssistencia()  {return Assistencia;}
	public void setAssistencia(String valor ) {if (valor!=null) Assistencia = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getLocomocoesFrutiferas()  {return LocomocoesFrutiferas;}
	public void setLocomocoesFrutiferas(String valor ) {if (valor!=null) LocomocoesFrutiferas = valor;}
	public String getLocomocoesInfrutiferas()  {return LocomocoesInfrutiferas;}
	public void setLocomocoesInfrutiferas(String valor ) {if (valor!=null) LocomocoesInfrutiferas = valor;}
	public String getLocomocaoHoraMarcada()  {return LocomocaoHoraMarcada;}
	public void setLocomocaoHoraMarcada(String valor ) {if (valor!=null) LocomocaoHoraMarcada = valor;}
	public String getId_ServentiaCargoEscala()  {return Id_ServentiaCargoEscala;}
	public void setId_ServentiaCargoEscala(String valor ) {if (valor!=null) Id_ServentiaCargoEscala = valor;}
	public String getIdMandJudPagamentoStatus()  {return idMandJudPagamentoStatus;}
	public void setIdMandJudPagamentoStatus(String valor ) {if (valor!=null) idMandJudPagamentoStatus = valor;}
	public String getId_Modelo()  {return Id_Modelo;}
	
	public void setId_Modelo(String valor ) {
	   if (valor != null) 
		  if (valor.equalsIgnoreCase("null")) {
		    Id_Modelo = ""; 
		  } else
			   if (!valor.equalsIgnoreCase("")) Id_Modelo = valor;
	  }
	 
	
	public void copiar(MandadoJudicialDt objeto){
		 if (objeto==null) return;
		Id_MandadoJudicial = objeto.getId();
		Id_MandadoTipo = objeto.getId_MandadoTipo();
		MandadoTipo = objeto.getMandadoTipo();
		Id_MandadoJudicialStatus = objeto.getId_MandadoJudicialStatus();
		MandadoJudicialStatus = objeto.getMandadoJudicialStatus();
		Id_Area = objeto.getId_Area();
		Area = objeto.getArea();
		Id_Zona = objeto.getId_Zona();
		Zona = objeto.getZona();
		Id_Regiao = objeto.getId_Regiao();
		Regiao = objeto.getRegiao();
		Id_Bairro = objeto.getId_Bairro();
		Bairro = objeto.getBairro();
		Id_ProcessoParte = objeto.getId_ProcessoParte();
		ProcessoParte = objeto.getProcessoParte();
		Id_EnderecoParte = objeto.getId_EnderecoParte();
		EnderecoParte = objeto.getEnderecoParte();
		Id_Pendencia = objeto.getId_Pendencia();
		Pendencia = objeto.getPendencia();
		Id_Escala = objeto.getId_Escala();
		Id_UsuarioServentia_1 = objeto.getId_UsuarioServentia_1();
		Id_UsuarioServentia_2 = objeto.getId_UsuarioServentia_2();
		Escala = objeto.getEscala();
		Valor = objeto.getValor();
		Assistencia = objeto.getAssistencia();
		CodigoTemp = objeto.getCodigoTemp();
		LocomocoesFrutiferas = objeto.getLocomocoesFrutiferas();
		LocomocoesInfrutiferas = objeto.getLocomocoesInfrutiferas();
		LocomocaoHoraMarcada = objeto.getLocomocaoHoraMarcada();
		Id_ServentiaCargoEscala = objeto.getId_ServentiaCargoEscala();
		idMandJudPagamentoStatus = objeto.getIdMandJudPagamentoStatus();
		Id_Modelo = objeto.getId_Modelo();
		resolutivo = objeto.getResolutivo();
	}

	public void limpar(){
		Id_MandadoJudicial="";
		Id_MandadoTipo="";
		MandadoTipo="";
		Id_MandadoJudicialStatus="";
		MandadoJudicialStatus="";
		Id_Area="";
		Area="";
		Id_Zona="";
		Zona="";
		Id_Regiao="";
		Regiao="";
		Id_Bairro="";
		Bairro="";
		Id_ProcessoParte="";
		ProcessoParte="";
		Id_EnderecoParte="";
		EnderecoParte="";
		Id_Pendencia="";
		Pendencia="";
		Id_Escala="";
		Id_UsuarioServentia_1="";
		Id_UsuarioServentia_2="";
		Escala="";
		Valor="";
		Assistencia="";
		CodigoTemp="";
		LocomocoesFrutiferas="";
		LocomocoesInfrutiferas="";
		LocomocaoHoraMarcada="";
		Id_ServentiaCargoEscala = "";
		idMandJudPagamentoStatus = "";
		Id_Modelo = "";
		resolutivo = "";
	}


	public String getPropriedades(){
		return "[Id_MandadoJudicial:" + Id_MandadoJudicial + ";Id_Modelo:" + Id_Modelo + "; Id_MandadoTipo:" + Id_MandadoTipo + ";MandadoTipo:" + MandadoTipo + ";Id_MandadoJudicialStatus:" + Id_MandadoJudicialStatus + ";MandadoJudicialStatus:" + MandadoJudicialStatus + ";Id_Area:" + Id_Area + ";Area:" + Area + ";Id_Zona:" + Id_Zona + ";Zona:" + Zona + ";Id_Regiao:" + Id_Regiao + ";Regiao:" + Regiao + ";Id_Bairro:" + Id_Bairro + ";Bairro:" + Bairro + ";Id_ProcessoParte:" + Id_ProcessoParte + ";NomeParte:" + ProcessoParte + ";Id_EnderecoParte:" + Id_EnderecoParte + ";EnderecoParte:" + EnderecoParte + ";Id_Pendencia:" + Id_Pendencia + ";Pendencia:" + Pendencia + ";Id_Escala:" + Id_Escala + ";Id_UsuarioServentia:" + Id_UsuarioServentia_1 + ";Id_UsuarioServentia_2:" + Id_UsuarioServentia_2 + ";Escala:" + Escala + ";Valor:" + Valor + ";Assistencia:" + Assistencia + ";resolutivo:" + resolutivo + ";CodigoTemp:" + CodigoTemp + ";LocomocoesFrutiferas:" + LocomocoesFrutiferas + ";LocomocoesInfrutiferas:" + LocomocoesInfrutiferas + ";LocomocaoHoraMarcada:" + LocomocaoHoraMarcada + "]";
	}
	
	
} 
