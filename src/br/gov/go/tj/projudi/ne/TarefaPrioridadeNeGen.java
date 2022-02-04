package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.TarefaPrioridadeDt;
import br.gov.go.tj.projudi.ps.TarefaPrioridadePs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class TarefaPrioridadeNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7709659644263844085L;
	protected  TarefaPrioridadeDt obDados;


	public TarefaPrioridadeNeGen() {

		obLog = new LogNe(); 

		obDados = new TarefaPrioridadeDt(); 

	}


//---------------------------------------------------------
	public void salvar(TarefaPrioridadeDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;
		////System.out.println("..neTarefaPrioridadesalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TarefaPrioridadePs obPersistencia = new TarefaPrioridadePs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("TarefaPrioridade",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("TarefaPrioridade",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
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
	 public abstract String Verificar(TarefaPrioridadeDt dados ); 


//---------------------------------------------------------

	public void excluir(TarefaPrioridadeDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TarefaPrioridadePs obPersistencia = new TarefaPrioridadePs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("TarefaPrioridade",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public TarefaPrioridadeDt consultarId(String id_tarefaprioridade ) throws Exception {

		TarefaPrioridadeDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		////System.out.println("..ne-ConsultaId_TarefaPrioridade" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TarefaPrioridadePs obPersistencia = new TarefaPrioridadePs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_tarefaprioridade ); 
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
		////System.out.println("..ne-ConsultaTarefaPrioridade" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				TarefaPrioridadePs obPersistencia = new TarefaPrioridadePs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	}
