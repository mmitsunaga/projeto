package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoArquivamentoTipoDt;
import br.gov.go.tj.projudi.ps.ProcessoArquivamentoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

public abstract class ProcessoArquivamentoTipoNeGen extends Negocio{


	private static final long serialVersionUID = -3815403459412052794L;
	protected  ProcessoArquivamentoTipoDt obDados;


	public ProcessoArquivamentoTipoNeGen() {
		obLog = new LogNe(); 
		obDados = new ProcessoArquivamentoTipoDt(); 
	}


	public void salvar(ProcessoArquivamentoTipoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoArquivamentoTipoPs obPersistencia = new ProcessoArquivamentoTipoPs(obFabricaConexao.getConexao()); 

			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoArquivamentoTipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ProcessoArquivamentoTipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			System.out.println("..ne-salvar"+ e.getMessage()); 
			throw new Exception(" <{Erro:.....}> ProcessoArquivamentoTipoNeGen.salvar() " + e.getMessage() );
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


	public abstract String Verificar(ProcessoArquivamentoTipoDt dados ); 


	public void excluir(ProcessoArquivamentoTipoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoArquivamentoTipoPs obPersistencia = new ProcessoArquivamentoTipoPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("ProcessoArquivamentoTipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
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

	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ProcessoArquivamentoTipoPs obPersistencia = new ProcessoArquivamentoTipoPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}

	
	public ProcessoArquivamentoTipoDt consultarId(String id_prococessoarquivamentotipo ) throws Exception {

		ProcessoArquivamentoTipoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ProcessoArquivamentoTipoPs obPersistencia = new ProcessoArquivamentoTipoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_prococessoarquivamentotipo ); 
			obDados.copiar(dtRetorno);
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}


	public long getQuantidadePaginas(){
		return QuantidadePaginas;
	}

	
	public List consultarDescricao(String descricao, String posicao ) throws Exception { 
		List tempList=null;
		FabricaConexao obFabricaConexao = null;


			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

			ProcessoArquivamentoTipoPs obPersistencia = new ProcessoArquivamentoTipoPs(obFabricaConexao.getConexao()); 

				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

}
