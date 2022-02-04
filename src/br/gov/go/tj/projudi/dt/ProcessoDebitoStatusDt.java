package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class ProcessoDebitoStatusDt extends ProcessoDebitoStatusDtGen {

   
	/**
	 * 
	 */
	private static final long serialVersionUID = -6904011135086703724L;
	
	public static final int CodigoPermissao = 914;
	
	public static final int NENHUM = 0;
	public static final int NOVO = 1;
	public static final int EM_ANALISE_PELO_FINANCEIRO = 2;
	public static final int DEVOLVIDO_PELO_FINANCEIRO = 3;
	public static final int SUSPENSO = 4;
	public static final int LIBERADO_PARA_ENVIO_CADIN = 5;
	public static final int ENVIADO_CADIN = 6;
	public static final int BAIXADO = 7;
	
//

}
