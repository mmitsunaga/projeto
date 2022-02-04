package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class ProcessoParteAssuntoDt extends ProcessoParteAssuntoDtGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7245117051510894435L;
	public static final int CodigoPermissao=315;
	private String Id_Processo;
	private String DispositivoLegal;
	private String Artigo;
	
//
	public void setId_Processo(String id_processo) {
		Id_Processo = id_processo;
		
	}
	public String getId_Processo() {
		return Id_Processo;
	}

	public String getDispositivoLegal() {
		return DispositivoLegal;
	}
	public void setDispositivoLegal(String dispositivoLegal) {
		DispositivoLegal = dispositivoLegal;
	}
	public String getArtigo() {
		return Artigo;
	}
	public void setArtigo(String artigo) {
		Artigo = artigo;
	}
	public String toJson(){
		return "{\"Id\":\"" + getId() +  "\",\"Assunto\":\"" + getAssunto() +  "\",\"DispositivoLegal\":\"" + getDispositivoLegal() +   "\",\"Artigo\":\"" + getArtigo() + "\",\"DataInclusao\":\"" + getDataInclusao() + "\",\"Id_ProcessoParte\":\"" + getId_ProcessoParte() + "\",\"ProcessoParte\":\"" + getProcessoParteNome() + "\",\"Id_Assunto\":\"" + getId_Assunto() + "\",\"Id_Processo\":\"" + getId_Processo() + "\",\"CodigoTemp\":\"" + getCodigoTemp() + "\"}";
	}
	public boolean isProntoSalvar() {
		return  (Id_Processo != null && !Id_Processo.isEmpty() && getId_ProcessoParte() != null && !getId_ProcessoParte().isEmpty()  && getId_Assunto() != null && !getId_Assunto().isEmpty());		
	}
	public boolean isProntoExcluir() {
		return  (Id_Processo != null && !Id_Processo.isEmpty() && getId_ProcessoParte() != null && !getId_ProcessoParte().isEmpty()  && getId_Assunto() != null && !getId_Assunto().isEmpty() && getId() != null && !getId().isEmpty());
	}
}
