package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.ps.RecursoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class RecursoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -7060437780734900215L;	
	protected  RecursoDt obDados;

	public RecursoNeGen() {		

		obLog = new LogNe(); 

		obDados = new RecursoDt(); 

	}


//---------------------------------------------------------
	public void salvar(RecursoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		//////System.out.println("..neRecursosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Recurso", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("Recurso", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(RecursoDt dados ) throws Exception; 


//---------------------------------------------------------

	public void excluir(RecursoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Recurso", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public RecursoDt consultarId(String id_recurso ) throws Exception {

		RecursoDt dtRetorno=null;
		FabricaConexao obFabricaConexao =null;
		//////System.out.println("..ne-ConsultaId_Recurso" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_recurso ); 
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
		//////System.out.println("..ne-ConsultaRecurso" ); 
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
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

	public List consultarDescricaoServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ServentiaNe ServentiaOrigemne = new ServentiaNe(); 
			tempList = ServentiaOrigemne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ServentiaOrigemne.getQuantidadePaginas();
			ServentiaOrigemne = null;
		
		return tempList;
	}


	public List consultarDescricaoProcessoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ProcessoTipoNe ProcessoTipone = new ProcessoTipoNe(); 
			tempList = ProcessoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ProcessoTipone.getQuantidadePaginas();
			ProcessoTipone = null;
		
		return tempList;
	}

	}
