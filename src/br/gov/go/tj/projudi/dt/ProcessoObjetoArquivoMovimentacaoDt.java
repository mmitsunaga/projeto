package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class ProcessoObjetoArquivoMovimentacaoDt extends ProcessoObjetoArquivoMovimentacaoDtGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8300515859530233803L;
	
	protected String FavorecidoLeilao;
	
	public void limpar(){
		Id_ProcessoObjetoArquivoMovimentacao="";
		ProcessoObjetoArquivoMovimentacao="";
		Id_ProcessoObjetoArquivo="";
		ProcessoObjetoArquivo="";
		DataMovimentacao="";
		DataRetorno="";
		CodigoTemp="";
		FavorecidoLeilao ="";
	}
	
	public String toJson(){
		return "{\"Id\":\"" + getId() 
		+ "\",\"ProcessoObjetoArquivoMovimentacao\":\"" + getProcessoObjetoArquivoMovimentacao() 
		+ "\",\"DataMovimentacao\":\"" + getDataMovimentacao() 
		+ "\",\"DataRetorno\":\"" + getDataRetorno() 
		+ "\",\"ProcessoObjetoArquivo\":\"" + getProcessoObjetoArquivo() 
		+ "\",\"CodigoTemp\":\"" + getCodigoTemp() 
		+ "\",\"Id_ProcessoObjetoArquivo\":\"" + getId_ProcessoObjetoArquivo()		
		+ "\"}";
	}

	public String getFavorecidoLeilao() { 
		return FavorecidoLeilao;
	}
	
	public void setFavorecidoLeilao(String favorecido) {
		FavorecidoLeilao = favorecido;
	}

	public boolean isEncaminarLeilao() {
		// TODO Auto-generated method stub
		return "Emcaminhado para Leilao".equals(ProcessoObjetoArquivoMovimentacao);
	}

	public boolean isTemFavorecidoLeilao() {
		return (FavorecidoLeilao!=null && !FavorecidoLeilao.isEmpty());
	}

}
