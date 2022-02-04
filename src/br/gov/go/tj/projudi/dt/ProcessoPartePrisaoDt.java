package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class ProcessoPartePrisaoDt extends ProcessoPartePrisaoDtGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5173053268487071017L;
	public static final int CodigoPermissao=317;
//

	public String toJson(){
		return "{\"Id\":\"" + getId() 
		+ "\",\"DataPrisao\":\"" + getDataPrisao() 
		+ "\",\"LocalCumpPena\":\"" + getLocalCumpPena() 
		+ "\",\"PrazoPrisao\":\"" + getPrazoPrisao() 
		+ "\",\"MoviPrisao\":\"" + getMoviPrisao() 
		+ "\",\"MoviEvento\":\"" + getMoviEvento()
		+ "\",\"DataEvento\":\"" + getDataEvento() 		
		+ "\",\"PrisaoTipo\":\"" + getPrisaoTipo() 		  		
		+ "\",\"Observacao\":\"" + getObservacao()
		+ "\",\"Id_ProcessoPartePrisao\":\"" + getId() 
		+ "\",\"Id_ProcessoParte\":\"" + getId_ProcessoParte()
		+ "\",\"Id_PrisaoTipo\":\"" + getId_PrisaoTipo() 
		+ "\",\"Id_LocalCumpPena\":\"" + getId_LocalCumpPena()
		+ "\",\"EventoTipo\":\"" + getEventoTipo()
		+ "\",\"Id_EventoTipo\":\"" + getId_EventoTipo() 
		+ "\",\"Id_MoviEvento\":\"" + getId_MoviEvento()  
		+ "\",\"Id_MoviPrisao\":\"" + getId_MoviPrisao() 
		+ "\"}";
	}

	public boolean isTemId() {
		
		if (getId()!=null && !getId().isEmpty()) {
			return true;
		}
		return false;
	}
	
//	+ "\",\"InformouFamiliares\":\"" + getInformouFamiliares() 
//	+ "\",\"InformouDefensoria\":\"" + getInformouDefensoria()  
}
