package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

public class AudienciaProcessoVotantesDt extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2251950382477352037L;
	private String Id_AudienciaProcessoVotantes;
	private String Id_AudienciaProcesso;
	private String Id_ServentiaCargo;
	private String ServentiaCargo;
	private String NomeUsuario;
	private String CargoTipo;
	private boolean Relator;
	private String Id_ImpedimentoTipo;
	private String ImpedimentoTipoCodigo;
	private String ImpedimentoTipo;
	private String OrdemVotante;
	private String Id_VotanteTipo;
	private String VotanteTipo;
	private String VotanteTipoCodigo;
	private boolean convocado; //jvosantos - 12/07/2019 16:16 - Adicionar flag para saber se votante foi convocado
	
	public AudienciaProcessoVotantesDt() {
		convocado = false;
	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		Id_AudienciaProcessoVotantes = id;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return Id_AudienciaProcessoVotantes;
	}

	public String getId_AudienciaProcesso() {
		return Id_AudienciaProcesso;
	}

	public void setId_AudienciaProcesso(String id_AudienciaProcesso) {
		Id_AudienciaProcesso = id_AudienciaProcesso;
	}

	public boolean isRelator() {
		return Relator;
	}

	public void setRelator(boolean relator) {
		Relator = relator;
	}

	public String getId_ImpedimentoTipo() {
		return Id_ImpedimentoTipo;
	}

	public void setId_ImpedimentoTipo(String id_ImpedimentoTipo) {
		Id_ImpedimentoTipo = id_ImpedimentoTipo;
	}

	public String getOrdemVotante() {
		return OrdemVotante;
	}

	public void setOrdemVotante(String ordemVotante) {
		OrdemVotante = ordemVotante;
	}

	public String getId_ServentiaCargo() {
		return Id_ServentiaCargo;
	}

	public void setId_ServentiaCargo(String id_ServentiaCargo) {
		Id_ServentiaCargo = id_ServentiaCargo;
	}

	public String getServentiaCargo() {
		return ServentiaCargo;
	}

	public void setServentiaCargo(String serventiaCargo) {
		ServentiaCargo = serventiaCargo;
	}

	public String getNomeUsuario() {
		return NomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		NomeUsuario = nomeUsuario;
	}

	public String getCargoTipo() {
		return CargoTipo;
	}

	public void setCargoTipo(String cargoTipo) {
		CargoTipo = cargoTipo;
	}

	public String getImpedimentoTipoCodigo() {
		return ImpedimentoTipoCodigo;
	}

	public void setImpedimentoTipoCodigo(String impedimentoTipoCodigo) {
		ImpedimentoTipoCodigo = impedimentoTipoCodigo;
	}

	public String getImpedimentoTipo() {
		return ImpedimentoTipo;
	}

	public void setImpedimentoTipo(String impedimentoTipo) {
		ImpedimentoTipo = impedimentoTipo;
	}

	public String getVotanteTipo() {
		return VotanteTipo;
	}

	public void setVotanteTipo(String votanteTipo) {
		VotanteTipo = votanteTipo;
	}

	public String getId_VotanteTipo() {
		return Id_VotanteTipo;
	}

	public void setId_VotanteTipo(String id_VotanteTipo) {
		Id_VotanteTipo = id_VotanteTipo;
	}

	public String getVotanteTipoCodigo() {
		return VotanteTipoCodigo;
	}

	public int getVotanteTipoCodigoInt() {
		return Funcoes.StringToInt(VotanteTipoCodigo, -1);
	}

	public void setVotanteTipoCodigo(String votanteTipoCodigo) {
		VotanteTipoCodigo = votanteTipoCodigo;
	}

	//jvosantos - 12/07/2019 16:16 - Adicionar flag para saber se votante foi convocado
	public boolean isConvocado() {
		return convocado;
	}

	//jvosantos - 12/07/2019 16:16 - Adicionar flag para saber se votante foi convocado
	public void setConvocado(boolean convocado) {
		this.convocado = convocado;
	}

}
