package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ps.ProcessoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ProcessoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -616820435068323845L;	
	protected  ProcessoDt obDados;


	public ProcessoNeGen() {
		
		obLog = new LogNe(); 

		obDados = new ProcessoDt(); 

	}


//---------------------------------------------------------
	public void salvar(ProcessoDt dados ) throws Exception {

		LogDt obLogDt;
		
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neProcessosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Processo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("Processo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ProcessoDt dados ); 


//---------------------------------------------------------

	public void excluir(ProcessoDt dados) throws Exception {

		LogDt obLogDt;
		
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Processo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ProcessoDt consultarId(String id_processo ) throws Exception {

		ProcessoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_Processo" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_processo ); 
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
		//////System.out.println("..ne-ConsultaProcesso" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
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

	public List consultarDescricaoProcessoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ProcessoTipoNe ProcessoTipone = new ProcessoTipoNe(); 
			tempList = ProcessoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ProcessoTipone.getQuantidadePaginas();
			ProcessoTipone = null;
		
		return tempList;
	}

	public List consultarDescricaoProcessoFase(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ProcessoFaseNe ProcessoFasene = new ProcessoFaseNe(); 
			tempList = ProcessoFasene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ProcessoFasene.getQuantidadePaginas();
			ProcessoFasene = null;
		
		return tempList;
	}

	public List consultarDescricaoProcessoStatus(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ProcessoStatusNe ProcessoStatusne = new ProcessoStatusNe(); 
			tempList = ProcessoStatusne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ProcessoStatusne.getQuantidadePaginas();
			ProcessoStatusne = null;
		
		return tempList;
	}

	public List consultarDescricaoProcessoPrioridade(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ProcessoPrioridadeNe ProcessoPrioridadene = new ProcessoPrioridadeNe(); 
			tempList = ProcessoPrioridadene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ProcessoPrioridadene.getQuantidadePaginas();
			ProcessoPrioridadene = null;
		
		return tempList;
	}

	public List consultarDescricaoServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ServentiaNe Serventiane = new ServentiaNe(); 
			tempList = Serventiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Serventiane.getQuantidadePaginas();
			Serventiane = null;
		
		return tempList;
	}

	public List consultarDescricaoArea(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			AreaNe Areane = new AreaNe(); 
			tempList = Areane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Areane.getQuantidadePaginas();
			Areane = null;
		
		return tempList;
	}

	public List consultarDescricaoObjetoPedido(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ObjetoPedidoNe ObjetoPedidone = new ObjetoPedidoNe(); 
			tempList = ObjetoPedidone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ObjetoPedidone.getQuantidadePaginas();
			ObjetoPedidone = null;
		
		return tempList;
	}

	public List consultarDescricaoClassificador(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ClassificadorNe Classificadorne = new ClassificadorNe(); 
			tempList = Classificadorne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Classificadorne.getQuantidadePaginas();
			Classificadorne = null;
		
		return tempList;
	}

	public List consultarDescricaoAreaDistribuicao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			AreaDistribuicaoNe AreaDistribuicaone = new AreaDistribuicaoNe(); 
			tempList = AreaDistribuicaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = AreaDistribuicaone.getQuantidadePaginas();
			AreaDistribuicaone = null;
		
		return tempList;
	}

	}
