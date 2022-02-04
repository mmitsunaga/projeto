package br.gov.go.tj.projudi.dt;

public class TarefaDt extends TarefaDtGen{

	private static final long serialVersionUID = 4263483546853996901L;
    public static final int CodigoPermissao=623;
    public static final int CodigoPermissaoVistarTarefa=663;
    public static final int CodigoPermissaoTarefaFinalizar=664;
    
    private String DataCriacao;

	public String getDataCriacao() {
		return DataCriacao;
	}

	public void setDataCriacao(String dataCriacao) {
		if(DataCriacao != null){
			DataCriacao = dataCriacao;
		}
	}
	
	public void copiar(TarefaDt objeto){
		 if (objeto==null){
			 return;
		 }
		 super.copiar(objeto);
		 DataCriacao = objeto.getDataCriacao();
	}

	public void limpar(){
		super.limpar();
		DataCriacao = "";
	}

	public String getPropriedades(){
		return "[Id_Tarefa:" + getId() + ";DataCriacao:" + getDataCriacao() +";Tarefa:" + getTarefa() + ";Descricao:" + getDescricao() + ";Resposta:" + getResposta() + ";DataInicio:" + getDataInicio() + ";Previsao:" + getPrevisao() + ";DataFim:" + getDataFim() + ";Id_TarefaPai:" + getId_TarefaPai() + ";TarefaPai:" + getTarefaPai() + ";PontosApf:" + getPontosApf() + ";PontosApg:" + getPontosApg() + ";Id_TarefaPrioridade:" + getId_TarefaPrioridade() + ";TarefaPrioridade:" + getTarefaPrioridade() + ";Id_TarefaStatus:" + getId_TarefaStatus() + ";TarefaStatusCodigo:" + getTarefaStatusCodigo() +";TarefaStatus:" + getTarefaStatus() + ";Id_TarefaTipo:" + getId_TarefaTipo() + ";TarefaTipo:" + getTarefaTipo() + ";Id_Projeto:" + getId_Projeto() + ";Projeto:" + getProjeto() + ";Id_ProjetoParticipanteResponsavel:" + getId_ProjetoParticipanteResponsavel() + ";ProjetoParticipanteResponsavel:" + getProjetoParticipanteResponsavel() + ";Id_UsuarioCriador:" + getId_UsuarioCriador() + ";UsuarioCriador:" + getUsuarioCriador() + ";Id_UsuarioFinalizador:" + getId_UsuarioFinalizador() + ";UsuarioFinalizador:" + getUsuarioFinalizador() + ";CodigoTemp:" + getCodigoTemp() + "]";
	}
}
