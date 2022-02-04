package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.TarefaPrioridadeDt;
import br.gov.go.tj.projudi.ps.TarefaPrioridadePs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class TarefaPrioridadeNe extends TarefaPrioridadeNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = 6392812799772148270L;

    //---------------------------------------------------------
	public  String Verificar(TarefaPrioridadeDt dados ) {

		String stRetorno="";

		if (dados.getTarefaPrioridade().length()==0)
			stRetorno += "O Campo TarefaPrioridade é obrigatório.";
		//System.out.println("..neTarefaPrioridadeVerificar()");
		return stRetorno;

	}

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				TarefaPrioridadePs obPersistencia = new TarefaPrioridadePs(obFabricaConexao.getConexao());
				stTemp=obPersistencia.consultarDescricaoJSON( descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;   
	}

}
