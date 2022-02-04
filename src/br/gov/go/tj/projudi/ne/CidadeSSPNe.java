package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.CidadeSSPDt;
import br.gov.go.tj.projudi.ps.CidadeSSPPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class CidadeSSPNe extends CidadeSSPNeGen {

	/**
     * 
     */
    private static final long serialVersionUID = -7067738768107449249L;

    public String Verificar(CidadeSSPDt dados) {

		String stRetorno = "";

		if (dados.getCidade().length() == 0) stRetorno += "O Campo Cidade é obrigatório.";
		if (dados.getCidadeTJ().length() == 0) stRetorno += "O Campo CidadeTJ é obrigatório.";
		if (dados.getEstado().length() == 0) stRetorno += "O Campo estado é obrigatório.";
		////System.out.println("..neCidadeSSPVerificar()");
		return stRetorno;
	}

	public CidadeSSPDt buscaCidadeTJ(String id_CidadeSSP) throws Exception {
		CidadeSSPDt cidadeSSPDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CidadeSSPPs  obPersistencia = new  CidadeSSPPs(obFabricaConexao.getConexao());

			cidadeSSPDt = obPersistencia.buscaCidadeTJ(id_CidadeSSP);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return cidadeSSPDt;
	}

}
