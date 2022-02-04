package br.gov.go.tj.projudi.dt;

public class ProcessoParteGuiaEmissaoConsultaDt extends Dados {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 7125681855702159731L;
	
	public ProcessoParteGuiaEmissaoConsultaDt()
	{		
	}
	
	public ProcessoParteGuiaEmissaoConsultaDt(ProcessoParteDt processoParte)
	{
		if(processoParte != null) {
			this.IdProcessoParte = processoParte.getId();
			this.Nome = processoParte.getNome();
		}
	}
	
	private String IdProcessoParte;

	@Override
	public void setId(String id) {
		this.IdProcessoParte = id;
	}

	@Override
	public String getId() {
		return this.IdProcessoParte;
	}
	
	private String Nome;
	
	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		this.Nome = nome;
	}
}
