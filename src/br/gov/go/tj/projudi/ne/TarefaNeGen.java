package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.TarefaDt;
import br.gov.go.tj.projudi.ps.TarefaPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class TarefaNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 6227031484738029342L;
	protected TarefaPs obPersistencia; 
	protected  TarefaDt obDados;


	public TarefaNeGen() {		

		obLog = new LogNe(); 

		obDados = new TarefaDt(); 

	}


//---------------------------------------------------------
	public void salvar(TarefaDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;
		////System.out.println("..neTarefasalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Tarefa",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("Tarefa",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){
			obFabricaConexao.cancelarTransacao();
			////System.out.println("..ne-salvar"+ e.getMessage()); 
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(TarefaDt dados ); 


//---------------------------------------------------------

	public void excluir(TarefaDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Tarefa",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public TarefaDt consultarId(String id_tarefa ) throws Exception {

		TarefaDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		////System.out.println("..ne-ConsultaId_Tarefa" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_tarefa ); 
			obDados.copiar(dtRetorno);
		} finally {
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
		////System.out.println("..ne-ConsultaTarefa" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoTarefa(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		TarefaNe Tarefane = new TarefaNe(); 
		tempList = Tarefane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Tarefane.getQuantidadePaginas();
		Tarefane = null;
		return tempList;
	}

	public List consultarDescricaoTarefaPrioridade(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		TarefaPrioridadeNe TarefaPrioridadene = new TarefaPrioridadeNe(); 
		tempList = TarefaPrioridadene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = TarefaPrioridadene.getQuantidadePaginas();
		TarefaPrioridadene = null;
		return tempList;
	}

	public List consultarDescricaoTarefaStatus(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		try{
			TarefaStatusNe TarefaStatusne = new TarefaStatusNe(); 
			tempList = TarefaStatusne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = TarefaStatusne.getQuantidadePaginas();
			TarefaStatusne = null;
		}catch(Exception e){
			
			throw e;
		}
		return tempList;
	}

	public List consultarDescricaoTarefaTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		TarefaTipoNe TarefaTipone = new TarefaTipoNe(); 
		tempList = TarefaTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = TarefaTipone.getQuantidadePaginas();
		TarefaTipone = null;
		return tempList;
	}

	public List consultarDescricaoProjeto(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ProjetoNe Projetone = new ProjetoNe(); 
		tempList = Projetone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Projetone.getQuantidadePaginas();
		Projetone = null;
		return tempList;
	}

	public List consultarDescricaoProjetoParticipante(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ProjetoParticipanteNe ProjetoParticipantene = new ProjetoParticipanteNe(); 
		tempList = ProjetoParticipantene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ProjetoParticipantene.getQuantidadePaginas();
		ProjetoParticipantene = null;
		return tempList;
	}

	public List consultarDescricaoUsuario(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		UsuarioNe Usuarione = new UsuarioNe(); 
		tempList = Usuarione.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Usuarione.getQuantidadePaginas();
		Usuarione = null;
		return tempList;
	}
}
