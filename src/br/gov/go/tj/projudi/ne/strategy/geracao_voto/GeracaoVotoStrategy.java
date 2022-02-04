package br.gov.go.tj.projudi.ne.strategy.geracao_voto;


import java.util.function.Consumer;
import java.util.function.Predicate;

import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.ne.AudienciaProcessoNe;
import br.gov.go.tj.projudi.ne.VotoNe;
import br.gov.go.tj.utils.FabricaConexao;


public class GeracaoVotoStrategy{
	protected AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
	protected VotoNe votoNe = new VotoNe();
	
	protected String idAudienciaProcesso;
	protected String idPendencia;
	protected String idUsuario;
	protected FabricaConexao fabrica;

	protected VotoDt votoDt;

	protected int tipo = -1;

	private void preGerar() throws Exception {
		votoDt = new VotoDt();
		
		PendenciaDt pendenciaDt = new PendenciaDt();
		pendenciaDt.setId(idPendencia);
		
		AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoDt();
		audienciaProcessoDt.setId(idAudienciaProcesso);
		
		votoDt.setVotoTipoCodigo(String.valueOf(tipo));
		votoDt.setId_UsuarioLog(idUsuario);
		votoDt.setAudienciaProcessoDt(audienciaProcessoDt);

		votoDt.setPendenciaDt(pendenciaDt);
	}
	
	public void gerar() throws Exception {
		votoNe.inserirVotoDesativandoAntigo(votoDt, fabrica);
	}
	
	protected void before() throws Exception {}
	protected void after() throws Exception {}

	protected Consumer<GeracaoVotoStrategy> afterConsumer;
	protected Predicate<GeracaoVotoStrategy> beforeConsumer;
	
	public VotoDt gerarVoto() throws Exception {
		preGerar();

		if(beforeConsumer != null)
			if(!beforeConsumer.test(this))
				return null;
		
		before();
		
		gerar();
		
		after();
		
		if(afterConsumer != null)
			afterConsumer.accept(this);
		
		return votoDt;
	}
	
	public GeracaoVotoStrategy setIdAudienciaProcesso(String idAudienciaProcesso) {
		this.idAudienciaProcesso = idAudienciaProcesso;
		return this;
	}
	public GeracaoVotoStrategy setIdPendencia(String idPendencia) {
		this.idPendencia = idPendencia;
		return this;
	}
	public GeracaoVotoStrategy setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
		return this;
	}
	public GeracaoVotoStrategy setFabrica(FabricaConexao fabrica) {
		this.fabrica = fabrica;
		return this;
	}
	public GeracaoVotoStrategy setAfterConsumer(Consumer<GeracaoVotoStrategy> t) {
		afterConsumer = t;
		return this;
	}
	public GeracaoVotoStrategy setBeforeConsumer(Predicate<GeracaoVotoStrategy> t) {
		beforeConsumer = t;
		return this;
	}

	public VotoDt getVotoDt() {
		return votoDt;
	}

	public void setVotoDt(VotoDt votoDt) {
		this.votoDt = votoDt;
	}

	public AudienciaProcessoNe getAudienciaProcessoNe() {
		return audienciaProcessoNe;
	}

	public VotoNe getVotoNe() {
		return votoNe;
	}

	public int getTipo() {
		return tipo;
	}

	public String getIdAudienciaProcesso() {
		return idAudienciaProcesso;
	}

	public String getIdPendencia() {
		return idPendencia;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public FabricaConexao getFabrica() {
		return fabrica;
	}
}