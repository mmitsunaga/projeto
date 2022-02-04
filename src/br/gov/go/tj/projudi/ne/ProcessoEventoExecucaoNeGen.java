package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt;
import br.gov.go.tj.projudi.ps.ProcessoEventoExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ProcessoEventoExecucaoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -4954514383208249135L;	
	protected  ProcessoEventoExecucaoDt obDados;


	public ProcessoEventoExecucaoNeGen() {
		

		obLog = new LogNe(); 

		obDados = new ProcessoEventoExecucaoDt(); 

	}


//---------------------------------------------------------
	public void salvar(ProcessoEventoExecucaoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neProcessoEventoExecucaosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoEventoExecucaoPs obPersistencia = new ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoEventoExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ProcessoEventoExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ProcessoEventoExecucaoDt dados ); 


//---------------------------------------------------------

	public void excluir(ProcessoEventoExecucaoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoEventoExecucaoPs obPersistencia = new ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoEventoExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ProcessoEventoExecucaoDt consultarId(String id_processoeventoexecucao ) throws Exception {

		ProcessoEventoExecucaoDt dtRetorno=null;
		//////System.out.println("..ne-ConsultaId_ProcessoEventoExecucao" );
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoEventoExecucaoPs obPersistencia = new ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_processoeventoexecucao ); 
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
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaProcessoEventoExecucao" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoEventoExecucaoPs obPersistencia = new ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally { 
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoProcessoEventoExecucao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ProcessoEventoExecucaoNe ProcessoEventoExecucaone = new ProcessoEventoExecucaoNe(); 
		tempList = ProcessoEventoExecucaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ProcessoEventoExecucaone.getQuantidadePaginas();
		ProcessoEventoExecucaone = null;
		return tempList;
	}

	public List consultarDescricaoMovimentacao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		MovimentacaoNe Movimentacaone = new MovimentacaoNe(); 
		tempList = Movimentacaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Movimentacaone.getQuantidadePaginas();
		Movimentacaone = null;
		return tempList;
	}

	public List consultarDescricaoProcessoExecucao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ProcessoExecucaoNe ProcessoExecucaone = new ProcessoExecucaoNe(); 
		tempList = ProcessoExecucaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ProcessoExecucaone.getQuantidadePaginas();
		ProcessoExecucaone = null;
		return tempList;
	}

	public List consultarDescricaoEventoExecucao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		EventoExecucaoNe EventoExecucaone = new EventoExecucaoNe(); 
		tempList = EventoExecucaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = EventoExecucaone.getQuantidadePaginas();
		EventoExecucaone = null;
		return tempList;
	}
}
