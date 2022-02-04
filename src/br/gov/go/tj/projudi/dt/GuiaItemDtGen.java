package br.gov.go.tj.projudi.dt;

public class GuiaItemDtGen extends Dados{

	private static final long serialVersionUID = 6757244833083383084L;
	private String Id_GuiaItem;
	private String GuiaItem;


	private String Id_GuiaEmissao;
	private String Id_Custa;
	private String Id_Processo;
	private String Id_ProcessoTipo;
	private String Id_Serventia;
	private String Id_Comarca;
	private String Id_GuiaStatus;
	private String DataRecebimento;
	private String DataEmissao;
	private String DataVencimento;
	private String NumeroGuiaCompleto;
	private String Id_GuiaModelo;
	private String Custa;
	private String CodigoRegimento;
	private String ReferenciaCalculo;
	private String GuiaItemCodigo;
	private String Quantidade;
	private String ValorCalculado;
	private String ValorReferencia;
	private String Parcelas;
	private String ParcelaCorrente;
	private String CodigoTemp;
	private String CodigoOficial;
	private String Id_ArrecadacaoCusta;
	private String ArrecadacaoCusta;
	private String CodigoArrecadacao;

//---------------------------------------------------------
	public GuiaItemDtGen() {

		limpar();

	}

	public String getId()  {return Id_GuiaItem;}
	public void setId(String valor ) {if(valor!=null) Id_GuiaItem = valor;}
	public String getGuiaItem()  {return GuiaItem;}
	public void setGuiaItem(String valor ) {if (valor!=null) GuiaItem = valor;}
	public String getId_GuiaEmissao()  {return Id_GuiaEmissao;}
	public void setId_GuiaEmissao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_GuiaEmissao = ""; Id_Custa = "";}else if (!valor.equalsIgnoreCase("")) Id_GuiaEmissao = valor;}
	public String getId_Custa()  {return Id_Custa;}
	public void setId_Custa(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Custa = ""; Id_Processo = "";}else if (!valor.equalsIgnoreCase("")) Id_Custa = valor;}
	public String getId_Processo()  {return Id_Processo;}
	public void setId_Processo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Processo = ""; Id_ProcessoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_Processo = valor;}
	public String getId_ProcessoTipo()  {return Id_ProcessoTipo;}
	public void setId_ProcessoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoTipo = ""; Id_Serventia = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoTipo = valor;}
	public String getId_Serventia()  {return Id_Serventia;}
	public void setId_Serventia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Serventia = ""; Id_Comarca = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia = valor;}
	public String getId_Comarca()  {return Id_Comarca;}
	public void setId_Comarca(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Comarca = ""; Id_GuiaStatus = "";}else if (!valor.equalsIgnoreCase("")) Id_Comarca = valor;}
	public String getId_GuiaStatus()  {return Id_GuiaStatus;}
	public void setId_GuiaStatus(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_GuiaStatus = ""; DataRecebimento = "";}else if (!valor.equalsIgnoreCase("")) Id_GuiaStatus = valor;}
	public String getDataRecebimento()  {return DataRecebimento;}
	public void setDataRecebimento(String valor ) {if (valor!=null) DataRecebimento = valor;}
	public String getDataEmissao()  {return DataEmissao;}
	public void setDataEmissao(String valor ) {if (valor!=null) DataEmissao = valor;}
	public String getDataVencimento()  {return DataVencimento;}
	public void setDataVencimento(String valor ) {if (valor!=null) DataVencimento = valor;}
	public String getNumeroGuiaCompleto()  {return NumeroGuiaCompleto;}
	public void setNumeroGuiaCompleto(String valor ) {if (valor!=null) NumeroGuiaCompleto = valor;}
	public String getId_GuiaModelo()  {return Id_GuiaModelo;}
	public void setId_GuiaModelo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_GuiaModelo = ""; Custa = "";}else if (!valor.equalsIgnoreCase("")) Id_GuiaModelo = valor;}
	public String getCusta()  {return Custa;}
	public void setCusta(String valor ) {if (valor!=null) Custa = valor;}
	public String getCodigoRegimento()  {return CodigoRegimento;}
	public void setCodigoRegimento(String valor ) {if (valor!=null) CodigoRegimento = valor;}
	public String getReferenciaCalculo()  {return ReferenciaCalculo;}
	public void setReferenciaCalculo(String valor ) {if (valor!=null) ReferenciaCalculo = valor;}
	public String getGuiaItemCodigo()  {return GuiaItemCodigo;}
	public void setGuiaItemCodigo(String valor ) {if (valor!=null) GuiaItemCodigo = valor;}
	public String getQuantidade()  {return Quantidade;}
	public void setQuantidade(String valor ) {if (valor!=null) Quantidade = valor;}
	public String getValorCalculado()  {return ValorCalculado;}
	public void setValorCalculado(String valor ) {if (valor!=null) ValorCalculado = valor;}
	public String getValorReferencia()  {return ValorReferencia;}
	public void setValorReferencia(String valor ) {if (valor!=null) ValorReferencia = valor;}
	public String getParcelas()  {return Parcelas;}
	public void setParcelas(String valor ) {if (valor!=null) Parcelas = valor;}
	public String getParcelaCorrente()  {return ParcelaCorrente;}
	public void setParcelaCorrente(String valor ) {if (valor!=null) ParcelaCorrente = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getCodigoOficial()  {return CodigoOficial;}
	public void setCodigoOficial(String valor ) {if (valor!=null) CodigoOficial = valor;}
	public String getId_ArrecadacaoCusta()  {return Id_ArrecadacaoCusta;}
	public void setId_ArrecadacaoCusta(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ArrecadacaoCusta = ""; ArrecadacaoCusta = "";}else if (!valor.equalsIgnoreCase("")) Id_ArrecadacaoCusta = valor;}
	public String getArrecadacaoCusta()  {return ArrecadacaoCusta;}
	public void setArrecadacaoCusta(String valor ) {if (valor!=null) ArrecadacaoCusta = valor;}
	public String getCodigoArrecadacao()  {return CodigoArrecadacao;}
	public void setCodigoArrecadacao(String valor ) {if (valor!=null) CodigoArrecadacao = valor;}


	public void copiar(GuiaItemDt objeto){
		 if (objeto==null) return;
		Id_GuiaItem = objeto.getId();
		GuiaItem = objeto.getGuiaItem();
		Id_GuiaEmissao = objeto.getId_GuiaEmissao();
		Id_Custa = objeto.getId_Custa();
		Id_Processo = objeto.getId_Processo();
		Id_ProcessoTipo = objeto.getId_ProcessoTipo();
		Id_Serventia = objeto.getId_Serventia();
		Id_Comarca = objeto.getId_Comarca();
		Id_GuiaStatus = objeto.getId_GuiaStatus();
		DataRecebimento = objeto.getDataRecebimento();
		DataEmissao = objeto.getDataEmissao();
		DataVencimento = objeto.getDataVencimento();
		NumeroGuiaCompleto = objeto.getNumeroGuiaCompleto();
		Id_GuiaModelo = objeto.getId_GuiaModelo();
		Custa = objeto.getCusta();
		CodigoRegimento = objeto.getCodigoRegimento();
		ReferenciaCalculo = objeto.getReferenciaCalculo();
		GuiaItemCodigo = objeto.getGuiaItemCodigo();
		Quantidade = objeto.getQuantidade();
		ValorCalculado = objeto.getValorCalculado();
		ValorReferencia = objeto.getValorReferencia();
		Parcelas = objeto.getParcelas();
		ParcelaCorrente = objeto.getParcelaCorrente();
		CodigoTemp = objeto.getCodigoTemp();
		CodigoOficial = objeto.getCodigoOficial();
		Id_ArrecadacaoCusta = objeto.getId_ArrecadacaoCusta();
		ArrecadacaoCusta = objeto.getArrecadacaoCusta();
		CodigoArrecadacao = objeto.getCodigoArrecadacao();
	}

	public void limpar(){
		Id_GuiaItem="";
		GuiaItem="";
		Id_GuiaEmissao="";
		Id_Custa="";
		Id_Processo="";
		Id_ProcessoTipo="";
		Id_Serventia="";
		Id_Comarca="";
		Id_GuiaStatus="";
		DataRecebimento="";
		DataEmissao="";
		DataVencimento="";
		NumeroGuiaCompleto="";
		Id_GuiaModelo="";
		Custa="";
		CodigoRegimento="";
		ReferenciaCalculo="";
		GuiaItemCodigo="";
		Quantidade="";
		ValorCalculado="";
		ValorReferencia="";
		Parcelas="";
		ParcelaCorrente="";
		CodigoTemp="";
		CodigoOficial="";
		Id_ArrecadacaoCusta="";
		ArrecadacaoCusta="";
		CodigoArrecadacao="";
	}


	public String getPropriedades(){
		return "[Id_GuiaItem:" + Id_GuiaItem + ";GuiaItem:" + GuiaItem + ";Id_GuiaEmissao:" + Id_GuiaEmissao + ";Id_Custa:" + Id_Custa + ";Id_Processo:" + Id_Processo + ";Id_ProcessoTipo:" + Id_ProcessoTipo + ";Id_Serventia:" + Id_Serventia + ";Id_Comarca:" + Id_Comarca + ";Id_GuiaStatus:" + Id_GuiaStatus + ";DataRecebimento:" + DataRecebimento + ";DataEmissao:" + DataEmissao + ";DataVencimento:" + DataVencimento + ";NumeroGuiaCompleto:" + NumeroGuiaCompleto + ";Id_GuiaModelo:" + Id_GuiaModelo + ";Custa:" + Custa + ";CodigoRegimento:" + CodigoRegimento + ";ReferenciaCalculo:" + ReferenciaCalculo + ";GuiaItemCodigo:" + GuiaItemCodigo + ";Quantidade:" + Quantidade + ";ValorCalculado:" + ValorCalculado + ";ValorReferencia:" + ValorReferencia + ";Parcelas:" + Parcelas + ";ParcelaCorrente:" + ParcelaCorrente + ";CodigoTemp:" + CodigoTemp + ";CodigoOficial:" + CodigoOficial + ";Id_ArrecadacaoCusta:" + Id_ArrecadacaoCusta + ";ArrecadacaoCusta:" + ArrecadacaoCusta + ";CodigoArrecadacao:" + CodigoArrecadacao + "]";
	}


} 
