package br.gov.go.tj.projudi.dt;

//jvosantos - 25/09/2019 10:41 - Extender Dados para usar os métodos getId e setId
public class VotanteDt extends Dados {
	
	private String idServentiaCargo;
	private String nome;
	private int ordem;
	private VotanteTipoDt votanteTipoDt;
	private ImpedimentoTipoDt impedimentoTipoDt;
	private String idAudienciaProcesso;
	private String idAudienciaProcessoVotante; // jvosantos - 25/09/2019 10:41 - Adicionar ID da tabela
	
	public VotanteDt() {
		nome = "";
	}
	
	public String getIdAudienciaProcesso() {
		return idAudienciaProcesso;
	}
	public void setIdAudienciaProcesso(String idAudienciaProcesso) {
		this.idAudienciaProcesso = idAudienciaProcesso;
	}
	public VotanteTipoDt getVotanteTipoDt() {
		return votanteTipoDt;
	}
	public void setVotanteTipoDt(VotanteTipoDt votanteTipoDt) {
		this.votanteTipoDt = votanteTipoDt;
	}
	public String getIdServentiaCargo() {
		return idServentiaCargo;
	}
	public void setIdServentiaCargo(String idServentiaCargo) {
		this.idServentiaCargo = idServentiaCargo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		if(nome != null) {
			this.nome = nome;
		}
	}
	public ImpedimentoTipoDt getImpedimentoTipoDt() {
		return impedimentoTipoDt;
	}
	public void setImpedimentoTipoDt(ImpedimentoTipoDt impedimentoTipoDt) {
		this.impedimentoTipoDt = impedimentoTipoDt;
	}
	public int getOrdem() {
		return ordem;
	}
	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	// jvosantos - 25/09/2019 10:41 - Adicionar ID da tabela
	@Override
	public void setId(String id) {
		this.idAudienciaProcessoVotante = id;
	}
	
	// jvosantos - 25/09/2019 10:41 - Adicionar ID da tabela
	@Override
	public String getId() {
		return idAudienciaProcessoVotante;
	}
	

}
