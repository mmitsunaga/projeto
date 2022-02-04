package br.gov.go.tj.projudi.dt;

public class ProcessoCriminalDt extends ProcessoCriminalDtGen{

	private static final long serialVersionUID = -753827064597221720L;
	public static final int CodigoPermissao=3226;

	private String Id_ProcessoArquivamentoTipo;
	private String ProcessoArquivamentoTipo;
	private String idUsuCertificaInconsistencias;
	private String nomeUsuCertificaInconsistencias;
	
	public ProcessoCriminalDt() {
		limpar();
	}
	
	public void copiar(ProcessoCriminalDt objeto){
		if (objeto==null) return;
		super.copiar(objeto);
		Id_ProcessoArquivamentoTipo = objeto.getId_ProcessoArquivamentoTipo();
		ProcessoArquivamentoTipo = objeto.getProcessoArquivamentoTipo();
		idUsuCertificaInconsistencias = objeto.getIdUsuCertificaInconsistencias();
		nomeUsuCertificaInconsistencias = objeto.getNomeUsuCertificaInconsistencias();
	}

	public void limpar(){
		super.limpar();
		Id_ProcessoArquivamentoTipo="";
		ProcessoArquivamentoTipo="";
	}
	public String getPropriedades(){
		return "[Id_ProcessoCriminal:" + getId() + ";Id_Processo:" + getId_Processo() + ";ProcessoNumero:" + getProcessoNumero() + ";ReuPreso:" + getReuPreso() + ";Inquerito:" + getInquerito() + ";DataPrisao:" + getDataPrisao() + ";DataOferecimentoDenuncia:" + getDataOferecimentoDenuncia() + ";DataRecebimentoDenuncia:" + getDataRecebimentoDenuncia() + ";DataTransacaoPenal:" + getDataTransacaoPenal() + ";DataSuspensaoPenal:" + getDataSuspensaoPenal() 
		+ ";DataFato:" + getDataFato()
		+ ";Id_ProcessoArquivamentoTipo:" + getId_ProcessoArquivamentoTipo()
		+ ";ProcessoArquivamentoTipo:" + getProcessoArquivamentoTipo()
		+ ";IdUsuCertificaInconsistencia:" + getIdUsuCertificaInconsistencias()
		+ ";CodigoTemp:" + CodigoTemp + "]";
	}

	public String getId_ProcessoArquivamentoTipo() {
		return Id_ProcessoArquivamentoTipo;
	}
	
	public void setId_ProcessoArquivamentoTipo(String id_ProcessoArquivamentoTipo) {
		if (id_ProcessoArquivamentoTipo!=null) {
			if (id_ProcessoArquivamentoTipo.equalsIgnoreCase("null")) {
				Id_ProcessoArquivamentoTipo = ""; 
				ProcessoArquivamentoTipo = "";
			}else if (!id_ProcessoArquivamentoTipo.equalsIgnoreCase("")) { 
				Id_ProcessoArquivamentoTipo = id_ProcessoArquivamentoTipo;
			}		
		}
	}

	public String getProcessoArquivamentoTipo() {
		return ProcessoArquivamentoTipo;
	}

	public void setProcessoArquivamentoTipo(String processoArquivamentoTipo) {
		if (processoArquivamentoTipo == null || processoArquivamentoTipo.equalsIgnoreCase("null")) {
			ProcessoArquivamentoTipo = "";
		} else {
			ProcessoArquivamentoTipo = processoArquivamentoTipo;
		}
	}

	public String getIdUsuCertificaInconsistencias() {
		return idUsuCertificaInconsistencias;
	}

	public void setIdUsuCertificaInconsistencias(String idUsuCertificaInconsistencias) {
		this.idUsuCertificaInconsistencias = idUsuCertificaInconsistencias;
	}

	public String getNomeUsuCertificaInconsistencias() {
		return nomeUsuCertificaInconsistencias;
	}
	
	public void setNomeUsuCertificaInconsistencias(String nomeUsuCertificaInconsistencias) {
		this.nomeUsuCertificaInconsistencias = nomeUsuCertificaInconsistencias;
	}
}
