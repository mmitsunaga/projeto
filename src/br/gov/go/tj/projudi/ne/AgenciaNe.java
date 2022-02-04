package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.AgenciaDt;
import br.gov.go.tj.projudi.ps.AgenciaPs;
import br.gov.go.tj.utils.FabricaConexao;

public class AgenciaNe extends AgenciaNeGen {

	private static final long serialVersionUID = 2584009387281208717L;

	public String Verificar(AgenciaDt dados) {

		String stRetorno = "";

		if (dados.getId_Banco().equalsIgnoreCase(""))
			stRetorno += "Banco é campo obrigatório. ";

		return stRetorno;

	}
	
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				AgenciaPs obPersistencia = new AgenciaPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return stTemp;   
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception { 
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				AgenciaPs obPersistencia = new AgenciaPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, ordenacao, quantidadeRegistros);
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return stTemp;   
	}
	
	public String consultarDescricaoBancoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception{
		String stTemp = "";
		
		BancoNe Bancone = new BancoNe(); 
		stTemp = Bancone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		Bancone = null;
		
		return stTemp;
	}

}