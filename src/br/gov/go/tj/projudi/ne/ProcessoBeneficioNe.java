package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.ProcessoBeneficioDt;
import br.gov.go.tj.projudi.ps.ProcessoBeneficioPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ProcessoBeneficioNe extends ProcessoBeneficioNeGen {

	private static final long serialVersionUID = 2636382073596111450L;

	public String Verificar(ProcessoBeneficioDt dados) {

		String stRetorno = "";
		return stRetorno;

	}
	
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoBeneficioPs obPersistencia = new  ProcessoBeneficioPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}

}
