package br.gov.go.tj.projudi.dt;

public class ContaJudicialMovimentacaoDtGen extends Dados{

	protected String Id_ContaJudicialMovi;
	protected String NumeroParcela;

	protected String Id_ContaJudicial;

	protected String Valor;
	protected String Data;
	protected String TipoPagamento;
	protected String Situacao;
	protected String CodigoTemp;
	protected String Id_Deposito;

//---------------------------------------------------------
	public ContaJudicialMovimentacaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ContaJudicialMovi;}
	public void setId(String valor ) {if(valor!=null) Id_ContaJudicialMovi = valor;}
	public String getNumeroParcela()  {return NumeroParcela;}
	public void setNumeroParcela(String valor ) {if (valor!=null) NumeroParcela = valor;}
	public String getId_ContaJudicial()  {return Id_ContaJudicial;}
	public void setId_ContaJudicial(String valor ) {if (valor!=null) Id_ContaJudicial = valor;}
	public String getValor()  {return Valor;}
	public void setValor(String valor ) {if (valor!=null) Valor = valor;}
	public String getData()  {return Data;}
	public void setData(String valor ) {if (valor!=null) Data = valor;}
	public String getTipoPagamento()  {return TipoPagamento;}
	public void setTipoPagamento(String valor ) {if (valor!=null) TipoPagamento = valor;}
	public String getSituacao()  {return Situacao;}
	public void setSituacao(String valor ) {if (valor!=null) Situacao = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getId_Deposito()  {return Id_Deposito;}
	public void setId_Deposito(String valor ) {if (valor!=null) Id_Deposito = valor;}


	public void copiar(ContaJudicialMovimentacaoDt objeto){
		 if (objeto==null) return;
		Id_ContaJudicialMovi = objeto.getId();
		Id_ContaJudicial = objeto.getId_ContaJudicial();
		NumeroParcela = objeto.getNumeroParcela();
		Valor = objeto.getValor();
		Data = objeto.getData();
		TipoPagamento = objeto.getTipoPagamento();
		Situacao = objeto.getSituacao();
		CodigoTemp = objeto.getCodigoTemp();
		Id_Deposito = objeto.getId_Deposito();
	}

	public void limpar(){
		Id_ContaJudicialMovi="";
		Id_ContaJudicial="";
		NumeroParcela="";
		Valor="";
		Data="";
		TipoPagamento="";
		Situacao="";
		CodigoTemp="";
		Id_Deposito="";
	}


	public String getPropriedades(){
		return "[Id_ContaJudicialMovi:" + Id_ContaJudicialMovi + ";Id_ContaJudicial:" + Id_ContaJudicial + ";NumeroParcela:" + NumeroParcela + ";Valor:" + Valor + ";Data:" + Data + ";TipoPagamento:" + TipoPagamento + ";Situacao:" + Situacao + ";CodigoTemp:" + CodigoTemp + ";Id_Deposito:" + Id_Deposito + "]";
	}


} 
