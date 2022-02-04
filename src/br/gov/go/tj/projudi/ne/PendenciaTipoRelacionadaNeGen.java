package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoRelacionadaDt;
import br.gov.go.tj.projudi.ps.PendenciaTipoRelacionadaPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class PendenciaTipoRelacionadaNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1857328342977784031L;	
	protected  PendenciaTipoRelacionadaDt obDados;

	protected List lisGeral = null; 
	public PendenciaTipoRelacionadaNeGen() {
		
		obLog = new LogNe(); 

		obDados = new PendenciaTipoRelacionadaDt(); 

	}


//---------------------------------------------------------
	public void salvarMultiplo(PendenciaTipoRelacionadaDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

//		////System.out.println("..nePendenciaTipoRelacionadasalvarMultiplo()");
		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			PendenciaTipoRelacionadaDt obDt = (PendenciaTipoRelacionadaDt)lisGeral.get(i);
			boolean boEncontrado =false;
			//se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")){
				//verifico qual id saiu da lista editada
				for(int j=0; j< listaEditada.length; j++)
					if (obDt.getId_PendenciaTipoRelacao().equalsIgnoreCase((String)listaEditada[j])){
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
				PendenciaTipoRelacionadaDt obDt = (PendenciaTipoRelacionadaDt)lisGeral.get(j);
				if (obDt.getId_PendenciaTipoRelacao().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
					lisIncluir.add(obDt);
					break;
				}
			}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaTipoRelacionadaPs obPersistencia = new PendenciaTipoRelacionadaPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				PendenciaTipoRelacionadaDt obDt = (PendenciaTipoRelacionadaDt)lisIncluir.get(i);
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("PendenciaTipoRelacionada", obDt.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			for(int i = 0; i < lisExcluir.size(); i++) {
				PendenciaTipoRelacionadaDt obDtTemp = (PendenciaTipoRelacionadaDt)lisExcluir.get(i); 

				obLogDt = new LogDt("PendenciaTipoRelacionada", obDtTemp.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

//---------------------------------------------------------
	public String consultarPendenciaTipoPendenciaTipoUlLiCheckBox(String id_pendenciatipoprincipal ) throws Exception {

		lisGeral=null;
		String tempUlLi="<ul>";
		////System.out.println("..ne-consultarPendenciaTipoPendenciaTipoUlLiCheckBoxPendenciaTipoRelacionada" ); 
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaTipoRelacionadaPs obPersistencia = new PendenciaTipoRelacionadaPs(obFabricaConexao.getConexao());
			lisGeral=obPersistencia.consultarPendenciaTipoPendenciaTipoGeral( id_pendenciatipoprincipal);
			for(int i = 0; i < lisGeral.size(); i++) {
				PendenciaTipoRelacionadaDt obDtTemp = (PendenciaTipoRelacionadaDt)lisGeral.get(i); 

				tempUlLi+= obDtTemp.getListaLiCheckBox();
			}
			tempUlLi+="</ul>";
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return tempUlLi;   
	}

//---------------------------------------------------------
	public void salvar(PendenciaTipoRelacionadaDt dados ) throws Exception {

		LogDt obLogDt;

		//////System.out.println("..nePendenciaTipoRelacionadasalvar()");
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaTipoRelacionadaPs obPersistencia = new PendenciaTipoRelacionadaPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("PendenciaTipoRelacionada",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("PendenciaTipoRelacionada",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(PendenciaTipoRelacionadaDt dados ); 


//---------------------------------------------------------

	public void excluir(PendenciaTipoRelacionadaDt dados) throws Exception {

		LogDt obLogDt;
		
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaTipoRelacionadaPs obPersistencia = new PendenciaTipoRelacionadaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("PendenciaTipoRelacionada",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public PendenciaTipoRelacionadaDt consultarId(String id_pendenciatiporelacionada ) throws Exception {

		PendenciaTipoRelacionadaDt dtRetorno=null;
		//////System.out.println("..ne-ConsultaId_PendenciaTipoRelacionada" );
		
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaTipoRelacionadaPs obPersistencia = new PendenciaTipoRelacionadaPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_pendenciatiporelacionada ); 
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
		//////System.out.println("..ne-ConsultaPendenciaTipoRelacionada" ); 
		
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaTipoRelacionadaPs obPersistencia = new PendenciaTipoRelacionadaPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoPendenciaTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		PendenciaTipoNe PendenciaTipone = new PendenciaTipoNe(); 
		tempList = PendenciaTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = PendenciaTipone.getQuantidadePaginas();
		PendenciaTipone = null;
		return tempList;
	}
}
