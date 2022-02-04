package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.CondenacaoExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class CondenacaoExecucaoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -7037164584367621904L;	
	protected  CondenacaoExecucaoDt obDados;


	public CondenacaoExecucaoNeGen() {		

		obLog = new LogNe(); 

		obDados = new CondenacaoExecucaoDt(); 

	}


//---------------------------------------------------------
	public void salvar(CondenacaoExecucaoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		//////System.out.println("..neCondenacaoExecucaosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CondenacaoExecucaoPs obPersistencia = new CondenacaoExecucaoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("CondenacaoExecucao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados);
                obLogDt = new LogDt("CondenacaoExecucao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());				
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(CondenacaoExecucaoDt dados ); 


//---------------------------------------------------------

	public void excluir(CondenacaoExecucaoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CondenacaoExecucaoPs obPersistencia = new CondenacaoExecucaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("CondenacaoExecucao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public CondenacaoExecucaoDt consultarId(String id_condenacaoexecucao ) throws Exception {

		CondenacaoExecucaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null; 
		//////System.out.println("..ne-ConsultaId_CondenacaoExecucao" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CondenacaoExecucaoPs obPersistencia = new CondenacaoExecucaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_condenacaoexecucao ); 
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
		//////System.out.println("..ne-ConsultaCondenacaoExecucao" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CondenacaoExecucaoPs obPersistencia = new CondenacaoExecucaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
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

	public List consultarDescricaoCrimeExecucao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			CrimeExecucaoNe CrimeExecucaone = new CrimeExecucaoNe(); 
			tempList = CrimeExecucaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = CrimeExecucaone.getQuantidadePaginas();
			CrimeExecucaone = null;
		
		return tempList;
	}

	}
