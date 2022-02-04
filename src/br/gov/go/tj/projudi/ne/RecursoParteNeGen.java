package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.RecursoParteDt;
import br.gov.go.tj.projudi.ps.RecursoPartePs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class RecursoParteNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8587819236380659116L;	
	protected  RecursoParteDt obDados;


	public RecursoParteNeGen() {

		obLog = new LogNe(); 

		obDados = new RecursoParteDt(); 

	}


//---------------------------------------------------------
	public void salvar(RecursoParteDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		//////System.out.println("..neRecursoPartesalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			RecursoPartePs obPersistencia = new  RecursoPartePs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("RecursoParte", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("RecursoParte", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(RecursoParteDt dados ); 


//---------------------------------------------------------

	public void excluir(RecursoParteDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			RecursoPartePs obPersistencia = new  RecursoPartePs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("RecursoParte", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public RecursoParteDt consultarId(String id_recursoparte ) throws Exception {

		RecursoParteDt dtRetorno=null;
		FabricaConexao obFabricaConexao =null;
		//////System.out.println("..ne-ConsultaId_RecursoParte" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RecursoPartePs obPersistencia = new  RecursoPartePs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_recursoparte ); 
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
		FabricaConexao obFabricaConexao =null;
		//////System.out.println("..ne-ConsultaRecursoParte" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				RecursoPartePs obPersistencia = new  RecursoPartePs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoRecurso(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		RecursoNe Recursone = new RecursoNe(); 
		tempList = Recursone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Recursone.getQuantidadePaginas();
		Recursone = null;
		
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

	public List consultarDescricaoProcessoParteTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ProcessoParteTipoNe ProcessoParteTipone = new ProcessoParteTipoNe(); 
			tempList = ProcessoParteTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ProcessoParteTipone.getQuantidadePaginas();
			ProcessoParteTipone = null;
		
		return tempList;
	}

	}
