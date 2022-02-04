package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoFisicoDt;
import br.gov.go.tj.projudi.ps.ProcessoParteDebitoFisicoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ProcessoParteDebitoFisicoNeGen extends Negocio{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6551774514998741911L;
	
	protected  ProcessoParteDebitoFisicoDt obDados;

	public ProcessoParteDebitoFisicoNeGen() {		

		obLog = new LogNe(); 

		obDados = new ProcessoParteDebitoFisicoDt(); 

	}


//---------------------------------------------------------
	public void salvar(ProcessoParteDebitoFisicoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoParteDebitoFisicoPs obPersistencia = new ProcessoParteDebitoFisicoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoParteDebitoFisico", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ProcessoParteDebitoFisico", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ProcessoParteDebitoFisicoDt dados ); 


//---------------------------------------------------------

	public void excluir(ProcessoParteDebitoFisicoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoParteDebitoFisicoPs obPersistencia = new ProcessoParteDebitoFisicoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoParteDebitoFisico", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); 
			dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ProcessoParteDebitoFisicoDt consultarId(String id_processopartedebitofisico ) throws Exception {

		ProcessoParteDebitoFisicoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteDebitoFisicoPs obPersistencia = new ProcessoParteDebitoFisicoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_processopartedebitofisico ); 
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
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteDebitoFisicoPs obPersistencia = new ProcessoParteDebitoFisicoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	public List consultarDescricaoProcessoDebito(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		ProcessoDebitoNe ProcessoDebitone = new ProcessoDebitoNe(); 
		tempList = ProcessoDebitone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ProcessoDebitone.getQuantidadePaginas();
		ProcessoDebitone = null;
		
		return tempList;
	}	
}
