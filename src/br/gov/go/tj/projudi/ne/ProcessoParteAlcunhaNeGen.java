package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAlcunhaDt;
import br.gov.go.tj.projudi.ps.ProcessoParteAlcunhaPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ProcessoParteAlcunhaNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7171917948584380778L;	
	protected  ProcessoParteAlcunhaDt obDados;

	public ProcessoParteAlcunhaNeGen() {

		

		obLog = new LogNe(); 

		obDados = new ProcessoParteAlcunhaDt(); 

	}


//---------------------------------------------------------
	public void salvar(ProcessoParteAlcunhaDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		
		//////System.out.println("..neProcessoParteAlcunhasalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoParteAlcunhaPs obPersistencia = new ProcessoParteAlcunhaPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoParteAlcunha", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ProcessoParteAlcunha", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ProcessoParteAlcunhaDt dados ); 


//---------------------------------------------------------

	public void excluir(ProcessoParteAlcunhaDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoParteAlcunhaPs obPersistencia = new ProcessoParteAlcunhaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoParteAlcunha", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ProcessoParteAlcunhaDt consultarId(String id_processopartealcunha ) throws Exception {

		ProcessoParteAlcunhaDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_ProcessoParteAlcunha" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteAlcunhaPs obPersistencia = new ProcessoParteAlcunhaPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_processopartealcunha ); 
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
		//////System.out.println("..ne-ConsultaProcessoParteAlcunha" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteAlcunhaPs obPersistencia = new ProcessoParteAlcunhaPs(obFabricaConexao.getConexao());
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

	public List consultarDescricaoAlcunha(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			AlcunhaNe Alcunhane = new AlcunhaNe(); 
			tempList = Alcunhane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Alcunhane.getQuantidadePaginas();
			Alcunhane = null;
		
		return tempList;
	}

	}
