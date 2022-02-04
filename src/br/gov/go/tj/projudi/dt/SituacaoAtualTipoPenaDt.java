package br.gov.go.tj.projudi.dt;

public class SituacaoAtualTipoPenaDt extends Dados{

	private static final long serialVersionUID = -1777527751105918862L;
	private String idSituacaoAtualTipoPena;
	private String idPenaExecucaoTipo;
	private String penaExecucaoTipo;
	private String idSituacaoAtualExecucao;

	public SituacaoAtualTipoPenaDt() {
		limpar();
	}
	public String getId()  {return idSituacaoAtualTipoPena;}
	public void setId(String valor ) {if(valor!=null) idSituacaoAtualTipoPena = valor;}
	
	public String getIdPenaExecucaoTipo() {return idPenaExecucaoTipo; }
	public void setIdPenaExecucaoTipo(String idPenaExecucaoTipo) {this.idPenaExecucaoTipo = idPenaExecucaoTipo; }

	public String getPenaExecucaoTipo() {return penaExecucaoTipo; }
	public void setPenaExecucaoTipo(String penaExecucaoTipo) {this.penaExecucaoTipo = penaExecucaoTipo; }

	public String getIdSituacaoAtualExecucao() {return idSituacaoAtualExecucao; }
	public void setIdSituacaoAtualExecucao(String idSituacaoAtualExecucao) {this.idSituacaoAtualExecucao = idSituacaoAtualExecucao; }

	public void copiar(SituacaoAtualTipoPenaDt objeto){
		idSituacaoAtualTipoPena = objeto.getId();
		idPenaExecucaoTipo = objeto.getIdPenaExecucaoTipo();
		idSituacaoAtualExecucao = objeto.getIdSituacaoAtualExecucao();
	}

	public void limpar(){
		idSituacaoAtualTipoPena="";
		idPenaExecucaoTipo="";
		idSituacaoAtualExecucao="";
		penaExecucaoTipo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[idSituacaoAtualTipoPena:" + idSituacaoAtualTipoPena + ";idPenaExecucaoTipo:" + idPenaExecucaoTipo + ";idSituacaoAtualExecucao:" + idSituacaoAtualExecucao + "]";
	}


} 
