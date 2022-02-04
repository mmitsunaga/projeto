package br.gov.go.tj.projudi.dt;

public class ProcessoPrioridadeDt extends ProcessoPrioridadeDtGen {

	private static final long serialVersionUID = -8675129476665647811L;

	public static final int CodigoPermissao = 227;
		
	public static final int NORMAL = 0;
	public static final int LIMINAR = 6;
	public static final int MAIOR_60_ANOS = 3;
	public static final int ANTECIPACAO_TUTELA = 5;
	public static final int DOENCA_GRAVE = 2;
	public static final int REU_PRESO = 1;
	public static final int MAIOR_80_ANOS = 4;
	public static final int ADOCAO = 8;
	public static final int DESTITUICAO_PODER_FAMILIAR = 9;		
	public static final int PESSOA_COM_DEFICIENCIA = 10;
	
    protected String Id_ProcessoPrioridade;
	protected String ProcessoPrioridade;
	protected String ProcessoPrioridadeCodigo;
	protected String ProcessoPrioridadeOrdem;
	
	public String getId()  {return Id_ProcessoPrioridade;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoPrioridade = valor;}
	public String getProcessoPrioridade()  {return ProcessoPrioridade;}
	public void setProcessoPrioridade(String valor ) {if (valor!=null) ProcessoPrioridade = valor;}
	public String getProcessoPrioridadeCodigo()  {return ProcessoPrioridadeCodigo;}
	public void setProcessoPrioridadeCodigo(String valor ) {if (valor!=null) ProcessoPrioridadeCodigo = valor;}
	public String getProcessoPrioridadeOrdem()  {return ProcessoPrioridadeOrdem;}
	public void setProcessoPrioridadeOrdem(String valor ) {if(valor!=null) ProcessoPrioridadeOrdem = valor;}

	public void copiar(ProcessoPrioridadeDt objeto){
		Id_ProcessoPrioridade = objeto.getId();
		ProcessoPrioridade = objeto.getProcessoPrioridade();
		ProcessoPrioridadeCodigo = objeto.getProcessoPrioridadeCodigo();
		CodigoTemp = objeto.getCodigoTemp();
		ProcessoPrioridadeOrdem = objeto.getProcessoPrioridadeOrdem();
	}

	public void limpar(){
		Id_ProcessoPrioridade="";
		ProcessoPrioridade="";
		ProcessoPrioridadeCodigo="";
		CodigoTemp="";
		ProcessoPrioridadeOrdem="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoPrioridade:" + Id_ProcessoPrioridade + ";ProcessoPrioridade:" + ProcessoPrioridade + ";ProcessoPrioridadeCodigo:" +
	      ProcessoPrioridadeCodigo + ";ProcessoPrioridadeOrdem:" + ProcessoPrioridadeOrdem + ";CodigoTemp:" + CodigoTemp + "]";
	}
	public static boolean isCodigoPrioridade(int codigo) {
		if(codigo==NORMAL || codigo==LIMINAR || codigo==MAIOR_60_ANOS || codigo==ANTECIPACAO_TUTELA || codigo==DOENCA_GRAVE || codigo==REU_PRESO || codigo==MAIOR_80_ANOS || codigo==ADOCAO || codigo==DESTITUICAO_PODER_FAMILIAR || codigo==PESSOA_COM_DEFICIENCIA) {
			return true;
		} else {
			return false;
		}
	}

}


