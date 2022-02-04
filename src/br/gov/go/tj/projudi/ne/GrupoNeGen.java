package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.GrupoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class GrupoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -8408472654341835062L;
	protected  GrupoDt obDados;


	public GrupoNeGen() {	

		obLog = new LogNe(); 

		obDados = new GrupoDt(); 

	}


//---------------------------------------------------------
	public void salvar(GrupoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neGruposalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GrupoPs obPersistencia = new GrupoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Grupo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("Grupo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(GrupoDt dados ); 


//---------------------------------------------------------

	public void excluir(GrupoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GrupoPs obPersistencia = new GrupoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Grupo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public GrupoDt consultarId(String id_grupo ) throws Exception {

		GrupoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_Grupo" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoPs obPersistencia = new GrupoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_grupo ); 
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
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaGrupo" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoPs obPersistencia = new GrupoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoServentiaTipo(String tempNomeBusca ) throws Exception {
		List tempList=null;
		ServentiaTipoNe ServentiaTipone = new ServentiaTipoNe(); 
		tempList = ServentiaTipone.consultarDescricao(tempNomeBusca);
		QuantidadePaginas = ServentiaTipone.getQuantidadePaginas();
		ServentiaTipone = null;
		return tempList;
	}

	public List consultarDescricaoGrupoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		GrupoTipoNe GrupoTipone = new GrupoTipoNe(); 
		tempList = GrupoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = GrupoTipone.getQuantidadePaginas();
		GrupoTipone = null;
		return tempList;
	}

}
