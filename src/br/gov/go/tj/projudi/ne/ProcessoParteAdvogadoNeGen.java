package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.ps.ProcessoParteAdvogadoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ProcessoParteAdvogadoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1524573784185948032L;

	
	
	protected  ProcessoParteAdvogadoDt obDados;
		 

	public ProcessoParteAdvogadoNeGen() {
		 		

		obLog = new LogNe(); 

		obDados = new ProcessoParteAdvogadoDt(); 

	}


//---------------------------------------------------------
	public void salvar(ProcessoParteAdvogadoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..neProcessoParteAdvogadosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoParteAdvogado", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ProcessoParteAdvogado", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ProcessoParteAdvogadoDt dados ); 


//---------------------------------------------------------

	public void excluir(ProcessoParteAdvogadoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoParteAdvogado", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ProcessoParteAdvogadoDt consultarId(String id_processoparteadvogado ) throws Exception {

		ProcessoParteAdvogadoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_ProcessoParteAdvogado" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_processoparteadvogado ); 
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
		//////System.out.println("..ne-ConsultaProcessoParteAdvogado" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				//stUltimaConsulta=descricao;
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoUsuarioServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		UsuarioServentiaNe UsuarioServentiane = new UsuarioServentiaNe(); 
		tempList = UsuarioServentiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = UsuarioServentiane.getQuantidadePaginas();
		UsuarioServentiane = null;
		
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
	
	public List consultarDescricaoProcesso(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ProcessoNe Processone = new ProcessoNe(); 
			tempList = Processone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Processone.getQuantidadePaginas();
			Processone = null;
		
		return tempList;
	}

	public List consultarDescricaoProcessoParteTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ProcessoParteTipoNe ProcessoParteTipone = new ProcessoParteTipoNe(); 
			tempList = ProcessoParteTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ProcessoParteTipone.getQuantidadePaginas();
			ProcessoParteTipone = null;
		
		return tempList;
	}

	}
