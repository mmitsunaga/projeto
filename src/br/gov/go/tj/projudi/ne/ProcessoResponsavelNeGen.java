package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.ps.ProcessoResponsavelPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ProcessoResponsavelNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -4516194641788138871L;
	
	protected  ProcessoResponsavelDt obDados;


	public ProcessoResponsavelNeGen() {
			 
		obLog = new LogNe(); 

		obDados = new ProcessoResponsavelDt(); 

	}


//---------------------------------------------------------
	public void salvar(ProcessoResponsavelDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;
		
		//////System.out.println("..neProcessoResponsavelsalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoResponsavel",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ProcessoResponsavel",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ProcessoResponsavelDt dados ); 


//---------------------------------------------------------

	public void excluir(ProcessoResponsavelDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoResponsavel",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ProcessoResponsavelDt consultarId(String id_processoresponsavel ) throws Exception {

		ProcessoResponsavelDt dtRetorno=null;
		FabricaConexao obFabricaConexao =null;
		////System.out.println("..ne-ConsultaId_ProcessoResponsavel" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_processoresponsavel ); 
			obDados.copiar(dtRetorno);
		} finally {
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
		//////System.out.println("..ne-ConsultaProcessoResponsavel" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			} finally { 
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoServentiaCargo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe(); 
		tempList = ServentiaCargone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ServentiaCargone.getQuantidadePaginas();
		ServentiaCargone = null;
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

	public List consultarDescricaoCargoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		CargoTipoNe CargoTipone = new CargoTipoNe(); 
		tempList = CargoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = CargoTipone.getQuantidadePaginas();
		CargoTipone = null;
		return tempList;
	}

	public List consultarDescricaoGrupo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		GrupoNe Grupone = new GrupoNe(); 
		tempList = Grupone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Grupone.getQuantidadePaginas();
		Grupone = null;
		return tempList;
	}
	
	public String consultarDescricaoGrupoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		GrupoNe grupoNe = new GrupoNe(); 
		stTemp = grupoNe.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
}
