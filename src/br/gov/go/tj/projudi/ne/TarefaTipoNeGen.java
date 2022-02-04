package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.TarefaTipoDt;
import br.gov.go.tj.projudi.ps.TarefaTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class TarefaTipoNeGen extends Negocio{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3477839725872030941L;
	protected  TarefaTipoDt obDados;

	public TarefaTipoNeGen() {

		

		obLog = new LogNe(); 

		obDados = new TarefaTipoDt(); 

	}


//---------------------------------------------------------
	public void salvar(TarefaTipoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;
		////System.out.println("..neTarefaTiposalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TarefaTipoPs obPersistencia = new TarefaTipoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("TarefaTipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("TarefaTipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
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
	 public abstract String Verificar(TarefaTipoDt dados ); 


//---------------------------------------------------------

	public void excluir(TarefaTipoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TarefaTipoPs obPersistencia = new TarefaTipoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("TarefaTipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public TarefaTipoDt consultarId(String id_tarefatipo ) throws Exception {

		TarefaTipoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		////System.out.println("..ne-ConsultaId_TarefaTipo" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TarefaTipoPs obPersistencia = new TarefaTipoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_tarefatipo ); 
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
		////System.out.println("..ne-ConsultaTarefaTipo" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				TarefaTipoPs obPersistencia = new TarefaTipoPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			} finally { 
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	}
