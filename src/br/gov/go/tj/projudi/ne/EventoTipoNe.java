package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.EventoTipoDt;

//---------------------------------------------------------
public class EventoTipoNe extends EventoTipoNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = -4846056172253184613L;

	//---------------------------------------------------------
	public  String Verificar(EventoTipoDt dados ) {

		String stRetorno="";

		if (dados.getEventoTipo().length()==0)
			stRetorno += "O Campo EventoTipo é obrigatório.";

		return stRetorno;

	}

}
