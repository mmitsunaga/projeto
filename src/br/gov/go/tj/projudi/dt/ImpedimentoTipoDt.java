package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

public class ImpedimentoTipoDt extends Dados {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5506073704955321866L;
	public static final int AGUARDANDO_INICIO_VOTACAO = 1;
	public static final int AGUARDANDO_VERIFICACAO_VOTANTE = 2;
	public static final int IMPEDIDO = 3;
	public static final int NAO_IMPEDIDO = 4;
	
	private String Id_ImpedimentoTipo;
	private String ImpedimentoTipoCodigo;
	private String ImpedimentoTipo;
	

	@Override
	public void setId(String id) {
		Id_ImpedimentoTipo = id;

	}

	@Override
	public String getId() {		
		return Id_ImpedimentoTipo;
	}

	public String getImpedimentoTipoCodigo() {
		return ImpedimentoTipoCodigo;
	}
	
	public int getImpedimentoTipoCodigoInt() {
		return Funcoes.StringToInt(ImpedimentoTipoCodigo);
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
	
	public void copiar(ImpedimentoTipoDt objeto){
		Id_ImpedimentoTipo = objeto.getId();
		ImpedimentoTipoCodigo = objeto.getImpedimentoTipoCodigo();
		ImpedimentoTipo = objeto.getImpedimentoTipo();
	}

}
