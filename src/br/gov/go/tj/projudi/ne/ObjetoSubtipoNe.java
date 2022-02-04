package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.ObjetoSubtipoDt;

public class ObjetoSubtipoNe extends ObjetoSubtipoNeGen{

	private static final long serialVersionUID = -8756745238505252888L;

	public  String Verificar(ObjetoSubtipoDt dados ) {

		String stRetorno="";

		if (dados.getObjetoSubtipo().length()==0)
			stRetorno += "O campo Objeto SubTipo é obrigatório.";

		return stRetorno;
	}

}
