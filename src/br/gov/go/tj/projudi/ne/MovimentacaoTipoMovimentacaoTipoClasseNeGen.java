package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoMovimentacaoTipoClasseDt;
import br.gov.go.tj.projudi.ps.MovimentacaoTipoMovimentacaoTipoClassePs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class MovimentacaoTipoMovimentacaoTipoClasseNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -7226855543061267352L;
	
	protected  MovimentacaoTipoMovimentacaoTipoClasseDt obDados;

	protected List lisGeral = null; 
	public MovimentacaoTipoMovimentacaoTipoClasseNeGen() {

		obLog = new LogNe(); 

		obDados = new MovimentacaoTipoMovimentacaoTipoClasseDt(); 

	}


//---------------------------------------------------------
	public void salvarMultiplo(MovimentacaoTipoMovimentacaoTipoClasseDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;


		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			MovimentacaoTipoMovimentacaoTipoClasseDt obDt = (MovimentacaoTipoMovimentacaoTipoClasseDt)lisGeral.get(i);
			boolean boEncontrado =false;
			//se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")){
				//verifico qual id saiu da lista editada
				for(int j=0; j< listaEditada.length; j++)
					if (obDt.getId_MovimentacaoTipo().equalsIgnoreCase((String)listaEditada[j])){
						boEncontrado = true;
						break;
					}
			//se o id do objeto não foi encontrado na lista editada coloco o objeto para ser excluido
			if (!boEncontrado) lisExcluir.add(obDt);
			}
		}

		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		for (int i = 0; i < listaEditada.length; i++)
			for(int j=0; j< lisGeral.size(); j++){
				MovimentacaoTipoMovimentacaoTipoClasseDt obDt = (MovimentacaoTipoMovimentacaoTipoClasseDt)lisGeral.get(j);
				if (obDt.getId_MovimentacaoTipo().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
					lisIncluir.add(obDt);
					break;
				}
			}

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoTipoMovimentacaoTipoClassePs obPersistencia = new MovimentacaoTipoMovimentacaoTipoClassePs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				MovimentacaoTipoMovimentacaoTipoClasseDt obDt = (MovimentacaoTipoMovimentacaoTipoClasseDt)lisIncluir.get(i);
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("MovimentacaoTipoMovimentacaoTipoClasse", obDt.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			for(int i = 0; i < lisExcluir.size(); i++) {
				MovimentacaoTipoMovimentacaoTipoClasseDt obDtTemp = (MovimentacaoTipoMovimentacaoTipoClasseDt)lisExcluir.get(i); 

				obLogDt = new LogDt("MovimentacaoTipoMovimentacaoTipoClasse", obDtTemp.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

//---------------------------------------------------------
	public String consultarMovimentacaoTipoMovimentacaoTipoClasseUlLiCheckBox(String id_movi_tipo_classe ) throws Exception {

		lisGeral=null;
		FabricaConexao obFabricaConexao = null;
		String tempUlLi="<ul>";

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoTipoMovimentacaoTipoClassePs obPersistencia = new MovimentacaoTipoMovimentacaoTipoClassePs(obFabricaConexao.getConexao());
			lisGeral=obPersistencia.consultarMovimentacaoTipoMovimentacaoTipoClasseGeral( id_movi_tipo_classe);
			for(int i = 0; i < lisGeral.size(); i++) {
				MovimentacaoTipoMovimentacaoTipoClasseDt obDtTemp = (MovimentacaoTipoMovimentacaoTipoClasseDt)lisGeral.get(i); 

				tempUlLi+= obDtTemp.getListaLiCheckBox();
			}
			tempUlLi+="</ul>";
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempUlLi;   
	}

//---------------------------------------------------------
	public void salvar(MovimentacaoTipoMovimentacaoTipoClasseDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoTipoMovimentacaoTipoClassePs obPersistencia = new MovimentacaoTipoMovimentacaoTipoClassePs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("MovimentacaoTipoMovimentacaoTipoClasse",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("MovimentacaoTipoMovimentacaoTipoClasse",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(MovimentacaoTipoMovimentacaoTipoClasseDt dados ); 


//---------------------------------------------------------

	public void excluir(MovimentacaoTipoMovimentacaoTipoClasseDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoTipoMovimentacaoTipoClassePs obPersistencia = new MovimentacaoTipoMovimentacaoTipoClassePs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("MovimentacaoTipoMovimentacaoTipoClasse",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public MovimentacaoTipoMovimentacaoTipoClasseDt consultarId(String id_movitipomovitipoclasse ) throws Exception {

		MovimentacaoTipoMovimentacaoTipoClasseDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoTipoMovimentacaoTipoClassePs obPersistencia = new MovimentacaoTipoMovimentacaoTipoClassePs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_movitipomovitipoclasse ); 
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

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoTipoMovimentacaoTipoClassePs obPersistencia = new MovimentacaoTipoMovimentacaoTipoClassePs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoMovimentacaoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		MovimentacaoTipoNe MovimentacaoTipone = new MovimentacaoTipoNe(); 
		tempList = MovimentacaoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = MovimentacaoTipone.getQuantidadePaginas();
		MovimentacaoTipone = null;
		return tempList;
	}

	public List consultarDescricaoMovimentacaoTipoClasse(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		MovimentacaoTipoClasseNe MovimentacaoTipoClassene = new MovimentacaoTipoClasseNe(); 
		tempList = MovimentacaoTipoClassene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = MovimentacaoTipoClassene.getQuantidadePaginas();
		MovimentacaoTipoClassene = null;
		return tempList;
	}
}
