package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoPartePrisaoDt;
import br.gov.go.tj.projudi.ps.ProcessoPartePrisaoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ProcessoPartePrisaoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -481300888846953747L;
	protected  ProcessoPartePrisaoDt obDados;


	public ProcessoPartePrisaoNeGen() {

		obLog = new LogNe(); 

		obDados = new ProcessoPartePrisaoDt(); 

	}


//---------------------------------------------------------
	public void salvar(ProcessoPartePrisaoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoPartePrisaoPs obPersistencia = new ProcessoPartePrisaoPs(obFabricaConexao.getConexao()); 

			/* use o id do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoPartePrisao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ProcessoPartePrisao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			System.out.println("..ne-salvar"+ e.getMessage()); 
			throw new Exception(" <{Erro:.....}> ProcessoPartePrisaoNeGen.salvar() " + e.getMessage() );
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ProcessoPartePrisaoDt dados ); 


//---------------------------------------------------------

	public void excluir(ProcessoPartePrisaoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoPartePrisaoPs obPersistencia = new ProcessoPartePrisaoPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("ProcessoPartePrisao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

 /**

  * Método para lista as area processuais

 * @author jrcorrea

 */

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ProcessoPartePrisaoPs obPersistencia = new ProcessoPartePrisaoPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
 //---------------------------------------------------------

	public ProcessoPartePrisaoDt consultarId(String id_procparteprisao ) throws Exception {

		ProcessoPartePrisaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ProcessoPartePrisaoPs obPersistencia = new ProcessoPartePrisaoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_procparteprisao ); 
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


			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

			ProcessoPartePrisaoPs obPersistencia = new ProcessoPartePrisaoPs(obFabricaConexao.getConexao()); 

				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoProcessoParte(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			ProcessoParteNe ProcessoPartene = new ProcessoParteNe(); 
			tempList = ProcessoPartene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ProcessoPartene.getQuantidadePaginas();
			ProcessoPartene = null;
		return tempList;
	}


	public List consultarDescricaoPrisaoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			PrisaoTipoNe PrisaoTipone = new PrisaoTipoNe(); 
			tempList = PrisaoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = PrisaoTipone.getQuantidadePaginas();
			PrisaoTipone = null;
		return tempList;
	}

	public String consultarDescricaoPrisaoTipoJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new PrisaoTipoNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	public List consultarDescricaoLocalCumprimentoPena(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			LocalCumprimentoPenaNe LocalCumprimentoPenane = new LocalCumprimentoPenaNe(); 
			tempList = LocalCumprimentoPenane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = LocalCumprimentoPenane.getQuantidadePaginas();
			LocalCumprimentoPenane = null;
		return tempList;
	}

	public String consultarDescricaoLocalCumprimentoPenaJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new LocalCumprimentoPenaNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	public List consultarDescricaoEventoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			EventoTipoNe EventoTipone = new EventoTipoNe(); 
			tempList = EventoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = EventoTipone.getQuantidadePaginas();
			EventoTipone = null;
		return tempList;
	}

	public String consultarDescricaoEventoTipoJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new EventoTipoNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	public List consultarDescricaoMovimentacao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			MovimentacaoNe Movimentacaone = new MovimentacaoNe(); 
			tempList = Movimentacaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Movimentacaone.getQuantidadePaginas();
			Movimentacaone = null;
		return tempList;
	}


	}
