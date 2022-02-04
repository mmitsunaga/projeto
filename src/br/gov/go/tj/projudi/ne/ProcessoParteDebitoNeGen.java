package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoDt;
import br.gov.go.tj.projudi.ps.ProcessoParteDebitoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ProcessoParteDebitoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 6232433575970672210L;
	
	protected  ProcessoParteDebitoDt obDados;


	public ProcessoParteDebitoNeGen() {		

		obLog = new LogNe(); 

		obDados = new ProcessoParteDebitoDt(); 

	}


//---------------------------------------------------------
	public void salvar(ProcessoParteDebitoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neProcessoParteDebitosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoParteDebitoPs obPersistencia = new ProcessoParteDebitoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoParteDebito", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ProcessoParteDebito", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ProcessoParteDebitoDt dados ); 


//---------------------------------------------------------

	public void excluir(ProcessoParteDebitoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoParteDebitoPs obPersistencia = new ProcessoParteDebitoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoParteDebito", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); 
			dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ProcessoParteDebitoDt consultarId(String id_processopartedebito ) throws Exception {

		ProcessoParteDebitoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_ProcessoParteDebito" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteDebitoPs obPersistencia = new ProcessoParteDebitoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_processopartedebito ); 
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
		//////System.out.println("..ne-ConsultaProcessoParteDebito" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ProcessoParteDebitoPs obPersistencia = new ProcessoParteDebitoPs(obFabricaConexao.getConexao());
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

	public List consultarDescricaoProcessoParte(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ProcessoParteNe ProcessoPartene = new ProcessoParteNe(); 
			tempList = ProcessoPartene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ProcessoPartene.getQuantidadePaginas();
			ProcessoPartene = null;
		
		return tempList;
	}

	}
