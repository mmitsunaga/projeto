package br.gov.go.tj.projudi.dt;

public class GuiaEmissaoDtGen extends Dados{

	private static final long serialVersionUID = -221364102751740620L;
	private String Id_GuiaEmissao;
	private String GuiaEmissao;


	private String Id_GuiaModelo;
	private String GuiaModelo;
	private String Id_Processo;
	private String NumeroProcessoDependente;
	private String Id_ProcessoTipo;
	private String ProcessoTipo;
	private String Id_NaturezaSPG;
	private String NaturezaSPG;
	private String Id_GuiaEmissaoPrincipal;
	private String Id_Serventia;
	private String Serventia;
	private String Id_Comarca;
	private String Comarca;
	private String Id_GuiaTipo;
	private String GuiaTipo;
	private String Id_GuiaStatus;
	private String GuiaStatus;
	private String Id_AreaDistribuicao;
	private String Id_ProcessoPrioridade;
	private String Id_Usuario;
	private String DataEmissao;
	private String DataRecebimento;
	private String DataVencimento;
	private String Requerente;
	private String Requerido;
	private String ValorAcao;
	private String NumeroGuiaCompleto;
	private String NumeroDUAM;
	private String QuantidadeParcelasDUAM;
	private String DataVencimentoDUAM;
	private String ValorImpostoMunicipalDUAM;
	private String CodigoTemp;

//---------------------------------------------------------
	public GuiaEmissaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_GuiaEmissao;}
	public void setId(String valor ) {if(valor!=null) Id_GuiaEmissao = valor;}
	public String getGuiaEmissao()  {return GuiaEmissao;}
	public void setGuiaEmissao(String valor ) {if (valor!=null) GuiaEmissao = valor;}
	public String getId_GuiaModelo()  {return Id_GuiaModelo;}
	public void setId_GuiaModelo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_GuiaModelo = ""; GuiaModelo = "";}else if (!valor.equalsIgnoreCase("")) Id_GuiaModelo = valor;}
	public String getGuiaModelo()  {return GuiaModelo;}
	public void setGuiaModelo(String valor ) {if (valor!=null) GuiaModelo = valor;}
	public String getId_Processo()  {return Id_Processo;}
	public void setId_Processo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Processo = "";}else if (!valor.equalsIgnoreCase("")) Id_Processo = valor;}
	public String getNumeroProcessoDependente()  {return NumeroProcessoDependente;}
	public void setNumeroProcessoDependente(String valor ) {if (valor!=null) NumeroProcessoDependente = valor;}
	public String getId_ProcessoTipo()  {return Id_ProcessoTipo;}
	public void setId_ProcessoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoTipo = ""; ProcessoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoTipo = valor;}
	public String getProcessoTipo()  {return ProcessoTipo;}
	public void setProcessoTipo(String valor ) {if (valor!=null) ProcessoTipo = valor;}
	public String getId_NaturezaSPG()  {return Id_NaturezaSPG;}
	public void setId_NaturezaSPG(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_NaturezaSPG = ""; NaturezaSPG = "";}else if (!valor.equalsIgnoreCase("")) Id_NaturezaSPG = valor;}
	public String getNaturezaSPG()  {return NaturezaSPG;}
	public void setNaturezaSPG(String valor ) {if (valor!=null) NaturezaSPG = valor;}
	public String getId_GuiaEmissaoPrincipal()  {return Id_GuiaEmissaoPrincipal;}
	public void setId_GuiaEmissaoPrincipal(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_GuiaEmissaoPrincipal = ""; Id_Serventia = "";}else if (!valor.equalsIgnoreCase("")) Id_GuiaEmissaoPrincipal = valor;}
	public String getId_Serventia()  {return Id_Serventia;}
	public void setId_Serventia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Serventia = ""; Serventia = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia = valor;}
	public String getServentia()  {return Serventia;}
	public void setServentia(String valor ) {if (valor!=null) Serventia = valor;}
	public String getId_Comarca()  {return Id_Comarca;}
	public void setId_Comarca(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Comarca = ""; Comarca = "";}else if (!valor.equalsIgnoreCase("")) Id_Comarca = valor;}
	public String getComarca()  {return Comarca;}
	public void setComarca(String valor ) {if (valor!=null) Comarca = valor;}
	public String getId_GuiaTipo()  {return Id_GuiaTipo;}
	public void setId_GuiaTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_GuiaTipo = ""; GuiaTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_GuiaTipo = valor;}
	public String getGuiaTipo()  {return GuiaTipo;}
	public void setGuiaTipo(String valor ) {if (valor!=null) GuiaTipo = valor;}
	public String getId_GuiaStatus()  {return Id_GuiaStatus;}
	public void setId_GuiaStatus(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_GuiaStatus = ""; GuiaStatus = "";}else if (!valor.equalsIgnoreCase("")) Id_GuiaStatus = valor;}
	public String getGuiaStatus()  {return GuiaStatus;}
	public void setGuiaStatus(String valor ) {if (valor!=null) GuiaStatus = valor;}
	public String getId_AreaDistribuicao()  {return Id_AreaDistribuicao;}
	public void setId_AreaDistribuicao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_AreaDistribuicao = ""; Id_ProcessoPrioridade = "";}else if (!valor.equalsIgnoreCase("")) Id_AreaDistribuicao = valor;}
	public String getId_ProcessoPrioridade()  {return Id_ProcessoPrioridade;}
	public void setId_ProcessoPrioridade(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoPrioridade = ""; Id_Usuario = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoPrioridade = valor;}
	public String getId_Usuario()  {return Id_Usuario;}
	public void setId_Usuario(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Usuario = ""; DataEmissao = "";}else if (!valor.equalsIgnoreCase("")) Id_Usuario = valor;}
	public String getDataEmissao()  {return DataEmissao;}
	public void setDataEmissao(String valor ) {if (valor!=null) DataEmissao = valor;}
	public String getDataRecebimento()  {return DataRecebimento;}
	public void setDataRecebimento(String valor ) {if (valor!=null) DataRecebimento = valor;}
	public String getDataVencimento()  {return DataVencimento;}
	public void setDataVencimento(String valor ) {if (valor!=null) DataVencimento = valor;}
	public String getRequerente()  {return Requerente;}
	public void setRequerente(String valor ) {if (valor!=null) Requerente = valor;}
	public String getRequerido()  {return Requerido;}
	public void setRequerido(String valor ) {if (valor!=null) Requerido = valor;}
	public String getValorAcao()  {return ValorAcao;}
	public void setValorAcao(String valor ) {if (valor!=null) ValorAcao = valor;}
	public String getNumeroGuiaCompleto()  {return NumeroGuiaCompleto;}
	public void setNumeroGuiaCompleto(String valor ) {if (valor!=null) NumeroGuiaCompleto = valor;}
	public String getNumeroDUAM()  {return NumeroDUAM;}
	public void setNumeroDUAM(String valor ) {if (valor!=null) NumeroDUAM = valor;}
	public String getQuantidadeParcelasDUAM()  {return QuantidadeParcelasDUAM;}
	public void setQuantidadeParcelasDUAM(String valor ) {if (valor!=null) QuantidadeParcelasDUAM = valor;}
	public String getDataVencimentoDUAM()  {return DataVencimentoDUAM;}
	public void setDataVencimentoDUAM(String valor ) {if (valor!=null) DataVencimentoDUAM = valor;}
	public String getValorImpostoMunicipalDUAM()  {return ValorImpostoMunicipalDUAM;}
	public void setValorImpostoMunicipalDUAM(String valor ) {if (valor!=null) ValorImpostoMunicipalDUAM = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(GuiaEmissaoDt objeto){
		 if (objeto==null) return;
		Id_GuiaEmissao = objeto.getId();
		GuiaEmissao = objeto.getGuiaEmissao();
		Id_GuiaModelo = objeto.getId_GuiaModelo();
		GuiaModelo = objeto.getGuiaModelo();
		Id_Processo = objeto.getId_Processo();
		NumeroProcessoDependente = objeto.getNumeroProcessoDependente();
		Id_ProcessoTipo = objeto.getId_ProcessoTipo();
		ProcessoTipo = objeto.getProcessoTipo();
		Id_NaturezaSPG = objeto.getId_NaturezaSPG();
		NaturezaSPG = objeto.getNaturezaSPG();
		Id_GuiaEmissaoPrincipal = objeto.getId_GuiaEmissaoPrincipal();
		Id_Serventia = objeto.getId_Serventia();
		Serventia = objeto.getServentia();
		Id_Comarca = objeto.getId_Comarca();
		Comarca = objeto.getComarca();
		Id_GuiaTipo = objeto.getId_GuiaTipo();
		GuiaTipo = objeto.getGuiaTipo();
		Id_GuiaStatus = objeto.getId_GuiaStatus();
		GuiaStatus = objeto.getGuiaStatus();
		Id_AreaDistribuicao = objeto.getId_AreaDistribuicao();
		Id_ProcessoPrioridade = objeto.getId_ProcessoPrioridade();
		Id_Usuario = objeto.getId_Usuario();
		DataEmissao = objeto.getDataEmissao();
		DataRecebimento = objeto.getDataRecebimento();
		DataVencimento = objeto.getDataVencimento();
		Requerente = objeto.getRequerente();
		Requerido = objeto.getRequerido();
		ValorAcao = objeto.getValorAcao();
		NumeroGuiaCompleto = objeto.getNumeroGuiaCompleto();
		NumeroDUAM = objeto.getNumeroDUAM();
		QuantidadeParcelasDUAM = objeto.getQuantidadeParcelasDUAM();
		DataVencimentoDUAM = objeto.getDataVencimentoDUAM();
		ValorImpostoMunicipalDUAM = objeto.getValorImpostoMunicipalDUAM();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_GuiaEmissao="";
		GuiaEmissao="";
		Id_GuiaModelo="";
		GuiaModelo="";
		Id_Processo="";
		NumeroProcessoDependente="";
		Id_ProcessoTipo="";
		ProcessoTipo="";
		Id_NaturezaSPG="";
		NaturezaSPG="";
		Id_GuiaEmissaoPrincipal="";
		Id_Serventia="";
		Serventia="";
		Id_Comarca="";
		Comarca="";
		Id_GuiaTipo="";
		GuiaTipo="";
		Id_GuiaStatus="";
		GuiaStatus="";
		Id_AreaDistribuicao="";
		Id_ProcessoPrioridade="";
		Id_Usuario="";
		DataEmissao="";
		DataRecebimento="";
		DataVencimento="";
		Requerente="";
		Requerido="";
		ValorAcao="";
		NumeroGuiaCompleto="";
		NumeroDUAM="";
		QuantidadeParcelasDUAM="";
		DataVencimentoDUAM="";
		ValorImpostoMunicipalDUAM="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_GuiaEmissao:" + Id_GuiaEmissao + ";GuiaEmissao:" + GuiaEmissao + ";Id_GuiaModelo:" + Id_GuiaModelo + ";GuiaModelo:" + GuiaModelo + ";Id_Processo:" + Id_Processo + ";NumeroProcessoDependente:" + NumeroProcessoDependente + ";Id_ProcessoTipo:" + Id_ProcessoTipo + ";ProcessoTipo:" + ProcessoTipo + ";Id_NaturezaSPG:" + Id_NaturezaSPG + ";NaturezaSPG:" + NaturezaSPG + ";Id_GuiaEmissaoPrincipal:" + Id_GuiaEmissaoPrincipal + ";Id_Serventia:" + Id_Serventia + ";Serventia:" + Serventia + ";Id_Comarca:" + Id_Comarca + ";Comarca:" + Comarca + ";Id_GuiaTipo:" + Id_GuiaTipo + ";GuiaTipo:" + GuiaTipo + ";Id_GuiaStatus:" + Id_GuiaStatus + ";GuiaStatus:" + GuiaStatus + ";Id_AreaDistribuicao:" + Id_AreaDistribuicao + ";Id_ProcessoPrioridade:" + Id_ProcessoPrioridade + ";Id_Usuario:" + Id_Usuario + ";DataEmissao:" + DataEmissao + ";DataRecebimento:" + DataRecebimento + ";DataVencimento:" + DataVencimento + ";Requerente:" + Requerente + ";Requerido:" + Requerido + ";ValorAcao:" + ValorAcao + ";NumeroGuiaCompleto:" + NumeroGuiaCompleto + ";NumeroDUAM:" + NumeroDUAM + ";QuantidadeParcelasDUAM:" + QuantidadeParcelasDUAM + ";DataVencimentoDUAM:" + DataVencimentoDUAM + ";ValorImpostoMunicipalDUAM:" + ValorImpostoMunicipalDUAM + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
