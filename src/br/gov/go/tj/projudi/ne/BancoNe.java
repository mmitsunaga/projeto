package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.BancoDt;
import br.gov.go.tj.projudi.ps.BancoPs;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.utils.FabricaConexao;

public class BancoNe extends BancoNeGen {

	private static final long serialVersionUID = 2429719588458224150L;

	public String Verificar(BancoDt dados) {

		String stRetorno = "";

		return stRetorno;

	}

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception{
		return consultarDescricaoJSON(descricao, posicao, Persistencia.ORDENACAO_PADRAO, null);  
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros ) throws Exception { 
		
		String stTemp = "";
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			BancoPs obPersistencia = new BancoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, ordenacao, quantidadeRegistros);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
}