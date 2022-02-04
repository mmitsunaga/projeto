package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteSinalDt;
import br.gov.go.tj.projudi.ps.ProcessoParteSinalPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ProcessoParteSinalNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1837038733066161343L;	
	protected  ProcessoParteSinalDt obDados;


	public ProcessoParteSinalNeGen() {
		
		obLog = new LogNe(); 

		obDados = new ProcessoParteSinalDt(); 

	}


//---------------------------------------------------------
	public void salvar(ProcessoParteSinalDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neProcessoParteSinalsalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoParteSinalPs obPersistencia = new  ProcessoParteSinalPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoParteSinal", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ProcessoParteSinal", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ProcessoParteSinalDt dados ); 


//---------------------------------------------------------

	public void excluir(ProcessoParteSinalDt dados) throws Exception {

		LogDt obLogDt;
		
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoParteSinalPs obPersistencia = new  ProcessoParteSinalPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoParteSinal", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ProcessoParteSinalDt consultarId(String id_processopartesinal ) throws Exception {

		ProcessoParteSinalDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_ProcessoParteSinal" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteSinalPs obPersistencia = new  ProcessoParteSinalPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_processopartesinal ); 
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
		//////System.out.println("..ne-ConsultaProcessoParteSinal" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteSinalPs obPersistencia = new  ProcessoParteSinalPs(obFabricaConexao.getConexao());
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

	public List consultarDescricaoSinal(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			SinalNe Sinalne = new SinalNe(); 
			tempList = Sinalne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Sinalne.getQuantidadePaginas();
			Sinalne = null;
		
		return tempList;
	}

	}
