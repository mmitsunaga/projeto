package br.gov.go.tj.projudi.dt;

public class ProcessoArquivamentoTipoDt extends ProcessoArquivamentoTipoDtGen{

	private static final long serialVersionUID = 7069955578382633620L;
	public static final int CodigoPermissao=316;
	
	public static final String FLUXO_PROCESSO_ARQUIVAMENTO_TIPO_BLOQUEIO = "1";
	public static final String FLUXO_PROCESSO_ARQUIVAMENTO_TIPO_DESBLOQUEIO = "2";
	
	public static final int INATIVO = -1;
	public static final int ATIVO = 0;
	
	public boolean isAtivo(){
		if (this.getCodigoTemp() == null) {
			return false;
		}
		return (this.getCodigoTemp().trim().equalsIgnoreCase(String.valueOf(ATIVO)));
	}	

}
