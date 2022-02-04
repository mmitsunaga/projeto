package br.gov.go.tj.projudi.dt;

public class ParametroComutacaoExecucaoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -696026107064630500L;
	private String Id_ParametroComutacaoExecucao;
	private String DataDecreto;


	private String FracaoHediondo;
	private String FracaoComum;
	private String FracaoComumReinc;
	private String FracaoComumFeminino;
	private String FracaoComumReincFeminino;
	private String PenaUnificada;
	private String BeneficioAcumulado;
	private String CodigoTemp;

//---------------------------------------------------------
	public ParametroComutacaoExecucaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ParametroComutacaoExecucao;}
	public void setId(String valor ) {if(valor!=null) Id_ParametroComutacaoExecucao = valor;}
	public String getDataDecreto()  {return DataDecreto;}
	public void setDataDecreto(String valor ) {if (valor!=null) DataDecreto = valor;}
	public String getFracaoHediondo()  {return FracaoHediondo;}
	public void setFracaoHediondo(String valor ) {if (valor!=null) FracaoHediondo = valor;}
	public String getFracaoComum()  {return FracaoComum;}
	public void setFracaoComum(String valor ) {if (valor!=null) FracaoComum = valor;}
	public String getFracaoComumReinc()  {return FracaoComumReinc;}
	public void setFracaoComumReinc(String valor ) {if (valor!=null) FracaoComumReinc = valor;}
	public String getFracaoComumFeminino()  {return FracaoComumFeminino;}
	public void setFracaoComumFeminino(String valor ) {if (valor!=null) FracaoComumFeminino = valor;}
	public String getFracaoComumReincFeminino()  {return FracaoComumReincFeminino;}
	public void setFracaoComumReincFeminino(String valor ) {if (valor!=null) FracaoComumReincFeminino = valor;}
	public String getPenaUnificada()  {return PenaUnificada;}
	public void setPenaUnificada(String valor ) {if (valor!=null) PenaUnificada = valor;}
	public String getBeneficioAcumulado()  {return BeneficioAcumulado;}
	public void setBeneficioAcumulado(String valor ) {if (valor!=null) BeneficioAcumulado = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ParametroComutacaoExecucaoDt objeto){
		 if (objeto==null) return;
		Id_ParametroComutacaoExecucao = objeto.getId();
		DataDecreto = objeto.getDataDecreto();
		FracaoHediondo = objeto.getFracaoHediondo();
		FracaoComum = objeto.getFracaoComum();
		FracaoComumReinc = objeto.getFracaoComumReinc();
		FracaoComumFeminino = objeto.getFracaoComumFeminino();
		FracaoComumReincFeminino = objeto.getFracaoComumReincFeminino();
		PenaUnificada = objeto.getPenaUnificada();
		BeneficioAcumulado = objeto.getBeneficioAcumulado();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ParametroComutacaoExecucao="";
		DataDecreto="";
		FracaoHediondo="";
		FracaoComum="";
		FracaoComumReinc="";
		FracaoComumFeminino="";
		FracaoComumReincFeminino="";
		PenaUnificada="";
		BeneficioAcumulado="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ParametroComutacaoExecucao:" + Id_ParametroComutacaoExecucao + ";DataDecreto:" + DataDecreto + ";FracaoHediondo:" + FracaoHediondo + ";FracaoComum:" + FracaoComum + ";FracaoComumReinc:" + FracaoComumReinc + ";FracaoComumFeminino:" + FracaoComumFeminino + ";FracaoComumReincFeminino:" + FracaoComumReincFeminino+ ";PenaUnificada:" + PenaUnificada+ ";BeneficioAcumulado:" + BeneficioAcumulado + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
