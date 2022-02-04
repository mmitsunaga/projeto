package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.ValidacaoUtil;


public class ProcessoTemaDt extends ProcessoTemaDtGen{
	private static final long serialVersionUID = 8595752320470637985L;
	public static final int CodigoPermissao=812;

	protected String Titulo;
	protected String TemaTipo;
	protected String TemaTipoCnj;
	protected String TemaOrigem;
	protected String TemaSituacao;
	protected String TemaSituacaoCnj;
	protected ProcessoDt processo;
	protected TemaDt tema;
	
	protected String aplicaTese;
	protected String julgaMerito;
	protected String motivo;
	protected String dataFinalSobrestado;
	private String id_Usuario;
	
	public String getTitulo()  {return Titulo;}
	public void setTitulo(String valor ) {if (valor!=null) Titulo = valor;}
	public String getTemaTipo()  {return TemaTipo;}
	public void setTemaTipo(String valor ) {if (valor!=null) TemaTipo = valor;}
	public String getTemaTipoCnj() { return TemaTipoCnj; }
	public void setTemaTipoCnj(String valor) { if (valor != null) TemaTipoCnj = valor;}
	public String getTemaOrigem()  {return TemaOrigem;}
	public void setTemaOrigem(String valor ) {if (valor!=null) TemaOrigem = valor;}
	public String getTemaSituacao()  {return TemaSituacao;}
	public void setTemaSituacao(String valor ) {if (valor!=null) TemaSituacao = valor;}
	public String getTemaSituacaoCnj() { return TemaSituacaoCnj; }
	public void setTemaSituacaoCnj(String valor) { if(valor != null) TemaSituacaoCnj = valor; }
	public String getMotivo() {return motivo;}
	public void setMotivo(String motivo) {this.motivo = motivo;}
	public String getDataFinalSobrestado() {return dataFinalSobrestado;}
	public void setDataFinalSobrestado(String dataFinalSobrestado) {this.dataFinalSobrestado = dataFinalSobrestado;}
	public String getId_Usuario() {return id_Usuario;}
	public void setId_Usuario(String id_Usuario) {this.id_Usuario = id_Usuario;}
	public String getAplicaTese() { return aplicaTese;}
	public void setAplicaTese(String valor) {if (valor!=null) this.aplicaTese = valor;}
	public String getJulgaMerito() { return julgaMerito; }
	public void setJulgaMerito(String valor) { if (valor!=null) this.julgaMerito = valor; }
	
	public ProcessoDt getProcesso() { return processo; }
	public void setProcesso(ProcessoDt processo) { this.processo = processo;}
	public String getTemaCodigoTitulo()  {return TemaCodigo + " - " + Titulo;}
	public TemaDt getTema() { return tema == null ? new TemaDt() : tema;}
	public void setTema(TemaDt valor) { if (valor != null) this.tema = valor;}

	@Override
	public void limpar() {
		super.limpar();
		TemaTipo = "";
		TemaOrigem = "";
		TemaSituacao = "";
	}
	
	@Override
	public void copiar(ProcessoTemaDt objeto) {
		if(objeto==null) return;
		TemaTipo = objeto.getTemaTipo();
		TemaOrigem = objeto.getTemaOrigem();
		TemaSituacao = objeto.getTemaSituacao();
		super.copiar(objeto);
	}
	
	/**
	 * Se TeseFirmada = 1, JulgaMerito = 1 se InfoProcessual != 1, senão 0
	 * Senao, retorna o valor de julgaMerito
	 */
	public String getJulgaMeritoParaTeseFirmada(){
		if (ValidacaoUtil.isVazio(getAplicaTese())) setAplicaTese("0");
		if (ValidacaoUtil.isVazio(getJulgaMerito())) setJulgaMerito("0");
		if (ValidacaoUtil.isNulo(getTema())) setTema(new TemaDt());
		if (ValidacaoUtil.isVazio(getTema().getOpcaoProcessual())) getTema().setOpcaoProcessual("0");
		return getAplicaTese().equals("1") ? getTema().getOpcaoProcessual().equals("0") ? "1" : "0" : getJulgaMerito();
	}
	
}