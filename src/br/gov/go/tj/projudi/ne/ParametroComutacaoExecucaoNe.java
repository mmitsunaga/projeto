package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.ParametroComutacaoExecucaoDt;
import br.gov.go.tj.projudi.ps.ParametroComutacaoExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class ParametroComutacaoExecucaoNe extends ParametroComutacaoExecucaoNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = 442228357196761253L;

	//---------------------------------------------------------
	public  String Verificar(ParametroComutacaoExecucaoDt dados ) {

		String stRetorno="";

		if (dados.getDataDecreto().length() == 0) stRetorno += "Informe a data do decreto!\n";
		if (dados.getFracaoComum().length() == 0) stRetorno += "Informe a fração a ser aplicada nos crimes comuns!\n";
		if (dados.getFracaoComumReinc().length() == 0) stRetorno += "Informe a fração a ser aplicada nos crimes comuns com reincidência!\n";
//		if (dados.getFracaoHediondo().length() == 0) stRetorno += "Informe a fração a ser aplicada nos crimes hediondos!\n";
		
		//System.out.println("..neParametroComutacaoExecucaoVerificar()");
		return stRetorno;

	}
	
	public List listarParametroComutacaoExecucao() throws Exception{
		List tempList=null;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ParametroComutacaoExecucaoPs obPersistencia = new ParametroComutacaoExecucaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.listarParametroComutacaoExecucao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	public List listarParametroComutacaoExecucao(List listaIdParametroComutacao) throws Exception{
		List tempList=null;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ParametroComutacaoExecucaoPs obPersistencia = new ParametroComutacaoExecucaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.listarParametroComutacaoExecucao(listaIdParametroComutacao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ParametroComutacaoExecucaoPs obPersistencia = new ParametroComutacaoExecucaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public String gerarJSONVazio() throws Exception {
		String stTemp = "";
		int qtdeColunas = 5;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			ParametroComutacaoExecucaoPs obPersistencia = new ParametroComutacaoExecucaoPs(obFabricaConexao.getConexao());
		
			stTemp = obPersistencia.gerarJSONParametroComutacaoVazio(0, "0", qtdeColunas);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}

}
