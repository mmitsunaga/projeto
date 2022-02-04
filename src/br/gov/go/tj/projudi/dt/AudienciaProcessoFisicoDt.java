package br.gov.go.tj.projudi.dt;

public class AudienciaProcessoFisicoDt extends AudienciaProcessoDt {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1974773538180970782L;	
	
	private ProcessoFisicoDt ProcessoFisicoDt;
	private String IdAudienciaProcessoFisico;
	
	public ProcessoFisicoDt getProcessoFisicoDt() {
		return ProcessoFisicoDt;
	}

	public void setProcessoFisicoDt(ProcessoFisicoDt processoFisicoDt) {
		ProcessoFisicoDt = processoFisicoDt;
	}
	
	public String getIdAudienciaProcessoFisico() {
		return IdAudienciaProcessoFisico;
	}

	public void setIdAudienciaProcessoFisico(String idAudienciaProcessoFisico) {
		IdAudienciaProcessoFisico = idAudienciaProcessoFisico;
	}
	
	public void limpar(){
		super.limpar();
		this.ProcessoFisicoDt = null;
		this.IdAudienciaProcessoFisico = "";		
	}
	
	public String getPropriedades(){
		return "[Id_AudienciaProcesso:" + getId() + ";Id_Audiencia:" + getId_Audiencia() + ";IdAudienciaProcessoFisico:" + getIdAudienciaProcessoFisico() + ";AudienciaTipo:" + getAudienciaTipo() + ";Id_AudienciaProcessoStatus:" + getId_AudienciaProcessoStatus() + ";AudienciaProcessoStatus:" + getAudienciaProcessoStatus() + ";Id_ServentiaCargo:" + getId_ServentiaCargo() + ";ServentiaCargo:" + getServentiaCargo() + ";Id_Processo:" + getId_Processo() + ";ProcessoNumero:" + getProcessoNumero() + ";DataMovimentacao:" + getDataMovimentacao() + ";CodigoTemp:" + getCodigoTemp() + ";AudienciaTipoCodigo:" + getAudienciaTipoCodigo() + ";AudienciaProcessoStatusCodigo:" + getAudienciaProcessoStatusCodigo()+ ";AudienciaProcessoStatusAnalista:" + getAudienciaProcessoStatusAnalista()+ ";AudienciaProcessoStatusCodigoAnalista:" + getAudienciaProcessoStatusCodigoAnalista() + ";Id_ArquivoAta:" + getId_ArquivoAta() + ";DataAudienciaOriginal:" + getDataAudienciaOriginal() + ";Id_ArquivoAtaSessaoIniciada:" + getId_ArquivoAtaSessaoIniciada() + ";Id_ArquivoAtaSessaoAdiada:" + getId_ArquivoAtaSessaoAdiada() + ";id_ServentiaCargoPresidente:" + getId_ServentiaCargoPresidente() + ";id_ServentiaMP:" + getId_ServentiaMP() + ";id_ServentiaCargoMP:" + getId_ServentiaCargoMP() + ";id_ServentiaCargoRedator:" + getId_ServentiaCargoRedator() + ";processoFisico:" + this.ProcessoFisicoDt.getNumeroProcesso() + "]";
	}
}