package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

public class ProcessoParteTipoDt extends ProcessoParteTipoDtGen {

    private static final long serialVersionUID = -2597237771647524072L;
    public static final int CodigoPermissao = 188;
    
    public static final int ID_POLO_ATIVO = 2;
    public static final int ID_POLO_PASSIVO = 3;

	public static final int POLO_PASSIVO_CODIGO = 0;
	public static final int POLO_ATIVO_CODIGO = 1;
	public static final int TERCEIRO = 2;
	public static final int TESTEMUNHA = 3;
	
	//Os três próximos são utilizados para importação arquivo SSP
	public static final int COMUNICANTE = 4;
		
	public static final int CURADOR = 7;
	public static final int LITIS_CONSORTE_ATIVO = 8;
	public static final int LITIS_CONSORTE_PASSIVO = 9;
	public static final int SUBSTITUTO_PROCESSUAL = 11;
	
	public static final int PERITO = 12;
	public static final int ARREMATANTE = 13;
	
	public static final int PACIENTE = 132;
	public static final int VITIMA = 134;
	public static final int CURATELADO = 56;
	public static final int INTERESSADO = 77;
	
	public boolean isPaciente(){
		if ((Funcoes.StringToInt(getProcessoParteTipoCodigo())) == ProcessoParteTipoDt.PACIENTE){
			return true;
		}
		
		return false;
	}
}
