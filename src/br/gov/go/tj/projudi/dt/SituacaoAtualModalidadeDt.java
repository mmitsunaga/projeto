package br.gov.go.tj.projudi.dt;

public class SituacaoAtualModalidadeDt extends Dados{

	private static final long serialVersionUID = -7064921264923055106L;
	private String idSituacaoAtualModalidade;
	private String idModalidade;
	private String modalidade;
	private String idSituacaoAtualExecucao;

	public SituacaoAtualModalidadeDt() {
		limpar();
	}
	public String getId()  {return idSituacaoAtualModalidade;}
	public void setId(String valor ) {if(valor!=null) idSituacaoAtualModalidade = valor;}
	
	public String getIdModalidade() {return idModalidade; }
	public void setIdModalidade(String idModalidade) {this.idModalidade = idModalidade; }

	public String getModalidade() {return modalidade; }
	public void setModalidade(String modalidade) {this.modalidade = modalidade; }

	public String getIdSituacaoAtualExecucao() {return idSituacaoAtualExecucao; }
	public void setIdSituacaoAtualExecucao(String idSituacaoAtualExecucao) {this.idSituacaoAtualExecucao = idSituacaoAtualExecucao; }

	public void copiar(SituacaoAtualModalidadeDt objeto){
		idSituacaoAtualModalidade = objeto.getId();
		idModalidade = objeto.getIdModalidade();
		idSituacaoAtualExecucao = objeto.getIdSituacaoAtualExecucao();
	}

	public void limpar(){
		idSituacaoAtualModalidade="";
		idModalidade="";
		idSituacaoAtualExecucao="";
		modalidade="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[idSituacaoAtualTipoPena:" + idSituacaoAtualModalidade + ";idModalidade:" + idModalidade + ";idSituacaoAtualExecucao:" + idSituacaoAtualExecucao + "]";
	}


} 
