package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class ProcessoStatusDt extends ProcessoStatusDtGen{

	/**
     * 
     */
    private static final long serialVersionUID = 8830161807684824542L;

    public static final int CodigoPermissao=184;
	
	public static final int ATIVO = 0;
	public static final int ARQUIVADO = 1;
	public static final int SUSPENSO = 2;
	public static final int ARQUIVADO_PROVISORIAMENTE = 3;
	public static final int ATIVO_PROVISORIAMENTE = 4;
	public static final int CALCULO = 6;
	public static final int ERRO_MIGRACAO = 7;
	public static final int SIGILOSO = 8;

	public static int ID_ATIVO = 1;
	
	public ProcessoStatusDt() {
		super();
	}
	
	public ProcessoStatusDt(String id, String descricao, String codigo) {
		setId(id);
		setProcessoStatus(descricao);
		setProcessoStatusCodigo(codigo);
	}
	
	
	
	

}
