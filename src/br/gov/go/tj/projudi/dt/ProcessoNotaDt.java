package br.gov.go.tj.projudi.dt;

public class ProcessoNotaDt extends ProcessoNotaDtGen{

	private static final long serialVersionUID = -4361022854334531271L;
	public static final int CodigoPermissao=7258;
	
	private String Id_Serventia;

	public void setId_Serventia(String id_Serventia) {
		Id_Serventia = id_Serventia;	
	}
	
	public String getId_Serventia(){
		return Id_Serventia;
	}
	
	public void limpar(){
		Id_ProcessoNota="";
		Id_UsuarioServentia="";
		Nome="";
		Id_Processo="";
		ProcNumero="";
		ProcessoNota="";
		DataCriacao="";
		CodigoTemp="";
		Id_Serventia="";
	}
	
	public String getPropriedades(){
		return "[Id_ProcessoNota:" + Id_ProcessoNota + ";Id_UsuarioServentia:" + Id_UsuarioServentia + ";Id_Serventia:" + Id_Serventia + ";Nome:" + Nome + ";Id_Processo:" + Id_Processo + ";ProcNumero:" + ProcNumero + ";ProcessoNota:" + ProcessoNota + ";DataCriacao:" + DataCriacao + ";CodigoTemp:" + CodigoTemp + "]";
	}
}
