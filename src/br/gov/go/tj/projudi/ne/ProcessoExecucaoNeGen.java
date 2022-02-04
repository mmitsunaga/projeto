package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoExecucaoDt;
import br.gov.go.tj.projudi.ps.ProcessoExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ProcessoExecucaoNeGen extends Negocio{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2842854172307754512L;	
	protected  ProcessoExecucaoDt obDados;

	public ProcessoExecucaoNeGen() {		

		obLog = new LogNe(); 

		obDados = new ProcessoExecucaoDt(); 

	}


//---------------------------------------------------------
	public void salvar(ProcessoExecucaoDt dados ) throws Exception {

		LogDt obLogDt;
		
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoExecucaoPs obPersistencia = new ProcessoExecucaoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ProcessoExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ProcessoExecucaoDt dados ); 


//---------------------------------------------------------

	public void excluir(ProcessoExecucaoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoExecucaoPs obPersistencia = new ProcessoExecucaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ProcessoExecucaoDt consultarId(String id_processoexecucao ) throws Exception {

		ProcessoExecucaoDt dtRetorno=null;
		//////System.out.println("..ne-ConsultaId_ProcessoExecucao" );
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoExecucaoPs obPersistencia = new ProcessoExecucaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_processoexecucao ); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

//---------------------------------------------------------

	public long getQuantidadePaginas(){
		return QuantidadePaginas;
	}
//---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		List tempList=null;
		//////System.out.println("..ne-ConsultaProcessoExecucao" ); 
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoExecucaoPs obPersistencia = new ProcessoExecucaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}


	public List consultarDescricaoProcesso(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ProcessoNe Processone = new ProcessoNe(); 
		tempList = Processone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Processone.getQuantidadePaginas();
		Processone = null;
		return tempList;
	}

	public List consultarDescricaoCidade(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		CidadeNe Cidadene = new CidadeNe(); 
		tempList = Cidadene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Cidadene.getQuantidadePaginas();
		Cidadene = null;
		return tempList;
	}

}
