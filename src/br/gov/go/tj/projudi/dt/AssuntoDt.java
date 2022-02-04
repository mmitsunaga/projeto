package br.gov.go.tj.projudi.dt;

public class AssuntoDt extends AssuntoDtGen {

	private static final long serialVersionUID = -1379757272533363387L;
	public static final int CodigoPermissao = 285;
	
	public static final int ACORDO_NAO_PERSECUCAO_PENAL = 7942;
	
	public boolean isAtivo(){
		if (getIsAtivo()!= null && (getIsAtivo().equals("1") || getIsAtivo().equals("true")))
			return true;
		return false;					
	}

}
