package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDebitoStatusDt;
import br.gov.go.tj.projudi.ps.ProcessoDebitoStatusPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class ProcessoDebitoStatusNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -8479527449687527820L;	
	protected  ProcessoDebitoStatusDt obDados;


	public ProcessoDebitoStatusNeGen() {		

		obLog = new LogNe(); 

		obDados = new ProcessoDebitoStatusDt(); 

	}


//---------------------------------------------------------
	public void salvar(ProcessoDebitoStatusDt dados ) throws Exception {

		LogDt obLogDt;
		
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neProcessoDebitosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoDebitoStatusPs obPersistencia = new ProcessoDebitoStatusPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoDebitoStatus", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ProcessoDebitoStatus", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ProcessoDebitoStatusDt dados ); 


//---------------------------------------------------------

	public void excluir(ProcessoDebitoStatusDt dados) throws Exception {

		LogDt obLogDt;
		
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoDebitoStatusPs obPersistencia = new ProcessoDebitoStatusPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoDebitoStatus", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ProcessoDebitoStatusDt consultarId(String idProcessoDebitoStatus ) throws Exception {

		ProcessoDebitoStatusDt dtRetorno=null;
		
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_ProcessoDebito" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoDebitoStatusPs obPersistencia = new ProcessoDebitoStatusPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(idProcessoDebitoStatus ); 
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
		//////System.out.println("..ne-ConsultaProcessoDebito" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ProcessoDebitoStatusPs obPersistencia = new ProcessoDebitoStatusPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	}
